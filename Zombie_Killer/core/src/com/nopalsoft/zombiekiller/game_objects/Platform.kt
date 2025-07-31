package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.math.Vector2

class Platform(x: Float, y: Float, val width: Float, @JvmField val height: Float) {
    @JvmField
    val position: Vector2 = Vector2(x, y)
}
