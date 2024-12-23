package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class Day20 {
    @Test
    fun testPart1() {
        val file = "/test-input-day-20.txt"
        val lines = loadStringLines(file)
        val grid = buildModel(lines)
        assertThat(calculateNumberOfCheats(grid, 1)).isEqualTo(44)
    }

    @Test
    fun solvePart1() {
        val file = "/real-input-day-20.txt"
        val lines = loadStringLines(file)
        val grid = buildModel(lines)
        println(calculateNumberOfCheats(grid, 100))
    }

    private fun calculateNumberOfCheats(
        grid: Array<Array<Char>>,
        greaterThan: Int,
    ): Int {
        val defaultDistance = shortestPathInMaze(grid)
        val distances = mutableListOf<Int>()
        for (y in 0..grid.size - 1) {
            for (x in 0..grid[0].size - 1) {
                val gridWithCheat = deepCopy(grid)
                if (gridWithCheat[y][x] == '#') {
                    gridWithCheat[y][x] = '.'
                }
                val distance = shortestPathInMaze(gridWithCheat)
                distances.add(distance)
            }
        }
        val numberOfCheatsGreater = distances.count { defaultDistance - it >= greaterThan }
        return numberOfCheatsGreater
    }

    private fun buildGrid(
        model: List<Pair<Int, Int>>,
        size: Int,
        numberOfBytes: Int,
    ): Array<Array<Char>> {
        val grid: Array<Array<Char>> = Array(size) { Array(size) { '.' } }
        model.take(numberOfBytes).forEach { pair -> grid[pair.second][pair.first] = '#' }
        return grid
    }

    data class Point(
        val x: Int,
        val y: Int,
        val distance: Int,
    )

    private fun shortestPathInMaze(maze: Array<Array<Char>>): Int {
        // Define directions for moving in the grid: up, down, left, right.
        val directions =
            arrayOf(
                Pair(-1, 0),
                Pair(1, 0),
                Pair(0, -1),
                Pair(0, 1),
            )

        val rows = maze.size
        val cols = maze[0].size

        // Find starting point 'S' and ending point 'E'.
        val start = findFirstS(maze)!!

        // BFS
        val queue: Queue<Point> = LinkedList()
        queue.add(start)

        // Keep track of visited cells.
        val visited = Array(rows) { BooleanArray(cols) }
        visited[start.y][start.x] = true

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            // If we reach end, return the distance.
            if (maze[current.y][current.x] == 'E') {
                return current.distance
            }

            // Explore all directions.
            for ((dx, dy) in directions) {
                val newX = current.x + dx
                val newY = current.y + dy

                // Check if the new position is within bounds, not an obstacle ('#'), and not visited.
                if (newX in 0 until rows &&
                    newY in 0 until cols &&
                    maze[newY][newX] != '#' &&
                    !visited[newY][newX]
                ) {
                    visited[newY][newX] = true
                    queue.add(Point(x = newX, y = newY, distance = current.distance + 1))
                }
            }
        }

        // If the queue is exhausted and 'E' was not reached, return -1.
        return -1
    }

    fun findFirstS(grid: Array<Array<Char>>): Point? {
        for (rowIndex in grid.indices) {
            val colIndex = grid[rowIndex].indexOfFirst { it == 'S' }
            if (colIndex != -1) {
                return Point(x = colIndex, y = rowIndex, distance = 0)
            }
        }
        return null // Return null if 'S' is not found
    }

    fun deepCopy(grid: Array<Array<Char>>): Array<Array<Char>> = grid.map { row -> row.copyOf() }.toTypedArray()

    private fun buildModel(lines: List<String>): Array<Array<Char>> {
        val grid: Array<Array<Char>> = Array(lines.size) { Array(lines.first().length) { '.' } }
        lines.forEachIndexed { y, s -> s.forEachIndexed { x, c -> grid[y][x] = c } }
        return grid
    }
}
