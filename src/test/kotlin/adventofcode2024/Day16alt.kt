package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day16alt {
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
    fun testPart12() {
        val model = buildModelFromInput("/test-input-day-16-3.txt")
        assertThat(solve(model)).isEqualTo(1003)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-16.txt")
        println(solve(model))
    }

    private fun solve(model: Pair<Pair<Int, Int>, Array<Array<Char>>>): Int {
        // comment
        val startingPoint = model.first
        val matrix = model.second
        val scores =
            walkIterative(
                startingPoint.first,
                startingPoint.second,
                Direction.RIGHT,
                matrix,
            )
        return scores.min()
    }

    private fun walkIterative(
        startX: Int,
        startY: Int,
        initialDirection: Direction,
        matrix: Array<Array<Char>>,
    ): MutableList<Int> {
        val scores = mutableListOf<Int>()
        val stack =
            mutableListOf(
                State(
                    x = startX,
                    y = startY,
                    direction = initialDirection,
                    score = 0,
                    visited = mutableSetOf(),
                ),
            )

        while (stack.isNotEmpty()) {
            // println(stack.last().visited.size)
            val current = stack.removeAt(stack.size - 1)
            val (x, y, direction, score, visited) = current

            // Check if we reached the goal
            if (matrix[y][x] == 'E') {
                // println("score")
                scores.add(score)
                continue
            }

            // Skip walls and already visited cells
            if (matrix[y][x] == '#') {
                // println("$x,$y is a wall")
                continue
            }
            if (Pair(x, y) in visited) {
                // println("$x,$y already visited")
                continue
            }

            // Add the current position to visited
            val newVisited = visited.plus(Pair(x, y))

            // Explore possible directions
            val newX = x + direction.x
            val newY = y + direction.y
            stack.add(State(newX, newY, direction, score + 1, newVisited))

            val clockwiseDirection = getClockwiseDirection(direction)
            if (matrix[y + clockwiseDirection.y][x + clockwiseDirection.x] != '#') {
                stack.add(
                    State(
                        x + clockwiseDirection.x,
                        y + clockwiseDirection.y,
                        clockwiseDirection,
                        score + 1001,
                        newVisited,
                    ),
                )
            }

            val counterClockwiseDirection = getCounterClockwiseDirection(direction)
            if (matrix[y + counterClockwiseDirection.y][x + counterClockwiseDirection.x] != '#') {
                stack.add(
                    State(
                        x + counterClockwiseDirection.x,
                        y + counterClockwiseDirection.y,
                        counterClockwiseDirection,
                        score + 1001,
                        newVisited,
                    ),
                )
            }
        }

        return scores
    }

    data class State(
        val x: Int,
        val y: Int,
        val direction: Direction,
        val score: Int,
        val visited: Set<Pair<Int, Int>>,
    )

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
    private fun buildModelFromInput(fileName: String): Pair<Pair<Int, Int>, Array<Array<Char>>> {
        val lines = loadStringLines(fileName)
        val maxY = lines.size
        val maxX = lines.first().length
        var startingPoint: Pair<Int, Int>? = null
        val matrix = Array(maxY) { Array(maxX) { ' ' } }
        lines
            .forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    matrix[y][x] = c
                    if (c == 'S') {
                        startingPoint = Pair(x, y)
                    }
                }
            }
        return Pair(startingPoint!!, matrix)
    }
}
