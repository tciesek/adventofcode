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
    val numbers = input.readLines().map { it.toInt() }.toSet()

    numbers.forEach { x ->
        val y = 2020 - x
        if (y in numbers) {
            return x * y
        }
    }
    throw IllegalStateException("Solution not found!")
}

fun solution2(input: File): Int {
    val numbers = input.readLines().map { it.toInt() }.toSet()

    numbers.forEach { x ->
        val diff = 2020 - x
        numbers.forEach { y ->
            val z = diff - y
            if (z in numbers) {
                return x * y * z
            }
        }
    }
    throw IllegalStateException("Solution not found!")
}

