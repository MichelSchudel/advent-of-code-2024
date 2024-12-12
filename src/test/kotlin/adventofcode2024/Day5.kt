package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day5 {
    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-5.txt")
        assertThat(solvePart1(model)).isEqualTo(143)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-5.txt")
        println(solvePart1(model))
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/test-input-day-5.txt")
        assertThat(solvePart2(model)).isEqualTo(123)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-5.txt")
        println(solvePart2(model))
    }

    private fun solvePart1(model: Pair<List<Pair<String, String>>, List<List<String>>>): Int {
        val correctUpdatesList = model.second.filter { isValid(it, model.first) }
        return calculateSum(correctUpdatesList)
    }

    private fun solvePart2(model: Pair<List<Pair<String, String>>, List<List<String>>>): Int {
        val incorrectUpdatesList = model.second.filter { !isValid(it, model.first) }
        val correctedUpdatesList = sortList(incorrectUpdatesList, model.first)
        return calculateSum(correctedUpdatesList)
    }

    private fun sortList(
        incorrectUpdates: List<List<String>>,
        rules: List<Pair<String, String>>,
    ): List<List<String>> =
        incorrectUpdates.map {
            it.sortedWith(
                object : Comparator<String> {
                    override fun compare(
                        e1: String,
                        e2: String,
                    ): Int {
                        if (rules.any { it == Pair(e1, e2) }) {
                            return -1
                        }
                        if (rules.any { it == Pair(e2, e1) }) {
                            return 1
                        }
                        return 0
                    }
                },
            )
        }

    private fun calculateSum(correctUpdatesList: List<List<String>>): Int =
        correctUpdatesList.sumOf {
            val middleIndex = it.size / 2
            it[middleIndex].toInt()
        }

    private fun isValid(
        update: List<String>,
        rules: List<Pair<String, String>>,
    ): Boolean = update.windowed(2).all { (a, b) -> rules.any { p -> p.first == a && p.second == b } }

    // model builders
    private fun buildModelFromInput(fileName: String): Pair<List<Pair<String, String>>, List<List<String>>> {
        val lines = loadStringLines(fileName)
        val rules = lines.takeWhile { it != "" }
        val updates = lines.dropWhile { it != "" }.dropWhile { it == "" }
        val pairs = rules.map { it.split("|").let { (a, b) -> Pair(a, b) } }
        val updatesList = updates.map { it.split(",") }
        return Pair(pairs, updatesList)
    }
}
