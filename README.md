# Stadion Templating

### Template Directives

Below is presented a template example for a JSON-LD document:

```json
{
  "@context": {
    "schema": "http://schema.org/",
    "Restaurant": "schema:Restaurant",
    "cuisine": "schema:servesCuisine",
    "menu": "schema:hasMenu",
    "items": {
      "@id": "schema:hasMenuItem",
      "@container": "@set"
    },
    "name": "schema:name",
    "address": "schema:address",
    "owner": "schema:founder",
    "foundingDate": "schema:foundingDate"
  },
  "restaurants": {
    "restaurant.{{id}}": {
      "@type": "schema:Restaraunt",
      "foundingDate": "{{opened | $datef:'dd/MM/yyyy'}}",
      "name": "{{name}}",
      "address": "{{'INDIRIZZO' | $lowercase}} {{address | $uppercase}}",
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

The extension follows a what you see is what you get approach so a JSON/JSON-LD template is actually a real JSON
document where
directives have been used in order to allow the templating engine to properly fill it. A template directive is always
wrapped
inside double graph eg. `{{name}}` and can be used both on a field name and on a field value. Multiple directive can be piped together so that the result of the leftmost
directive is passed to the next directive
until a pipe is ended eg. `{{date ! datef:'dd/MM/yyyy'}}`: in this case the value of a property named date is passed to
a function named `datef` with one argument
`dd/MM/yyyy`. A directive can be specified as part of a string so that the result of the directive evaluation will be
composed with the string enclosing it eg. `"my string with a directive in it {{someproperty}}"`.
Each directive is evaluated against the context that is the object returned by the resource method to which the template
annotation has benn applied. In order to determine the context properly the template engine works as follow:
- every time a JSON array is started in the template, the engine will check if the context is a collection/array or is a
  single object. If it is a collection the directives specified in the object inside the array (in the example above the directives inside the second JSON objects into the items array)
  will be evaluated for each element in the collection/array. Otherwise, the object is passed down as it is.
- every time a JSON object is started in the template, the engine will check again if the context is a collection/array or not.
  if it is, the engine will check as well if the field name of the object is dynamic or not (eg. the `restaurant{{id}}` field name in the example above is dynamic).
  If it is then the collection will be iterated. This additional check is done to reduce to the minimum the possibility to have
  several JSON object with same field name thus and invalid JSON document. If the field name it is not dynamic the list will be passed
  as it is down in the template.
- The current context for a specific template section can be manipulated via the $ctx property. See below for details about it.

Below the various types of directives are detailed.
`
#### Property Interpolation

Property interpolation allows to access the value of a field of Java object eg. if a class `Restaurant` has a
field `name`
the directive `{{name}}` will produce with the value of the name property. This directive needs getters to be defined
for all the property
that needs to be accessed.
The result of a property interpolation can be placed inside a static string
eg. `"Restaurnt name:{{name}}"`.
A property might be composed by several part to traverse an object graph.
As an example lets assume that we are templating a Restaurant POJO which has a field of type Menu that in turns has a
list of menù items
according to the classes below:

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
```

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
```

```java
public class Menu {

    private double coverPrice;

    private List<Entry> entries;

    // getters and setters
}
```

```java
public class Entry {

    private String name;

    private Double price;

    // getters and setters
}
```

It is possible to start from the restaurant and reach the price field inside and object of type Entry with the following
property:
`{{menu.entries.[0].name}}`. The property name will pick up the name of the first menu entry in the list held by the
menu object of
the restaurant object. The `[0]` property part is an accessor to retrieve the element of a list at a specified index (0
based).

Alternatively to property interpolation, jpointer and xpath can be used if the input to the template is a json or an xml.

#### Literals

Literals are simply literals values. The supported literals are:

* Strings in the form `'my string'`;
* Booleans.
* Integers.
* Doubles.

Literals usually appears as argument of a Function or as the static input of a pipe.

#### Functions

Functions accept one or more parameters and are usually used in the context of a pipe. A function must be specified in
the following form:
`$functionName:arg1:arg2:argN`.
For example the function `$datef:'dd/MM/yyyy'` formats a date/temporal Java object into a string with the specified
format. Most of the functions
must be used in the context of a pipe eg. `{{myDateProp | $datef:'yyyy-MM-dd'}}`: in this case the result of the
evaluation of a property
with name `myDateProp`' is passed to the function to format it according to the specified format.

The following is a list of the available functions:

| Function name | Description                                                  | Parameters number | Parameters description | Example                               |
|---------------|--------------------------------------------------------------|-------------------|------------------------|---------------------------------------|
| $lowecase     | lowercase a string                                           | 0                 |                        | {{mystrprop \| $uppercase}}           |
| $uppercase    | uppercase a string                                           | 0                 |                        | {{mystrprop \| lowercase}}            |
| $datef        | formats a date according to the format passed as a parameter | 1                 | The date format        | {{mydateprop \| $datef:'dd/MM/yyyy'}} |
| $jpointer     | evaluate a json pointer on the input (only for json input).  | 1                 | The json pointer path  | {{$jpointer: '/path/to/value'}}       |
| $xpath        | evaluate an xpath on the input (only for xml input).         | 1                 | The xpath              | {{$xpath: 'path/to/value/text()'}}    |

#### Context

Context is a special directive used to set the context for the property evaluation in a container for child fields. See
the below JSON
extracted from the example template above:

```json
{
  "$ctx": "{{owner}}",
  "name": "{{name}}",
  "surname": "{{surname}}",
  "@type": "schema:Person"
}
```

The above template part basically will set as the context for the evaluation of the fields below the `$ctx` property
the object contained inside the property `owner` of the object being evaluated.
Similarly,see the following template part:

```json
[
  {
    "$ctx": "{{entries}}"
  },
  {
    "name": "{{name}}",
    "price": "{{price}}"
  }
]
```

Every object in the array of items will be populated using the object inside the property `entries` of the object being
evaluated.

#### Math

The templating engine has math support as well. Inside graph parenthesis of a directive it is possible to place whatever
math expression eg. `{{ 9 * 3 - (100/2^10)}}` and so on. Property name can be used as variable inside the expression eg.
`{{some.numeric.prop/some.other.numeric.prop}}`.

#### If

Filters are directives that allow to control whether or not a piece of the template has to be rendered. Consider the
following
template piece:

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
The if field is telling the engine to render an array element only if the price field of the entry is greater then 5.
Consider now these template piece:

```json
{
  "scope": "{{owner}}",
  "$if": "{{name $!eq 'The name'}}",
  "name": "{{name}}",
  "surname": "{{surname}}",
  "@type": "schema:Person"
}
```

The filter in this case is telling the engine to render the entire JSON object only if the name field of the owner is not equal
to `The name`.

If the filter needs to be applied to a single field the syntax is instead the following:

```json
{
  "scope": "{{owner}}",
  "name": "{{name $!eq 'The name'}}{{name}}",
  "surname": "{{surname}}",
  "@type": "schema:Person"
}
```

The property name in this case will appear only if the filter is matched. When using a filter inline it must be always in
separate graphs from the rest of the directive.

The following is a list of supported filters.

| Filter name | Description                                                                      | Example                                        |
|-------------|----------------------------------------------------------------------------------|------------------------------------------------|
| $eq         | equals filter, same as Java ==                                                   | {{strprop $eq 'some string'}}                  |
| $gt         | greater than filter, same as Java >                                              | {{intprop $gt 5}}                              |
| $gte        | greater than or equals to filter, same as Java >=                                | {{intprop $gte 5}}                             |
| $lt         | less than filter, same as Java <                                                 | {{intprop $lt 5}}                              |
| $lte        | less than or equals to filter, same as Java <=                                   | {{intprop $lte 5}}                             |
| $in         | in filter, check the left param for equality in one of the right side parameters | {{intprop $in 1,2,3,4,5}}                      |
| $contains   | check if the left side string value contains the right side string value         | {{strprop $contains 'my string'}}              |
| $and        | put in and two filters, same as Java &&                                          | {{strprop $eq 'my string' $and intprop $gt 5}} |
| $or         | put in or two filters, same as Java \|\|                                         | {{strprop $eq 'my string' $or intprop $gt 5}}  |

Note that every filter must always be prefixed with a `$` symbol. Moreover if
the filter condition must be negated the filter needs to be prefixed with the `!` symbol eg. `$!eq` means not equal to.

