# Stadion Templating

A Java templating engine that applies JSON or XML templates against Java POJOs, JSON documents, or XML documents to
produce JSON or XML output. Multiple input sources can be combined in a single template execution.

## Why Stadion Templating?

- **WYSIWYG templates** - the template is a valid JSON (or XML) document with the same structure as the desired output.
  No mental mapping between a transformation DSL and the result: what you write is what you get.
- **Simpler than Jolt** - Jolt's spec-based transformations require learning a dedicated DSL with non-obvious
  semantics (`shift`, `default`, `modify`, `cardinality`, â€¦). Stadion templates are plain JSON with `{{directive}}`
  placeholders, readable and editable without specialist knowledge. Missing or null values are handled inline via
  `$if_then_else` and the `$null` filter, covering the most common `default`/`cardinality` use cases.
- **Heterogeneous input in a single pass** - a single template execution can draw from multiple sources simultaneously
  (e.g. a JSON document, an XML document, and a Java POJO), with `$isJSON` / `$isXML` / `$isPojo` guards to route
  each section to the right source. Orchestrating this with Jolt, XSLT, or any single-source engine requires external
  glue code.
- **Familiar pipe syntax** - the `{{value | $function:arg1:arg2}}` notation mirrors Angular pipes: both the pipe
  operator `|` and the colon-separated argument syntax are identical. Frontend developers can read and write Stadion
  templates without any prior knowledge of the engine.

## Supported input/output combinations

| Input                         | Output |
|-------------------------------|--------|
| Java POJO                     | JSON   |
| Java POJO                     | XML    |
| JSON                          | JSON   |
| JSON                          | XML    |
| XML                           | JSON   |
| XML                           | XML    |
| JSON + XML (composite)        | JSON   |
| JSON + XML (composite)        | XML    |
| POJO + JSON (composite)       | JSON   |
| POJO + JSON (composite)       | XML    |
| POJO + XML (composite)        | JSON   |
| POJO + XML (composite)        | XML    |
| POJO + JSON + XML (composite) | JSON   |
| POJO + JSON + XML (composite) | XML    |

## Installation

Add the JitPack repository and the dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.extra-red-srl</groupId>
    <artifactId>stadion-templating</artifactId>
    <version>1.0.2</version>
</dependency>
```

Replace `OWNER` with the GitHub user or organisation that hosts the repository, and `v1.0.1` with the desired
[release tag](https://github.com/OWNER/stadion-templating/releases).

For Gradle:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.OWNER:stadion-templating:v1.0.1'
}
```

## Quick start

```java
TemplateCatalog<String> catalog =
        new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(templatesPath));
TemplatingFacade<String> facade = new TemplatingFacadeImpl<>(catalog);

// single POJO input
facade.applyTemplate(templateId, MediaType.A_JSON, outputStream,
        InputData.pojoInputData(myPojo));

// single JSON input
facade.applyTemplate(templateId, MediaType.A_JSON, outputStream,
        InputData.jsonInputData(jsonInputStream));

// single XML input
facade.applyTemplate(templateId, MediaType.A_XML, outputStream,
        InputData.xmlInputData(xmlInputStream));

// composite: POJO + JSON + XML
facade.applyTemplate(templateId, MediaType.A_JSON, outputStream,
        InputData.pojoInputData(myPojo),
        InputData.jsonInputData(jsonInputStream),
        InputData.xmlInputData(xmlInputStream));
```

## Template Catalog

The `TemplateCatalog<ID>` interface is the storage and retrieval abstraction for compiled templates.
`ID` is the type of the template identifier — both built-in implementations use `String`.

### Built-in implementations

#### `DirectoryTemplateCatalog` — filesystem directory

Reads and writes templates from a directory on the local filesystem. The template identifier is the
full filename (e.g. `myTemplate.json`).

```java
// from a path string, a Path, or a URI
TemplateCatalog<String> catalog = new DirectoryTemplateCatalog("/opt/templates");
TemplateCatalog<String> catalog = new DirectoryTemplateCatalog(Path.of("/opt/templates"));
```

#### `ResourcesTemplateCatalog` — classpath

Read-only catalog that loads templates bundled inside the application JAR from the
`/stadion-templates/` resource directory. `saveTemplate` throws `UnsupportedOperationException`.

```java
TemplateCatalog<String> catalog = new ResourcesTemplateCatalog();
```

Place templates at `src/main/resources/stadion-templates/myTemplate.json` and load them by name:

```java
facade.loadTemplateByName("myTemplate", MediaType.A_JSON);
```

#### `CachingTemplateCatalog` — in-memory cache decorator

Wraps any `TemplateCatalog` and caches compiled templates so the underlying storage is only hit on
a miss. Default settings: 200 entries, 10-minute TTL (Caffeine cache).

```java
// default settings
TemplateCatalog<String> catalog = new CachingTemplateCatalog<>(new DirectoryTemplateCatalog("/opt/templates"));

// custom capacity and TTL
TemplateCatalog<String> catalog = new CachingTemplateCatalog<>(
        new DirectoryTemplateCatalog("/opt/templates"), 500, Duration.ofMinutes(30));
```

`saveTemplate` automatically evicts the affected entry. For external updates (another process,
cluster node) call `invalidate(id)` or `invalidateAll()` explicitly.

### `TemplateCatalog` API

| Method | Description |
|--------|-------------|
| `saveTemplate(byte[] content, TemplateMetadata<ID> metadata)` | Persists a new template; returns metadata enriched with the generated `id` |
| `loadTemplateByName(String name, MediaType mediaType)` | Loads and compiles a template by logical name |
| `loadTemplateById(ID id, MediaType mediaType)` | Loads and compiles a template by its identifier |
| `loadTemplateContent(ID id)` | Returns the raw template bytes without compiling |
| `findOne(ID id)` | Returns the `TemplateMetadata` for the given identifier |
| `searchTemplates(SearchParams params)` | Returns all templates matching name and/or type criteria |

### `TemplateMetadata<ID>`

Each template is described by a `TemplateMetadata<ID>` record:

| Field | Type | Description |
|-------|------|-------------|
| `id` | `ID` | Unique identifier assigned by the catalog (e.g. filename) |
| `name` | `String` | Logical name of the template (without extension) |
| `templateType` | `TemplateType` | File format: `JSON` or `XML` |

```java
TemplateMetadata<String> meta = catalog.findOne("myTemplate.json");
System.out.println(meta.getName());         // "myTemplate"
System.out.println(meta.getTemplateType()); // JSON

SearchParams params = new SearchParams();
params.setTemplateType(TemplateType.JSON);
List<TemplateMetadata<String>> jsonTemplates = catalog.searchTemplates(params);
```

### Custom catalog

To integrate with a different backend (database, object storage, remote service), extend
`AbstractTemplateCatalog<ID>` and implement the abstract storage methods:

```java
public class DatabaseTemplateCatalog extends AbstractTemplateCatalog<Long> {

    @Override
    public byte[] loadTemplateContent(Long id) {
        // load raw bytes from DB by id
    }

    @Override
    public TemplateMetadata<Long> saveTemplate(byte[] content, TemplateMetadata<Long> metadata)
            throws InvalidTemplateException, IOException {
        validate(metadata); // provided by AbstractTemplateCatalog
        // persist content and metadata, assign generated id
        return metadata;
    }

    @Override
    public TemplateMetadata<Long> findOne(Long id) { ... }

    @Override
    public List<TemplateMetadata<Long>> searchTemplates(SearchParams params) { ... }
}
```

Wrap it with `CachingTemplateCatalog` to add transparent caching without any extra code:

```java
TemplateCatalog<Long> catalog = new CachingTemplateCatalog<>(new DatabaseTemplateCatalog(...));
```

## Template Directives

A template is a JSON (or XML) document where directives have been placed to drive the transformation.
A directive is always wrapped in double braces: `{{directive}}` and can appear both as a field name and as a field value.
Multiple directives can be piped together with `|` so the output of the left-hand side becomes the input of the
right-hand side: `{{opened | $d_fmt:'dd/MM/yyyy':'yyyy-MM-dd'}}`.
A directive can also be embedded inside a static string: `"Restaurant: {{name}}"`.

The engine determines the evaluation context as follows:

- **Array in the template** if the current context is a collection, the template object inside the array is evaluated
  for each element. Otherwise the single object is passed through.
- **Object in the template** if the context is a collection *and* the field name is dynamic (e.g. `"restaurant.{{id}}"`),
  the collection is iterated to avoid duplicate field names. If the field name is static, the collection is passed down
  as-is.
- **`$ctx`** - explicitly switches the evaluation context for the enclosing section. See [Context](#context) below.

Below is a complete template example for a JSON-LD document:

```json
{
  "@context": {
    "schema": "http://schema.org/"
  },
  "restaurants": {
    "restaurant.{{id}}": {
      "@type": "schema:Restaurant",
      "foundingDate": "{{opened | $d_fmt:'dd/MM/yyyy':'yyyy-MM-dd'}}",
      "name": "{{name}}",
      "address": "{{'INDIRIZZO' | $lower}} {{address | $upper}}",
      "owner": {
        "@type": "schema:Person",
        "$ctx": "{{owner}}",
        "name": "{{name}} {{surname}}"
      },
      "menu": {
        "@type": "schema:Menu",
        "$ctx": "{{menu}}",
        "items": [
          {
            "$ctx": "{{entries}}"
          },
          {
            "name": "{{name}}",
            "price": "{{price}}"
          }
        ]
      }
    }
  }
}
```

---

### Property Interpolation

Accesses a field of a Java object via its getter. `{{name}}` returns the value of the `getName()` method.
Getters must be defined for all accessed fields.

A dotted path traverses an object graph: `{{menu.entries.[0].name}}` navigates from the root object to
`getMenu()`, then `getEntries()`, then takes the element at index 0, then calls `getName()`.
The `[N]` notation accesses the element at index N (0-based) of any `List`.

```java
public class Restaurant {
    private Long id;
    private String name;
    private Owner owner;
    private String address;
    private Menu menu;
    private LocalDate opened;
    // getters and setters
}

public class Owner {
    private String name;
    private String surname;
    // getters and setters
}

public class Menu {
    private double coverPrice;
    private List<Entry> entries;
    // getters and setters
}

public class Entry {
    private String name;
    private Double price;
    // getters and setters
}
```

For JSON and XML input, use `$jpointer` and `$xpath` respectively instead of property paths.

---

### Literals

Literal values can be used as the starting value of a pipe or as function arguments.

| Type    | Syntax              | Example        |
|---------|---------------------|----------------|
| String  | `'value'`           | `'hello'`      |
| Boolean | `true` / `false`    | `true`         |
| Integer | plain number        | `42`           |
| Double  | number with `.`     | `3.14`         |

---

### Functions

Functions are invoked as `$functionName:arg1:arg2` and are typically used in pipes.

| Function                                     | Description                                                           | Parameters | Example                                             |
|----------------------------------------------|-----------------------------------------------------------------------|------------|-----------------------------------------------------|
| `$lower`                                     | Lowercases a string                                                   | 0          | `{{name \| $lower}}`                                |
| `$upper`                                     | Uppercases a string                                                   | 0          | `{{name \| $upper}}`                                |
| `$titlecase`                                 | Title-cases a string                                                  | 0          | `{{name \| $titlecase}}`                            |
| `$d_fmt:'srcFmt':'tgtFmt'`                   | Parses a date string with `srcFmt` and reformats it with `tgtFmt`     | 2          | `{{dob \| $d_fmt:'dd/MM/yyyy':'yyyy-MM-dd'}}`       |
| `$dt_fmt:'srcFmt':'tgtFmt'`                  | Parses a datetime string with `srcFmt` and reformats it with `tgtFmt` | 2          | `{{ts \| $dt_fmt:'dd/MM/yyyy HH:mm':'yyyy-MM-dd'}}` |
| `$replace:'search':'replacement'`            | Replaces all occurrences of `search` with `replacement`               | 2          | `{{code \| $replace:'p:2:':''}}`                    |
| `$jpointer:'/path'`                          | Evaluates a JSON Pointer on the current context (JSON input only)     | 1          | `{{$jpointer:'/producer/name'}}`                    |
| `$xpath:'expr'`                              | Evaluates an XPath expression on the current context (XML input only) | 1          | `{{$xpath:'//producer/name/text()'}}`               |
| `$if_then_else:'filter':'thenVal':'elseVal'` | Returns `thenVal` if `filter` is true, `elseVal` otherwise            | 3          | `{{$if_then_else:'price $null':'0':'price'}}`       |
| `$this`                                      | Returns the current context object unchanged                          | 0          | `{{$this}}`                                         |
| `$int`                                       | Converts the value to an integer                                      | 0          | `{{amount \| $int}}`                                |
| `$double`                                    | Converts the value to a double                                        | 0          | `{{amount \| $double}}`                             |
| `$string`                                    | Converts the value to a string                                        | 0          | `{{id \| $string}}`                                 |

---

### Context

`$ctx` switches the evaluation context for the fields of the enclosing object or array iteration.

```json
{
  "$ctx": "{{owner}}",
  "name": "{{name}}",
  "surname": "{{surname}}"
}
```

`{{name}}` and `{{surname}}` are evaluated on the `Owner` object, not on the root object.

In an array, `$ctx` switches the context for each iterated element:

```json
[
  { "$ctx": "{{entries}}" },
  {
    "name": "{{name}}",
    "price": "{{price}}"
  }
]
```

---

### Math

Arithmetic expressions are supported inside `{{ }}`:

```
{{ 9 * 3 - (100 / 2^10) }}
{{some.numeric.prop / some.other.numeric.prop}}
```

---

### If

`$if` controls whether a section of the template is rendered. It can appear on an array context element
(filters which collection items are output) or on a plain object (renders or skips the whole object).

**Filter array items:**
```json
[
  {
    "$ctx": "{{entries}}",
    "$if": "{{price $gt 5}}"
  },
  {
    "name": "{{name}}",
    "price": "{{price}}"
  }
]
```

**Conditionally render an object:**
```json
{
  "$ctx": "{{owner}}",
  "$if": "{{name $!eq 'The name'}}",
  "name": "{{name}}",
  "surname": "{{surname}}"
}
```

**Conditionally render a single field value** (filter and value must be in separate `{{ }}`):
```json
{
  "name": "{{name $!eq 'The name'}}{{name}}"
}
```

---

### Filters

Filters are boolean expressions used inside `$if` or `$if_then_else`. Every filter keyword is prefixed with `$`.
Negate any filter by inserting `!` after `$`: `$!eq` means "not equal".

| Filter      | Description                                 | Example                                  |
|-------------|---------------------------------------------|------------------------------------------|
| `$eq`       | Equal to                                    | `{{strProp $eq 'value'}}`                |
| `$gt`       | Greater than                                | `{{numProp $gt 5}}`                      |
| `$gte`      | Greater than or equal to                    | `{{numProp $gte 5}}`                     |
| `$lt`       | Less than                                   | `{{numProp $lt 5}}`                      |
| `$lte`      | Less than or equal to                       | `{{numProp $lte 5}}`                     |
| `$in`       | Value equals one of a comma-separated list  | `{{strProp $in 'a','b','c'}}`            |
| `$contains` | String contains substring                   | `{{strProp $contains 'sub'}}`            |
| `$null`     | Value is null                               | `{{prop $null}}`                         |
| `$isJSON`   | Current context is a JSON node              | `{{$isJSON}}`                            |
| `$isXML`    | Current context is an XML node              | `{{$isXML}}`                             |
| `$isPojo`   | Current context is a Java POJO              | `{{$isPojo}}`                            |
| `$and`      | Logical AND of two filters                  | `{{strProp $eq 'x' $and numProp $gt 5}}` |
| `$or`       | Logical OR of two filters                   | `{{strProp $eq 'x' $or numProp $gt 5}}`  |

---

### Inline

When `{{$inlineN}}` (where N is any digit, e.g. `{{$inline1}}`, `{{$inline2}}`) is used as a **field name**, the
engine suppresses the wrapping object or array boundary and writes its contents directly into the parent level.
This flattens or "inlines" the output of the enclosed section into the surrounding object.

The digit suffix exists only to ensure the template remains valid JSON (object keys must be unique within the same
object). All inline markers, regardless of their number, behave identically.

**Without inline** the iterated fields would be nested inside an unnamed array:
```json
{
  "productId": "...",
  "submodelFields": [
    { "$ctx": "{{$jpointer:'/submodels'}}"},
    { "name": "{{$jpointer:'/idShort'}}"}
  ]
}
```
Output: `{ "productId": "...", "submodelFields": [{"name": "Nameplate"}, {"name": "TechnicalData"}] }`

**With inline** the fields from each iterated element are written directly at the enclosing object level:
```json
{
  "productId": "...",
  "{{$inline1}}": [
    { "$ctx": "{{$jpointer:'/submodels'}}"},
    {
      "$if": "{{$jpointer:'/idShort' $eq 'Nameplate'}}",
      "manufacturerName": "{{$jpointer:'/value'}}"
    }
  ]
}
```
Output: `{ "productId": "...", "manufacturerName": "Extrared SRL" }`

Inline sections can be nested to traverse multiple collection levels and flatten their results at any depth:

```json
{
  "productId": "{{$jpointer:'/id' | $replace:'urn:uuid:':''}}",
  "{{$inline1}}": [
    { "$ctx": "{{$jpointer:'/submodels'}}"},
    {
      "$if": "{{$jpointer:'/idShort' $eq 'DigitalProductPassport'}}",
      "{{$inline2}}": [
        { "$ctx": "{{$jpointer:'/submodelElements'}}"},
        {
          "$if": "{{$jpointer:'/idShort' $eq 'BatchId'}}",
          "batchId": "{{$jpointer:'/value'}}"
        }
      ]
    }
  ]
}
```

Output: `{ "productId": "a1b2-...", "batchId": "BATCH-001" }`

The inner `{{$inline2}}` iterates `submodelElements` and writes matching fields directly into the root object,
bypassing both the `submodels` and `submodelElements` nesting.

---

### Composite input and `$isJSON` / `$isXML` / `$isPojo`

When multiple input sources are passed to `applyTemplate()`, the engine combines them into a list and the template
receives all of them. `$isJSON`, `$isXML`, and `$isPojo` allow the template to branch on which source is currently
in context, making it possible to extract fields from different source types in the same template execution:

```json
[
  {
    "$if": "{{$isJSON}}",
    "$ctx": "{{$jpointer:'/composition/materials'}}"
  },
  {
    "$if": "{{$isXML}}",
    "$ctx": "{{$xpath:'//materials/material'}}"
  },
  {
    "name": "{{name}}",
    "quantity": "{{quantity}}"
  }
]
```

This template renders material items from a JSON source if the context is JSON, or from an XML source if the context
is XML. With composite input all active sources are iterated simultaneously.

When a POJO is combined with JSON or XML sources, use `$isPojo` to branch to property-path directives:

```json
{
  "name": "{{$isPojo}}{{name}}",
  "{{$inline1}}": [
    { "$if": "{{$isJSON}}", "$ctx": "{{$jpointer:'/extra'}}" },
    { "extraField": "{{$jpointer:'/value'}}" }
  ]
}
```

---

## XML Templates

XML templates follow the same principles as JSON templates: the template is a valid XML document with the same
structure as the desired output, enriched with `{{directive}}` expressions and `stadion:` attributes.

The `stadion` namespace must be declared on the root element:

```xml
xmlns:stadion="http://localhost:8080/stadion"
```

### Dynamic values

Any element text content or attribute value may contain one or more `{{...}}` expressions. Multiple expressions in
the same string are concatenated at runtime:

```xml
<fullAddress>{{$xpath:'city/text()'}} {{$xpath:'country/text()'}}</fullAddress>
```

### `stadion:ctx` — context switch

Narrows the evaluation scope for the element and all its children. Equivalent to `$ctx` in JSON:

```xml
<manufacturer stadion:ctx="{{$xpath:'producer'}}">
    <name>{{$xpath:'companyName/text()'}}</name>
    <city>{{$xpath:'address/text()'}}</city>
</manufacturer>
```

### `stadion:collection` — repeating elements

Marks the element as a collection node. The engine iterates over all items in the context and emits the
element's children once per item. Equivalent to a JSON array:

```xml
<materials stadion:collection="true" stadion:ctx="{{$xpath:'composition/materials'}}">
    <material>{{$xpath:'text()'}}</material>
</materials>
```

### `stadion:if` — conditional rendering

The element and all its children are emitted only when the filter expression evaluates to `true`.
The same filter operators available in JSON (`eq`, `not`, `isNull`, `gt`, `gte`, `lt`, `lte`, `and`, `or`,
`in`, `contains`) can be used:

```xml
<recycled stadion:if="{{not(isNull($xpath:'recycled/text()'))}}">
    {{$xpath:'recycled/text()'}}
</recycled>
```

### `<stadion:inline>` — transparent wrapper

Suppresses the wrapper element tag and writes its children directly into the parent. Equivalent to `{{$inlineN}}`
in JSON. Use `<stadion:inline>` as the element name:

```xml
<stadion:inline>
    <efficiency>{{$xpath:'efficiency/text()'}}</efficiency>
    <producedOn>{{$xpath:'producedOn/text()'}}</producedOn>
</stadion:inline>
```

Output: `<efficiency>10</efficiency><producedOn>22/04/2024</producedOn>` — no wrapper tag is emitted.

Combinable with `stadion:collection` to iterate and flatten a collection:

```xml
<stadion:inline stadion:collection="true" stadion:ctx="{{$xpath:'composition/materials'}}">
    <material>{{$xpath:'text()'}}</material>
</stadion:inline>
```

Output: one `<material>` element per item, written directly into the parent without any wrapper.

### Complete XML template example

```xml
<Product xmlns:stadion="http://localhost:8080/stadion"
         stadion:ctx="{{$xpath:'/Product'}}">

    <name>{{$xpath:'@name'}}</name>

    <!-- context switch: children resolved relative to producer -->
    <manufacturer stadion:ctx="{{$xpath:'producer'}}">
        <location>{{$xpath:'address/text()'}} {{$xpath:'city/text()'}}</location>
        <name>{{$xpath:'companyName/text()'}}</name>
    </manufacturer>

    <!-- collection: one <material> per node -->
    <materials stadion:collection="true" stadion:ctx="{{$xpath:'composition/materials'}}">
        <material>{{$xpath:'text()'}}</material>
    </materials>

    <!-- conditional: only emitted when recycled is present -->
    <recycled stadion:if="{{not(isNull($xpath:'recycled/text()'))}}">
        {{$xpath:'recycled/text()'}}
    </recycled>

    <!-- inline: children go straight into <Product>, no wrapper tag -->
    <stadion:inline>
        <efficiency>{{$xpath:'efficiency/text()'}}</efficiency>
        <producedOn>{{$xpath:'producedOn/text()'}}</producedOn>
    </stadion:inline>

</Product>
```

---

## Extension Points (SPI)

Stadion Templating is built on the Java `ServiceLoader` SPI, so every major subsystem can be replaced
or extended by providing your own implementation and registering it in
`META-INF/services/`.

### 1 — Custom directive / function (`TemplateDirectiveFactory`)

Implement `TemplateDirectiveFactory` to add a new `{{$myFunction:arg}}` directive callable from templates.

```java
public class Sha256Factory extends AbstractDirectiveFactory {

    @Override
    public DirectiveInfo getInfo() {
        return new DirectiveInfo("sha256"); // no parameters
    }

    @Override
    public TemplateDirective createDirective(Object... params) {
        validate(params);
        return neew Sha256Directive()
    }
}
```

Register in `META-INF/services/it.extrared.stadion.templating.directive.factory.TemplateDirectiveFactory`:

```
com.example.Sha256Factory
```

The directive is then available in templates as `{{value | $sha256}}`.

### 2 — Custom logging backend (`LogWriterFactory`)

By default the engine logs via JUL (`java.util.logging`). Replace it by implementing `LogWriterFactory`
(and optionally a thin `LogWriter` adapter for your framework):

```java
public class Slf4jLogWriterFactory implements LogWriterFactory {

    @Override
    public LogWriter getLogger(Class<?> clazz) {
        return new Slf4jLogWriter(LoggerFactory.getLogger(clazz));
    }
}
```

Register in `META-INF/services/it.extrared.stadion.log.LogWriterFactory`:

```
com.example.Slf4jLogWriterFactory
```

Only the first registered factory is used; if none is found the built-in JUL factory is used as fallback.
