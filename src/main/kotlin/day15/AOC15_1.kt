package day15

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    readInput("src/main/resources/day15/input.txt")
}

fun readInput(fileName: String) {
    val input = buildSet {
        File(fileName).forEachLine { line ->
            add(parseLine(line))
        }
    }

    val targetLineIdx = 2000000

    val pointsSet = buildSet {
        input.forEach {
            val points = convertToPoints(it, targetLineIdx)
            addAll(points)
        }
    }

    val beconsInTargetLine = input.map { it.beacon }.filter { it.second == targetLineIdx }.toSet().size

    val tenthRow = pointsSet.filter { it.second == targetLineIdx }.toList()

    println("result: ${tenthRow.size - beconsInTargetLine}")
}

fun getManhatanDistance(first: Pair<Int, Int>, second: Pair<Int, Int>): Int {
    val x =
        max(first.first, second.first) - min(first.first, second.first)
    val y = max(first.second, second.second) - min(
        first.second,
        second.second
    )
    return x + y
}


fun convertToPoints(sensorData: LineData, targetLineIdx: Int): Set<Pair<Int, Int>> {
    return buildSet {
        val sensorBeaconDistance = getManhatanDistance(sensorData.sensor, sensorData.beacon)
        println("Manhatan distance: $sensorBeaconDistance")
        if (abs(sensorData.sensor.second - targetLineIdx) <= sensorBeaconDistance) {

            for (i in sensorData.sensor.first - sensorBeaconDistance until sensorData.sensor.first + sensorBeaconDistance) {
                val pair = Pair(i, targetLineIdx)
                val manhatanDistance = getManhatanDistance(pair, sensorData.sensor)
                if (manhatanDistance <= sensorBeaconDistance){
                    add(pair)
                }
            }
        }
    }
}

fun parseLine(line: String): LineData {
    val list = line.substring(10, line.length).split(": closest beacon is at ").toList()
    return LineData(parseCoordinate(list[0]), parseCoordinate(list[1]))
}

fun parseCoordinate(coordinatesString: String): Pair<Int, Int> {
    val list = coordinatesString.split(",").map { it.trim() }.toList()
    return Pair(list[0].substring(2, list[0].length).toInt(), list[1].substring(2, list[1].length).toInt())
}

data class LineData(val sensor: Pair<Int, Int>, val beacon: Pair<Int, Int>)