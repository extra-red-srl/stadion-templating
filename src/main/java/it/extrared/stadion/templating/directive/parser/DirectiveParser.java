/*
 * Copyright 2026 Extrared
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.extrared.stadion.templating.directive.parser;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import it.extrared.stadion.templating.directive.FilterAndDirective;
import it.extrared.stadion.templating.directive.FilterTemplateDirective;
import it.extrared.stadion.templating.directive.Literal;
import it.extrared.stadion.templating.directive.PipeDirective;
import it.extrared.stadion.templating.directive.StringFieldComposer;
import it.extrared.stadion.templating.directive.TemplateDirective;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Entry-point parser for template expressions.
 *
 * <p>Scans a raw template string for {@code {{...}}} placeholders using a regex, strips the
 * delimiters, and dispatches each inner expression through the following chain: {@code FilterParser
 * → LiteralParser → FunctionParser → InlineParser → PropertyNameParser}.
 *
 * <p>When a template string contains both static text and directives (e.g. {@code "Restaurant:
 * {{name}}"}) the result is wrapped in a {@link StringFieldComposer} so the static and dynamic
 * parts are concatenated at evaluation time.
 */
public class DirectiveParser {
    private static final Pattern DIRECTIVE_FINDER = Pattern.compile("\\{\\{.*?\\}\\}");
    private static final String PIPE_SEPARATOR = "\\|";

    private static final LogWriter LOG = LogWriters.getLogger(DirectiveParser.class);

    private final SingleDirectiveParser parserChain;

    public DirectiveParser() {
        this.parserChain = buildParsersChain();
    }

    private SingleDirectiveParser buildParsersChain() {
        PropertyNameParser pnParser = new PropertyNameParser();
        InlineParser inlineParser = new InlineParser(pnParser);
        FunctionParser functionParser = new FunctionParser(inlineParser);
        LiteralParser literalParser = new LiteralParser(functionParser);
        return new FilterParser(literalParser);
    }

    /**
     * Builds the parameter sub-chain used for evaluating filter operands and function arguments:
     * {@code FunctionParser → LiteralParser → PropertyNameParser}.
     *
     * <p>{@link FilterParser} and {@link InlineParser} are intentionally excluded: filter operands
     * cannot be nested filters, and inline markers are only meaningful at the top level.
     */
    static SingleDirectiveParser buildParameterChain() {
        return new FunctionParser(new LiteralParser(new PropertyNameParser()));
    }

    /**
     * Parses a raw template string and returns the corresponding directive.
     *
     * @param strDirective the raw string, which may contain zero or more {@code {{...}}}
     *     placeholders
     * @return the directive representing the full expression
     */
    public TemplateDirective parse(String strDirective) {
        LOG.debug("Parsing template directive %s".formatted(strDirective));
        Matcher matcher = DIRECTIVE_FINDER.matcher(strDirective);
        boolean matched = false;
        String composing = strDirective;
        LinkedList<TemplateDirective> toStringComposing = new LinkedList<>();
        while (matcher.find()) {
            matched = true;
            MatchResult result = matcher.toMatchResult();
            int start = result.start();
            int end = result.end();
            TemplateDirective templateDirective =
                    toResult(strDirective.substring(start + 2, end - 2));
            if (templateDirective instanceof FilterTemplateDirective) {
                composing = composing.replace(result.group(), "");
                if (!toStringComposing.isEmpty())
                    throw new TemplatingException(
                            "A filter should be always specified as a single field value of a $if field or as the first directive of a field eg. {{filter}}{{some|other|directive}}");
            } else {
                composing = composing.replace(result.group(), "%s");
            }
            toStringComposing.addLast(templateDirective);
        }
        return toResult(matched, composing, strDirective, toStringComposing);
    }

    private TemplateDirective toResult(
            boolean matched,
            String composing,
            String strDirective,
            List<TemplateDirective> toStringComposing) {
        TemplateDirective result = null;
        if (!matched) {
            LOG.debug("%s is not a directive. Returning it as a literal".formatted(strDirective));
            result = new Literal(strDirective);
        } else if (composing.replaceAll(" ", "").equals("%s")) {
            // only one directive but we might have a filter
            if (toStringComposing.size() == 2
                    && toStringComposing.get(0) instanceof FilterTemplateDirective) {
                result = new FilterAndDirective(toStringComposing.get(0), toStringComposing.get(1));
            } else {
                result = toStringComposing.getFirst();
            }
        } else if (composing.isEmpty() && !toStringComposing.isEmpty()) {
            result = toStringComposing.getFirst();
        } else {
            // multiple directives inside a single field one might be a filter
            TemplateDirective first = toStringComposing.getFirst();
            if (first instanceof FilterTemplateDirective) {
                result =
                        new FilterAndDirective(
                                toStringComposing.getFirst(),
                                new StringFieldComposer(
                                        composing,
                                        toStringComposing
                                                .subList(1, toStringComposing.size())
                                                .toArray(
                                                        new TemplateDirective
                                                                [toStringComposing.size() - 1])));
            } else {
                result =
                        new StringFieldComposer(
                                composing, toStringComposing.toArray(new TemplateDirective[0]));
            }
        }
        return result;
    }

    private TemplateDirective toResult(String directive) {
        if (directive == null || directive.isBlank()) return new Literal(null);
        String[] pipeParts = directive.split(PIPE_SEPARATOR);
        TemplateDirective[] directives = new TemplateDirective[pipeParts.length];
        for (int i = 0; i < pipeParts.length; i++) {
            String part = pipeParts[i];
            LOG.debug("Parsing template directive pipe part %s".formatted(part));
            directives[i] = parserChain.parse(part);
        }
        if (directives.length > 1) return new PipeDirective(directives);
        return directives[0];
    }

    public SingleDirectiveParser getParserChain() {
        return parserChain;
    }
}
