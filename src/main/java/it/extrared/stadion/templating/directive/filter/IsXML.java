package it.extrared.stadion.templating.directive.filter;

import org.w3c.dom.Node;

public class IsXML implements Filter {

    @Override
    public boolean evaluate(Object context) {
        return context instanceof Node;
    }
}
