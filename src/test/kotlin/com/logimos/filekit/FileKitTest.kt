package com.logimos.filekit

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.io.File
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FileKitTest {

    private fun tempFile(contents: String = ""): File {
        val file = Files.createTempFile("filekit", ".txt")
        Files.write(file, contents.toByteArray(Charsets.UTF_8))
        return file.toFile()
    }

    @Test
    fun `create file and test contents`() {
        val file = tempFile()
        FileKit.createFile(file.toString(), "hello")
        assertEquals("hello", file.readText())
    }

    @Test
    fun `create file creates parent dirs`() {
        val tmpDir = Files.createTempDirectory("filekit").toFile()
        val subDir = File(tmpDir, "subdir")
        val file = File(subDir, "file.txt")
        FileKit.createFile(file.path, "test")
        assertTrue(file.exists())
        assertEquals("test", file.readText())
    }

    @Test
    fun `delete file`() {
        val file = tempFile("test")
        assertTrue(file.exists())
        FileKit.deleteFile(file.path)
        assertFalse(file.exists())
    }

    @Test
    fun `find and replace text`() {
        val file = tempFile("abc 123 def 123")
        FileKit.findAndReplace(file.path, Regex("123"), "XYZ")
        assertEquals("abc XYZ def XYZ", file.readText())
    }

    @Test
    fun `append to end of file`() {
        val file = tempFile("foo\n")
        FileKit.appendToFile(file.path, "bar")
        assertEquals("foo\nbar", file.readText())
    }

    @Test
    fun testAppendToEnd_createsParentDirs() {
        val tmpDir = Files.createTempDirectory("filekit").toFile()
        val subDir = File(tmpDir, "deep/dir")
        val file = File(subDir, "append.txt")
        FileKit.appendToFile(file.path, "hi")
        assertTrue(file.exists())
        assertEquals("hi", file.readText())
    }

    @Test
    fun `append after pattern`() {
        val file = tempFile("one\ntwo\nthree\n")
        FileKit.appendAfterPattern(file.path, Regex("two"), "INSERTED")
        println(file.readText() )
        assertEquals("one\ntwo\nINSERTED\nthree\n", file.readText().replace("\r", ""))
    }

    @Test
    fun `render template`() {
        val result = FileKit.renderTemplate("Welcome, {{ user }}!", mapOf("user" to "Megan"))
        assertEquals("Welcome, Megan!", result)
    }

    @Test
    fun `create file from template`() {
        val file = tempFile()
        FileKit.createFileFromTemplate(file.path, "ID: {{ id }}", mapOf("id" to 42))
        assertEquals("ID: 42", file.readText())
    }

    @Test
    fun `render template from resource`() {
        val result = FileKit.renderTemplateFromResource("templates/hello.peb", mapOf("name" to "Megan"))
        assertEquals("Hello, Megan!", result.trim())
    }

    @Test
    fun `create file from template resource`() {
        val file = tempFile()
        FileKit.createFileFromTemplateResource(file.path, "templates/hello.peb", mapOf("name" to "Megan"))
        assertEquals("Hello, Megan!", file.readText().trim())
    }

    @Test
    fun `render template with custom filters`() {
        val template = "{{ 'hello world' | kebab }} {{ 'user_name' | camel }} {{ 'test' | plural }}"
        val result = FileKit.renderTemplate(template, emptyMap())
        assertEquals("hello-world userName tests", result)
    }

    @Test fun `kebab case`() { assertEquals("foo-bar", FileKit.renderTemplate("{{ 'Foo Bar' | kebab }}", emptyMap())) }
    @Test fun `snake case`() { assertEquals("foo_bar", FileKit.renderTemplate("{{ 'Foo Bar' | snake }}", emptyMap())) }
    @Test fun `scream case`() { assertEquals("FOO_BAR", FileKit.renderTemplate("{{ 'Foo Bar' | scream }}", emptyMap())) }
    @Test fun `camel case`() { assertEquals("fooBar", FileKit.renderTemplate("{{ 'foo bar' | camel }}", emptyMap())) }
    @Test fun `pascal case`() { assertEquals("FooBar", FileKit.renderTemplate("{{ 'foo bar' | pascal }}", emptyMap())) }
    @Test fun `capitalize`() { assertEquals("Hello", FileKit.renderTemplate("{{ 'hello' | capitalize }}", emptyMap())) }
    @Test fun `lower`() { assertEquals("hello", FileKit.renderTemplate("{{ 'HeLLo' | lower }}", emptyMap())) }
    @Test fun `upper`() { assertEquals("HELLO", FileKit.renderTemplate("{{ 'HeLLo' | upper }}", emptyMap())) }
    @Test fun `slug`() { assertEquals("foo-bar", FileKit.renderTemplate("{{ 'Foo Bar!!' | slug }}", emptyMap())) }
    @Test fun `plural`() { assertEquals("cities", FileKit.renderTemplate("{{ 'city' | plural }}", emptyMap())) }
    @Test fun `singular`() { assertEquals("city", FileKit.renderTemplate("{{ 'cities' | singular }}", emptyMap())) }
    @Test fun `default filter`() { assertEquals("fallback", FileKit.renderTemplate("{{ none | default(value='fallback') }}", mapOf("none" to null))) }
    @Test fun `join filter`() { assertEquals("a_b_c", FileKit.renderTemplate("{{ vals | join(sep='_') }}", mapOf("vals" to listOf("a","b","c")))) }

}
