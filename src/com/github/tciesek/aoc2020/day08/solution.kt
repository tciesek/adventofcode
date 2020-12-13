package com.github.tciesek.aoc2020.day08

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

val testInput = "input_test.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

fun main() {
    println("Part 1:")
    assertEquals(5, solution(testInput))
    println(solution(input))

    println("Part 2:")
    assertEquals(8, solution2(testInput))
    println(solution2(input))
}

fun solution(input: File): Int {
    val bootCode = input.readLines()
        .map { it.toInstruction() }

    val console = HandheldGameConsole(bootCode)

    runCatching { console.run() }
    return console.accumulator
}

fun solution2(input: File): Int {
    val bootCode = input.readLines()
        .map { it.toInstruction() }

    for (i in 0..bootCode.size) {
        val modifiedBootCode = when (val instruction = bootCode[i]) {
            is NoOperation -> bootCode.toMutableList().also { it[i] = Jump(instruction.argument) }
            is Jump -> bootCode.toMutableList().also { it[i] = NoOperation(instruction.argument) }
            is Accumulate -> continue
        }

        val console = HandheldGameConsole(modifiedBootCode)
        runCatching { console.run() }
            .onSuccess { return console.accumulator }
    }

    throw IllegalStateException("Result not found")
}


class HandheldGameConsole(private val bootCode: List<Instruction>) {
    var accumulator = 0
    private var currentLine = 0
    private val stackTrace = mutableListOf<Int>()

    private fun execute(instruction: Instruction) {
        when (instruction) {
            is Accumulate -> {
                accumulator += instruction.argument
                currentLine += 1
            }
            is Jump -> currentLine += instruction.argument
            is NoOperation -> currentLine += 1
        }
    }

    fun run() {
        while (currentLine < bootCode.size) {
            if (currentLine in stackTrace) {
                throw IllegalStateException("Endless loop detected!")
            }
            val currentInstruction = bootCode[currentLine]
            stackTrace += currentLine
            execute(currentInstruction)
        }
    }
}

sealed class Instruction(val name: String, open val argument: Int)
data class Accumulate(override val argument: Int) : Instruction("acc", argument)
data class Jump(override val argument: Int) : Instruction("jmp", argument)
data class NoOperation(override val argument: Int) : Instruction("nop", argument)

private fun String.toInstruction(): Instruction {
    val instructionParts = this.split(" ")

    return when (instructionParts[0]) {
        "acc" -> Accumulate(instructionParts[1].toInt())
        "jmp" -> Jump(instructionParts[1].toInt())
        "nop" -> NoOperation(instructionParts[1].toInt())
        else -> throw IllegalStateException()
    }
}

