package org.example

import io.ktor.util.collections.*
import java.io.File
import kotlin.math.abs
import kotlin.time.measureTimedValue

private val range = 1..3

private data class State(
    val previous: Int,
    val difference: Int?,
)

@Suppress("DuplicatedCode")
private fun List<Int>.isSafe(): Boolean {
    fold<Int, State?>(null) { previousState, element ->
        if (previousState == null) return@fold State(element, null)
        val (previousElement, previousDifference) = previousState
        val difference = element - previousElement
        if (abs(difference) !in range) return false
        if (previousDifference == null) return@fold State(element, difference)
        if (difference > 0 && previousDifference < 0) return false
        else if (difference < 0 && previousDifference > 0) return false
        State(element, difference)
    }
    return true
}

@Suppress("DuplicatedCode")
private fun List<Int>.isSafeDampener(ignoredIndex: Int): Boolean {
    foldIndexed<Int, State?>(null) { index, previousState, element ->
        if (index == ignoredIndex) return@foldIndexed previousState
        if (previousState == null) return@foldIndexed State(element, null)
        val (previousElement, previousDifference) = previousState
        val difference = element - previousElement
        if (abs(difference) !in range) return false
        if (previousDifference == null) return@foldIndexed State(element, difference)
        if (difference > 0 && previousDifference < 0) return false
        else if (difference < 0 && previousDifference > 0) return false
        State(element, difference)
    }
    return true
}

private fun List<Int>.isSafeReusing(): Boolean =
    isSafeDampener(-1)

private fun List<Int>.isSafeDampener(): Boolean =
    indices.any(::isSafeDampener)

private suspend fun solvePuzzle(folder: File) {
    val lines = loadInput(folder, day = 2)

    val reports = lines.map { line ->
        line.split(" ").map { it.toInt() }
    }

    measureTimedValue {
        reports.count(List<Int>::isSafe)
    }.present()

    measureTimedValue {
        reports.count(List<Int>::isSafeReusing)
    }.present()

    measureTimedValue {
        reports.count(List<Int>::isSafeDampener)
    }.present()
}

suspend fun main() {
    inputFolders.forEach {
        println("Solutions for $it folder")
        solvePuzzle(it)
    }
}
