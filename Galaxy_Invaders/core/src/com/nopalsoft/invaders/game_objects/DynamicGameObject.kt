package com.nopalsoft.invaders.game_objects

import com.badlogic.gdx.math.Vector2

open class DynamicGameObject : GameObject {
    @JvmField
    val velocity: Vector2

    constructor(x: Float, y: Float, width: Float, height: Float) : super(x, y, width, height) {
        velocity = Vector2()
    }

    constructor(x: Float, y: Float, radio: Float) : super(x, y, radio) {
        velocity = Vector2()
    }
}
