package com.github.tciesek.aoc2020.day09

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

val testInput = "input_test.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

fun main() {
    println("Part 1:")
    assertEquals(127, findInvalidNumber(5, testInput))

    val invalidNumber = findInvalidNumber(25, input)
    println(invalidNumber)

    println("Part 2:")
    assertEquals(62, findEncryptionWeakness(127, testInput))
    println(findEncryptionWeakness(invalidNumber, input))

}

fun findInvalidNumber(preambleSize: Int, input: File): Long {
    return input.readLines()
        .map { it.toLong() }
        .windowed(preambleSize + 1, 1)
        .first {
            val preamble = it.subList(0, it.size - 1)
            val product = preamble.flatMap { outer -> preamble.map { inner -> outer + inner } }
            it.last() !in product
        }
        .last()
}

fun findEncryptionWeakness(invalidNumber: Long, input: File): Long {
     input.useLines { lines ->
         val iterator = lines.iterator()
         val contiguousNumbers = mutableListOf<Long>()
         while (contiguousNumbers.sum() != invalidNumber) {
             contiguousNumbers.add(iterator.next().toLong())
             while(contiguousNumbers.sum() > invalidNumber) {
                 contiguousNumbers.removeFirst()
             }
         }
         return contiguousNumbers.minOrNull()!! + contiguousNumbers.maxOrNull()!!
     }

}




