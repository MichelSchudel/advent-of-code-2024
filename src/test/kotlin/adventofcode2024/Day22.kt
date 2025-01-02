package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day22 {
    @Test
    fun testPart1Trivial() {
        println(createKeyList(initialKey = 123L, times = 10))
    }

    @Test
    fun testPart1() {
        val secrets = buildModel(loadStringLines("/test-input-day-22.txt"))
        assertThat(solvePart1(secrets = secrets)).isEqualTo(37327623)
    }

    @Test
    fun solvePart1() {
        val secrets = buildModel(loadStringLines("/real-input-day-22.txt"))
        println(solvePart1(secrets = secrets))
    }

    @Test
    fun testPart2Trivial() {
        println(solvePart2(listOf(123L)))
    }

    @Test
    fun testPart2() {
        assertThat(solvePart2(listOf(1L,2L,3L, 2024L))).isEqualTo(23)
    }

    @Test
    fun solvePart2() {
        val secrets = buildModel(loadStringLines("/real-input-day-22.txt"))
        println(solvePart2(secrets = secrets))
    }

    private fun solvePart1(secrets: List<Long>): Long = secrets.sumOf { createKeyList(it, 2000).last() }

    private fun solvePart2(secrets: List<Long>): Long {
        //comments
        val keys = secrets.map { listOf(it) + createKeyList(it, 2000) }
        val ones = keys.map { getOnes(it)}
        val changes = ones.map { getChanges(it)}
        val windowedChanged = changes.map { mapChange(it)}
        val map = mutableMapOf<List<Long>, Long>()
        windowedChanged.forEach { mergeToMap(map, it)  }
        val entry = map.maxBy { it.value }
        return entry.value
    }

    private fun mergeToMap(map: MutableMap<List<Long>, Long>, it: List<Pair<List<Long>, Long>>) {
        it.forEach { mergeToMap(map, it) }
    }

    private fun mergeToMap(map: MutableMap<List<Long>, Long>, it: Pair<List<Long>, Long>) {
        val key = it.first
        val value = it.second
        if (map.containsKey(key)) {
            map[key] = map[key]!!.plus(value)
        } else {
            map[key] = value
        }
    }


    private fun mapChange(change: List<Pair<Long, Long>>): List<Pair<List<Long>, Long>> {
        return change.windowed(4).map { (a,b,c,d) -> Pair(listOf(a.second, b.second,c.second, d.second),d.first) }
            .distinctBy { it.first }
    }

    private fun getOnes(keys: List<Long>): List<Long> {
       return keys.map { it.toString().last().toString().toLong() }
    }

    private fun getChanges(ones: List<Long>): List<Pair<Long, Long>> {
        return ones.windowed(2).map { (a,b) -> Pair(b,b - a) }
    }

    private fun createKeyList(
        initialKey: Long,
        times: Int,
    ): List<Long> {
        val l = mutableListOf<Long>()
        var key = initialKey
        repeat(times) {
            key = calcNewKey(key)
            l.add(key)
        }
        return l
    }

    private fun calcNewKey(secretNumber: Long): Long {
        var workingKey = secretNumber
        workingKey = (workingKey * 64) xor workingKey
        workingKey = workingKey.mod(16777216L)

        workingKey = workingKey / 32 xor workingKey

        workingKey = workingKey.mod(16777216L)

        workingKey = (workingKey * 2048) xor workingKey
        workingKey = workingKey.mod(16777216L)

        return workingKey
    }

    private fun buildModel(lines: List<String>): List<Long> = lines.map { it.toLong() }
}
