package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day13 {
    data class Machine(
        val buttonA: Pair<Long, Long>,
        val buttonB: Pair<Long, Long>,
        val prize: Pair<Long, Long>,
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

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-13.txt")
        val changedModel =
            model.map { machine ->
                machine.copy(
                    prize =
                        Pair(
                            machine.prize.first + 10000000000000,
                            machine.prize.second + 10000000000000,
                        ),
                )
            }
        println(solve(changedModel))
    }

    private fun solve(model: List<Machine>): Long {
        val buttonCombinations = model.map { findSolution(it) }
        val pairs = buttonCombinations.map { pairList -> pairList.map { pair -> 3 * pair.first + pair.second } }
        return pairs.flatten().sum()
    }

    fun findSolution(machine: Machine): List<Pair<Long, Long>> {
        val list = mutableListOf<Pair<Long, Long>>()
        val commonDenominator = machine.buttonA.first * machine.buttonB.second - machine.buttonA.second * machine.buttonB.first
        val numberOfTimesButtonAPressed =
            (machine.prize.first * machine.buttonB.second - machine.prize.second * machine.buttonB.first) / commonDenominator
        val numberOfTimesButtonBPressed =
            (machine.buttonA.first * machine.prize.second - machine.buttonA.second * machine.prize.first) / commonDenominator
        if (machine.buttonA.first * numberOfTimesButtonAPressed + machine.buttonB.first * numberOfTimesButtonBPressed == machine.prize.first &&
            machine.buttonA.second * numberOfTimesButtonAPressed + machine.buttonB.second * numberOfTimesButtonBPressed ==
            machine.prize.second
        ) {
            list.add(Pair(numberOfTimesButtonAPressed, numberOfTimesButtonBPressed))
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
            val buttonAX = result!!.groups[1]!!.value.toLong()
            val buttonAY = result.groups[2]!!.value.toLong()
            val result2 = regex2.matchEntire(buttonBLine)
            val buttonBX = result2!!.groups[1]!!.value.toLong()
            val buttonBY = result2.groups[2]!!.value.toLong()
            val result3 = regex3.matchEntire(prizeLine)
            val prizeX = result3!!.groups[1]!!.value.toLong()
            val prizeY = result3.groups[2]!!.value.toLong()
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
