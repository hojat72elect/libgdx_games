package com.gamestudio24.martianrun.utils

import com.gamestudio24.martianrun.enums.Difficulty
import com.gamestudio24.martianrun.enums.GameState

/**
 * A utility singleton that holds the current [Difficulty]
 * and [GameState] of the game.
 */
class GameManager private constructor() {
    @JvmField
    var gameState = GameState.OVER
    var difficulty: Difficulty? = null

    val isMaxDifficulty = difficulty == Difficulty.entries.toTypedArray()[Difficulty.entries.size - 1]

    fun resetDifficulty() {
        this.difficulty = Difficulty.entries.toTypedArray()[0]
    }

    companion object {
        const val PREFERENCES_NAME: String = "preferences"

        @JvmStatic
        val instance: GameManager = GameManager()
    }
}
