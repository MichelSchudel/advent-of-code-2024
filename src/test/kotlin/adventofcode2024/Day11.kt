package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day11 {
    @Test
    fun testPart1Trivial() {
        val model = buildModelFromInput("/test-input-day-11-trivial.txt")
        assertThat(solvePart1(model = model, blinks = 1L)).isEqualTo(7L)
    }

    @Test
    fun testPart1Trivial2() {
        val model = buildModelFromInput("/test-input-day-11-trivial2.txt")
        assertThat(solvePart1(model = model, blinks = 6L)).isEqualTo(22L)
        assertThat(solvePart1(model = model, blinks = 25L)).isEqualTo(55312L)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-11.txt")
        println(solvePart1(model = model, blinks = 25L))
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-11.txt")
        println(solvePart1(model = model, blinks = 75L))
    }

    private fun solvePart1(
        model: Map<Long, Long>,
        blinks: Long,
    ): Long {
        var newMap = mutableMapOf<Long, Long>()
        var originalMap = model.toMap()
        (1..blinks).forEach { _ ->
            newMap = originalMap.toMutableMap()
            newMap.keys.toList().forEach {
                transformAndUpdate(it, originalMap, newMap)
            }
            originalMap = newMap
        }
        return newMap.map { it.value }.sum()
    }

    private fun transformAndUpdate(
        key: Long,
        originalMap: Map<Long, Long>,
        newMap: MutableMap<Long, Long>,
    ) {
        val value = originalMap[key]!!
        if (key == 0L) {
            incrementCountBy(1, value, newMap)
        } else if (key.toString().length % 2 == 0) {
            val pair = splitInMiddle(key.toString())
            incrementCountBy(pair.first.toLong(), value, newMap)
            incrementCountBy(pair.second.toLong(), value, newMap)
        } else {
            incrementCountBy(key * 2024, value, newMap)
        }
        decrementCountBy(key, value, newMap)
    }

    private fun incrementCountBy(
        key: Long,
        quantity: Long,
        model: MutableMap<Long, Long>,
    ) {
        model[key] = model.getOrDefault(key, 0) + quantity
    }

    private fun decrementCountBy(
        key: Long,
        quantity: Long,
        model: MutableMap<Long, Long>,
    ) {
        model[key] = model[key]!! - quantity
        model.remove(key, 0) // Optionally remove key if value becomes 0
    }

    private fun splitInMiddle(input: String): Pair<String, String> {
        val middle = input.length / 2
        return Pair(input.substring(0, middle), input.substring(middle))
    }

    // model builders
    private fun buildModelFromInput(fileName: String): Map<Long, Long> {
        val lines = loadStringLines(fileName)
        return lines
            .first()
            .split(" ")
            .map { it.toLong() }
            .groupingBy { it }
            .eachCount()
            .mapValues { (_, value) -> value.toLong() }
    }
}
