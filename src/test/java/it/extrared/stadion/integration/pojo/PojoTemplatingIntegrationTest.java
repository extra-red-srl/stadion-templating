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
package it.extrared.stadion.integration.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.extrared.stadion.TemplatingFacade;
import it.extrared.stadion.TemplatingFacadeImpl;
import it.extrared.stadion.catalog.CachingTemplateCatalog;
import it.extrared.stadion.catalog.DirectoryTemplateCatalog;
import it.extrared.stadion.catalog.TemplateCatalog;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.formats.TemplateType;
import it.extrared.stadion.integration.StadionIntegrationTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PojoTemplatingIntegrationTest extends StadionIntegrationTest {

    // ── POJO model ────────────────────────────────────────────────────────────

    public static class Restaurant {
        private Long id;
        private String name;
        private String address;
        private LocalDate opened;
        private Owner owner;
        private Menu menu;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public LocalDate getOpened() {
            return opened;
        }

        public void setOpened(LocalDate opened) {
            this.opened = opened;
        }

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

        public Menu getMenu() {
            return menu;
        }

        public void setMenu(Menu menu) {
            this.menu = menu;
        }
    }

    public static class Owner {
        private String name;
        private String surname;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }
    }

    public static class Menu {
        private List<Entry> entries;

        public List<Entry> getEntries() {
            return entries;
        }

        public void setEntries(List<Entry> entries) {
            this.entries = entries;
        }
    }

    public static class Entry {
        private String name;
        private Double price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }

    // ── test fixtures ─────────────────────────────────────────────────────────

    private TemplateCatalog<String> catalog;
    private TemplatingFacade<String> facade;
    private String templateId;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() throws IOException, InvalidTemplateException {
        catalog = new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        facade = new TemplatingFacadeImpl<>(catalog);
        templateId = saveTemplate(catalog, "testPojoTemplate", TemplateType.JSON);

        Owner owner = new Owner();
        owner.setName("Mario");
        owner.setSurname("Rossi");

        Entry cheap = new Entry();
        cheap.setName("water");
        cheap.setPrice(2.0);

        Entry expensive = new Entry();
        expensive.setName("pizza");
        expensive.setPrice(8.5);

        Menu menu = new Menu();
        menu.setEntries(List.of(cheap, expensive));

        restaurant = new Restaurant();
        restaurant.setId(42L);
        restaurant.setName("la bella napoli");
        restaurant.setAddress("Via Roma 1");
        restaurant.setOpened(LocalDate.of(2020, 6, 15));
        restaurant.setOwner(owner);
        restaurant.setMenu(menu);
    }

    // ── tests ─────────────────────────────────────────────────────────────────

    @Test
    public void testSimpleFieldsAndFunctions()
            throws ServiceNotFound, IOException, InvalidTemplateException {
        JsonNode result = applyAndParse(restaurant);

        // Long id written as JSON number, asText() returns its string representation
        assertEquals("42", result.at("/restaurantId").asText());
        // $uppercase function applied to String property
        assertEquals("LA BELLA NAPOLI", result.at("/restaurantName").asText());
        // $lowercase function applied to String property
        assertEquals("via roma 1", result.at("/restaurantAddress").asText());
        // $d_fmt reformats LocalDate.toString() ("2020-06-15") from yyyy-MM-dd to
        // dd/MM/yyyy
        assertEquals("15/06/2020", result.at("/openedOn").asText());
    }

    @Test
    public void testCtxDirectiveWithNestedObject()
            throws ServiceNotFound, IOException, InvalidTemplateException {
        JsonNode result = applyAndParse(restaurant);

        JsonNode owner = result.at("/owner");
        assertFalse(owner.isNull(), "owner node must be present");
        // $ctx switches context to Owner POJO; {{name}} and {{surname}} resolve on
        // Owner
        assertEquals("Mario Rossi", owner.at("/fullName").asText());
    }

    @Test
    public void testCollectionIteration()
            throws ServiceNotFound, IOException, InvalidTemplateException {
        JsonNode result = applyAndParse(restaurant);

        ArrayNode items = (ArrayNode) result.at("/menuItems");
        assertFalse(items.isNull(), "menuItems must be present");
        // Both entries are rendered (no filter)
        assertEquals(2, items.size());
        assertEquals("water", items.get(0).get("item").asText());
        assertEquals(2.0, items.get(0).get("price").asDouble());
        assertEquals("pizza", items.get(1).get("item").asText());
        assertEquals(8.5, items.get(1).get("price").asDouble());
    }

    @Test
    public void testIfFilterOnCollection()
            throws ServiceNotFound, IOException, InvalidTemplateException {
        JsonNode result = applyAndParse(restaurant);

        ArrayNode expensive = (ArrayNode) result.at("/expensiveItems");
        assertFalse(expensive.isNull(), "expensiveItems must be present");
        // Only pizza (price 8.5) passes $gte 5.0; water (price 2.0) is filtered out
        assertEquals(1, expensive.size());
        assertEquals("pizza", expensive.get(0).get("item").asText());
        assertEquals(8.5, expensive.get(0).get("price").asDouble());
    }

    @Test
    public void testConcurrentAccessOnCachedTemplate() throws Exception {
        // Verifies NodeExecutionContext immutability fix: 20 threads share the same
        // cached
        // StadionTemplate instance and apply it concurrently with independent results.
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<CompletableFuture<JsonNode>> futures =
                IntStream.range(0, threadCount)
                        .mapToObj(
                                i ->
                                        CompletableFuture.supplyAsync(
                                                () -> {
                                                    try {
                                                        return applyAndParse(restaurant);
                                                    } catch (Exception e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                },
                                                executor))
                        .toList();

        List<JsonNode> results = futures.stream().map(CompletableFuture::join).toList();
        executor.shutdown();

        for (JsonNode result : results) {
            assertEquals(
                    "LA BELLA NAPOLI",
                    result.at("/restaurantName").asText(),
                    "restaurantName must be correct in every thread result");
            assertEquals(
                    "Mario Rossi",
                    //
                    result.at("/owner/fullName").asText(),
                    "owner fullName must be correct in every thread result");
            assertEquals(
                    2,
                    ((ArrayNode) result.at("/menuItems")).size(),
                    "all menu items must be present in every thread result");
            assertEquals(
                    1,
                    ((ArrayNode) result.at("/expensiveItems")).size(),
                    "filter must work correctly in every thread result");
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private JsonNode applyAndParse(Object pojo)
            throws ServiceNotFound, IOException, InvalidTemplateException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        facade.applyTemplateOnPojo(templateId, MediaType.A_JSON, baos, pojo);
        return new ObjectMapper().readTree(baos.toByteArray());
    }
}
