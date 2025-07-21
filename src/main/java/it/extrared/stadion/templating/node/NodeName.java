package it.extrared.stadion.templating.node;

public enum NodeName {
    CTX,
    IF;

    public String xmlValue() {
        return name().toLowerCase();
    }

    public String jsonValue() {
        return "$" + name().toLowerCase();
    }
}
