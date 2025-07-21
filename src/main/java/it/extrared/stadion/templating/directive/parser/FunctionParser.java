package it.extrared.stadion.templating.directive.parser;

import static it.extrared.stadion.utils.CommonUtils.hasText;
import static it.extrared.stadion.utils.CommonUtils.unwrapWhiteSpace;

import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import it.extrared.stadion.templating.directive.DirectiveProvider;
import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.factory.DirectiveInfo;
import it.extrared.stadion.templating.directive.factory.TemplateDirectiveFactory;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FunctionParser extends ChainingSingleDirectiveParser {

    private static final DirectiveProvider PROVIDER = new DirectiveProvider();

    private static final String FUN_PARAM_SEP = ":";

    private static final String FUN_STARTER = "\\$";

    static final Pattern DIRECTIVES_FUN_PATTERN;

    private static final LogWriter LOG = LogWriters.getLogger(FunctionParser.class);

    static {
        // load the directives factories programmatically allowing pluggability
        Iterator<TemplateDirectiveFactory> spis = PROVIDER.spiIterator();
        StringBuilder functions = new StringBuilder();
        while (spis.hasNext()) {
            TemplateDirectiveFactory spi = spis.next();
            DirectiveInfo info = spi.getInfo();
            if (!info.getName().equals("")) {
                functions.append(FUN_STARTER).append(info.getName()).append("|");
            }
        }
        functions.setLength(functions.length() - 1);
        DIRECTIVES_FUN_PATTERN = Pattern.compile(functions.toString());
    }

    public FunctionParser(SingleDirectiveParser next) {
        super(next);
    }

    @Override
    public TemplateDirective parse(String value) {
        Matcher matcher = DIRECTIVES_FUN_PATTERN.matcher(value);
        LiteralParser parser = new LiteralParser(null);
        TemplateDirective result = null;
        if (matcher.find()) {
            String function = matcher.group();
            // keeps only params
            String params = value.replace(function, "");
            Object[] objparams;
            if (!params.isBlank())
                objparams =
                        Stream.of(params.split(FUN_PARAM_SEP))
                                .filter(s -> hasText(s))
                                .map(s -> unwrapWhiteSpace(s))
                                .map(v -> parser.parse(v))
                                .filter(p -> p != null)
                                .map(p -> p.run(null))
                                .toArray();
            else objparams = new Object[0];
            function = function.replaceFirst(FUN_STARTER, "");
            result = PROVIDER.createDirective(function, objparams);
        }
        if (result == null) result = super.parse(value);
        LOG.debug("Directive %s result in a function.".formatted(value));
        return result;
    }
}
