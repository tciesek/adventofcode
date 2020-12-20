package com.github.tciesek.aoc2020.day11

import com.github.tciesek.aoc2020.day11.Tile.EmptySeat
import com.github.tciesek.aoc2020.day11.Tile.Floor
import com.github.tciesek.aoc2020.day11.Tile.OccupiedSeat
import com.github.tciesek.util.toRelativeFile
import java.io.File
import kotlin.test.assertEquals

val testInput = "input_test.txt".toRelativeFile()
val input = "input.txt".toRelativeFile()

fun main() {
    println("Part 1:")
    assertEquals(37, solution(testInput, adjacentTilesRule))
    println(solution(input, adjacentTilesRule))

    println("Part 2:")
    assertEquals(26, solution(testInput, adjacentSeatsRule))
    println(solution(input, adjacentSeatsRule))
    println("Finished")

}

fun solution(input: File, replacementRule: (Pair<Int, Int>, TileMap) -> Tile): Int {
    val initMap = TileMap.fromFile(input)

    var newMap: TileMap? = null
    var previousMap: TileMap
    do {
        previousMap = newMap ?: initMap
        newMap = applyRound(previousMap, replacementRule)
    } while (previousMap != newMap)

    return newMap.tiles.filter { it.value == OccupiedSeat }.count()
}

fun applyRound(map: TileMap, replacementRule: (Pair<Int, Int>, TileMap) -> Tile): TileMap {
    val newMap = map.tiles.keys.map { position ->
        position to replacementRule(position, map)
    }.toMap()

    return TileMap(map.width, map.height, newMap)
}

private val adjacentTilesRule = fun(position: Pair<Int, Int>, map: TileMap) = when (map[position]!!) {
    EmptySeat -> if (map.getAdjacentTiles(position).all { it.value in listOf(EmptySeat, Floor) }) OccupiedSeat else EmptySeat
    OccupiedSeat -> if (map.getAdjacentTiles(position).filter { it.value == OccupiedSeat }.size >= 4) EmptySeat else OccupiedSeat
    Floor -> Floor
}

private val adjacentSeatsRule = fun(position: Pair<Int, Int>, map: TileMap) = when (map[position]!!) {
    EmptySeat -> if (map.getAdjacentSeats(position).all { it.value in listOf(EmptySeat, Floor) }) OccupiedSeat else EmptySeat
    OccupiedSeat -> if (map.getAdjacentSeats(position).filter { it.value == OccupiedSeat }.size >= 5) EmptySeat else OccupiedSeat
    Floor -> Floor
}

data class TileMap(val width: Int, val height: Int, val tiles: Map<Pair<Int, Int>, Tile>) {
    companion object {
        fun fromFile(input: File): TileMap {
            val lines = input.readLines()
            val width = lines.first().length
            val height = lines.size

            val map = lines.flatMapIndexed { y, line ->
                line.mapIndexed { x, char -> (x to y) to Tile.fromRepresentation(char) }
            }.toMap()

            return TileMap(width, height, map)
        }
    }


    fun print() {
        val builder = StringBuilder()

        for (y in 0 until height) {
            for (x in 0 until width) {
                builder.append(tiles[x to y]!!.representation)
            }
            builder.appendLine()
        }

        println(builder.toString())
    }

    operator fun get(position: Pair<Int, Int>) = tiles[position]

    fun getAdjacentTiles(position: Pair<Int, Int>): Map<Pair<Int, Int>, Tile> {
        val (x, y) = position

        return listOf(
            x - 1 to y - 1, x to y - 1, x + 1 to y - 1,
            x - 1 to y, x + 1 to y,
            x - 1 to y + 1, x to y + 1, x + 1 to y + 1
        ).mapNotNull { tiles[it]?.let { tile -> it to tile } }.toMap()
    }


    fun getAdjacentSeats(position: Pair<Int, Int>): Map<Pair<Int, Int>, Tile> {
        val (x, y) = position

        return listOfNotNull(
            firstSeatInLine(position, -1 to -1), firstSeatInLine(position, 0 to -1), firstSeatInLine(position, 1 to -1),
            firstSeatInLine(position, -1 to 0), firstSeatInLine(position, 1 to 0),
            firstSeatInLine(position, -1 to 1), firstSeatInLine(position, 0 to 1), firstSeatInLine(position, 1 to 1)
        ).mapNotNull { tiles[it]?.let { tile -> it to tile } }.toMap()
    }

    private fun firstSeatInLine(start: Pair<Int, Int>, vector: Pair<Int, Int>): Pair<Int, Int>? =
        firstInLine(start, vector) { it == EmptySeat || it == OccupiedSeat }

    private fun firstInLine(start: Pair<Int, Int>, vector: Pair<Int, Int>, predicate: (Tile) -> Boolean): Pair<Int, Int>? =
        generateSequence(start) { it.first + vector.first to it.second + vector.second }
            .takeWhile { it in tiles }
            .drop(1)
            .firstOrNull { predicate(tiles[it]!!) }
}


enum class Tile(val representation: Char) {
    Floor('.'),
    EmptySeat('L'),
    OccupiedSeat('#');

    companion object {
        private val representationToTile: Map<Char, Tile> = values().map { it.representation to it }.toMap()

        fun fromRepresentation(representation: Char): Tile = representationToTile[representation]
            ?: throw IllegalArgumentException("Tile type not defined: $representation")
    }
}
