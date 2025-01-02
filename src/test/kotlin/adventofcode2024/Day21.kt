package adventofcode2024

import org.junit.jupiter.api.Test

class Day21 {
    data class Button(
        val number: Char,
        val x: Int,
        val y: Int,
    )

    val numericKeypad =
        listOf(
            Button('7', 0, 0),
            Button('8', 1, 0),
            Button('9', 2, 0),
            Button('4', 0, 1),
            Button('5', 1, 1),
            Button('6', 2, 1),
            Button('1', 0, 2),
            Button('2', 1, 2),
            Button('3', 2, 2),
            Button('0', 1, 3),
            Button('A', 2, 3),
        )

    val directionalKeyboardMap =
        mapOf(
            Pair('<', 'v') to ">A",
            Pair('<', '>') to ">>A",
            Pair('<', '^') to ">^A",
            Pair('<', 'A') to ">>^A",
            Pair('A', '^') to "<A",
            Pair('A', '>') to "vA",
            Pair('A', 'v') to "<vA",
            Pair('A', '<') to "v<<A",
            Pair('v', '^') to "^A",
            Pair('v', '<') to "<A",
            Pair('v', '>') to ">A",
            Pair('v', 'A') to ">^A",
            Pair('^', 'A') to ">A",
            Pair('^', 'v') to "vA",
            Pair('^', '<') to "v<A",
            Pair('^', '>') to "v>A",
            Pair('>', 'A') to "^A",
            Pair('>', 'v') to "<A",
            Pair('>', '^') to "<^A",
            Pair('>', '<') to "<<A",
        )

    val codeList =
        listOf(
            "029A",
            "980A",
            "179A",
            "456A",
            "379A",
        )

    val realCodeList =
        listOf(
            "341A",
            "803A",
            "149A",
            "683A",
            "208A",
        )

    fun determineRouteNumericKeypad(
        source: Char,
        dest: Char,
    ): String {
        var s = ""
        val sourceButton = numericKeypad.find { it.number == source }!!
        val destButton = numericKeypad.find { it.number == dest }!!
        // up and right movements are always safe
        // up
        // right
        if (destButton.x > sourceButton.x) {
            s = s + ">".repeat(destButton.x - sourceButton.x)
        }
        if (destButton.y < sourceButton.y) {
            s = s + "^".repeat(sourceButton.y - destButton.y)
        }
        // left
        if (destButton.x < sourceButton.x) {
            s = s + "<".repeat(sourceButton.x - destButton.x)
        }
        // down
        if (destButton.y > sourceButton.y) {
            s = s + "v".repeat(destButton.y - sourceButton.y)
        }
        s = s + "A"
        return s
    }

    fun determineRouteDirectionalKeypad(
        source: Char,
        dest: Char,
    ): String {
        if (source == dest) {
            return "A"
        } else {
            return directionalKeyboardMap[Pair(source, dest)]!!
        }
    }

    @Test
    fun testPart1() {
        println(
            codeList
                .map {
                    println(decode(it))
                    decode(it)
                }.map {
                    println("length: ${it.second.length}, num: ${getNum(it.first)}")
                    it.second.length * getNum(it.first)
                }.sum(),
        )
    }

    @Test
    fun solvePart1() {
        println(
            realCodeList
                .map {
                    println(decode(it))
                    decode(it)
                }.map {
                    println("length: ${it.second.length}, num: ${getNum(it.first)}")
                    it.second.length * getNum(it.first)
                }.sum(),
        )
    }

    private fun getNum(s: String): Int {
        val r = Regex("^(\\d+).*$")
        val result = r.matchEntire(s)
        return result!!.groups[1]!!.value.toInt()
    }

    private fun decode(code: String): Pair<String, String> {
        val dir1 =
            (
                "A" +
                    code
            ).windowed(2)
                .map { Pair(it[0], it[1]) }
                .map { determineRouteNumericKeypad(it.first, it.second) }
                .joinToString("")
        println(dir1)
        val dir2 =
            (
                "A" +
                    dir1
            ).windowed(2)
                .map { Pair(it[0], it[1]) }
                .map { determineRouteDirectionalKeypad(it.first, it.second) }
                .joinToString("")

        println(dir2)
        val dir3 =
            (
                "A" +
                    dir2
            ).windowed(2)
                .map { Pair(it[0], it[1]) }
                .map { determineRouteDirectionalKeypad(it.first, it.second) }
                .joinToString("")
        println(dir3)
        return Pair(code, dir3)
    }
}
