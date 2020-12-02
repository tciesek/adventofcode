package com.github.tciesek.aoc2020.day02

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

fun main() {
    println("Part 1:")
    val rentalPlacePasswordPolicyProvider = { policy: String -> RentalPlacePasswordPolicy.from(policy) }
    assertEquals(2, solution(rentalPlacePasswordPolicyProvider, "input_test.txt".toRelativeFile()))
    println(solution(rentalPlacePasswordPolicyProvider, "input.txt".toRelativeFile()))

    println("Part 2:")
    val tobogganCorporatePasswordPolicyProvider = { policy: String -> TobogganCorporatePasswordPolicy.from(policy) }
    assertEquals(1, solution(tobogganCorporatePasswordPolicyProvider, "input_test.txt".toRelativeFile()))
    println(solution(tobogganCorporatePasswordPolicyProvider, "input.txt".toRelativeFile()))
}

interface PasswordPolicy {
    fun isValid(password: String): Boolean
}

class RentalPlacePasswordPolicy(
    private val min: Int,
    private val max: Int,
    private val validatedChar: Char
) : PasswordPolicy {
    companion object {
        fun from(stringPolicy: String): PasswordPolicy {
            val (range, letter) = stringPolicy.split(" ")
            val (min, max) = range.split("-").map { it.toInt() }

            return RentalPlacePasswordPolicy(min, max, letter[0])
        }
    }

    override fun isValid(password: String): Boolean {
        val count = password.filter { it == validatedChar }.count()

        return count in min..max
    }
}

class TobogganCorporatePasswordPolicy(
    private val firstPosition: Int,
    private val secondPosition: Int,
    private val validatedChar: Char
) : PasswordPolicy {
    companion object {
        fun from(stringPolicy: String): PasswordPolicy {
            val (positions, letter) = stringPolicy.split(" ")
            val (firstPosition, secondPosition) = positions.split("-")
                .map { it.toInt() - 1 } // Toboggan Corporate doesn't know about 0 index concept

            return TobogganCorporatePasswordPolicy(firstPosition, secondPosition, letter[0])
        }
    }

    override fun isValid(password: String): Boolean {
        return (password[firstPosition] == validatedChar) xor (password[secondPosition] == validatedChar)
    }
}

fun solution(policyFactory: (String) -> PasswordPolicy, input: File): Int {
    return input.useLines { entries ->
        entries.map { it.split(": ") }
            .map { policyFactory(it[0]) to it[1] }
            .filter { (policy, password) -> policy.isValid(password) }
            .count()
    }
}
