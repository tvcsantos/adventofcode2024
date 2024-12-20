package org.example

import java.io.File
import kotlin.math.abs
import kotlin.time.measureTimedValue

private fun List<Long>.distance(other: List<Long>): Long {
    val firstListOrdered = this.sorted().asSequence()
    val secondListOrdered = other.sorted().asSequence()

    return firstListOrdered.zip(secondListOrdered) { a, b ->
        abs(a - b)
    }.sum()
}

private fun List<Long>.similarity(other: List<Long>): Long {
    val memory: MutableMap<Long, Long> = mutableMapOf()

    val secondMutable = other.toMutableList()

    return this.fold(0L) { acc, i ->
        if (secondMutable.isEmpty()) {
            return@similarity acc
        }
        val occurrences = memory[i]
        val totalOccurrences = if (occurrences == null) {
            val size = secondMutable.size
            secondMutable.removeAll { it == i }
            val localOccurrences = (size - secondMutable.size).toLong()
            memory[i] = localOccurrences
            localOccurrences
        } else {
            occurrences
        }
        acc + i * totalOccurrences
    }
}

private suspend fun solvePuzzle(folder: File) {
    val lines = loadInput(folder = folder, day = 1)

    val (firstList, secondList) =  lines
        .asSequence()
        .map {
            val elements = it.split(" ")
            val firstNumber = elements.first().toLong()
            val secondNumber = elements.last().toLong()
            firstNumber to secondNumber
        }.unzip()

    measureTimedValue {
        firstList.distance(secondList)
    }.present()

    measureTimedValue {
        firstList.similarity(secondList)
    }.present()
}

suspend fun main() {
    inputFolders.forEach {
        println("Solutions for $it folder")
        solvePuzzle(it)
    }
}
