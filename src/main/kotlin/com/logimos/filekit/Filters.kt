package com.logimos.filekit

import io.pebbletemplates.pebble.extension.Filter
import io.pebbletemplates.pebble.template.PebbleTemplate
import io.pebbletemplates.pebble.template.EvaluationContext
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

abstract class SimpleFilter : Filter {
    override fun getArgumentNames() = null
    abstract override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int): Any?
}

class KebabCaseFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.toKebab(it) }
}

class SnakeCaseFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.toSnake(it) }
}

class ScreamCaseFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.toScream(it) }
}

class CamelCaseFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.toCamel(it) }
}

class PascalCaseFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.toPascal(it) }
}

class CapitalizeFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.capitalize(it) }
}

class LowerFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.lowercase()
}

class UpperFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.uppercase()
}

class SlugFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.slug(it) }
}

class PluralFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.plural(it) }
}

class SingularFilter : SimpleFilter() {
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input?.toString()?.let { StringUtils.singular(it) }
}

class DefaultFilter : Filter {
    override fun getArgumentNames() = listOf("value")
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int) =
        input ?: args["value"]
}

class JoinFilter : Filter {
    override fun getArgumentNames() = listOf("sep")
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int): Any? {
        val sep = args["sep"]?.toString() ?: ","
        return if (input is Iterable<*>) input.joinToString(sep) else input?.toString()
    }
}


class DateFormatFilter : Filter {
    override fun getArgumentNames() = listOf("pattern")
    override fun apply(input: Any?, args: Map<String, Any?>, self: PebbleTemplate, context: EvaluationContext, lineNumber: Int): Any? {
        val pattern = args["pattern"]?.toString() ?: "yyyy-MM-dd"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val date = when (input) {
            is LocalDateTime -> input
            is java.util.Date -> input.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
            is String -> LocalDateTime.parse(input)
            else -> LocalDateTime.now()
        }
        return date.format(formatter)
    }
}
