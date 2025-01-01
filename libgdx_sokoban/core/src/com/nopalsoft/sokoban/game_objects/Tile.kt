package com.nopalsoft.sokoban.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.game.Board

/**
 * All objects are useful (the floor, the character, the boxes)
 */
open class Tile(var position: Int) : Actor() {
    // ALL MAPS ARE 25x15 32px tiles which gives a resolution of 800x480
    val size = 32F * Board.UNIT_SCALE // Size of the card

    init {
        setSize(size, size)
        setPosition(mapPositions[position]!!.x, mapPositions[position]!!.y)
    }

    /**
     * If it is UNDO it moves without animation (quickFix).
     */
    fun moveToPosition(pos: Int, undo: Boolean) {
        var time = .05f
        if (Settings.animationWalkIsON && !undo) time = .45f
        this.position = pos
        addAction(
            Actions.sequence(
                Actions.moveTo(
                    mapPositions[position]!!.x, mapPositions[position]!!.y, time
                ), Actions.run { this.endMovingToPosition() })
        )
    }

    /**
     * It is called automatically when it has already moved to the position.
     * This function is going to be used by the children of Tile class.
     */
    protected open fun endMovingToPosition() {
    }

    companion object {
        val mapPositions: LinkedHashMap<Int, Vector2> = LinkedHashMap()

        init {
            // Positions start from left to right from bottom to top.
            var tilePosition = 0
            for (y in 0..14) {
                for (x in 0..24) {
                    mapPositions[tilePosition] = Vector2(x * 32 * Board.UNIT_SCALE, y * 32 * Board.UNIT_SCALE)
                    tilePosition++
                }
            }
        }
    }
}
