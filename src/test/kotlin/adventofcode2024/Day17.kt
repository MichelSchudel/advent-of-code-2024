package adventofcode2024

import org.junit.jupiter.api.Test

class Day17 {
    private val inputProgram = listOf(2L, 4L, 1L, 5L, 7L, 5L, 1L, 6L, 0L, 3L, 4L, 3L, 5L, 5L, 3L, 0L)

    @Test
    fun testPart1() {
        val computer = Computer()
        println(computer.runHardcodedProgram(34615120))
    }

    @Test
    fun solvePart2() {
        val lookup = mutableMapOf<Long, MutableSet<Long>>()
        for (i in 0..1023L) {
            val computer = Computer()
            val simResult = computer.runHardcodedProgram(i)[0]
            lookup.computeIfAbsent(simResult.toLong()) { mutableSetOf() }.add(i)
        }
        val result = backwardSolver(lookup, inputProgram.size, 0)
        println(result)
    }

    private fun backwardSolver(
        lookup: Map<Long, Set<Long>>,
        position: Int,
        remainder: Long,
    ): Long {
        val pos = position - 1
        var rem = remainder
        if (pos < 0) return rem

        rem = rem shl 3
        lookup[inputProgram[pos]]?.forEach { bits ->
            if ((bits and 1016) == (rem and 1016)) {
                val solution = backwardSolver(lookup, pos, rem or bits)
                if (solution > -1) return solution
            }
        }
        return -1
    }

    class Computer {
        private var aRegister = 0L
        private var bRegister = 0L
        private var cRegister = 0L
        private val output = mutableListOf<Int>()

        fun runHardcodedProgram(a: Long): List<Int> {
            this.aRegister = a
            do {
                bRegister = aRegister and 7
                bRegister = bRegister xor 5
                cRegister = aRegister shr bRegister.toInt()
                bRegister = bRegister xor 6
                aRegister = aRegister shr 3
                bRegister = bRegister xor cRegister
                output.add(bRegister.toInt() and 7)
            } while (aRegister != 0L)
            return output
        }
    }
}
