package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day1 {
    @Test
    fun testPart1() {
        val bothLists = buildModelFromTestInput()
        assertThat(solvePart1(bothLists.first, bothLists.second)).isEqualTo(11)
    }

    @Test
    fun solvePart1() {
        val bothLists = buildModelFromRealInput()
        println(solvePart1(bothLists.first, bothLists.second))
    }

    @Test
    fun testPart2() {
        val bothLists = buildModelFromTestInput()
        assertThat(solvePart2(bothLists.first, bothLists.second)).isEqualTo(31)
    }

    @Test
    fun solvePart2() {
        val bothLists = buildModelFromRealInput()
        println(solvePart2(bothLists.first, bothLists.second))
    }

    private fun solvePart1(
        list1: List<Int>,
        list2: List<Int>,
    ): Int = list1.sorted().zip(list2.sorted()) { e1, e2 -> abs(e2 - e1) }.sum()

    private fun solvePart2(
        list1: List<Int>,
        list2: List<Int>,
    ): Int = list1.sumOf { number -> list2.count { it == number } * number }

    // model builders

    private fun buildModelFromTestInput(): Pair<List<Int>, List<Int>> {
        val lines = loadStringLines("/test-input-day-1.txt")
        val bothLists = buildModel(lines)
        return bothLists
    }

    private fun buildModelFromRealInput(): Pair<List<Int>, List<Int>> {
        val lines = loadStringLines("/real-input-day-1.txt")
        val bothLists = buildModel(lines)
        return bothLists
    }

    // model builders
    private fun buildModel(lines: List<String>): Pair<List<Int>, List<Int>> =
        lines
            .map {
                it.split("   ").let { (a, b) -> a.toInt() to b.toInt() }
            }.unzip()
}
