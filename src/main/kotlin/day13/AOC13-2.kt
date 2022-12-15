package day13

import isInTheRightOrder
import unwrapList
import java.io.File
import java.nio.charset.StandardCharsets

fun main() {
    readFullInput("src/main/resources/day13/AOC13-input.txt")
}

fun readFullInput(fileName: String) {

    val notSortedList = File(fileName)
        .readLines(StandardCharsets.UTF_8)
        .filter { !it.isBlank() }
        .map { unwrapList(it) }
        .map { Line(it) }

    val sorted = notSortedList.toTypedArray().sorted()

    val resultList = listOf(Line(unwrapList("[[2]]")), Line(unwrapList("[[6]]")))
        .map { sorted.indexOf(it) }
        .toList()

    val result = resultList
        .fold(1) { sum, element -> sum * (element +1) }

    println("result: $result")
}


class Line(val value: List<*>) : Comparable<Line> {

    override fun compareTo(other: Line): Int {
        return when (isInTheRightOrder(this.value, other.value)) {
            null -> 0
            true -> -1
            else -> 1
        }
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Line

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
