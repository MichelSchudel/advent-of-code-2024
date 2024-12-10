package adventofcode2024

import adventofcode2023.loadStringLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day7 {
    val mapPart1 =
        mapOf(
            "+" to { a: Long, b: Long -> a + b },
            "*" to { a: Long, b: Long -> a * b },
        )

    val mapPart2 =
        mapOf(
            "+" to { a: Long, b: Long -> a + b },
            "*" to { a: Long, b: Long -> a * b },
            "||" to { a: Long, b: Long -> (a.toString() + b.toString()).toLong() },
        )

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-7.txt")
        assertThat(solve(model, mapPart1)).isEqualTo(3749)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-7.txt")
        println(solve(model, mapPart1))
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/test-input-day-7.txt")
        assertThat(solve(model, mapPart2)).isEqualTo(11387)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-7.txt")
        println(solve(model, mapPart2))
    }

    fun solve(
        model: List<Pair<Long, List<Long>>>,
        operatorMap: Map<String, (Long, Long) -> Long>,
    ): Long =
        model
            .filter {
                createCalculations(
                    it.second.first(),
                    it.second.drop(1),
                    ArrayList(),
                    operatorMap,
                ).contains(it.first)
            }.sumOf { it.first }

    fun createCalculations(
        inbetweenResult: Long,
        remainingOperands: List<Long>,
        result: ArrayList<Long>,
        operatorMap: Map<String, (Long, Long) -> Long>,
    ): ArrayList<Long> {
        if (remainingOperands.isEmpty()) {
            result.add(inbetweenResult)
        } else {
            operatorMap.forEach {
                createCalculations(
                    it.value.invoke(inbetweenResult, remainingOperands.first()),
                    remainingOperands.drop(1),
                    result,
                    operatorMap,
                )
            }
        }
        return result
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Pair<Long, List<Long>>> {
        val lines = loadStringLines(fileName)
        val s =
            lines.map {
                it.split(":").let { (result, factors) ->
                    Pair(
                        result.toLong(),
                        factors
                            .trim()
                            .split(" ")
                            .map {
                                it.toLong()
                            },
                    )
                }
            }
        return s
    }
}
