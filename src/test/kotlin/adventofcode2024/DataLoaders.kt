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
