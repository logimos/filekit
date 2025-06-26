package com.logimos.filekit

import io.pebbletemplates.pebble.extension.Function
import io.pebbletemplates.pebble.template.PebbleTemplate
import io.pebbletemplates.pebble.template.EvaluationContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class NowFunction : Function {
    override fun getArgumentNames() = listOf("pattern")
    override fun execute(args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int): Any? {
        val pattern = args["pattern"]?.toString() ?: "yyyy-MM-dd HH:mm"
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
    }
}

class UuidFunction : Function {
    override fun getArgumentNames() = emptyList<String>()
    override fun execute(args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int): Any? {
        return UUID.randomUUID().toString()
    } 
}