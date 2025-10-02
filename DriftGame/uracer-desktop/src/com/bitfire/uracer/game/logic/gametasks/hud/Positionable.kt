package com.bitfire.uracer.game.logic.gametasks.hud

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable

/**
 * Represents a positionable hud element
 */
abstract class Positionable : Disposable {
    @JvmField
    protected var position = Vector2()

    @JvmField
    var scale = 1F
    var x = position.x
    var y = position.y

    override fun dispose() {}

    fun setPosition(x: Float, y: Float) {
        this.position.set(x, y)
    }

    fun setPosition(position: Vector2) {
        this.position.set(position)
    }

    abstract val width: Float
    abstract val height: Float
}
