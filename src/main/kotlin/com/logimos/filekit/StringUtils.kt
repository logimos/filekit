package com.logimos.filekit

object StringUtils {
    fun toKebab(str: String) =
        str.trim().splitWords().joinToString("-") { it.lowercase() }

    fun toSnake(str: String) =
        str.trim().splitWords().joinToString("_") { it.lowercase() }

    fun toScream(str: String) =
        str.trim().splitWords().joinToString("_") { it.uppercase() }

    fun toCamel(str: String): String {
        val words = str.trim().splitWords()
        return if (words.isEmpty()) "" else
            words.first().lowercase() + words.drop(1).joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
    }

    fun toPascal(str: String) =
        str.trim().splitWords().joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }

    fun capitalize(str: String) =
        str.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    fun slug(str: String) =
        str.trim().splitWords().joinToString("-") { it.lowercase().replace(Regex("[^a-z0-9-]"), "") }

    fun plural(str: String) =
        when {
            str.endsWith("y", true) && str.length > 1 && !"aeiou".contains(str[str.length - 2], true) -> str.dropLast(1) + "ies"
            str.endsWith("s") || str.endsWith("sh") || str.endsWith("ch") -> str + "es"
            else -> str + "s"
        }

    fun singular(str: String) =
        when {
            str.endsWith("ies") -> str.dropLast(3) + "y"
            str.endsWith("es") -> str.dropLast(2)
            str.endsWith("s") && str.length > 1 -> str.dropLast(1)
            else -> str
        }

    // Helper: splits a string into words (from kebab, snake, camel, Pascal, and space cases)
    private fun String.splitWords(): List<String> =
        Regex("[A-Za-z0-9]+").findAll(this)
            .map { it.value }
            .flatMap { splitCamel(it) }
            .toList()

    private fun splitCamel(str: String): List<String> =
        str.split(Regex("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|(?<=[0-9])(?=[A-Za-z])|(?<=[A-Za-z])(?=[0-9])"))
}
