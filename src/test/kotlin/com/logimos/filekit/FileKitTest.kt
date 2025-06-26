package com.logimos.filekit

import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.test.assertTrue

class FileKitTest {
    @Test
    fun testCreateFile() {
        val temp = Files.createTempFile("filekit", ".txt")
        FileKit.createFile(temp.toString(), "hello")
        assertTrue(java.io.File(temp.toString()).readText() == "hello")
    }
}
