package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day15 {
    data class WarehouseItem(
        val x: Int,
        val y: Int,
        val item: Char,
    )

    @Test
    fun testPart1Trivial() {
        val model = buildModelFromInput("/test-input-day-15-trivial.txt")
        assertThat(solve(model)).isEqualTo(2028)
    }

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-15.txt")
        assertThat(solve(model)).isEqualTo(10092)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-15.txt")
        println(solve(model))
    }

    private fun solve(model: Pair<List<WarehouseItem>, List<Pair<Int, Int>>>): Int {
        // comments
        val warehouse = model.first.toMutableList()
        val instructions = model.second
        // renderSpace(warehouse)
        instructions.forEach { instruction ->
            // println("instruction: $instruction")
            val robot = warehouse.find { it.item == '@' }!!
            val targetXCoordinate = robot.x + instruction.first
            val targetYCoordinate = robot.y + instruction.second
            val adjacent = warehouse.find { it.x == targetXCoordinate && it.y == targetYCoordinate }
            if (adjacent == null) {
                // just move robot
                warehouse.remove(robot)
                warehouse.add(WarehouseItem(x = targetXCoordinate, y = targetYCoordinate, item = '@'))
            } else if (adjacent.item == '#') {
                // obstruction, do nothing
            } else if (adjacent.item == 'O') {
                // there's a box, see if we can move it and all behind, else do nothing
                val movableBlock = getMovableBlock(adjacent, instruction, warehouse)
                if (!movableBlock.isEmpty()) {
                    // move crates
                    movableBlock.forEach {
                        warehouse.remove(it)
                        warehouse.add(WarehouseItem(x = it.x + instruction.first, y = it.y + instruction.second, item = 'O'))
                    }
                    // move robot
                    warehouse.remove(robot)
                    warehouse.add(WarehouseItem(x = targetXCoordinate, y = targetYCoordinate, item = '@'))
                }
            }
            // renderSpace(warehouse)
        }
        return warehouse.filter { it.item == 'O' }.map { it.y * 100 + it.x }.sum()
    }

    private fun getMovableBlock(
        adjacent: WarehouseItem,
        instruction: Pair<Int, Int>,
        warehouse: List<WarehouseItem>,
    ): List<WarehouseItem> {
        var currentItem: WarehouseItem? = adjacent
        val blockList = mutableListOf<WarehouseItem>()
        do {
            blockList.add(currentItem!!)
            currentItem = warehouse.find { it.x == currentItem!!.x + instruction.first && it.y == currentItem!!.y + instruction.second }
        } while (currentItem != null)
        if (blockList.any { it.item == '#' }) return emptyList()
        return blockList
    }

    fun renderSpace(space: List<WarehouseItem>) {
        val maxX = space.map { it.x }.maxBy { it } - 1
        val maxY = space.map { it.y }.maxBy { it } - 1
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                val c = space.find { it.x == x && it.y == y }
                if (c == null) print(".") else print(c.item)
            }
            println()
        }
        println()
    }

    // model builders
    private fun buildModelFromInput(fileName: String): Pair<List<WarehouseItem>, List<Pair<Int, Int>>> {
        val lines = loadStringLines(fileName)
        val cut = lines.split { it.isBlank() }
        val warehouseLines = cut[0]
        val instructionLine = cut[1].joinToString("")
        val warehouseItems =
            warehouseLines
                .mapIndexed { y, line ->
                    line.mapIndexed { x, c ->
                        WarehouseItem(x, y, c)
                    }
                }.flatten()
                .filter { it.item != '.' }
        val instructions =
            instructionLine.map { c ->
                when (c) {
                    '^' -> Pair(0, -1)
                    'v' -> Pair(0, 1)
                    '<' -> Pair(-1, 0)
                    '>' -> Pair(1, 0)
                    else -> throw Exception("Unknown instruction line $c")
                }
            }
        return Pair(warehouseItems, instructions)
    }
}
