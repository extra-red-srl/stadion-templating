package it.extrared.stadion.templating.directive.filter;

import com.fasterxml.jackson.databind.JsonNode;

public class IsJSON implements Filter {

    @Override
    public boolean evaluate(Object context) {
        return context instanceof JsonNode;
    }
}
