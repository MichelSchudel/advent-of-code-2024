package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day13 {
    data class Machine(
        val buttonA: Pair<Int, Int>,
        val buttonB: Pair<Int, Int>,
        val prize: Pair<Int, Int>,
    )

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-13.txt")
        assertThat(solve(model)).isEqualTo(480)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-13.txt")
        println(solve(model))
    }

    private fun solve(model: List<Machine>): Int {
        val buttonCombinations = model.map { findSolutions(it) }
        val pairs = buttonCombinations.map { pairList -> pairList.map { pair -> 3 * pair.first + pair.second } }
        return pairs.flatten().sum()
    }

    private fun findSolutions(machine: Machine): List<Pair<Int, Int>> {
        val list = mutableListOf<Pair<Int, Int>>()
        for (a in 0..100) {
            for (b in 0..100) {
                if (a * machine.buttonA.first + b * machine.buttonB.first == machine.prize.first &&
                    a * machine.buttonA.second + b * machine.buttonB.second == machine.prize.second
                ) {
                    list.add(Pair(a, b))
                }
            }
        }
        return list
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Machine> {
        val lines = loadStringLines(fileName)
        val regex = Regex("Button A: X\\+(\\d+), Y\\+(\\d+)")
        val regex2 = Regex("Button B: X\\+(\\d+), Y\\+(\\d+)")
        val regex3 = Regex("Prize: X=(\\d+), Y=(\\d+)")
        val machineDescriptions = lines.split { it.isBlank() }
        return machineDescriptions.map {
            val buttonALine = it[0]
            val buttonBLine = it[1]
            val prizeLine = it[2]
            val result = regex.matchEntire(buttonALine)
            val buttonAX = result!!.groups[1]!!.value.toInt()
            val buttonAY = result.groups[2]!!.value.toInt()
            val result2 = regex2.matchEntire(buttonBLine)
            val buttonBX = result2!!.groups[1]!!.value.toInt()
            val buttonBY = result2.groups[2]!!.value.toInt()
            val result3 = regex3.matchEntire(prizeLine)
            val prizeX = result3!!.groups[1]!!.value.toInt()
            val prizeY = result3.groups[2]!!.value.toInt()
            Machine(
                buttonA = Pair(buttonAX, buttonAY),
                buttonB = Pair(buttonBX, buttonBY),
                prize = Pair(prizeX, prizeY),
            )
        }
    }

    // Extension function to split a list
    fun <T> List<T>.split(delimiter: (T) -> Boolean): List<List<T>> {
        val result = mutableListOf<MutableList<T>>()
        var currentList = mutableListOf<T>()

        for (item in this) {
            if (delimiter(item)) {
                if (currentList.isNotEmpty()) {
                    result.add(currentList)
                    currentList = mutableListOf()
                }
            } else {
                currentList.add(item)
            }
        }

        if (currentList.isNotEmpty()) {
            result.add(currentList)
        }

        return result
    }
}
