package it.extrared.stadion.integration.templating.directive.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.extrared.stadion.templating.directive.parser.DirectiveParser;
import org.junit.jupiter.api.Test;

public class FiltersTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testGt() throws JsonProcessingException {
        String json =
                """
                {
                 "value": 10
                }
                """;
        JsonNode node = OBJECT_MAPPER.readTree(json);
        String txtFilter = "{{$jpointer:'/value' $gt 9}}";
        String txtFilter2 = "{{$jpointer:'/value' $gt 20}}";
        DirectiveParser directiveParser = new DirectiveParser();
        assertTrue((Boolean) directiveParser.parse(txtFilter).run(node));
        assertFalse((Boolean) directiveParser.parse(txtFilter2).run(node));
    }

    @Test
    public void testLt() throws JsonProcessingException {
        String json =
                """
                {
                 "value": 10
                }
                """;
        JsonNode node = OBJECT_MAPPER.readTree(json);
        String txtFilter = "{{$jpointer:'/value' $lt 9}}";
        String txtFilter2 = "{{$jpointer:'/value' $lt 20}}";
        DirectiveParser directiveParser = new DirectiveParser();
        assertFalse((Boolean) directiveParser.parse(txtFilter).run(node));
        assertTrue((Boolean) directiveParser.parse(txtFilter2).run(node));
    }

    @Test
    public void testLte() throws JsonProcessingException {
        String json =
                """
                {
                 "value": 10
                }
                """;
        JsonNode node = OBJECT_MAPPER.readTree(json);
        String txtFilter = "{{$jpointer:'/value' $lte 10}}";
        String txtFilter2 = "{{$jpointer:'/value' $lte 1}}";
        DirectiveParser directiveParser = new DirectiveParser();
        assertTrue((Boolean) directiveParser.parse(txtFilter).run(node));
        assertFalse((Boolean) directiveParser.parse(txtFilter2).run(node));
    }

    @Test
    public void testContains() throws JsonProcessingException {
        String json =
                """
                {
                 "value": "abcd"
                }
                """;
        JsonNode node = OBJECT_MAPPER.readTree(json);
        String txtFilter = "{{$jpointer:'/value' $contains 'ab'}}";
        String txtFilter2 = "{{$jpointer:'/value' $contains 'cde'}}";
        DirectiveParser directiveParser = new DirectiveParser();
        assertTrue((Boolean) directiveParser.parse(txtFilter).run(node));
        assertFalse((Boolean) directiveParser.parse(txtFilter2).run(node));
    }

    @Test
    public void testIn() throws JsonProcessingException {
        String json =
                """
                {
                 "value": "a"
                }
                """;
        JsonNode node = OBJECT_MAPPER.readTree(json);
        String txtFilter = "{{$jpointer:'/value' $in 'a','b','c','d'}}";
        String txtFilter2 = "{{$jpointer:'/value' $in 'b','c','d'}}";
        DirectiveParser directiveParser = new DirectiveParser();
        assertTrue((Boolean) directiveParser.parse(txtFilter).run(node));
        assertFalse((Boolean) directiveParser.parse(txtFilter2).run(node));
    }
}
