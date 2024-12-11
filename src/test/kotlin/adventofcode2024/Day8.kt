package adventofcode2024

import adventofcode2023.loadStringLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day8 {
    data class Coordinate(
        val x: Int,
        val y: Int,
        var frequency: Char,
    )

    data class Vector(
        val x: Int,
        val y: Int,
    )

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-8.txt")
        assertThat(solve(model, false)).isEqualTo(14)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-8.txt")
        println(solve(model, false))
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/test-input-day-8.txt")
        assertThat(solve(model, true)).isEqualTo(34)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-8.txt")
        println(solve(model, true))
    }

    private fun solve(
        coordinates: List<Coordinate>,
        endless: Boolean,
    ): Int {
        val width = coordinates.maxOfOrNull { it.x }!!
        val height = coordinates.maxOfOrNull { it.y }!!
        val antennas = coordinates.filter { it.frequency != '.' }
        val antinodes = antennas.map { findAntinodes(it, coordinates, endless) }
        return antinodes
            .flatten()
            .distinctBy { Pair(it.x, it.y) }
            .count { it.x <= width && it.y <= height && it.x >= 0 && it.y >= 0 }
    }

    private fun findAntinodes(
        antenna: Coordinate,
        map: List<Coordinate>,
        endless: Boolean,
    ): List<Coordinate> {
        val counterparts = map.filter { it.frequency == antenna.frequency && it != antenna }
        val vectors =
            counterparts
                .map { counterpart -> Pair(counterpart, Vector(counterpart.x - antenna.x, counterpart.y - antenna.y)) }
                .map { pair -> createAntinodeCoordinates(pair.first, pair.second, endless) }
                .flatten()
        return vectors
    }

    private fun createAntinodeCoordinates(
        counterpart: Coordinate,
        vector: Vector,
        endless: Boolean,
    ): List<Coordinate> {
        if (!endless) {
            return listOf(Coordinate(x = counterpart.x + vector.x, y = counterpart.y + vector.y, frequency = '#'))
        } else {
            val list = mutableListOf<Coordinate>()
            // add one antinode on the counterpart itself
            list.add(Coordinate(x = counterpart.x, y = counterpart.y, frequency = '#'))
            var newX = counterpart.x + vector.x
            var newY = counterpart.y + vector.y
            for (i in 1..50) {
                list.add(Coordinate(x = newX, y = newY, frequency = '#'))
                newX += vector.x
                newY += vector.y
            }
            return list
        }
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Coordinate> {
        val lines = loadStringLines(fileName)
        return lines
            .mapIndexed { y, line ->
                line.chars().toArray().mapIndexed { x, c -> Coordinate(x, y, c.toChar()) }
            }.flatten()
    }
}
