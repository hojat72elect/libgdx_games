package ca.hojat.sokoban.objects

import ca.hojat.sokoban.Settings
import ca.hojat.sokoban.game.GameBoard
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions

/**
 * Every tile that we draw on the screen is a child of this class.
 */
open class Tile(var position: Int) : Actor() {

    // ALL MAPS ARE 25x15 Tiles of 32px which gives a resolution of 800x480
    val SIZE = 32F * GameBoard.UNIT_SCALE // Token size

    init {
        setSize(SIZE, SIZE)
        setPosition(mapPosiciones[position]!!.x, mapPosiciones[position]!!.y)
    }

    /**
     * If it is UNDO it moves without animation (quickFix).
     */
    fun moveToPosition(pos: Int, undo: Boolean) {
        var time = .05f
        if (Settings.animationWalkIsON && !undo) time = .45f
        this.position = pos
        addAction(Actions.sequence(Actions.moveTo(mapPosiciones[position]!!.x, mapPosiciones[position]!!.y, time), Actions.run { this.onMovementToPositionCompleted() }))
    }

    /**
     * It is called automatically when it has already moved to the position.
     */
    protected open fun onMovementToPositionCompleted() {
    }

    companion object {
        private val mapPosiciones = LinkedHashMap<Int, Vector2>()

        init {
            // The positions start from left to right from bottom to top.
            var posicionTile = 0
            for (y in 0..14) {
                for (x in 0..24) {
                    mapPosiciones[posicionTile] = Vector2(x * 32 * GameBoard.UNIT_SCALE, y * 32 * GameBoard.UNIT_SCALE)
                    posicionTile++
                }
            }
        }
    }
}
