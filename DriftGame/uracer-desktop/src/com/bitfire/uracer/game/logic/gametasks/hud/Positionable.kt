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
    var scale = 0F

    init {
        this.scale = 1F
    }

    override fun dispose() {}

    fun setPosition(x: Float, y: Float) {
        this.position.set(x, y)
    }

    fun setPosition(position: Vector2) {
        this.position.set(position)
    }

    var x: Float
        get() = position.x
        set(x) {
            position.x = x
        }

    var y: Float
        get() = position.y
        set(y) {
            position.y = y
        }

    abstract val width: Float
    abstract val height: Float
}
