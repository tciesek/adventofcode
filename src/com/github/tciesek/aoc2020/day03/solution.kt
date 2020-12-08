package com.github.tciesek.aoc2020.day03

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

val testInput = "input_test.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

fun main() {
    println("Part 1:")
    assertEquals(7, traverse(3 to 1, testInput))
    println(traverse(3 to 1, "input.txt".toRelativeFile()))

    println("Part 2:")
    val slopes = listOf(
        1 to 1,
        3 to 1,
        5 to 1,
        7 to 1,
        1 to 2
    )

    slopes.map { traverse(it, testInput) }
        .also { assertEquals(it, listOf(2, 7, 3, 4, 2)) }
        .fold(1) { acc, it -> acc * it }
        .also { assertEquals(it, 336) }

    val result = slopes.map { traverse(it, input) }
        .fold(1) { acc, it -> acc * it }

    println(result)
}

const val TREE = '#'

fun traverse(slopeStep: Pair<Int, Int>, input: File): Int {
    var position = 0 to 0
    var treesEncountered = 0
    var lineIndex = 0

    input.forEachLine { line ->
        if(lineIndex % slopeStep.second > 0) {
            lineIndex += 1
            return@forEachLine
        }

        if(line[(position.first) % line.length] == TREE) {
            treesEncountered += 1
        }
        position += slopeStep
        lineIndex += 1
    }
    return treesEncountered
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = this.first + other.first to this.second + other.second
