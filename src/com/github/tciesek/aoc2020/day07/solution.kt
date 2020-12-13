package com.github.tciesek.aoc2020.day07

import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

val testInput = "input_test.txt".toRelativeFile()
val testInput2 = "input_test2.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

fun main() {
    assertEquals(
        "light red" to LuggagePolicy(mapOf("bright white" to 1, "muted yellow" to 2)),
        "light red bags contain 1 bright white bag, 2 muted yellow bags.".toLuggagePolicy()
    )
    assertEquals(
        "dark orange" to LuggagePolicy(mapOf("bright white" to 3, "muted yellow" to 4)),
        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.".toLuggagePolicy()
    )
    assertEquals(
        "bright white" to LuggagePolicy(mapOf("shiny gold" to 1)),
        "bright white bags contain 1 shiny gold bag.".toLuggagePolicy()
    )

    assertEquals(4, solution(testInput))


    println("Part 1:")
    println(solution(input))

    println("Part 2:")
    assertEquals(126, solution2(testInput2))
    println(solution2(input))
}


data class LuggagePolicy(val allowedContent: Map<String, Int>) {
    fun allows(content: String, allPolicies: Map<String, LuggagePolicy>): Boolean {
        if(allowedContent[content] ?: 0 >= 1) {
            return true
        }

        return allowedContent.keys.map { allPolicies[it] }
            .any { policy -> policy?.allows(content, allPolicies) == true }
    }

    fun bagsCount(allPolicies: Map<String, LuggagePolicy>): Int {
        val outerBags = allowedContent.values.sum()
        val internalBags = allPolicies
            .filter { it.key in allowedContent.keys }
            .map { (allowedContent[it.key] ?: 0) * (allPolicies[it.key]?.bagsCount(allPolicies) ?: 0) }
            .sum()

        return outerBags + internalBags
    }
}

fun String.toLuggagePolicy() : Pair<String, LuggagePolicy> {
    val typeToContent = this.split(" contain ")

    val type = typeToContent[0].substringBefore("bags").trim()
    val contentPart = typeToContent[1]

    if(contentPart == "no other bags.") {
        return type to LuggagePolicy(emptyMap())
    }

    val allowedContent = contentPart
        .split("bags?[,.]".toRegex()).filter { it.isNotBlank() }.map { it.trim() }
        .map { it.substringAfter(" ") to it.substringBefore(" ").toInt() }.toMap()

    return type to LuggagePolicy(allowedContent)
}

fun solution(input: File): Int {
    val policies = input.readLines().filter { it.isNotBlank() }
        .map { it.toLuggagePolicy() }
        .toMap()

    val content = "shiny gold"

    return policies.filterValues { it.allows(content, policies) }.count()
}

fun solution2(input: File): Int {
    val policies = input.readLines().filter { it.isNotBlank() }
        .map { it.toLuggagePolicy() }
        .toMap()

    return policies["shiny gold"]!!.bagsCount(policies)
}
