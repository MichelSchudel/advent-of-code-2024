package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day6 {
    enum class Direction(
        val xVector: Int,
        val yVector: Int,
    ) {
        UP(0, -1),
        DOWN(0, 1),
        RIGHT(1, 0),
        LEFT(-1, 0),
    }

    class Coordinate(
        val x: Int,
        val y: Int,
        var obstacle: Boolean,
        var guardStartingPpint: Boolean,
    )

    data class GuardHistory(
        val x: Int,
        val y: Int,
        val direction: Direction,
    )

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-6.txt")
        val initialGuardCoordinate = model.find { it.guardStartingPpint }!!
        assertThat(solvePart1(initialGuardCoordinate, model)).isEqualTo(41)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-6.txt")
        val initialGuardCoordinate = model.find { it.guardStartingPpint }!!
        println(solvePart1(initialGuardCoordinate, model))
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/test-input-day-6.txt")
        val initialGuardCoordinate = model.find { it.guardStartingPpint }!!
        assertThat(solvePart2(initialGuardCoordinate, model)).isEqualTo(6)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-6.txt")
        val initialGuardCoordinate = model.find { it.guardStartingPpint }!!
        println(solvePart2(initialGuardCoordinate, model))
    }

    private fun solvePart2(
        initialGuardCoordinate: Coordinate,
        model: List<Coordinate>,
    ): Int {
        var numberOfPossibleObstructions = 0
        val maxXSize = model.maxBy { it.x }!!.x
        val maxYSize = model.maxBy { it.y }!!.y
        for (y: Int in 0..maxYSize) {
            for (x: Int in 0..maxXSize) {
                val mutableModel = model.toMutableList()
                val coordinate =
                    model.find { it.x == x && it.y == y && !it.obstacle && !it.guardStartingPpint }?.apply { this.obstacle = true }
                        ?: Coordinate(x, y, true, false).also { mutableModel.add(it) }
                try {
                    solvePart1(initialGuardCoordinate, model)
                } catch (e: RuntimeException) {
                    println("loop detected x=${coordinate.x}, y =${coordinate.y}")
                    numberOfPossibleObstructions++
                }
                coordinate.obstacle = false
            }
        }
        return numberOfPossibleObstructions
    }

    private fun solvePart1(
        initialGuardCoordinate: Coordinate,
        model: List<Coordinate>,
    ): Int {
        val guardHistoryList = mutableSetOf<GuardHistory>()
        var currentGuardCoordinate = initialGuardCoordinate
        guardHistoryList.add(GuardHistory(currentGuardCoordinate.x, currentGuardCoordinate.y, Direction.UP))
        var currentGuardDirection = Direction.UP
        do {
            val nextGuardPosition =
                model.find {
                    it.x == currentGuardCoordinate!!.x + currentGuardDirection.xVector &&
                        it.y == currentGuardCoordinate!!.y + currentGuardDirection.yVector
                }
            if (nextGuardPosition != null) {
                if (nextGuardPosition.obstacle) {
                    // turn 90 degrees
                    currentGuardDirection =
                        when (currentGuardDirection) {
                            Direction.UP -> Direction.RIGHT
                            Direction.RIGHT -> Direction.DOWN
                            Direction.DOWN -> Direction.LEFT
                            Direction.LEFT -> Direction.UP
                        }
                } else {
                    // move to next position
                    currentGuardCoordinate = nextGuardPosition
                }
                val guardhistory =
                    GuardHistory(currentGuardCoordinate.x, currentGuardCoordinate.y, currentGuardDirection)
                if (guardHistoryList.contains(guardhistory)) throw RuntimeException("loop detected")
                guardHistoryList.add(guardhistory)
            }
        } while (nextGuardPosition != null)
        return guardHistoryList.map { it.x to it.y }.toSet().size
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Coordinate> {
        val lines = loadStringLines(fileName)
        return lines
            .mapIndexed { y, line ->
                line.chars().toArray().mapIndexed { x, c -> createCoordinate(x, y, c.toChar()) }
            }.flatten()
    }

    private fun createCoordinate(
        x: Int,
        y: Int,
        c: Char,
    ): Coordinate {
        val obstacle = (c == '#')
        val guardVisited = (c == '^')
        return Coordinate(x, y, obstacle, guardVisited)
    }
}
