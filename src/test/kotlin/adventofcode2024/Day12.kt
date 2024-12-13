package adventofcode2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day12 {
    data class Coordinate(
        val x: Int,
        val y: Int,
        var plantType: Char,
    )

    @Test
    fun testPart1Trivial() {
        val model = buildModelFromInput("/test-input-day-12-trivial.txt")
        assertThat(solve(model, discountApplied = false)).isEqualTo(140)
    }

    @Test
    fun testPart1Trivial2() {
        val model = buildModelFromInput("/test-input-day-12-trivial2.txt")
        assertThat(solve(model, discountApplied = false)).isEqualTo(772)
    }

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-12.txt")
        assertThat(solve(model, discountApplied = false)).isEqualTo(1930)
    }

    @Test
    fun solve() {
        val model = buildModelFromInput("/real-input-day-12.txt")
        println(solve(model, discountApplied = false))
    }

    @Test
    fun testPart2() {
        val model = buildModelFromInput("/test-input-day-12-trivial.txt")
        assertThat(solve(model, discountApplied = true)).isEqualTo(80)
    }

    @Test
    fun solvePart2() {
        val model = buildModelFromInput("/real-input-day-12.txt")
        println(solve(model, discountApplied = true))
    }

    private fun solve(
        model: List<Coordinate>,
        discountApplied: Boolean,
    ): Int {
        val visited = mutableListOf<Coordinate>()
        val groups =
            model.map { coordinate ->
                getGroup(coordinate, null, model, visited, mutableListOf())
            }
        val actualGroups = groups.filter { it.size > 0 }
        return actualGroups.map { calculatePrice(it, model, discountApplied) }.sum()
    }

    private fun calculatePrice(
        group: List<Coordinate>,
        model: List<Coordinate>,
        discountApplied: Boolean,
    ): Int {
        val area = group.size
        return if (discountApplied) {
            val corners = getCorners(group)
            area * corners
        } else {
            val perimeter = group.sumOf { countSides(it, model) }
            area * perimeter
        }
    }

    private fun getCorners(group: List<Coordinate>): Int =
        group.sumOf {
            getCorners(it, group)
        }

    private fun getCorners(
        coordinate: Coordinate,
        group: List<Coordinate>,
    ): Int {
        var corners = 0
        // inner corners
        // top right
        if (group.any { it.x == coordinate.x + 1 && it.y == coordinate.y } &&
            group.any { it.x == coordinate.x && it.y == coordinate.y - 1 } &&
            group.none { it.x == coordinate.x + 1 && it.y == coordinate.y - 1 }
        ) {
            corners++
        }
        // bottom right
        if (group.any { it.x == coordinate.x + 1 && it.y == coordinate.y } &&
            group.any { it.x == coordinate.x && it.y == coordinate.y + 1 } &&
            group.none { it.x == coordinate.x + 1 && it.y == coordinate.y + 1 }
        ) {
            corners++
        }
        // bottom left
        if (group.any { it.x == coordinate.x - 1 && it.y == coordinate.y } &&
            group.any { it.x == coordinate.x && it.y == coordinate.y + 1 } &&
            group.none { it.x == coordinate.x - 1 && it.y == coordinate.y + 1 }
        ) {
            corners++
        }
        // top left
        if (group.any { it.x == coordinate.x - 1 && it.y == coordinate.y } &&
            group.any { it.x == coordinate.x && it.y == coordinate.y - 1 } &&
            group.none { it.x == coordinate.x - 1 && it.y == coordinate.y - 1 }
        ) {
            corners++
        }

        // outer corners
        //  top and right
        if (group.none { it.x == coordinate.x + 1 && it.y == coordinate.y } &&
            group.none { it.x == coordinate.x && it.y == coordinate.y - 1 }
        ) {
            corners++
        }

        // top and left
        if (group.none { it.x == coordinate.x - 1 && it.y == coordinate.y } &&
            group.none { it.x == coordinate.x && it.y == coordinate.y - 1 }
        ) {
            corners++
        }

        // bottom and left
        if (group.none { it.x == coordinate.x - 1 && it.y == coordinate.y } &&
            group.none { it.x == coordinate.x && it.y == coordinate.y + 1 }
        ) {
            corners++
        }

        // bottom and right
        if (group.none { it.x == coordinate.x + 1 && it.y == coordinate.y } &&
            group.none { it.x == coordinate.x && it.y == coordinate.y + 1 }
        ) {
            corners++
        }

        return corners
    }

    private fun countSides(
        coordinate: Coordinate,
        model: List<Coordinate>,
    ): Int {
        var perimeter = 0
        val left = model.firstOrNull { it.x == coordinate.x - 1 && it.y == coordinate.y }
        if (left == null || left.plantType != coordinate.plantType) perimeter++
        val right = model.firstOrNull { it.x == coordinate.x + 1 && it.y == coordinate.y }
        if (right == null || right.plantType != coordinate.plantType) perimeter++
        val top = model.firstOrNull { it.x == coordinate.x && it.y == coordinate.y - 1 }
        if (top == null || top.plantType != coordinate.plantType) perimeter++
        val bottom = model.firstOrNull { it.x == coordinate.x && it.y == coordinate.y + 1 }
        if (bottom == null || bottom.plantType != coordinate.plantType) perimeter++
        return perimeter
    }

    private fun getGroup(
        coordinate: Coordinate,
        currentPlantType: Char? = null,
        model: List<Coordinate>,
        visited: MutableList<Coordinate>,
        group: MutableList<Coordinate>,
    ): List<Coordinate> {
        if (visited.contains(coordinate)) {
            return group
        }
        if (currentPlantType == null || coordinate.plantType == currentPlantType) {
            visited.add(coordinate)
            group.add(coordinate)
            model
                .firstOrNull { it.x == coordinate.x - 1 && it.y == coordinate.y }
                ?.let { getGroup(it, coordinate.plantType, model, visited, group) }
            model
                .firstOrNull { it.x == coordinate.x + 1 && it.y == coordinate.y }
                ?.let { getGroup(it, coordinate.plantType, model, visited, group) }
            model
                .firstOrNull { it.x == coordinate.x && it.y == coordinate.y - 1 }
                ?.let { getGroup(it, coordinate.plantType, model, visited, group) }
            model
                .firstOrNull { it.x == coordinate.x && it.y == coordinate.y + 1 }
                ?.let { getGroup(it, coordinate.plantType, model, visited, group) }
        }
        return group
    }

    // model builders
    private fun buildModelFromInput(fileName: String): List<Coordinate> {
        val lines = loadStringLines(fileName)
        return lines
            .mapIndexed { y, line ->
                line.mapIndexed { x, c ->
                    Coordinate(x, y, c)
                }
            }.flatten()
    }
}
