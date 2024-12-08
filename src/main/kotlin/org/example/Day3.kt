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

private data class ComputeState(
    val enabled: Boolean,
    val sum: Long
)

private fun computeConditional(memory: List<String>): Long {
    fun computeSlot(initialState: ComputeState, memory: String): ComputeState =
        mulConditionalRegex.findAll(memory).fold(initialState) { state, match ->
            when {
                match.value == "do()" -> state.copy(enabled = true)
                match.value == "don't()" -> state.copy(enabled = false)
                state.enabled -> {
                    val x = match.groupValues[1].toLong()
                    val y = match.groupValues[2].toLong()
                    state.copy(sum = state.sum + x * y)
                }
                else -> state
            }
        }
    return memory.fold(ComputeState(enabled = true, sum = 0), ::computeSlot).sum
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
