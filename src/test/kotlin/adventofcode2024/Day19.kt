package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day19 {
    data class DesignPart(
        val name: String,
        val remainder: String,
        val history: List<String>,
    )

    @Test
    fun testPart1() {
        val file = "/test-input-day-19.txt"
        val lines = loadStringLines(file)
        val model = buildModel(lines)
        assertThat(solvePart1(model)).isEqualTo(6)
    }

    @Test
    fun solvePart1() {
        val file = "/real-input-day-19.txt"
        val lines = loadStringLines(file)
        val model = buildModel(lines)
        println(solvePart1(model))
    }

    @Test
    fun testPart2() {
        val file = "/test-input-day-19.txt"
        val lines = loadStringLines(file)
        val model = buildModel(lines)
        assertThat(solvePart2(model)).isEqualTo(16)
    }

    @Test
    fun solvePart2() {
        val file = "/real-input-day-19.txt"
        val lines = loadStringLines(file)
        val model = buildModel(lines)
        println(solvePart2(model))
    }

    private fun solvePart1(model: Pair<List<String>, List<String>>): Int {
        val towels = model.first
        val designs = model.second
        val pattern = "^(" + towels.joinToString("|") + ")+$"
        val regex = Regex(pattern)
        return designs.count { regex.matches(it) }
    }

    private fun solvePart2(model: Pair<List<String>, List<String>>): Long {
        val towels = model.first
        val designs = model.second
        val solutions =
            designs.map {
                buildSolutions(
                    towels = towels,
                    design = it,
                    end = 1,
                    solutionMap = HashMap(),
                )
            }
        return solutions.sum()
    }

    private fun buildSolutions(
        towels: List<String>,
        design: String,
        end: Int,
        solutionMap: MutableMap<String, Long>,
    ): Long {
        if (solutionMap.containsKey(design)) {
            return solutionMap[design]!!
        }

        if (design.isEmpty()) {
            // solution found
            return 1
        }

        if (end > design.length) {
            // dead end
            return 0
        }

        if (towels.contains(design.substring(0, end))) { // Found towel to match prefix of line
            val total =
                buildSolutions(towels, design.substring(end), 1, solutionMap) +
                    buildSolutions(
                        towels,
                        design,
                        end + 1,
                        solutionMap,
                    )
            solutionMap[design] = total
            return total
        }

        val total = buildSolutions(towels, design, end + 1, solutionMap)
        solutionMap[design] = total
        return total
    }

    private fun buildModel(lines: List<String>): Pair<List<String>, List<String>> {
        val towels = lines.first().split(",").map { it.trim() }
        val designs = lines.drop(2)
        return Pair(towels, designs)
    }
}
