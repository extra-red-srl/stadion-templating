package it.extrared.stadion.templating.directive.factory;

/** This class represents the directive metadata. */
public class DirectiveInfo {

    private String name;

    private ParameterInfo[] parameters;

    public DirectiveInfo(String name, ParameterInfo... parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * @return the directive name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parameters' metadata.
     */
    public ParameterInfo[] getParameters() {
        return parameters;
    }
}
