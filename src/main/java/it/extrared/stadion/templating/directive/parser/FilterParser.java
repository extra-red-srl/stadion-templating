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
import it.extrared.stadion.templating.directive.FilterTemplateDirective;
import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.filter.*;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A parser able to compile a filter directive. It uses the Shunting Yard Algorithm to obtain a
 * Reverse Polish Annotation of the filter and then compile it.
 */
public class FilterParser extends ChainingSingleDirectiveParser {

    private static final Map<String, FilterFactory> FACTORIES =
            Map.ofEntries(
                    new AbstractMap.SimpleImmutableEntry<>(
                            "and",
                            (params) ->
                                    new And((Filter) params.getFirst(), (Filter) params.get(1))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "or",
                            (params) -> new Or((Filter) params.getFirst(), (Filter) params.get(1))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "eq",
                            (params) ->
                                    new Eq(
                                            (TemplateDirective) params.getFirst(),
                                            (TemplateDirective) params.get(1))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "gt",
                            (params) ->
                                    new GreaterThan(
                                            (TemplateDirective) params.getFirst(),
                                            (TemplateDirective) params.get(1))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "gte",
                            (params) ->
                                    new GreaterThanEq(
                                            (TemplateDirective) params.getFirst(),
                                            (TemplateDirective) params.get(1))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "lt",
                            (params) ->
                                    new LessThan(
                                            (TemplateDirective) params.getFirst(),
                                            (TemplateDirective) params.get(1))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "lte",
                            (params) ->
                                    new LessThanEq(
                                            (TemplateDirective) params.getFirst(),
                                            (TemplateDirective) params.get(1))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "in",
                            (params) ->
                                    new InFilter(
                                            (TemplateDirective) params.getFirst(),
                                            params.subList(1, params.size())
                                                    .toArray(
                                                            new TemplateDirective
                                                                    [params.size() - 1]))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "contains",
                            (params) ->
                                    new ContainsFilter(
                                            (TemplateDirective) params.getFirst(),
                                            (TemplateDirective) params.get(1))),
                    new AbstractMap.SimpleImmutableEntry<>(
                            "null", (params) -> new IsNull((TemplateDirective) params.getFirst())),
                    new AbstractMap.SimpleImmutableEntry<>("isJSON", (params -> new IsJSON())),
                    new AbstractMap.SimpleImmutableEntry<>("isXML", (params -> new IsXML())),
                    new AbstractMap.SimpleImmutableEntry<>("isPojo", (params -> new IsPojo())));

    private static final Pattern FILTER_PATTERN =
            Pattern.compile(
                    FACTORIES.keySet().stream()
                            .map(
                                    k -> {
                                        StringBuilder sb = new StringBuilder();
                                        if (hasArguments(k)) sb.append("(\\s|^)");
                                        sb.append("\\$").append(k);
                                        if (hasArguments(k)) sb.append("(\\s|$)");
                                        return sb.toString();
                                    })
                            .collect(Collectors.joining("|"))
                            .concat("|\\s\\$!"));

    private final SingleDirectiveParser innerParser;

    private static final String LEFT_PAR = "(";

    private static final String RIGHT_PAR = ")";

    private static final String DOLLAR = "$";

    public FilterParser(SingleDirectiveParser next) {
        super(next);
        this.innerParser = DirectiveParser.buildParameterChain();
    }

    @Override
    public TemplateDirective parse(String value) {
        if (FILTER_PATTERN.matcher(value).find()) {
            if (value.contains("|"))
                throw new RuntimeException(
                        "Detected | in directive %s. Filters cannot be used in pipes"
                                .formatted(value));

            Queue<Object> output = toRPN(value);
            // now we know that we have params and then operators,
            // lets build the filter.
            List<TemplateDirective> params = new LinkedList<>();
            List<Filter> fparams = new LinkedList<>();
            while (!output.isEmpty()) {
                Object o = output.poll();
                if (o instanceof TemplateDirective) {
                    params.add((TemplateDirective) o);
                } else {
                    String fname = (String) o;
                    boolean isNot = isNot(fname);
                    if (isNot) fname = fname.substring(1);
                    FilterFactory ff = FACTORIES.get(fname);
                    if (ff == null)
                        throw new TemplatingException("%s is not a know filter".formatted(fname));
                    Filter filter;
                    if (isNotOperator(fname)) {
                        filter = ff.newFilter(params);
                        params.clear();
                    } else {
                        filter = ff.newFilter(fparams);
                        fparams.clear();
                    }
                    if (isNot) filter = new Not(filter);
                    fparams.add(filter);
                }
            }
            return new FilterTemplateDirective(fparams.getFirst());
        }
        return super.parse(value);
    }

    // Shunting Yard Algorithm to obtain the reverse polish notation of the filter
    private Queue<Object> toRPN(String filter) {
        Stack<String> operators = new Stack<>();
        Queue<Object> output = new LinkedList<>();
        String[] parts = filter.split("\\s|,");
        for (String p : parts) {
            if (p.startsWith(LEFT_PAR)) {
                p = p.substring(1);
                operators.add(LEFT_PAR);
                output.add(innerParser.parse(p));
            } else if (p.startsWith(DOLLAR) && isFilter(p)) {
                String cleaned = removeDollar(p);
                String first = operators.isEmpty() ? null : operators.peek();
                while (first != null && !operators.isEmpty() && !prioritizeOver(cleaned, first)) {
                    String op = operators.pop();
                    if (!Objects.equals(op, LEFT_PAR)) output.add(op);
                    else break;
                }
                operators.add(cleaned);
            } else if (p.endsWith(RIGHT_PAR)) {
                output.add(innerParser.parse(p.substring(0, p.length() - 1)));
                String operator = operators.isEmpty() ? null : operators.pop();
                while (operator != null && !operators.isEmpty() && !operator.equals(LEFT_PAR)) {
                    output.add(operator);
                    operator = operators.pop();
                }
            } else {
                output.add(innerParser.parse(p));
            }
        }
        while (!operators.isEmpty()) {
            String operator = operators.pop();
            if (!operator.equals(LEFT_PAR)) output.add(operator);
        }
        return output;
    }

    boolean prioritizeOver(String o1, String o2) {
        return (!isAnd(o1) && !isOr(o1)) && (!isNotOperator(o1) || !isNotOperator(o2));
    }

    private boolean isNotOperator(String string) {
        return !isAnd(string) && !isOr(string);
    }

    private boolean isAnd(String string) {
        return Objects.equals("and", string);
    }

    private boolean isOr(String string) {
        return Objects.equals("or", string);
    }

    private boolean isNot(String string) {
        return string.startsWith("!");
    }

    private String removeDollar(String string) {
        if (string.startsWith("$")) {
            return string.substring(1);
        }
        return string;
    }

    @FunctionalInterface
    interface FilterFactory {
        Filter newFilter(List<?> params);
    }

    private static boolean hasArguments(String filter) {
        return filter != null
                && !Objects.equals("isXML", filter)
                && !Objects.equals("isJSON", filter);
    }

    private boolean isOperand(String part) {
        return part.startsWith("$");
    }

    private boolean isFilter(String part) {
        return FACTORIES.keySet().stream().anyMatch(k -> part.endsWith(k));
    }
}
