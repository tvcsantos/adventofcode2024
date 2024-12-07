package org.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.time.TimedValue

private val client = HttpClient(CIO)

suspend fun loadInput(folder: File, day: Int): List<String> {
    val lines = withContext(kotlinx.coroutines.Dispatchers.IO) {
        File(folder, "day$day.txt")
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
        if (!folder.exists()) {
            folder.mkdir()
        }
        File(folder, "day$day.txt").writeText(text)
    }

    return text.lines()
        .filterNot(String::isBlank)
}

fun <T> TimedValue<T>.present() {
    println("Solution: $value. Execution time: $duration")
}

private val exampleFolder = File("example")
private val inputFolder = File("input")

val inputFolders = listOf(exampleFolder, inputFolder)
