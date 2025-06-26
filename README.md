# Logimos FileKit

A powerful Kotlin library for file operations and template rendering using the Pebble templating engine. FileKit provides a simple and intuitive API for common file operations combined with advanced templating capabilities.

## Features

- **File Operations**: Create, delete, append, and modify files with ease
- **Template Rendering**: Powerful templating with Pebble engine
- **String Transformations**: Built-in filters for case conversion, pluralization, and more
- **Extensible**: Easy to add custom filters and functions
- **Type Safe**: Written in Kotlin with full type safety

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.logimos:filekit:1.0.0")
}
```

### Maven

```xml
<dependency>
    <groupId>com.logimos</groupId>
    <artifactId>filekit</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

```kotlin
import com.logimos.filekit.FileKit

// Create a simple file
FileKit.createFile("output.txt", "Hello, World!")

// Create a file from a template
FileKit.createFileFromTemplate(
    "UserService.kt",
    """
    class {{ className | pascal }} {
        fun get{{ entityName | pascal }}(): {{ entityName | pascal }} {
            // Implementation
        }
    }
    """,
    mapOf(
        "className" to "user service",
        "entityName" to "user"
    )
)
```

## File Operations

### Basic File Operations

```kotlin
// Create a file (creates parent directories if needed)
FileKit.createFile("path/to/file.txt", "content")

// Delete a file
FileKit.deleteFile("path/to/file.txt")

// Append content to a file
FileKit.appendToFile("path/to/file.txt", "additional content")

// Find and replace text using regex
FileKit.findAndReplace("path/to/file.txt", Regex("old"), "new")

// Append content after a specific pattern
FileKit.appendAfterPattern("path/to/file.txt", Regex("pattern"), "inserted content")
```

### Template Rendering

```kotlin
// Render a template string
val result = FileKit.renderTemplate(
    "Hello, {{ name | capitalize }}!",
    mapOf("name" to "world")
)
// Result: "Hello, World!"

// Render from a resource template
val result = FileKit.renderTemplateFromResource(
    "templates/email.peb",
    mapOf("user" to "John", "company" to "Acme Corp")
)

// Create file from template
FileKit.createFileFromTemplate(
    "output.txt",
    "Generated on {{ now() }}",
    emptyMap()
)

// Create file from resource template
FileKit.createFileFromTemplateResource(
    "output.txt",
    "templates/config.peb",
    mapOf("port" to 8080, "host" to "localhost")
)
```

## Built-in Filters

FileKit comes with a comprehensive set of string transformation filters:

### Case Conversion Filters

```kotlin
// Kebab case: "hello world" -> "hello-world"
{{ "hello world" | kebab }}

// Snake case: "hello world" -> "hello_world"
{{ "hello world" | snake }}

// Scream case: "hello world" -> "HELLO_WORLD"
{{ "hello world" | scream }}

// Camel case: "hello world" -> "helloWorld"
{{ "hello world" | camel }}

// Pascal case: "hello world" -> "HelloWorld"
{{ "hello world" | pascal }}
```

### Text Transformation Filters

```kotlin
// Capitalize: "hello" -> "Hello"
{{ "hello" | capitalize }}

// Lowercase: "HELLO" -> "hello"
{{ "HELLO" | lower }}

// Uppercase: "hello" -> "HELLO"
{{ "hello" | upper }}

// Slug: "Hello World!" -> "hello-world"
{{ "Hello World!" | slug }}
```

### Pluralization Filters

```kotlin
// Plural: "city" -> "cities"
{{ "city" | plural }}

// Singular: "cities" -> "city"
{{ "cities" | singular }}
```

### Utility Filters

```kotlin
// Default value for null
{{ none | default(value="fallback") }}

// Join array with separator
{{ ["a", "b", "c"] | join(sep="_") }}

// Date formatting
{{ date | date(pattern="yyyy-MM-dd") }}
```

## Built-in Functions

```kotlin
// Current timestamp
{{ now() }}
{{ now(pattern="yyyy-MM-dd HH:mm:ss") }}

// Generate UUID
{{ uuid() }}
```

## Adding Custom Functions

To add custom functions, create a new function class and register it in the extension:

```kotlin
// 1. Create your function class
class CustomFunction : Function {
    override fun getArgumentNames() = listOf("param1", "param2")
    
    override fun execute(
        args: Map<String, Any?>, 
        self: PebbleTemplate, 
        context: EvaluationContext, 
        lineNumber: Int
    ): Any? {
        val param1 = args["param1"]?.toString()
        val param2 = args["param2"]?.toString()
        
        // Your custom logic here
        return "$param1-$param2"
    }
}

// 2. Create a custom extension
class CustomExtension : AbstractExtension() {
    override fun getFunctions(): Map<String, Function> = mapOf(
        "custom" to CustomFunction()
    )
}

// 3. Use in your own PebbleEngine
val pebble = PebbleEngine.Builder()
    .extension(FileKitExtension())
    .extension(CustomExtension())
    .build()
```

## Adding Custom Filters

To add custom filters, create a new filter class and register it:

```kotlin
// 1. Create your filter class
class ReverseFilter : SimpleFilter() {
    override fun apply(
        input: Any?, 
        args: Map<String, Any?>, 
        self: PebbleTemplate, 
        context: EvaluationContext, 
        lineNumber: Int
    ): Any? {
        return input?.toString()?.reversed()
    }
}

// 2. Add to your extension
class CustomExtension : AbstractExtension() {
    override fun getFilters(): Map<String, Filter> = mapOf(
        "reverse" to ReverseFilter()
    )
}

// 3. Use in templates
{{ "hello" | reverse }}  // Output: "olleh"
```

## Advanced Examples

### Generating a Complete Project Structure

```kotlin
val projectName = "my-awesome-project"
val entityName = "user"

// Generate main class
FileKit.createFileFromTemplate(
    "src/main/kotlin/${projectName.replace("-", "/")}/Main.kt",
    """
    package ${projectName.replace("-", ".")}
    
    fun main() {
        println("Hello from {{ projectName | pascal }}!")
    }
    """,
    mapOf("projectName" to projectName)
)

// Generate entity class
FileKit.createFileFromTemplate(
    "src/main/kotlin/${projectName.replace("-", "/")}/${entityName | pascal }.kt",
    """
    data class {{ entityName | pascal }}(
        val id: String,
        val name: String,
        val email: String
    )
    """,
    mapOf("entityName" to entityName)
)

// Generate API endpoint
FileKit.createFileFromTemplate(
    "src/main/kotlin/${projectName.replace("-", "/")}/api/${entityName | plural | pascal }Controller.kt",
    """
    @RestController
    @RequestMapping("/api/{{ entityName | plural | kebab }}")
    class {{ entityName | pascal }}Controller {
        @GetMapping
        fun get{{ entityName | plural | pascal }}(): List<{{ entityName | pascal }}> {
            // Implementation
        }
    }
    """,
    mapOf("entityName" to entityName)
)
```

### Configuration File Generation

```kotlin
val config = mapOf(
    "appName" to "My Application",
    "version" to "1.0.0",
    "database" to mapOf(
        "host" to "localhost",
        "port" to 5432,
        "name" to "myapp"
    ),
    "features" to listOf("auth", "logging", "caching")
)

FileKit.createFileFromTemplate(
    "config/application.yml",
    """
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
    """,
    config
)
```

## Testing

Run the test suite:

```bash
./gradlew test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Related Documentation

- [Pebble Template Syntax Guide](PEBBLE_SYNTAX.md) - Complete guide to Pebble template syntax
- [API Reference](API.md) - Detailed API documentation 