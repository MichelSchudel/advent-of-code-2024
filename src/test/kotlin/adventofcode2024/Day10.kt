package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day10 {
    data class Coordinate(
        val x: Int,
        val y: Int,
        var height: Int,
    )

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-10.txt")
        assertThat(solvePart1(model)).isEqualTo(36)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-10.txt")
        println(solvePart1(model))
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/test-input-day-10.txt")
        assertThat(solvePart2(model)).isEqualTo(81)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-10.txt")
        println(solvePart2(model))
    }

    private fun solvePart1(model: List<Coordinate>): Int =
        model
            .filter { it.height == 0 }
            .map { getPaths(it, model, ArrayList()) }
            .sumOf { it.distinctBy { Pair(it.x, it.y) }.count() }

    private fun solvePart2(model: List<Coordinate>): Int =
        model
            .filter {
                it.height == 0
            }.map { getPaths(it, model, ArrayList()) }
            .sumOf { it.count() }

    private fun getPaths(
        coordinate: Coordinate,
        model: List<Coordinate>,
        finishes: ArrayList<Coordinate>,
    ): List<Coordinate> {
        if (coordinate.height == 9) {
            finishes.add(coordinate)
        } else {
            model
                .firstOrNull { it.x == coordinate.x - 1 && it.y == coordinate.y && it.height == coordinate.height + 1 }
                ?.let { getPaths(it, model, finishes) }
            model
                .firstOrNull { it.x == coordinate.x && it.y == coordinate.y - 1 && it.height == coordinate.height + 1 }
                ?.let { getPaths(it, model, finishes) }
            model
                .firstOrNull { it.x == coordinate.x + 1 && it.y == coordinate.y && it.height == coordinate.height + 1 }
                ?.let { getPaths(it, model, finishes) }
            model
                .firstOrNull { it.x == coordinate.x && it.y == coordinate.y + 1 && it.height == coordinate.height + 1 }
                ?.let { getPaths(it, model, finishes) }
        }
        return finishes
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Coordinate> {
        val lines = loadStringLines(fileName)
        return lines
            .mapIndexed { y, line ->
                line.mapIndexed { x, c ->
                    Coordinate(x, y, if (c.toString() == ".") -1 else c.toString().toInt())
                }
            }.flatten()
    }
}
