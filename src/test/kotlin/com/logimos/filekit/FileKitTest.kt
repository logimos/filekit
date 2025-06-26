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
    fun `delete file`() {
        val file = tempFile("test")
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

    
}
