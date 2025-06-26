# Pebble Template Syntax Guide

This guide covers the complete Pebble template syntax used in Logimos FileKit. Pebble is a powerful templating engine that provides a clean, readable syntax for generating dynamic content.

## Table of Contents

1. [Basic Syntax](#basic-syntax)
2. [Variables](#variables)
3. [Filters](#filters)
4. [Functions](#functions)
5. [Control Structures](#control-structures)
6. [Template Inheritance](#template-inheritance)
7. [Includes](#includes)
8. [Macros](#macros)
9. [Comments](#comments)
10. [Whitespace Control](#whitespace-control)
11. [FileKit-Specific Features](#filekit-specific-features)

## Basic Syntax

Pebble uses double curly braces `{{ }}` for output and `{% %}` for control structures.

```pebble
{# This is a comment #}
Hello, {{ name }}!

{% if user.isLoggedIn %}
    Welcome back, {{ user.name }}!
{% else %}
    Please log in.
{% endif %}
```

## Variables

### Basic Variable Output

```pebble
{{ variableName }}
{{ user.name }}
{{ user.address.street }}
```

### Safe Navigation

```pebble
{{ user?.address?.street }}
{{ user?.name | default(value="Anonymous") }}
```

### Array/List Access

```pebble
{{ users[0].name }}
{{ items[1] }}
{{ list[userIndex] }}
```

## Filters

Filters transform variables using the pipe operator `|`.

### Basic Filter Usage

```pebble
{{ "hello world" | upper }}
{{ userName | capitalize }}
{{ text | lower | trim }}
```

### Filters with Arguments

```pebble
{{ list | join(sep=", ") }}
{{ date | date(pattern="yyyy-MM-dd") }}
{{ value | default(value="fallback") }}
```

### FileKit Built-in Filters

#### Case Conversion Filters

```pebble
{# Input: "hello world" #}
{{ "hello world" | kebab }}     {# Output: "hello-world" #}
{{ "hello world" | snake }}     {# Output: "hello_world" #}
{{ "hello world" | scream }}    {# Output: "HELLO_WORLD" #}
{{ "hello world" | camel }}     {# Output: "helloWorld" #}
{{ "hello world" | pascal }}    {# Output: "HelloWorld" #}
```

#### Text Transformation Filters

```pebble
{{ "hello" | capitalize }}      {# Output: "Hello" #}
{{ "HELLO" | lower }}          {# Output: "hello" #}
{{ "hello" | upper }}          {# Output: "HELLO" #}
{{ "Hello World!" | slug }}    {# Output: "hello-world" #}
```

#### Pluralization Filters

```pebble
{{ "city" | plural }}          {# Output: "cities" #}
{{ "cities" | singular }}      {# Output: "city" #}
{{ "user" | plural }}          {# Output: "users" #}
{{ "users" | singular }}       {# Output: "user" #}
```

#### Utility Filters

```pebble
{# Default value for null #}
{{ none | default(value="fallback") }}

{# Join arrays #}
{{ ["a", "b", "c"] | join(sep="_") }}

{# Date formatting #}
{{ date | date(pattern="yyyy-MM-dd") }}
```

## Functions

Functions are called without the pipe operator.

### FileKit Built-in Functions

```pebble
{# Current timestamp #}
{{ now() }}
{{ now(pattern="yyyy-MM-dd HH:mm:ss") }}

{# Generate UUID #}
{{ uuid() }}
```

### Function with Arguments

```pebble
{{ functionName(arg1, arg2) }}
{{ customFunction(param1="value1", param2="value2") }}
```

## Control Structures

### If Statements

```pebble
{% if condition %}
    Content when true
{% endif %}

{% if user.isActive %}
    Welcome, {{ user.name }}!
{% endif %}

{% if age >= 18 %}
    You are an adult.
{% else %}
    You are a minor.
{% endif %}
```

### If-Else Statements

```pebble
{% if user.isLoggedIn %}
    Welcome back!
{% else %}
    Please log in.
{% endif %}
```

### If-Elsif-Else Statements

```pebble
{% if user.role == "admin" %}
    Admin panel
{% elsif user.role == "moderator" %}
    Moderator panel
{% else %}
    User panel
{% endif %}
```

### For Loops

```pebble
{% for item in items %}
    {{ item.name }}
{% endfor %}

{% for user in users %}
    <li>{{ user.name }} - {{ user.email }}</li>
{% endfor %}
```

### For Loop with Index

```pebble
{% for item in items %}
    {{ loop.index }}. {{ item.name }}
{% endfor %}
```

### For Loop Variables

```pebble
{% for item in items %}
    {% if loop.first %}
        First item: {{ item.name }}
    {% endif %}
    
    {{ loop.index }}. {{ item.name }}
    
    {% if loop.last %}
        Last item: {{ item.name }}
    {% endif %}
{% endfor %}
```

### For Loop with Range

```pebble
{% for i in 1..10 %}
    {{ i }}
{% endfor %}

{% for i in 0..items.size - 1 %}
    {{ items[i].name }}
{% endfor %}
```

### While Loops

```pebble
{% set counter = 0 %}
{% while counter < 5 %}
    {{ counter }}
    {% set counter = counter + 1 %}
{% endwhile %}
```

### Set Variables

```pebble
{% set name = "John" %}
{% set fullName = firstName ~ " " ~ lastName %}
{% set items = ["apple", "banana", "cherry"] %}
```

## Template Inheritance

### Base Template (base.peb)

```pebble
<!DOCTYPE html>
<html>
<head>
    <title>{% block title %}Default Title{% endblock %}</title>
    {% block head %}{% endblock %}
</head>
<body>
    <header>
        {% block header %}
            <h1>Default Header</h1>
        {% endblock %}
    </header>
    
    <main>
        {% block content %}
            Default content
        {% endblock %}
    </main>
    
    <footer>
        {% block footer %}
            <p>Generated on {{ now() }}</p>
        {% endblock %}
    </footer>
</body>
</html>
```

### Child Template (child.peb)

```pebble
{% extends "base.peb" %}

{% block title %}My Page{% endblock %}

{% block head %}
    <link rel="stylesheet" href="/styles.css">
{% endblock %}

{% block content %}
    <h2>Welcome to {{ pageName }}</h2>
    <p>Hello, {{ userName }}!</p>
{% endblock %}
```

### Block Nesting

```pebble
{% block content %}
    <div class="container">
        {% block inner %}
            <p>Inner content</p>
        {% endblock %}
    </div>
{% endblock %}
```

## Includes

### Basic Include

```pebble
{% include "header.peb" %}
{% include "footer.peb" %}
```

### Include with Variables

```pebble
{% include "user-card.peb" with { user: currentUser } %}
{% include "product.peb" with { product: item, showPrice: true } %}
```

### Include with Context

```pebble
{% include "partial.peb" only %}
{% include "partial.peb" ignore missing %}
```

## Macros

### Defining Macros

```pebble
{% macro input(name, value="", type="text") %}
    <input type="{{ type }}" name="{{ name }}" value="{{ value }}">
{% endmacro %}

{% macro button(text, type="button") %}
    <button type="{{ type }}">{{ text }}</button>
{% endmacro %}
```

### Using Macros

```pebble
{% from "forms.peb" import input, button %}

<form>
    {{ input("username") }}
    {{ input("password", type="password") }}
    {{ button("Submit", type="submit") }}
</form>
```

### Importing Macros

```pebble
{% import "forms.peb" as forms %}

<form>
    {{ forms.input("username") }}
    {{ forms.button("Submit") }}
</form>
```

## Comments

### Single-line Comments

```pebble
{# This is a comment #}
{{ name }} {# Display the name #}
```

### Multi-line Comments

```pebble
{#
    This is a multi-line comment.
    It can span multiple lines.
    Useful for complex documentation.
#}
```

## Whitespace Control

### Trim Whitespace

```pebble
{%- for item in items -%}
    {{ item.name }}
{%- endfor -%}
```

### Preserve Whitespace

```pebble
{% for item in items %}
    {{ item.name }}
{% endfor %}
```

## FileKit-Specific Features

### Code Generation Examples

#### Kotlin Class Generation

```pebble
data class {{ className | pascal }}(
    {% for field in fields %}
    val {{ field.name | camel }}: {{ field.type }}{% if not loop.last %},{% endif %}
    {% endfor %}
) {
    {% for method in methods %}
    fun {{ method.name | camel }}({% for param in method.parameters %}{{ param.name | camel }}: {{ param.type }}{% if not loop.last %}, {% endif %}{% endfor %}): {{ method.returnType }} {
        // Implementation
    }
    {% endfor %}
}
```

#### API Controller Generation

```pebble
@RestController
@RequestMapping("/api/{{ entityName | plural | kebab }}")
class {{ entityName | pascal }}Controller {
    
    @GetMapping
    fun get{{ entityName | plural | pascal }}(): List<{{ entityName | pascal }}> {
        // Implementation
    }
    
    @GetMapping("/{id}")
    fun get{{ entityName | pascal }}ById(@PathVariable id: String): {{ entityName | pascal }} {
        // Implementation
    }
    
    @PostMapping
    fun create{{ entityName | pascal }}(@RequestBody {{ entityName | camel }}: {{ entityName | pascal }}): {{ entityName | pascal }} {
        // Implementation
    }
}
```

#### Configuration File Generation

```pebble
app:
  name: {{ appName }}
  version: {{ version }}
  generated: {{ now(pattern="yyyy-MM-dd HH:mm:ss") }}

database:
  host: {{ database.host }}
  port: {{ database.port }}
  name: {{ database.name }}

features:
  {% for feature in features %}
  - {{ feature }}
  {% endfor %}

{% if hasAuth %}
auth:
  enabled: true
  provider: {{ authProvider }}
{% endif %}
```

### Advanced Template Patterns

#### Conditional File Generation

```pebble
{% if generateTests %}
// Test file
@Test
fun `test {{ className | camel }} creation`() {
    val {{ className | camel }} = {{ className | pascal }}(
        {% for field in fields %}
        {{ field.name | camel }} = "test-{{ field.name | kebab }}"{% if not loop.last %},{% endif %}
        {% endfor %}
    )
    
    assertNotNull({{ className | camel }})
}
{% endif %}
```

#### Dynamic Import Statements

```pebble
package {{ packageName }}

{% for import in imports %}
import {{ import }}
{% endfor %}

{% if hasSpringAnnotations %}
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
{% endif %}
```

## Best Practices

### 1. Use Descriptive Variable Names

```pebble
{# Good #}
{{ user.firstName | capitalize }}

{# Avoid #}
{{ u.fn | cap }}
```

### 2. Chain Filters Logically

```pebble
{# Good #}
{{ "hello world" | trim | capitalize }}

{# Avoid #}
{{ "hello world" | capitalize | trim }}
```

### 3. Use Comments for Complex Logic

```pebble
{# Generate different content based on user role #}
{% if user.role == "admin" %}
    {# Admin sees all data #}
    {% for item in allItems %}
        {{ item.name }}
    {% endfor %}
{% else %}
    {# Regular users see filtered data #}
    {% for item in user.items %}
        {{ item.name }}
    {% endfor %}
{% endif %}
```

### 4. Leverage Template Inheritance

```pebble
{# base.peb - Define common structure #}
{% block content %}{% endblock %}

{# specific.peb - Extend and customize #}
{% extends "base.peb" %}
{% block content %}
    Specific content here
{% endblock %}
```

### 5. Use Macros for Reusable Components

```pebble
{% macro formField(name, label, type="text") %}
    <div class="form-group">
        <label for="{{ name }}">{{ label }}</label>
        <input type="{{ type }}" id="{{ name }}" name="{{ name }}" class="form-control">
    </div>
{% endmacro %}
```

## Common Patterns

### 1. Conditional Rendering

```pebble
{% if items is not empty %}
    <ul>
    {% for item in items %}
        <li>{{ item.name }}</li>
    {% endfor %}
    </ul>
{% else %}
    <p>No items found.</p>
{% endif %}
```

### 2. Dynamic Class Names

```pebble
<div class="item {% if item.isActive %}active{% endif %}">
    {{ item.name }}
</div>
```

### 3. Nested Data Access

```pebble
{% if user?.profile?.avatar %}
    <img src="{{ user.profile.avatar }}" alt="{{ user.name }}">
{% else %}
    <div class="default-avatar">{{ user.name | first | upper }}</div>
{% endif %}
```

### 4. List Processing

```pebble
{% set categories = items | map(attribute="category") | unique %}
{% for category in categories %}
    <h3>{{ category | capitalize }}</h3>
    {% for item in items | filter(attribute="category", value=category) %}
        <p>{{ item.name }}</p>
    {% endfor %}
{% endfor %}
```

This comprehensive guide covers all the essential Pebble template syntax features you'll need when working with Logimos FileKit. The examples demonstrate practical usage patterns for code generation, configuration files, and dynamic content creation. 