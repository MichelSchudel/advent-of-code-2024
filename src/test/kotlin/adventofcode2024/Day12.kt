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
        assertThat(solvePart1(model)).isEqualTo(140)
    }

    @Test
    fun testPart1Trivial2() {
        val model = buildModelFromInput("/test-input-day-12-trivial2.txt")
        assertThat(solvePart1(model)).isEqualTo(772)
    }

    @Test
    fun testPart1() {
        val model = buildModelFromInput("/test-input-day-12.txt")
        assertThat(solvePart1(model)).isEqualTo(1930)
    }

    @Test
    fun solvePart1() {
        val model = buildModelFromInput("/real-input-day-12.txt")
        println(solvePart1(model))
    }

    private fun solvePart1(model: List<Coordinate>): Int {
        val visited = mutableListOf<Coordinate>()
        val groups =
            model.map { coordinate ->
                println("Next coordinate")
                getGroup(coordinate, null, model, visited, mutableListOf())
            }
        val actualGroups = groups.filter { it.size > 0 }
        return actualGroups.map { calculatePrice(it, model) }.sum()
    }

    private fun calculatePrice(
        group: List<Coordinate>,
        model: List<Coordinate>,
    ): Int {
        val area = group.size
        val perimeter = group.map { countSides(it, model) }.sum()
        return area * perimeter
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
        println("visting coordinate $coordinate, current plantType $currentPlantType")
        if (visited.contains(coordinate)) {
            println("coordinate $coordinate already visited or added to group")
            return group
        }
        if (currentPlantType == null || coordinate.plantType == currentPlantType) {
            println("adding $coordinate to group")
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
