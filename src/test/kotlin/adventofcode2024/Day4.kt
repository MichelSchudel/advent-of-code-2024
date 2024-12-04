package adventofcode2024

import adventofcode2023.loadStringLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day4 {

    data class Coordinate(
        val x: Int,
        val y: Int,
        val c: Char,
    )

    @Test
    fun testPart1() {
        val coordinates = buildModelFromInput("/test-input-day-4.txt")
        assertThat(solvePart1(coordinates)).isEqualTo(18)
    }

    @Test
    fun solvePart1() {
        val coordinates = buildModelFromInput("/real-input-day-4.txt")
        println(solvePart1(coordinates))
    }

    @Test
    fun testPart2() {
        val coordinates = buildModelFromInput("/test-input-day-4.txt")
        assertThat(solvePart2(coordinates)).isEqualTo(9)
    }

    @Test
    fun solvePart2() {
        val coordinates = buildModelFromInput("/real-input-day-4.txt")
        println(solvePart2(coordinates))
    }

    private fun solvePart1(coordinates: List<Coordinate>): Int {
        val word = "XMAS"
        return coordinates.sumOf { countWords(it, word, 0, coordinates) }
    }

    private fun countWords(coordinate: Coordinate,
                           word: String,
                           index: Int,
                           coordinates: List<Coordinate>,
                           xDirection: Int? = null,
                           yDirection: Int? = null   ): Int {
        if (coordinate.c != word[index]) {
            return 0
        }
        if (coordinate.c == word[index] && index == word.length - 1) {
            return 1
        }
        val surroundingCoordinates = coordinates.filter {
            abs(it.x - coordinate.x) <= 1 && abs(it.y - coordinate.y) <= 1 && it != coordinate
                    && (xDirection == null || it.x - coordinate.x - xDirection == 0)
                    && (yDirection == null || it.y - coordinate.y - yDirection == 0)
        }
        return surroundingCoordinates.sumOf { countWords(it, word, index + 1, coordinates, it.x - coordinate.x, it.y - coordinate.y) }
    }

    private fun solvePart2(coordinates: List<Coordinate>): Int {
        val allStartingCoordinates = coordinates.filter { it.c == 'A' }
        return allStartingCoordinates.count { isCross(it, coordinates) }
    }

    private fun isCross(coordinate: Coordinate, coordinates: List<Coordinate>): Boolean {
        val topLeft = coordinates.find { it.x == coordinate.x -1 && it.y  == coordinate.y -1 }
        val topRight = coordinates.find { it.x == coordinate.x +1 && it.y  == coordinate.y -1 }
        val bottomLeft = coordinates.find { it.x == coordinate.x -1 && it.y  == coordinate.y +1 }
        val bottomRight = coordinates.find { it.x == coordinate.x+1 && it.y  == coordinate.y +1 }
        if (topRight == null || topLeft == null || bottomLeft == null || bottomRight == null) return false
        if (topLeft.c == 'M' && topRight.c == 'M' && bottomLeft.c == 'S' && bottomRight.c == 'S') return true
        if (topLeft.c == 'S' && topRight.c == 'S' && bottomLeft.c == 'M' && bottomRight.c == 'M') return true
        if (topLeft.c == 'M' && topRight.c == 'S' && bottomLeft.c == 'M' && bottomRight.c == 'S') return true
        if (topLeft.c == 'S' && topRight.c == 'M' && bottomLeft.c == 'S' && bottomRight.c == 'M') return true
        return false
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Coordinate> {
        val lines = loadStringLines(fileName)
        return lines.mapIndexed { y, line -> line.chars().toArray().mapIndexed { x, c -> Coordinate(x = x, y = y, c = c.toChar()) } }.flatten().sortedWith(compareBy({ it.y }, { it.x }))
    }
}
