package com.logimos.filekit

import io.pebbletemplates.pebble.extension.AbstractExtension
import io.pebbletemplates.pebble.extension.Filter
import io.pebbletemplates.pebble.extension.Function

class FileKitExtension : AbstractExtension() {
    override fun getFilters(): Map<String, Filter> = mapOf(
        "kebab" to KebabCaseFilter(),
        "snake" to SnakeCaseFilter(),
        "scream" to ScreamCaseFilter(),
        "camel" to CamelCaseFilter(),
        "pascal" to PascalCaseFilter(),
        "capitalize" to CapitalizeFilter(),
        "lower" to LowerFilter(),
        "upper" to UpperFilter(),
        "slug" to SlugFilter(),
        "plural" to PluralFilter(),
        "singular" to SingularFilter(),
        "default" to DefaultFilter(),
        "join" to JoinFilter(),
        "date" to DateFormatFilter(),
    )

    override fun getFunctions(): Map<String, Function> = mapOf(
        "now" to NowFunction(),
        "uuid" to UuidFunction(),
    )
} 