package it.extrared.stadion.templating.directive.factory;

/** Metadata of a parameter of a function. */
public class ParameterInfo {

    private Class<?> type;

    private Integer pos;
    private boolean optional;

    public ParameterInfo(Class<?> type, Integer pos) {
        this(type, pos, false);
    }

    public ParameterInfo(Class<?> type, Integer pos, boolean optional) {
        this.type = type;
        this.pos = pos;
        this.optional = optional;
    }

    /**
     * @return the type of the parameter.
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Set the type of the parameter.
     *
     * @param type the param type
     * @return this object.
     */
    public ParameterInfo setType(Class<?> type) {
        this.type = type;
        return this;
    }

    public Integer getPos() {
        return pos;
    }

    public ParameterInfo setPos(Integer pos) {
        this.pos = pos;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }
}
