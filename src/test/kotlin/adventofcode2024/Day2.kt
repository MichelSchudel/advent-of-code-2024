package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day2 {
    @Test
    fun testPart1() {
        val reports = buildModelFromTestInput()
        assertThat(solvePart1(reports)).isEqualTo(2)
    }

    @Test
    fun solvePart1() {
        val reports = buildModelFromRealInput()
        println(solvePart1(reports))
    }

    @Test
    fun testPart2() {
        val reports = buildModelFromTestInput()
        assertThat(solvePart2(reports)).isEqualTo(4)
    }

    @Test
    fun solvePart2() {
        val reports = buildModelFromRealInput()
        println(solvePart2(reports))
    }

    private fun solvePart2(reportList: List<List<Int>>): Int =
        reportList
            .map { report ->
                List(report.size) { index ->
                    report.filterIndexed { i, _ -> i != index }
                }
            }.map { list ->
                if (solvePart1(list) > 0) 1 else 0
            }.sum()

    private fun solvePart1(reportList: List<List<Int>>): Int =
        reportList
            .map { report ->
                (report == report.sorted() || report == report.sorted().reversed()) &&
                    report
                        .windowed(2)
                        .none { (a, b) ->
                            abs(a - b) < 1 || abs(a - b) > 3
                        }
            }.count { it }

    // model builders

    private fun buildModelFromTestInput(): List<List<Int>> =
        loadStringLines("/test-input-day-2.txt").map { line ->
            line.split(" ").map { it.toInt() }
        }

    private fun buildModelFromRealInput(): List<List<Int>> =
        loadStringLines("/real-input-day-2.txt").map { line ->
            line.split(" ").map {
                it.toInt()
            }
        }
}
