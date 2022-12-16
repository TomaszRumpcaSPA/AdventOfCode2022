package day15

import java.io.File
import java.math.BigDecimal

fun main() {
    readInput2("src/main/resources/day15/input.txt")
}

const val max = 4000000

fun readInput2(fileName: String) {

    val input = buildSet {
        File(fileName).forEachLine { line ->
            add(parseLine(line))
        }
    }

    val sensorList = input.map { SensorRange(it.sensor, getManhatanDistance(it.sensor, it.beacon)) }.toList()

    val lines = buildList {
        sensorList.forEach {
            addAll(computeRangeLines(it))
        }
    }

    for (i in lines.indices) {
        for (j in i + 1 until lines.size) {
            val intersection = findIntersection(lines[i], lines[j])
            val result = checkIntersectionSurrounding(intersection, sensorList)
            if(result != null){
                println("result: $result")
                break
            }
        }
    }
}

private fun checkIntersectionSurrounding(
    intersection: Pair<Int, Int>?,
    sensorList: List<SensorRange>
): BigDecimal? {
    if (intersection != null && intersection.first > 0 && intersection.first < max && intersection.second > 0 &&
        intersection.second < max
    ) {
        for (i in intersection.first - 1..intersection.first + 1) {
            for (j in intersection.second - 1..intersection.second + 1) {
                val inRangeOfKnownSensor =
                    sensorList.any { getManhatanDistance(it.sensor, Pair(i, j)) <= it.manhattanDistanceToBeacon }
                if (!inRangeOfKnownSensor) {
                    return (BigDecimal(i).multiply(BigDecimal(4000000))).add(BigDecimal(j))
                }
            }
        }
    }
    return null
}

fun computeRangeLines(sensorRange: SensorRange): List<Line> {
    return buildList {
        val start1 = Pair(sensorRange.sensor.first, sensorRange.sensor.second + sensorRange.manhattanDistanceToBeacon)
        val end1 = Pair(sensorRange.sensor.first + sensorRange.manhattanDistanceToBeacon, sensorRange.sensor.second)
        add(Line(start1, end1))

        val start2 = Pair(sensorRange.sensor.first + sensorRange.manhattanDistanceToBeacon, sensorRange.sensor.second)
        val end2 = Pair(sensorRange.sensor.first, sensorRange.sensor.second - sensorRange.manhattanDistanceToBeacon)
        add(Line(start2, end2))

        val start3 = Pair(sensorRange.sensor.first, sensorRange.sensor.second - sensorRange.manhattanDistanceToBeacon)
        val end3 = Pair(sensorRange.sensor.first - sensorRange.manhattanDistanceToBeacon, sensorRange.sensor.second)
        add(Line(start3, end3))

        val start4 = Pair(sensorRange.sensor.first - sensorRange.manhattanDistanceToBeacon, sensorRange.sensor.second)
        val end4 = Pair(sensorRange.sensor.first, sensorRange.sensor.second + sensorRange.manhattanDistanceToBeacon)
        add(Line(start4, end4))
    }
}

fun findIntersection(l1: Line, l2: Line): Pair<Int, Int>? {
    val a1 = BigDecimal(l1.end.second).subtract(BigDecimal(l1.start.second))
    val b1 = BigDecimal(l1.start.first).subtract(BigDecimal(l1.end.first))

    val c1 = a1.multiply(BigDecimal(l1.start.first)).add(b1.multiply(BigDecimal(l1.start.second)))

    val a2 = BigDecimal(l2.end.second).subtract(BigDecimal(l2.start.second))
    val b2 = BigDecimal(l2.start.first).subtract(BigDecimal(l2.end.first))
    val c2 = a2.multiply(BigDecimal(l2.start.first)).add(b2.multiply(BigDecimal( l2.start.second)))

    val delta = (a1.multiply(b2)).subtract(a2.multiply(b1))

    if (delta == BigDecimal.ZERO) {
        return null
    }
    val x = ((b2.multiply(c1)).subtract(b1.multiply(c2))).divide(delta)
    val y = ((a1.multiply(c2)).subtract(a2.multiply(c1))).divide(delta)
    return Pair(x.toInt(),y.toInt())
}

data class SensorRange(
    val sensor: Pair<Int, Int>,
    val manhattanDistanceToBeacon: Int
)

data class Line(
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>,
)
