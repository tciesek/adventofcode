package com.github.tciesek.aoc2020.day10

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

val testInput = "input_test.txt".toRelativeFile()
val testInput2 = "input_test2.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

fun main() {
    println("Part 1:")
    assertEquals(35, solution(getSortedAdapterValues(testInput)))
    assertEquals(220, solution(getSortedAdapterValues(testInput2)))
    println(solution(getSortedAdapterValues(input)))

    println("Part 2:")
    assertEquals(8, connectingPossibilities(getSortedAdapterValues(testInput)))
    assertEquals(19208, connectingPossibilities(getSortedAdapterValues(testInput2)))
    println(connectingPossibilities(getSortedAdapterValues(input)))
}

fun getSortedAdapterValues(input: File) = input.readLines()
    .map { it.toLong() }
    .sorted()

fun solution(adapters: List<Long>): Int {
    val joltageDifferences = mutableMapOf(3L to 1)
    var currentJoltage = 0L

    adapters.forEach { adapterJoltage ->
        val diff = adapterJoltage - currentJoltage
        if (diff !in 1..3) {
            joltageDifferences[1]!! * joltageDifferences[3]!!
        }
        currentJoltage = adapterJoltage
        joltageDifferences.compute(diff) { _, value -> (value ?: 0) + 1 }
    }

    return joltageDifferences[1]!! * joltageDifferences[3]!!
}


fun connectingPossibilities(adapters: List<Long>): Long {
    return 1 + connectingCombinations(0, adapters, mutableMapOf())
}

fun connectingCombinations(currentJoltage: Long, adapters: List<Long>, memory: MutableMap<Long, Long>): Long {
    if (adapters.isEmpty()) {
        return 0
    }

    val possibilities = adapters.mapIndexed { index, adapterJoltage -> index to adapterJoltage }
        .filter { (_, adapterJoltage) -> adapterJoltage - currentJoltage in 1..3 }

    return possibilities.size - 1 + possibilities.fold(0L, { acc, (index, adapterJoltage) ->
        val combinations = memory[adapterJoltage]
            ?: connectingCombinations(adapterJoltage, adapters.subList(index + 1, adapters.size), memory)

        memory[adapterJoltage] = combinations
        acc + combinations
    })
}
