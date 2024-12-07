package org.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.withContext
import java.io.File

private val client = HttpClient(CIO)

suspend fun loadInput(day: Int): List<String> {
    val lines = withContext(kotlinx.coroutines.Dispatchers.IO) {
        File("input/day$day.txt")
            .takeIf { it.exists() }
            ?.readLines()
    }

    if (lines != null) {
        return lines
    }

    val session = System.getenv("AOC_SESSION")
        ?: error("AOC_SESSION environment variable not set")

    val text = client.get("https://adventofcode.com/2024/day/$day/input") {
        headers {
            append("Cookie", "session=$session")
        }
    }.bodyAsText()

    withContext(kotlinx.coroutines.Dispatchers.IO) {
        val outputDir = File("input")
        if (!outputDir.exists()) {
            outputDir.mkdir()
        }
        File("input/day$day.txt").writeText(text)
    }

    return text.lines()
        .filterNot(String::isBlank)
}