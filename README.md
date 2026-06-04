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
  (e.g. a JSON document and an XML document), with `$isJSON` / `$isXML` guards to route each section to the right
  source. Orchestrating this with Jolt, XSLT, or any single-source engine requires external glue code.
- **Familiar pipe syntax** - the `{{value | $function:arg1:arg2}}` notation mirrors Angular pipes: both the pipe
  operator `|` and the colon-separated argument syntax are identical. Frontend developers can read and write Stadion
  templates without any prior knowledge of the engine.

## Supported input/output combinations

| Input                  | Output |
|------------------------|--------|
| Java POJO              | JSON   |
| Java POJO              | XML    |
| JSON                   | JSON   |
| JSON                   | XML    |
| XML                    | JSON   |
| XML                    | XML    |
| JSON + XML (composite) | JSON   |
| JSON + XML (composite) | XML    |

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
    <version>1.0.1</version>
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

facade.applyTemplateOnPojo(templateId, MediaType.A_JSON, outputStream, myPojo);

facade.applyTemplate(templateId, MediaType.A_JSON, outputStream, MediaType.A_JSON, jsonInputStream);

facade.applyTemplate(templateId, MediaType.A_JSON, outputStream, MediaType.A_XML, xmlInputStream);

InputData xmlInput  = InputData.builder().mediaType(MediaType.A_XML).input(xmlStream).build();
InputData jsonInput = InputData.builder().mediaType(MediaType.A_JSON).input(jsonStream).build();
facade.applyTemplate(templateId, MediaType.A_JSON, outputStream, xmlInput, jsonInput);
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

### Composite input and `$isJSON` / `$isXML`

When multiple input sources are passed to `applyTemplate()`, the engine combines them into a list and the template
receives both. `$isJSON` and `$isXML` allow the template to branch on which source is currently in context,
making it possible to extract fields from different sources in the same template:

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
is XML - both can be active simultaneously when composite input is used.

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
