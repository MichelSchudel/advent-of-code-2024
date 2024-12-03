package adventofcode2024

import adventofcode2023.loadStringLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3 {
    @Test
    fun testPart1() {
        val program = buildModelFromTestInput()
        assertThat(solvePart1(program)).isEqualTo(161)
    }

    @Test
    fun solvePart1() {
        val program = buildModelFromRealInput()
        println(solvePart1(program))
    }

    @Test
    fun testPart2() {
        val program = buildModelFromTestInputPart2()
        assertThat(solvePart2(program)).isEqualTo(48)
    }

    @Test
    fun solvePart2() {
        val program = buildModelFromRealInput()
        println(solvePart2(program))
    }

    private fun solvePart2(program: String): Int {
        val fixedProgram =
            buildString {
                append("do()")
                append(program)
                if (program.lastIndexOf("do()") > program.lastIndexOf("don't()")) {
                    append("don't()")
                }
            }
        val regexBlock = Regex(pattern = """do\(\)(.*?)don't\(\)""", RegexOption.DOT_MATCHES_ALL)
        val sanitizedProgram = regexBlock.findAll(fixedProgram).map { it.value }.joinToString()
        return solvePart1(sanitizedProgram)
    }

    private fun solvePart1(program: String): Int {
        val regex = Regex(pattern = "mul\\((\\d+),(\\d+)\\)")
        return regex
            .findAll(program)
            .sumOf { matchResult ->
                val (e1, e2) = matchResult.destructured
                e1.toInt() * e2.toInt()
            }
    }

    // model builders
    private fun buildModelFromTestInput(): String = loadStringLines("/test-input-day-3.txt").joinToString()

    private fun buildModelFromRealInput(): String = loadStringLines("/real-input-day-3.txt").joinToString()

    private fun buildModelFromTestInputPart2(): String = loadStringLines("/test-input-day-3-part-2.txt").joinToString()
}
