package com.github.tciesek.aoc2020.day04

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

val testInput = "input_test.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

val test2ValidInput = "input2_valid_test.txt".toRelativeFile()
val test2InvalidInput = "input2_invalid_test.txt".toRelativeFile()

fun main() {
    println("Part 1:")
    assertEquals(2, solution(testInput))
    println(solution(input))

    println("Part 2:")
    assertEquals(0, solution2(test2InvalidInput))
    assertEquals(4, solution2(test2ValidInput))
    println(solution2(input))
}

data class Passport(
    val birthYear: Int,
    val issueYear: Int,
    val expirationYear: Int,
    val height: String,
    val hairColor: String,
    val eyeColor: String,
    val passportId: String,
)

class PassportValidator {
    fun isValid(passport: Passport): Boolean {
        return with(passport) {
            listOf(
                birthYear in 1920..2002,
                issueYear in 2010..2020,
                expirationYear in 2020..2030,
                height.let {
                    when (it.substring(it.length - 2)) {
                        "cm" -> it.substringBefore("cm").toInt() in 150..193
                        "in" -> it.substringBefore("in").toInt() in 59..76
                        else -> false
                    }
                },
                hairColor.matches("#[0-9a-f]{6}".toRegex()),
                eyeColor in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth"),
                passportId.matches("[0-9]{9}".toRegex())
            ).all { it }
        }
    }
}

fun Map<String, String>.toPassport(): Passport {
    val requiredKeys = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

    if (!this.keys.containsAll(requiredKeys)) {
        throw IllegalArgumentException("Not a passport")
    }

    return Passport(
        birthYear = this["byr"]!!.toInt(),
        issueYear = this["iyr"]!!.toInt(),
        expirationYear = this["eyr"]!!.toInt(),
        height = this["hgt"]!!,
        hairColor = this["hcl"]!!,
        eyeColor = this["ecl"]!!,
        passportId = this["pid"]!!
    )
}

fun solution(input: File): Int {
    val passports = getPassports(input)
    return passports.count()
}

fun solution2(input: File): Int {
    val passportValidator = PassportValidator()

    val passports = getPassports(input)
        .filter { passportValidator.isValid(it) }

    return passports.count()
}

private fun getPassports(input: File): List<Passport> {
    val text = input.readText()
    val documents = text.split("\n\n")

    return documents
        .map { it.split("\\s+".toRegex()) }
        .map { pairs ->
            pairs.filter { it.isNotBlank() }.map { pair ->
                val keyToValue = pair.split(":")
                keyToValue[0] to keyToValue[1]
            }.toMap()
        }
        .map { runCatching { it.toPassport() } }
        .filter { it.isSuccess }
        .map { it.getOrThrow() }
}

