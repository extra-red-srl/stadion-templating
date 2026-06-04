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
package it.extrared.stadion.integration.templating.directive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.parser.DirectiveParser;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PropertyDirectiveTest {

    public static class TestPojo {
        private String value;

        private InnerPojo innerPojo;

        private List<InnerPojo> innerPojos;

        public List<InnerPojo> getInnerPojos() {
            return innerPojos;
        }

        public void setInnerPojos(List<InnerPojo> innerPojos) {
            this.innerPojos = innerPojos;
        }

        public InnerPojo getInnerPojo() {
            return innerPojo;
        }

        public void setInnerPojo(InnerPojo innerPojo) {
            this.innerPojo = innerPojo;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class InnerPojo {
        private String subValue;

        public String getSubValue() {
            return subValue;
        }

        public void setSubValue(String subValue) {
            this.subValue = subValue;
        }
    }

    @Test
    public void testPropertyName() {
        String pn = "{{value}}";
        String pn2 = "{{innerPojo.subValue}}";
        String pn3 = "{{innerPojos.[0].subValue}}";
        TestPojo pojo = new TestPojo();
        pojo.setValue("value");
        InnerPojo innerPojo = new InnerPojo();
        innerPojo.setSubValue("subValue");
        pojo.setInnerPojo(innerPojo);
        pojo.setInnerPojos(List.of(innerPojo));
        DirectiveParser directiveParser = new DirectiveParser();
        TemplateDirective directive = directiveParser.parse(pn);
        assertEquals(pojo.getValue(), directive.run(pojo));
        directive = directiveParser.parse(pn2);
        assertEquals(pojo.getInnerPojo().getSubValue(), directive.run(pojo));
        directive = directiveParser.parse(pn3);
        assertEquals(pojo.getInnerPojos().getFirst().getSubValue(), directive.run(pojo));
    }
}
