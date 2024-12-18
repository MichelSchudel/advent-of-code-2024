package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day16 {
    @Test
    fun test() {
        val file = "/test-input-day-16.txt"
        val lines = loadStringLines(file)
        val maze = Maze(lines)
        val result = maze.findPaths()
        assertThat(result.first).isEqualTo(7036)
        assertThat(result.second).isEqualTo(45)
    }

    @Test
    fun solve() {
        val file = "/real-input-day-16.txt"
        val lines = loadStringLines(file)
        val game = Maze(lines)
        val result = game.findPaths()
        println("shortest path: ${result.first}")
        println("number of unique tiles: ${result.second}")
    }

    class Maze(
        lines: List<String>,
    ) {
        private val walls = mutableSetOf<Pair<Int, Int>>()
        private val startPos: Pair<Int, Int>
        private val endPos: Pair<Int, Int>

        init {
            var theStartPos = 0 to 0
            var theEndPos = 0 to 0
            lines.indices.forEach { y ->
                lines[y].indices.forEach { x ->
                    if (lines[y][x] == '#') {
                        walls.add(y to x)
                    }
                    if (lines[y][x] == 'S') {
                        theStartPos = y to x
                    }
                    if (lines[y][x] == 'E') {
                        theEndPos = y to x
                    }
                }
            }

            startPos = theStartPos
            endPos = theEndPos
        }

        private fun addPair(
            a: Pair<Int, Int>,
            b: Pair<Int, Int>,
        ): Pair<Int, Int> = Pair(a.first + b.first, a.second + b.second)

        fun findPaths(): Pair<Int, Int> {
            val deerList =
                mutableListOf(
                    Deer(startPos, 0 to 1, 0, mutableSetOf()),
                    Deer(startPos, -1 to 0, 1000, mutableSetOf()),
                )

            val scores = mutableMapOf<Pair<Int, Int>, Int>()
            val deerAtEnd = mutableListOf<Deer>()

            while (deerList.isNotEmpty()) {
                val deer = deerList.removeAt(0)
                while (true) {
                    deer.forward()
                    if (walls.contains(deer.pos)) break
                    if (deer.visited.contains(deer.pos)) break
                    if ((scores[deer.pos] ?: Int.MAX_VALUE) < deer.score - 1000) break
                    if (!walls.contains(addPair(deer.pos, deer.direction.turnRight()))) {
                        deerList.add(
                            Deer(
                                deer.pos,
                                deer.direction.turnRight(),
                                deer.score + 1000,
                                deer.visited.toMutableSet(),
                            ),
                        )
                    }
                    if (!walls.contains(addPair(deer.pos, deer.direction.turnLeft()))) {
                        deerList.add(
                            Deer(
                                deer.pos,
                                deer.direction.turnLeft(),
                                deer.score + 1000,
                                deer.visited.toMutableSet(),
                            ),
                        )
                    }
                    if ((scores[deer.pos] ?: Int.MAX_VALUE) > deer.score) {
                        scores[deer.pos] = deer.score
                    }
                    if (deer.pos == endPos) {
                        deerAtEnd.add(deer)
                        break
                    }
                }
            }
            val part1 = scores[endPos]!!
            val part2 =
                deerAtEnd
                    .asSequence()
                    .filter { it.score == scores[endPos] }
                    .map { it.visited }
                    .flatten()
                    .toSet()
                    .count() + 1
            return part1 to part2
        }

        private class Deer(
            var pos: Pair<Int, Int>,
            val direction: Pair<Int, Int>,
            var score: Int,
            val visited: MutableSet<Pair<Int, Int>>,
        ) {
            fun addPair(
                a: Pair<Int, Int>,
                b: Pair<Int, Int>,
            ): Pair<Int, Int> = Pair(a.first + b.first, a.second + b.second)

            fun forward() {
                visited.add(pos)
                pos = addPair(pos, direction)
                score += 1
            }
        }

        private operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> = first - other.first to second - other.second

        private fun Pair<Int, Int>.turnRight() =
            when (this) {
                0 to 1 -> 1 to 0
                1 to 0 -> 0 to -1
                0 to -1 -> -1 to 0
                else -> 0 to 1
            }

        private fun Pair<Int, Int>.turnLeft() = this.turnRight().turnRight().turnRight()
    }
}
