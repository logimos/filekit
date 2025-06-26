package com.logimos.filekit

object FileKit {
    fun createFile(path: String, content: String = "") {
        java.io.File(path).writeText(content)
    }
}