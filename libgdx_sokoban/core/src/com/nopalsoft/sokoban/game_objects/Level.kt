package com.nopalsoft.sokoban.game_objects

/**
 * Represents a level in our Sokoban game.
 *
 * This class stores information about a level, including the number of stars earned,
 * the best time achieved, and the best number of moves made.
 */
data class Level(
    var numStars: Int = 0,
    var bestTime: Int = 0,
    var bestMoves: Int = 0
)
