package day14

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    readInput("src/main/resources/day14/input.txt")
}

fun readInput(fileName: String) {
    val pointsSet = buildSet {
        File(fileName).forEachLine { line ->
            for ((lineStart, lineEnd) in line.split(" -> ").windowed(2, 1)) {
                val (startX, startY) = lineStart.split(",")
                val (endX, endY) = lineEnd.split(",")
                val points = convertToPoints(startX.toInt(), startY.toInt(), endX.toInt(), endY.toInt())
                addAll(points)
            }
        }
    }

    val maxY = pointsSet.maxBy { it.second }.second

    val reasonableVerticalSize = maxY + 2
    val reasonableHorizontalSize = 1000
    val horizontalShift = 0

    val map = Array(reasonableVerticalSize) {Array(reasonableHorizontalSize) {false} }

    pointsSet.forEach {
        blockNode(map, it, horizontalShift)
    }

    for (i in 0 until reasonableHorizontalSize) {
        map[reasonableVerticalSize - 1][i] = true
    }

    visualize(map)

    var go = true
    var conut = 0
    while (go){
        go = simulateSandUnit(map, horizontalShift)
        conut++
    }

    visualize(map)

    println("result: $conut")
}

fun visualize(map: Array<Array<Boolean>>){
    for (i in map.indices) {
        for (j in map[i].indices) {
            if(!map[i][j]){
                print(" ")
            } else {
                print("X")
            }
        }
        println()
    }
}

fun convertToPoints(startX: Int, startY: Int, endX: Int, endY: Int): List<Pair<Int, Int>> {
    return buildList {
        if (startX == endX) {
            IntRange(min(startY, endY), max(startY, endY)).forEach {
                add(Pair(startX, it + 1))
            }
        } else if (startY == endY) {
            IntRange(min(startX, endX), max(startX, endX)).forEach {
                add(Pair(it, startY + 1))
            }
        }
    }
}

fun blockNode(map: Array<Array<Boolean>>, point: Pair<Int, Int>, horizontalShift: Int) {
    var depth = 0
    var horizontalCoordinate = 500
    while (point.second >= depth) {

        if (point.second == depth + 1) {
            if (point.first == horizontalCoordinate - 1) {
                map[depth][horizontalCoordinate - 1 - horizontalShift] = true
                break
            } else if (point.first == horizontalCoordinate) {
                map[depth][horizontalCoordinate - horizontalShift] = true
                break
            } else if (point.first == horizontalCoordinate + 1) {
                map[depth][horizontalCoordinate + 1 - horizontalShift] = true
                break
            }
        } else {
            if (point.first < horizontalCoordinate) {
                depth++
                horizontalCoordinate--
            } else if (point.first == horizontalCoordinate) {
                depth++
            } else {
                depth++
                horizontalCoordinate++
            }
        }
    }
}

fun simulateSandUnit(map: Array<Array<Boolean>>, horizontalShift: Int): Boolean {
    var sandCameToRest = false
    var depth = 0
    var horizontalCoordinate = 500 - horizontalShift

    while (!sandCameToRest) {

        if (!map[depth + 1][horizontalCoordinate]) {
            depth++
        } else if (!map[depth + 1][horizontalCoordinate - 1]) {
            depth++
            horizontalCoordinate--
        } else if (!map[depth + 1][horizontalCoordinate + 1]) {
            depth++
            horizontalCoordinate++
        } else {
            map[depth][horizontalCoordinate] = true
            sandCameToRest = true
        }

        if (depth == 0 && horizontalCoordinate == 500 - horizontalShift) {
            return false
        }
    }
    return true
}
