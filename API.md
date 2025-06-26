# FileKit API Reference

This document provides detailed API documentation for the Logimos FileKit library.

## Table of Contents

1. [FileKit Object](#filekit-object)
2. [FileKitExtension](#filekitextension)
3. [Filters](#filters)
4. [Functions](#functions)
5. [StringUtils](#stringutils)
6. [GetModule](#getmodule)

## FileKit Object

The main entry point for file operations and template rendering.

### File Operations

#### `createFile(path: String, content: String = "")`

Creates a new file with the specified content. Creates parent directories if they don't exist.

**Parameters:**
- `path: String` - The file path to create
- `content: String` - The content to write to the file (default: empty string)

**Example:**
```kotlin
FileKit.createFile("output.txt", "Hello, World!")
FileKit.createFile("path/to/file.txt", "Content here")
```

#### `deleteFile(path: String)`

Deletes the specified file.

**Parameters:**
- `path: String` - The file path to delete

**Example:**
```kotlin
FileKit.deleteFile("output.txt")
```

#### `findAndReplace(path: String, find: Regex, replace: String)`

Finds and replaces text in a file using a regular expression.

**Parameters:**
- `path: String` - The file path to modify
- `find: Regex` - The regular expression to find
- `replace: String` - The replacement text

**Example:**
```kotlin
FileKit.findAndReplace("config.txt", Regex("localhost"), "production-server")
FileKit.findAndReplace("data.txt", Regex("\\d+"), "NUMBER")
```

#### `appendToFile(path: String, content: String)`

Appends content to the end of a file. Creates parent directories if they don't exist.

**Parameters:**
- `path: String` - The file path to append to
- `content: String` - The content to append

**Example:**
```kotlin
FileKit.appendToFile("log.txt", "New log entry\n")
```

#### `appendAfterPattern(path: String, pattern: Regex, content: String)`

Appends content after the first line that matches the pattern.

**Parameters:**
- `path: String` - The file path to modify
- `pattern: Regex` - The pattern to match
- `content: String` - The content to insert

**Example:**
```kotlin
FileKit.appendAfterPattern("config.txt", Regex("database:"), "  host: localhost")
```

### Template Rendering

#### `renderTemplate(template: String, parameters: Map<String, Any?>): String`

Renders a template string with the provided parameters.

**Parameters:**
- `template: String` - The template string to render
- `parameters: Map<String, Any?>` - The parameters to use in the template

**Returns:**
- `String` - The rendered template

**Example:**
```kotlin
val result = FileKit.renderTemplate(
    "Hello, {{ name | capitalize }}!",
    mapOf("name" to "world")
)
// Result: "Hello, World!"
```

#### `renderTemplateFromResource(resourcePath: String, parameters: Map<String, Any?>): String`

Renders a template from a resource file with the provided parameters.

**Parameters:**
- `resourcePath: String` - The resource path to the template file
- `parameters: Map<String, Any?>` - The parameters to use in the template

**Returns:**
- `String` - The rendered template

**Example:**
```kotlin
val result = FileKit.renderTemplateFromResource(
    "templates/email.peb",
    mapOf("user" to "John", "company" to "Acme Corp")
)
```

#### `createFileFromTemplate(path: String, template: String, parameters: Map<String, Any?>)`

Creates a file from a template string.

**Parameters:**
- `path: String` - The destination file path
- `template: String` - The template string to render
- `parameters: Map<String, Any?>` - The parameters to use in the template

**Example:**
```kotlin
FileKit.createFileFromTemplate(
    "UserService.kt",
    """
    class {{ className | pascal }} {
        fun get{{ entityName | pascal }}(): {{ entityName | pascal }} {
            // Implementation
        }
    }
    """,
    mapOf("className" to "user service", "entityName" to "user")
)
```

#### `createFileFromTemplateResource(destPath: String, resourcePath: String, parameters: Map<String, Any?>)`

Creates a file from a template resource.

**Parameters:**
- `destPath: String` - The destination file path
- `resourcePath: String` - The resource path to the template file
- `parameters: Map<String, Any?>` - The parameters to use in the template

**Example:**
```kotlin
FileKit.createFileFromTemplateResource(
    "output.txt",
    "templates/config.peb",
    mapOf("port" to 8080, "host" to "localhost")
)
```

## FileKitExtension

Extension class that provides custom filters and functions to the Pebble engine.

### Constructor

```kotlin
FileKitExtension()
```

### Usage

```kotlin
val pebble = PebbleEngine.Builder()
    .extension(FileKitExtension())
    .build()
```

## Filters

All filters implement the `Filter` interface and can be used in templates with the pipe operator `|`.

### Case Conversion Filters

#### KebabCaseFilter

Converts text to kebab-case (lowercase with hyphens).

**Template Usage:**
```pebble
{{ "hello world" | kebab }}
```

**Output:** `hello-world`

#### SnakeCaseFilter

Converts text to snake_case (lowercase with underscores).

**Template Usage:**
```pebble
{{ "hello world" | snake }}
```

**Output:** `hello_world`

#### ScreamCaseFilter

Converts text to SCREAM_CASE (uppercase with underscores).

**Template Usage:**
```pebble
{{ "hello world" | scream }}
```

**Output:** `HELLO_WORLD`

#### CamelCaseFilter

Converts text to camelCase.

**Template Usage:**
```pebble
{{ "hello world" | camel }}
```

**Output:** `helloWorld`

#### PascalCaseFilter

Converts text to PascalCase.

**Template Usage:**
```pebble
{{ "hello world" | pascal }}
```

**Output:** `HelloWorld`

### Text Transformation Filters

#### CapitalizeFilter

Capitalizes the first character of the text.

**Template Usage:**
```pebble
{{ "hello" | capitalize }}
```

**Output:** `Hello`

#### LowerFilter

Converts text to lowercase.

**Template Usage:**
```pebble
{{ "HELLO" | lower }}
```

**Output:** `hello`

#### UpperFilter

Converts text to uppercase.

**Template Usage:**
```pebble
{{ "hello" | upper }}
```

**Output:** `HELLO`

#### SlugFilter

Converts text to a URL-friendly slug.

**Template Usage:**
```pebble
{{ "Hello World!" | slug }}
```

**Output:** `hello-world`

### Pluralization Filters

#### PluralFilter

Converts a singular noun to its plural form.

**Template Usage:**
```pebble
{{ "city" | plural }}
{{ "user" | plural }}
{{ "category" | plural }}
```

**Output:** `cities`, `users`, `categories`

#### SingularFilter

Converts a plural noun to its singular form.

**Template Usage:**
```pebble
{{ "cities" | singular }}
{{ "users" | singular }}
{{ "categories" | singular }}
```

**Output:** `city`, `user`, `category`

### Utility Filters

#### DefaultFilter

Provides a default value when the input is null.

**Template Usage:**
```pebble
{{ none | default(value="fallback") }}
{{ user.name | default(value="Anonymous") }}
```

**Parameters:**
- `value: Any?` - The default value to use

#### JoinFilter

Joins an array or list with a separator.

**Template Usage:**
```pebble
{{ ["a", "b", "c"] | join(sep="_") }}
{{ items | join(sep=", ") }}
```

**Parameters:**
- `sep: String` - The separator to use (default: `","`)

#### DateFormatFilter

Formats a date using a specified pattern.

**Template Usage:**
```pebble
{{ date | date(pattern="yyyy-MM-dd") }}
{{ now() | date(pattern="yyyy-MM-dd HH:mm:ss") }}
```

**Parameters:**
- `pattern: String` - The date format pattern (default: `"yyyy-MM-dd"`)

## Functions

All functions implement the `Function` interface and can be called in templates.

### NowFunction

Returns the current timestamp.

**Template Usage:**
```pebble
{{ now() }}
{{ now(pattern="yyyy-MM-dd HH:mm:ss") }}
```

**Parameters:**
- `pattern: String` - The date format pattern (default: `"yyyy-MM-dd HH:mm"`)

**Returns:**
- `String` - The formatted current timestamp

### UuidFunction

Generates a random UUID.

**Template Usage:**
```pebble
{{ uuid() }}
```

**Returns:**
- `String` - A random UUID

## StringUtils

Utility object providing string transformation methods.

### Case Conversion Methods

#### `toKebab(str: String): String`

Converts a string to kebab-case.

```kotlin
StringUtils.toKebab("hello world") // Returns: "hello-world"
StringUtils.toKebab("UserProfile") // Returns: "user-profile"
```

#### `toSnake(str: String): String`

Converts a string to snake_case.

```kotlin
StringUtils.toSnake("hello world") // Returns: "hello_world"
StringUtils.toSnake("UserProfile") // Returns: "user_profile"
```

#### `toScream(str: String): String`

Converts a string to SCREAM_CASE.

```kotlin
StringUtils.toScream("hello world") // Returns: "HELLO_WORLD"
StringUtils.toScream("UserProfile") // Returns: "USER_PROFILE"
```

#### `toCamel(str: String): String`

Converts a string to camelCase.

```kotlin
StringUtils.toCamel("hello world") // Returns: "helloWorld"
StringUtils.toCamel("user_profile") // Returns: "userProfile"
```

#### `toPascal(str: String): String`

Converts a string to PascalCase.

```kotlin
StringUtils.toPascal("hello world") // Returns: "HelloWorld"
StringUtils.toPascal("user_profile") // Returns: "UserProfile"
```

### Text Transformation Methods

#### `capitalize(str: String): String`

Capitalizes the first character of a string.

```kotlin
StringUtils.capitalize("hello") // Returns: "Hello"
StringUtils.capitalize("world") // Returns: "World"
```

#### `slug(str: String): String`

Converts a string to a URL-friendly slug.

```kotlin
StringUtils.slug("Hello World!") // Returns: "hello-world"
StringUtils.slug("User Profile & Settings") // Returns: "user-profile-settings"
```

### Pluralization Methods

#### `plural(str: String): String`

Converts a singular noun to its plural form.

```kotlin
StringUtils.plural("city") // Returns: "cities"
StringUtils.plural("user") // Returns: "users"
StringUtils.plural("category") // Returns: "categories"
StringUtils.plural("box") // Returns: "boxes"
```

#### `singular(str: String): String`

Converts a plural noun to its singular form.

```kotlin
StringUtils.singular("cities") // Returns: "city"
StringUtils.singular("users") // Returns: "user"
StringUtils.singular("categories") // Returns: "category"
StringUtils.singular("boxes") // Returns: "box"
```

## Creating Custom Extensions

### Custom Filter Example

```kotlin
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

class CustomExtension : AbstractExtension() {
    override fun getFilters(): Map<String, Filter> = mapOf(
        "reverse" to ReverseFilter()
    )
}
```

### Custom Function Example

```kotlin
class RandomFunction : Function {
    override fun getArgumentNames() = listOf("min", "max")
    
    override fun execute(
        args: Map<String, Any?>, 
        self: PebbleTemplate, 
        context: EvaluationContext, 
        lineNumber: Int
    ): Any? {
        val min = (args["min"] as? Number)?.toInt() ?: 0
        val max = (args["max"] as? Number)?.toInt() ?: 100
        return (min..max).random()
    }
}

class CustomExtension : AbstractExtension() {
    override fun getFunctions(): Map<String, Function> = mapOf(
        "random" to RandomFunction()
    )
}
```

### Using Custom Extensions

```kotlin
val pebble = PebbleEngine.Builder()
    .extension(FileKitExtension())
    .extension(CustomExtension())
    .build()

// Use in templates
// {{ "hello" | reverse }} -> "olleh"
// {{ random(min=1, max=10) }} -> random number between 1 and 10
```

## Error Handling

### Common Exceptions

#### FileNotFoundException
Thrown when trying to read a non-existent file.

```kotlin
try {
    FileKit.renderTemplateFromResource("nonexistent.peb", emptyMap())
} catch (e: FileNotFoundException) {
    println("Template file not found: ${e.message}")
}
```

#### TemplateException
Thrown when there's a syntax error in a template.

```kotlin
try {
    FileKit.renderTemplate("{{ invalid syntax }}", emptyMap())
} catch (e: TemplateException) {
    println("Template syntax error: ${e.message}")
}
```

#### IOException
Thrown when there are file system issues.

```kotlin
try {
    FileKit.createFile("/invalid/path/file.txt", "content")
} catch (e: IOException) {
    println("File operation failed: ${e.message}")
}
```

## Performance Considerations

### Template Caching

The Pebble engine automatically caches compiled templates for better performance. Templates are compiled once and reused for subsequent renders.

### Large File Operations

For large files, consider using streaming operations instead of loading the entire file into memory:

```kotlin
// For very large files, consider using Java NIO
val lines = Files.readAllLines(Paths.get("large-file.txt"))
// Process lines individually
```

### Memory Management

When working with many templates, consider clearing the template cache if memory becomes an issue:

```kotlin
// Clear template cache (if using custom PebbleEngine)
pebbleEngine.templateCache.clear()
```

## Thread Safety

The FileKit object is thread-safe for read operations. However, when multiple threads are writing to the same file, consider using proper synchronization:

```kotlin
// For concurrent file access
synchronized(FileKit) {
    FileKit.appendToFile("log.txt", "Thread-safe log entry\n")
}
```

## Best Practices

### 1. Use Descriptive Template Variables

```kotlin
// Good
mapOf("userName" to "John", "userEmail" to "john@example.com")

// Avoid
mapOf("n" to "John", "e" to "john@example.com")
```

### 2. Validate Input Parameters

```kotlin
fun createUserFile(userName: String, userEmail: String) {
    require(userName.isNotBlank()) { "Username cannot be blank" }
    require(userEmail.contains("@")) { "Invalid email format" }
    
    FileKit.createFileFromTemplate(
        "User.kt",
        userTemplate,
        mapOf("userName" to userName, "userEmail" to userEmail)
    )
}
```

### 3. Use Resource Templates for Complex Templates

```kotlin
// Store complex templates in resource files
FileKit.createFileFromTemplateResource(
    "output.txt",
    "templates/complex-template.peb",
    parameters
)
```

### 4. Handle Template Errors Gracefully

```kotlin
fun safeRenderTemplate(template: String, params: Map<String, Any?>): String {
    return try {
        FileKit.renderTemplate(template, params)
    } catch (e: Exception) {
        println("Template rendering failed: ${e.message}")
        "Template Error: ${e.message}"
    }
}
```

## Get Module

### Installation

To use FileKit in your project, add the following configuration to your `build.gradle.kts` file:

#### Repository Configuration

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/logimos/filekit")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

#### Dependency Configuration

```kotlin
dependencies {
    implementation("com.logimos:filekit:1.0.0")
}
```

### GitHub Packages Authentication

To access the GitHub Packages repository, you need to provide authentication credentials. You can do this in several ways:

#### Option 1: Environment Variables (Recommended for CI/CD)

```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-github-personal-access-token
```

#### Option 2: Gradle Properties

Create or update your `~/.gradle/gradle.properties` file:

```properties
gpr.user=your-github-username
gpr.key=your-github-personal-access-token
```

#### Option 3: Project Properties

Add to your project's `gradle.properties`:

```properties
gpr.user=your-github-username
gpr.key=your-github-personal-access-token
```

### Creating a GitHub Personal Access Token

1. Go to GitHub Settings → Developer settings → Personal access tokens
2. Click "Generate new token (classic)"
3. Select the following scopes:
   - `read:packages` - Download packages from GitHub Package Registry
   - `write:packages` - Upload packages to GitHub Package Registry (if you plan to publish)
4. Copy the generated token and use it as your `GITHUB_TOKEN`

### Alternative: Local Installation

If you prefer to install FileKit locally, you can build and install it to your local Maven repository:

```bash
./gradlew publishToMavenLocal
```

Then use the local dependency:

```kotlin
dependencies {
    implementation("com.logimos:filekit:1.0.0")
}
```

### Version Compatibility

FileKit requires:
- Kotlin 2.0.0 or higher
- Java 21 or higher
- Pebble Templates 3.2.2

### Quick Verification

After adding the dependency, you can verify the installation by running:

```kotlin
import com.logimos.filekit.FileKit

fun main() {
    // Test basic functionality
    FileKit.createFile("test.txt", "Hello from FileKit!")
    println("FileKit is working correctly!")
}
```

This API reference provides comprehensive documentation for all FileKit components. For more examples and usage patterns, see the main [README.md](README.md) and [Pebble Syntax Guide](PEBBLE_SYNTAX.md).
