import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import kotlin.math.max

fun main() {
    readInput("src/main/resources/day13/AOC13-input.txt")
}

fun readInput(fileName: String) {
    var first: List<*>? = null
    var second: List<*>? = null
    var idx = 0
    var total = 0
    File(fileName).forEachLine { line ->
        if (first == null) {
            first = unwrapList(line)
        } else if (second == null) {
            second = unwrapList(line)
        } else {
            idx++
            val result = isInTheRightOrder(first!!, second!!)
            if (result != null && result == true) {
                total += idx
            }
            first = null
            second = null
        }
    }
    println("result: $total")
}

fun isInTheRightOrder(left: Any, right: Any): Boolean? {
    // both are ints
    if (left is Int && right is Int) {
        if (left == right) {
            return null
        } else {
            return left < right
        }
    }

    //both are list of
    if (left is List<*> && right is List<*>) {
        if (left.size == 0  && right.size == 0) {
            return null
        }
        return isInTheRightOrder(left, right)
    }

    //one value is an integer
    val leftList: List<*> = convertToList(left)
    val rightList: List<*> = convertToList(right)

    return isInTheRightOrder(leftList, rightList)
}

private fun convertToList(left: Any): List<*> {
    val leftList: List<*>
    if (left is List<*>) {
        leftList = left
    } else {
        leftList = listOf(left)
    }
    return leftList
}

fun unwrapList(list: String): List<*> {
    val mapper = ObjectMapper()
    return mapper.readValue(list, List::class.java)
}

fun isInTheRightOrder(left: List<*>, right: List<*>): Boolean? {

    val max = max(left.size, right.size)

    for (i in 0 until max) {

        if (left.size <= i && right.size > i) {
            // left run out of items
            return true
        } else if (left.size > i && right.size <= i) {
            // right run out of items
            return false
        }

        val leftValue = left[i]
        val rightValue = right[i]
        val inTheRightOrder = isInTheRightOrder(leftValue!!, rightValue!!)

        if (inTheRightOrder != null) return inTheRightOrder
    }

    return null
}
