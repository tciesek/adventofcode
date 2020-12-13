package com.github.tciesek.aoc2020.day06

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.streams.toList
import kotlin.test.assertEquals

val testInput = "input_test.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

fun main() {
    println("Part 1:")
    assertEquals(11, solution(testInput))
    println(solution(input))

    println("Part 2:")
    assertEquals(6, solution2(testInput))
    println(solution2(input))
}

fun solution(input: File): Int {
    val groupAnswers = input.readText().split("\n\n")

    return groupAnswers
        .map { answers -> answers.filter { !it.isWhitespace() }.map { it }.distinct() }
        .map { it.size }
        .sum()
}

fun solution2(input: File): Int {
    val groupsAnswers = input.readText().split("\n\n")

    return groupsAnswers
        .map { groupAnswers -> groupAnswers.split("\n").filter { it.isNotBlank() }
            .map { personAnswers -> personAnswers.chars().toList().toSet() }
        }
        .map { it.fold(it[0]) { acc, current -> acc.intersect(current) } }
        .map { it.size }
        .sum()
}
