package com.nopalsoft.zombiekiller.game_objects

import com.badlogic.gdx.math.Vector2

class Platform(x: Float, y: Float, val width: Float, val height: Float) {
    val position: Vector2 = Vector2(x, y)
}
