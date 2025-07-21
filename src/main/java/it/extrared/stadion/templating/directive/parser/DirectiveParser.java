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
        FunctionParser functionParser = new FunctionParser(pnParser);
        LiteralParser literalParser = new LiteralParser(functionParser);
        return new FilterParser(literalParser);
    }

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
}
