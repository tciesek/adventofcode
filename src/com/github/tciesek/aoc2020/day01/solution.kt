package com.github.tciesek.aoc2020.day01

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

fun main() {
    println("Part 1:")
    assertEquals(514579, solution("input_test.txt".toRelativeFile()))
    println(solution("input.txt".toRelativeFile()))

    println("Part 2:")
    assertEquals(241861950, solution2("input_test.txt".toRelativeFile()))
    println(solution2("input.txt".toRelativeFile()))
}

fun solution(input: File): Int {
    val lines = input.readLines().map { it.toInt() }.toSet()

    lines.forEach { x ->
        lines.forEach { y ->
            if(x + y == 2020) {
                return x * y
            }
        }
    }
    throw IllegalStateException("Solution not found!")
}

fun solution2(input: File): Int {
    val lines = input.readLines().map { it.toInt() }

    lines.forEach { x ->
        lines.forEach { y ->
            lines.forEach { z ->
                if (x + y + z == 2020) {
                    return x * y * z
                }
            }
        }
    }
    throw IllegalStateException("Solution not found!")
}

