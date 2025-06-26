package com.logimos.filekit

import java.io.File
import java.io.StringWriter
import io.pebbletemplates.pebble.PebbleEngine

object FileKit {

    fun createFile(path: String, content: String = "") {
        val file = File(path)
        file.parentFile?.mkdirs()
        file.writeText(content)
    }

    fun deleteFile(path: String) {
        File(path).delete()
    }

    fun findAndReplace(path: String, find: Regex, replace: String) {
        val file = File(path)
        if (!file.exists()) return
        val content = file.readText()
        file.writeText(content.replace(find, replace))
    }

    fun appendToFile(path: String, content: String) {
        File(path).appendText(content)
    }

    fun appendAfterPattern(path: String, pattern: Regex, content: String) {
        val file = File(path)
        if (!file.exists()) return
        val originalContent = file.readText()
        val lines = file.readLines()
        val newLines = mutableListOf<String>()
        var appended = false
        for (line in lines) {
            newLines.add(line)
            if (!appended && pattern.containsMatchIn(line)) {
                newLines.add(content)
                appended = true
            }
        }
        val result = newLines.joinToString("\n")
        // Preserve trailing newline if the original file had one
        val finalContent = if (originalContent.endsWith("\n")) "$result\n" else result
        file.writeText(finalContent)
    }
    
    // template functions
    private val pebble = PebbleEngine.Builder().build()

    fun renderTemplate(template: String, parameters: Map<String, Any?>): String {
        val compiledTemplate = pebble.getLiteralTemplate(template)
        val writer = StringWriter()
        compiledTemplate.evaluate(writer, parameters)
        return writer.toString()
    }

    fun renderTemplateFromResource(resourcePath: String, parameters: Map<String, Any?>): String {
        val compiledTemplate = pebble.getTemplate(resourcePath)
        val writer = StringWriter()
        compiledTemplate.evaluate(writer, parameters)
        return writer.toString()
    }

    fun createFileFromTemplate(path: String, template: String, parameters: Map<String, Any?>) {
        val content = renderTemplate(template, parameters)
        createFile(path, content)
    }

    fun createFileFromTemplateResource(destPath: String, resourcePath: String, parameters: Map<String, Any?>) {
        val content = renderTemplateFromResource(resourcePath, parameters)
        createFile(destPath, content)
    }
    
    
}