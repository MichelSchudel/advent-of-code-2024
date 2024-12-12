package adventofcode2024

import java.io.File

fun loadStringLines(fileName: String): List<String> {
    val list = mutableListOf<String>()
    val content = Day1::class.java.getResource(fileName)!!.file
    File(content).forEachLine {
        list.add(it)
    }
    return list
}
