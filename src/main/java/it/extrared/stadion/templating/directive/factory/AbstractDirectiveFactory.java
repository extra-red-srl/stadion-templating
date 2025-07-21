package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.TemplateDirective;
import java.util.List;
import java.util.stream.Stream;

/**
 * Base implementation of a {@link TemplateDirectiveFactory} that performs some basic validation
 * before creating a directive.
 */
public abstract class AbstractDirectiveFactory implements TemplateDirectiveFactory {

    protected void validate(Object... params) {
        DirectiveInfo directiveInfo = getInfo();
        ParameterInfo[] pInfos = directiveInfo.getParameters();
        List<ParameterInfo> mandatoryParams =
                Stream.of(pInfos).filter(pi -> !pi.isOptional()).toList();
        List<ParameterInfo> optionalParams =
                Stream.of(pInfos).filter(ParameterInfo::isOptional).toList();
        int mandatorySize = mandatoryParams.size();
        if (params.length != mandatorySize)
            throw new TemplatingException(
                    String.format(
                            "Directive %s has wrong number of parameters: found %s but %s are needed",
                            directiveInfo.getName(), params.length, pInfos.length));
        if (mandatorySize > 1) {
            for (int i = 0; i < mandatorySize; i++) {
                ParameterInfo pinfo = mandatoryParams.get(i);
                if (pinfo.getPos() >= params.length) {
                    throw new TemplatingException(
                            "A mandatory parameter for function %s seems to be missing at position %s"
                                    .formatted(directiveInfo.getName(), pinfo.getPos()));
                } else if (pinfo.getPos() != -1) {
                    Object p = params[pinfo.getPos()];
                    validateParamType(p, pinfo, directiveInfo);
                }
            }
        }
        int optionalSize = optionalParams.size();
        if (optionalSize > 0) {
            for (ParameterInfo pinfo : optionalParams) {
                if (pinfo.getPos() < params.length && pinfo.getPos() != -1) {
                    Object param = params[pinfo.getPos()];
                    validateParamType(param, pinfo, directiveInfo);
                }
            }
        }
    }

    private void validateParamType(Object p, ParameterInfo pinfo, DirectiveInfo directiveInfo) {
        if (p != null && !pinfo.getType().isAssignableFrom(p.getClass())) {
            throw new TemplatingException(
                    String.format(
                            "Parameter %s of function %s should be of type %s but %s was provided instead.",
                            pinfo.getPos(),
                            directiveInfo.getName(),
                            pinfo.getType().getSimpleName().toLowerCase(),
                            p.getClass().getSimpleName().toLowerCase()));
        }
    }

    @Override
    public TemplateDirective createDirective(Object... params) {
        validate(params);
        return createInternal(params);
    }

    protected abstract TemplateDirective createInternal(Object... params);
}
