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

    private fun countWords(
        coordinate: Coordinate,
        word: String,
        index: Int,
        coordinates: List<Coordinate>,
        xDirection: Int? = null,
        yDirection: Int? = null,
    ): Int {
        if (coordinate.c != word[index]) return 0
        if (coordinate.c == word[index] && index == word.length - 1) return 1
        val surroundingCoordinates =
            coordinates.filter {
                abs(it.x - coordinate.x) <= 1 &&
                    abs(it.y - coordinate.y) <= 1 &&
                    it != coordinate &&
                    (xDirection == null || it.x - coordinate.x - xDirection == 0) &&
                    (yDirection == null || it.y - coordinate.y - yDirection == 0)
            }
        return surroundingCoordinates.sumOf {
            countWords(
                coordinate = it,
                word = word,
                index = index + 1,
                coordinates = coordinates,
                xDirection = it.x - coordinate.x,
                yDirection = it.y - coordinate.y,
            )
        }
    }

    private fun solvePart2(coordinates: List<Coordinate>): Int = coordinates.filter { it.c == 'A' }.count { isXMas(it, coordinates) }

    private fun isXMas(
        coordinate: Coordinate,
        coordinates: List<Coordinate>,
    ): Boolean {
        val s =
            coordinates
                .filter {
                    abs(it.x - coordinate.x) == 1 && abs(it.y - coordinate.y) == 1 && (it.c == 'M' || it.c == 'S')
                }.map { it.c }
                .joinToString("")
        return when (s.length) {
            4 -> s == "MMSS" || s == "SSMM" || s == "MSMS" || s == "SMSM"
            else -> false
        }
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Coordinate> {
        val lines = loadStringLines(fileName)
        return lines
            .mapIndexed {
                    y,
                    line,
                ->
                line.chars().toArray().mapIndexed {
                        x,
                        c,
                    ->
                    Coordinate(x = x, y = y, c = c.toChar())
                }
            }.flatten()
            .sortedWith(compareBy({ it.y }, { it.x }))
    }
}
