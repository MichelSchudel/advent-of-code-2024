package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day9 {
    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-9.txt")
        assertThat(solve(model)).isEqualTo(1928)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-9.txt")
        println(solve(model))
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/test-input-day-9.txt")
        assertThat(solvePart2(model)).isEqualTo(2858)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-9.txt")
        println(solvePart2(model))
    }

    private fun solve(model: Array<String>): Long {
        val workspace = model
        // compact
        var indexOfLastBlock = workspace.indexOfLast { it != "." }
        var indexOfFirstFreeSpace = workspace.indexOfFirst { it == "." }
        while (indexOfLastBlock > indexOfFirstFreeSpace) {
            workspace[indexOfFirstFreeSpace] = workspace[indexOfLastBlock]
            workspace[indexOfLastBlock] = "."
            indexOfLastBlock = workspace.indexOfLast { it != "." }
            indexOfFirstFreeSpace = workspace.indexOfFirst { it == "." }
        }
        // checksum
        return workspace.takeWhile { it != "." }.mapIndexed { index, c -> c.toLong() * index }.sum()
    }

    private fun solvePart2(model: Array<String>): Long {
        // println(model.joinToString(""))
        val workspace = model
        // compact
        // initial program index
        var programIndex =
            model
                .filter { it != "." }
                .map { it.toLong() }
                .max()
        while (programIndex >= 0) {
            // println("program index: $programIndex")
            val programBlocks =
                model
                    .mapIndexed { index, block -> Pair(index, block) }
                    .filter { pair -> pair.second == programIndex.toString() }
            val programLength = programBlocks.count()
            val startingIndex = programBlocks.map { it.first }.min()
            val freeSpaceIndex = findFreeSpaceForProgramLength(model, programLength)
            if (freeSpaceIndex != -1 && freeSpaceIndex < startingIndex) {
                for (i in 0..programLength - 1) {
                    model[freeSpaceIndex + i] = programIndex.toString()
                    model[startingIndex + i] = "."
                }
            }
            programIndex--
            // println(model.joinToString(""))
        }
        // checksum
        return workspace.mapIndexed { index, c -> if (c != ".") c.toLong() * index else 0 }.sum()
    }

    private fun findFreeSpaceForProgramLength(
        model: Array<String>,
        length: Int,
    ): Int {
        var index = 0
        var count = 0
        do {
            val value = model[index]
            if (value == ".") {
                count++
            } else {
                count = 0
            }
            index++
        } while (index < model.size && count < length)
        if (count < length) return -1 else return index - count
    }

    // model builders
    private fun buildModelFromInput(fileName: String): Array<String> {
        val lines = loadStringLines(fileName)
        return lines
            .first()
            .windowed(size = 2, step = 2, partialWindows = true)
            .mapIndexed { index, s ->
                List(s[0].toString().toInt()) { index.toString() } +
                    if (s.length <= 1) listOf() else List(s[1].toString().toInt()) { "." }
            }.flatten()
            .toTypedArray()
    }
}
