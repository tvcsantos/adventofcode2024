package org.example

import java.io.File
import kotlin.time.measureTimedValue

private val mulRegex: Regex =
    Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

private fun compute(memory: List<String>): Long {
    fun computeSlot(memory: String): Long =
        mulRegex.findAll(memory).sumOf {
            val x = it.groupValues[1].toLong()
            val y = it.groupValues[2].toLong()
            x * y
        }
    return memory.sumOf(::computeSlot)
}

private val mulConditionalRegex: Regex =
    Regex("""do\(\)|don't\(\)|mul\((\d{1,3}),(\d{1,3})\)""")

private fun computeConditional(memory: List<String>): Long {
    var enabled = true
    fun computeSlot(memory: String): Long =
        mulConditionalRegex.findAll(memory).sumOf {
            val match = it.value
            when {
                match == "do()" -> { enabled = true; 0 }
                match == "don't()" -> { enabled = false; 0 }
                enabled -> {
                    val x = it.groupValues[1].toLong()
                    val y = it.groupValues[2].toLong()
                    x * y
                }
                else -> 0
            }
        }
    return memory.sumOf(::computeSlot)
}

private suspend fun solvePuzzle(folder: File) {
    val memory = loadInput(folder, day = 3)

    measureTimedValue {
        compute(memory)
    }.present()

    measureTimedValue {
        computeConditional(memory)
    }.present()
}

suspend fun main() {
    inputFolders.forEach {
        println("Solutions for $it folder")
        solvePuzzle(it)
    }
}
