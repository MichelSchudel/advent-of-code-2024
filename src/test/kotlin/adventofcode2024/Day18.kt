import adventofcode2024.loadStringLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class Day18 {
    @Test
    fun testPart1() {
        val file = "/test-input-day-18.txt"
        val lines = loadStringLines(file)
        val model = buildModel(lines)
        val grid = buildGrid(model = model, size = 7, numberOfBytes = 12)
        assertThat(shortestPathInMaze(grid)).isEqualTo(22)
    }

    @Test
    fun solvePart1() {
        val file = "/real-input-day-18.txt"
        val lines = loadStringLines(file)
        val model = buildModel(lines)
        val grid = buildGrid(model = model, size = 71, numberOfBytes = 1024)
        println( shortestPathInMaze(grid))
    }

    @Test
    fun testPart2() {
        val file = "/test-input-day-18.txt"
        val lines = loadStringLines(file)
        val model = buildModel(lines)
        var numberOfBytes=12
        do {
            val grid = buildGrid(model = model, size = 7, numberOfBytes = numberOfBytes)
            numberOfBytes++
        } while (shortestPathInMaze(grid) != -1)
        assertThat( model.get(numberOfBytes- 2)).isEqualTo(Pair(6,1))
    }

    @Test
    fun solvePart2() {
        val file = "/real-input-day-18.txt"
        val lines = loadStringLines(file)
        val model = buildModel(lines)
        var numberOfBytes=1024
        do {
            val grid = buildGrid(model = model, size = 71, numberOfBytes = numberOfBytes)
            numberOfBytes++
        } while (shortestPathInMaze(grid) != -1)
        println( model.get(numberOfBytes- 2))
    }

    private fun buildGrid(model: List<Pair<Int, Int>>, size: Int, numberOfBytes: Int): Array<Array<Char>> {
        val grid: Array<Array<Char>> = Array(size) { Array(size) { '.' } }
        model.take(numberOfBytes).forEach { pair -> grid[pair.second][pair.first] = '#' }
        return grid
    }

    fun shortestPathInMaze(maze: Array<Array<Char>>): Int {
        data class Point(val x: Int, val y: Int, val distance: Int)

        // Define directions for moving in the grid: up, down, left, right.
        val directions = arrayOf(
            Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)
        )

        val rows = maze.size
        val cols = maze[0].size

        // Find starting point 'S' and ending point 'E'.
        var start = Point(0, 0, 0)

        // BFS
        val queue: Queue<Point> = LinkedList()
        queue.add(start)

        // Keep track of visited cells.
        val visited = Array(rows) { BooleanArray(cols) }
        visited[start.x][start.y] = true

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            // If we reach the bottom right, return the distance.
            if (current.x == maze.size - 1 && current.y == maze.size - 1) {
                return current.distance
            }

            // Explore all directions.
            for ((dx, dy) in directions) {
                val newX = current.x + dx
                val newY = current.y + dy

                // Check if the new position is within bounds, not an obstacle ('#'), and not visited.
                if (newX in 0 until rows &&
                    newY in 0 until cols &&
                    maze[newX][newY] != '#' &&
                    !visited[newX][newY]
                ) {
                    visited[newX][newY] = true
                    queue.add(Point(newX, newY, current.distance + 1))
                }
            }
        }

        // If the queue is exhausted and 'E' was not reached, return -1.
        return -1
    }

    fun buildModel(lines: List<String>): List<Pair<Int, Int>> {
        val l = lines.map { it.split(",") }
        return l.map { elements -> Pair(elements[0].toInt(), elements[1].toInt()) }
    }
}
