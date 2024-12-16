package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day16 {
    data class Coordinate(
        val x: Int,
        val y: Int,
        val item: Char,
    )

    data class Step(
        val x: Int,
        val y: Int,
        val score: Int,
        val direction: Direction,
    )

    enum class Direction(
        val x: Int,
        val y: Int,
    ) {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0),
    }

    @Test
    fun testPart1Trivial() {
        val model = buildModelFromInput("/test-input-day-16.txt")
        assertThat(solve(model)).isEqualTo(7036)
    }

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-16-2.txt")
        assertThat(solve(model)).isEqualTo(11048)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-16.txt")
        println(solve(model))
    }

    private fun solve(model: List<Coordinate>): Int {
        // comment
        val startingPoint = model.find { it.item == 'S' }!!
        val validPaths =
            walk(
                startingPoint.x,
                startingPoint.y,
                Direction.RIGHT,
                1,
                listOf(),
                mutableListOf(),
                model,
            )
        // println("valid paths:" + validPaths.forEach { println(it) })
        return validPaths.map { it.map { it.score }.sum() }.min() - 1
    }

    private fun walk(
        targetX: Int,
        targetY: Int,
        currentDirection: Direction,
        incurredScore: Int,
        currentPath: List<Step>,
        validPaths: MutableList<List<Step>>,
        model: List<Coordinate>,
    ): List<List<Step>> {
        // println("depth: ${currentPath.size}")
        // if (currentPath.size > 2000) renderCurrentPath(currentPath, model)
        val targetCoordinate = model.find { it.x == targetX && it.y == targetY && it.item != 'S' }
        if (targetCoordinate != null && targetCoordinate.item == 'E') {
            validPaths.add(
                currentPath.plus(
                    Step(
                        x = targetCoordinate.x,
                        y = targetCoordinate.y,
                        score = incurredScore,
                        direction = currentDirection,
                    ),
                ),
            )
            // println("valid path found at depth ${currentPath.size}!")
            return validPaths
        }
        if (targetCoordinate != null && targetCoordinate.item == '#') {
            // dead end, do nothing
            return validPaths
        }
        if (currentPath.any { it.x == targetX && it.y == targetY }) {
            // cycle detected, do nothing
            // println("cycle, path size ${currentPath.size}")
            return validPaths
        }
        // recursively walk next nodes
        if (targetCoordinate != null) {
            throw RuntimeException("at this point the target coordinate should not have been found")
        }

        // add current node to path
        val newPath =
            currentPath.plus(
                Step(
                    x = targetX,
                    y = targetY,
                    score = incurredScore,
                    direction = currentDirection,
                ),
            )

        // straight ahead
        // println("walk to ${targetX + currentDirection.x}, ${targetY + currentDirection.y} ")
        val next = model.find { it.x == targetX + currentDirection.x && it.y == targetY + currentDirection.x }
        if (next == null || next.item != 'E') {
            walk(
                targetX + currentDirection.x,
                targetY + currentDirection.y,
                currentDirection,
                1,
                newPath,
                validPaths,
                model,
            )
        }
        // clockwise
        val clockwiseDirection = getClockwiseDirection(currentDirection)
        // println("walk to ${targetX + clockwiseDirection.x}, ${targetY + clockwiseDirection.y} ")
        val next2 = model.find { it.x == targetX + clockwiseDirection.x && it.y == targetY + clockwiseDirection.y }
        if (next2 == null || next2.item != 'E') {
            walk(
                targetX + clockwiseDirection.x,
                targetY + clockwiseDirection.y,
                clockwiseDirection,
                1001,
                newPath,
                validPaths,
                model,
            )
        }
        // counterclockwise
        val counterClockwiseDirection = getCounterClockwiseDirection(currentDirection)
        // println("walk to ${targetX + counterClockwiseDirection.x}, ${targetY + counterClockwiseDirection.y} ")
        val next3 = model.find { it.x == targetX + counterClockwiseDirection.x && it.y == targetY + counterClockwiseDirection.y }
        if (next3 == null || next3.item == 'E') {
            walk(
                targetX + counterClockwiseDirection.x,
                targetY + counterClockwiseDirection.y,
                counterClockwiseDirection,
                1001,
                newPath,
                validPaths,
                model,
            )
        }
        return validPaths
    }

    fun renderCurrentPath(
        currentPath: List<Step>,
        model: List<Coordinate>,
    ) {
        val maxX = model.map { it.x }.max()!!
        val maxY = model.map { it.y }.max()!!
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (currentPath.any { it.x == x && it.y == y }) {
                    val currentStep = currentPath.find { it.x == x && it.y == y }!!
                    when (currentStep.direction) {
                        Direction.UP -> print("^")
                        Direction.DOWN -> print("v")
                        Direction.RIGHT -> print(">")
                        Direction.LEFT -> print("<")
                    }
                } else {
                    val c = model.find { it.x == x && it.y == y }
                    if (c != null) {
                        print(c.item.toString())
                    } else {
                        print(".")
                    }
                }
            }
            println()
        }
        println()
    }

    private fun getClockwiseDirection(currentDirection: Direction): Direction =
        when (currentDirection) {
            Direction.RIGHT -> Direction.DOWN
            Direction.LEFT -> Direction.UP
            Direction.UP -> Direction.RIGHT
            Direction.DOWN -> Direction.LEFT
        }

    private fun getCounterClockwiseDirection(currentDirection: Direction): Direction =
        when (currentDirection) {
            Direction.RIGHT -> Direction.UP
            Direction.LEFT -> Direction.DOWN
            Direction.UP -> Direction.LEFT
            Direction.DOWN -> Direction.RIGHT
        }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Day16.Coordinate> {
        val lines = loadStringLines(fileName)
        return lines
            .mapIndexed { y, line ->
                line.mapIndexed { x, c ->
                    Day16.Coordinate(x, y, c)
                }
            }.flatten()
            .filter { it.item != '.' }
    }
}
