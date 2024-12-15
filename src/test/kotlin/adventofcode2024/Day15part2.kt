package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day15part2 {
    data class WarehouseItem(
        val x: Int,
        val y: Int,
        val item: Char,
    )

    @Test
    fun testPart2Trivial() {
        val model = buildModelFromInput(loadStringLines("/test-input-day-15-part-2-trivial.txt"))
        assertThat(solve(model)).isEqualTo(618)
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput(loadStringLines("/test-input-day-15-part-2.txt"))
        assertThat(solve(model)).isEqualTo(9021)
    }

    @Test
    fun solvePart2() {
        val pre = preprocess(loadStringLines("/real-input-day-15.txt"))
        val model = buildModelFromInput(pre)
        println(solve(model))
    }

    private fun preprocess(lines: List<String>): List<String> {
        val modelLines = lines.takeWhile { it != "" }
        val alteredModel =
            modelLines.map { line ->
                line
                    .map { c ->
                        when (c) {
                            'O' -> "[]"
                            '.' -> ".."
                            '@' -> "@."
                            '#' -> "##"
                            else -> throw RuntimeException()
                        }
                    }.joinToString("")
            }
        val instructions = lines.dropWhile { it != "" }.drop(1)
        return alteredModel + listOf("") + instructions
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
            } else if (adjacent.item == '[' || adjacent.item == ']') {
                // there's a box, see if we can move it and all behind, else do nothing
                val movableBlocks = getMovableBlocks(adjacent, instruction, warehouse, mutableListOf())
                if (!movableBlocks.isEmpty()) {
                    // move crates
                    movableBlocks.forEach {
                        warehouse.remove(it)
                        warehouse.add(
                            WarehouseItem(
                                x = it.x + instruction.first,
                                y = it.y + instruction.second,
                                item = it.item,
                            ),
                        )
                    }
                    // move robot
                    warehouse.remove(robot)
                    warehouse.add(WarehouseItem(x = targetXCoordinate, y = targetYCoordinate, item = '@'))
                }
            }
            // renderSpace(warehouse)
        }
        return warehouse.filter { it.item == '[' }.map { it.y * 100 + it.x }.sum()
    }

    private fun getMovableBlocks(
        adjacentItem: WarehouseItem,
        instruction: Pair<Int, Int>,
        warehouse: List<WarehouseItem>,
        movableBlockCollection: MutableList<WarehouseItem>,
    ): List<WarehouseItem> {
        var currentItem: WarehouseItem? = adjacentItem

        val otherCratePart = findOtherCratePart(currentItem!!, warehouse)
        movableBlockCollection.add(currentItem)
        movableBlockCollection.add(otherCratePart)
        // now, search all crate parts that touch
        if (instruction.second != 0) {
            // vertical search
            val firstAdjacent =
                warehouse.find {
                    it.x == currentItem!!.x + instruction.first &&
                        it.y == currentItem!!.y + instruction.second &&
                        (it.item == '[' || it.item == ']')
                }
            val secondAdjacent =
                warehouse.find {
                    it.x == otherCratePart.x + instruction.first &&
                        it.y == otherCratePart.y + instruction.second &&
                        (it.item == '[' || it.item == ']')
                }
            if (firstAdjacent != null) getMovableBlocks(firstAdjacent, instruction, warehouse, movableBlockCollection)
            if (secondAdjacent != null) getMovableBlocks(secondAdjacent, instruction, warehouse, movableBlockCollection)
        } else if (instruction.first == -1) {
            val leftBlock = listOf(currentItem, otherCratePart).minBy { it.x }
            val firstAdjacent =
                warehouse.find {
                    it.x == leftBlock!!.x + instruction.first &&
                        it.y == leftBlock!!.y + instruction.second &&
                        (it.item == ']')
                }
            if (firstAdjacent != null) getMovableBlocks(firstAdjacent, instruction, warehouse, movableBlockCollection)
        } else if (instruction.first == 1) {
            val rightBlock = listOf(currentItem, otherCratePart).maxBy { it.x }
            val firstAdjacent =
                warehouse.find {
                    it.x == rightBlock!!.x + instruction.first &&
                        it.y == rightBlock!!.y + instruction.second &&
                        (it.item == '[')
                }
            if (firstAdjacent != null) getMovableBlocks(firstAdjacent, instruction, warehouse, movableBlockCollection)
        }
        // determine if block can be moved
        val movedBlockCollection =
            movableBlockCollection.map {
                WarehouseItem(
                    item = it.item,
                    x = it.x + instruction.first,
                    y =
                        it.y + instruction.second,
                )
            }
        val canBeMoved =
            movedBlockCollection.none { block ->
                warehouse.any { warehouseItem ->
                    warehouseItem.x == block.x &&
                        warehouseItem.y == block.y &&
                        warehouseItem.item == '#'
                }
            }

        if (!canBeMoved) return emptyList()
        return movableBlockCollection.distinct()
    }

    private fun findOtherCratePart(
        warehouseItem: WarehouseItem,
        warehouse: List<WarehouseItem>,
    ): WarehouseItem {
        if (warehouseItem.item == '[') {
            return warehouse.find { it.y == warehouseItem.y && it.x == warehouseItem.x + 1 }!!
        } else {
            return warehouse.find { it.y == warehouseItem.y && it.x == warehouseItem.x - 1 }!!
        }
    }

    fun renderSpace(space: List<WarehouseItem>) {
        val maxX = space.map { it.x }.maxBy { it }
        val maxY = space.map { it.y }.maxBy { it }
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
    private fun buildModelFromInput(lines: List<String>): Pair<List<WarehouseItem>, List<Pair<Int, Int>>> {
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
