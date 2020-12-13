package com.github.tciesek.aoc2020.day05

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals


val testInput = "input_test.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

fun main() {
    println("Part 1:")
    assertEquals(70, BoardingPass("BFFFBBFRRR").row)
    assertEquals(7, BoardingPass("BFFFBBFRRR").column)
    assertEquals(567, BoardingPass("BFFFBBFRRR").seatId)

    assertEquals(14, BoardingPass("FFFBBBFRRR").row)
    assertEquals(7, BoardingPass("FFFBBBFRRR").column)
    assertEquals(119, BoardingPass("FFFBBBFRRR").seatId)

    assertEquals(102, BoardingPass("BBFFBBFRLL").row)
    assertEquals(4, BoardingPass("BBFFBBFRLL").column)
    assertEquals(820, BoardingPass("BBFFBBFRLL").seatId)


    assertEquals(820, solution(testInput))
    println(solution(input))

    println("Part 2:")
    println(solution2(input))
}

data class BoardingPass(val boardingPass: String) {
    val row = boardingPass.substring(0..6)
        .map {
            when (it) {
                'F' -> '0'
                'B' -> '1'
                else -> throw IllegalArgumentException()
            }
        }.joinToString(separator = "")
        .toInt(2)

    val column = boardingPass.substring(7..9)
        .map {
            when (it) {
                'L' -> '0'
                'R' -> '1'
                else -> throw IllegalArgumentException()
            }
        }
        .joinToString(separator = "")
        .toInt(2)

    val seatId = row * 8 + column
}

fun solution(input: File): Int {
    return input.useLines { lines ->
        lines.map { BoardingPass(it) }
            .map { it.seatId }
            .max()!!
    }
}

fun solution2(input: File): Int {
    return input.useLines { lines ->
        val takenSeats = lines.map { BoardingPass(it) }
            .map { it.seatId }
            .toSet()

        val allSeats = 0..(127 * 8 + 7)

        return allSeats.single { it !in takenSeats && it - 1 in takenSeats && it + 1 in takenSeats }
    }
}
