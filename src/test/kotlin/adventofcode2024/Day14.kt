package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day14 {
    data class Robot(
        val id: Int,
        val position: Pair<Int, Int>,
        val vector: Pair<Int, Int>,
    )

    @Test
    fun testPartTrivial() {
        solve(
            listOf(
                Robot(id = 0, position = Pair(2, 4), vector = Pair(2, -3)),
            ),
            spaceX = 11,
            spaceY = 7,
            turns = 5,
        )
    }

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-14.txt")
        assertThat(solve(model, 11, 7, 100L)).isEqualTo(12)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-14.txt")
        println(solve(model, 101, 103, 100L))
    }

    /**
     * Actually, the number returned here is not important, it's just printing out grids
     * that might contain a Christmas tree. So I just did a visual inspection of all diagrams
     * that have a lot of robots on the same line.
     */
    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-14.txt")
        solve(model, 101, 103, 10000L)
    }

    private fun solve(
        model: List<Robot>,
        spaceX: Int,
        spaceY: Int,
        turns: Long,
    ): Int {
        var newPositions = model
        for (i in 1..turns) {
            newPositions = newPositions.map { determineNewPosition(it, spaceX, spaceY) }
            mightContainChristmasTree(newPositions, spaceX, spaceY, i)
        }
        // calculate safety
        val q1 = newPositions.filter { it.position.first < spaceX / 2 && it.position.second < spaceY / 2 }
        val q2 = newPositions.filter { it.position.first > spaceX / 2 && it.position.second < spaceY / 2 }
        val q3 = newPositions.filter { it.position.first < spaceX / 2 && it.position.second > spaceY / 2 }
        val q4 = newPositions.filter { it.position.first > spaceX / 2 && it.position.second > spaceY / 2 }
        return q1.count() * q2.count() * q3.count() * q4.count()
    }

    private fun mightContainChristmasTree(
        newPositions: List<Robot>,
        spaceX: Int,
        spaceY: Int,
        index: Long,
    ): Boolean {
        val search = 20
        newPositions.forEach {
            val robot = it
            val sameLine = newPositions.filter { it.position.second == robot.position.second }
            if (sameLine.count() > search) {
                println(index)
                println()
                renderSpace(newPositions, spaceX, spaceY)
            }
            if (sameLine.count() > search) return true
        }
        return false
    }

    fun determineNewPosition(
        robot: Robot,
        spacex: Int,
        spaceY: Int,
    ): Robot {
        var newX = robot.position.first + robot.vector.first
        var newY = robot.position.second + robot.vector.second
        if (newX > spacex - 1) {
            newX = newX - spacex
        }
        if (newX < 0) {
            newX = spacex + newX
        }
        if (newY > spaceY - 1) {
            newY = newY - spaceY
        }
        if (newY < 0) {
            newY = spaceY + newY
        }
        return robot.copy(position = Pair(newX, newY))
    }

    fun renderSpace(
        space: List<Robot>,
        spaceX: Int,
        spaceY: Int,
    ) {
        for (y in 0..spaceY - 1) {
            for (x in 0..spaceX - 1) {
                val c = space.count { it.position.first == x && it.position.second == y }
                if (c > 0) print("*") else print(".")
            }
            println()
        }
        println()
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Robot> {
        val lines = loadStringLines(fileName)
        val regex = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""")
        return lines.mapIndexed { index, line ->
            val result = regex.matchEntire(line)
            val (ix, iy, vx, vy) = result!!.destructured
            Robot(
                id = index,
                position = Pair(ix.toInt(), iy.toInt()),
                vector = Pair(vx.toInt(), vy.toInt()),
            )
        }
    }
}
