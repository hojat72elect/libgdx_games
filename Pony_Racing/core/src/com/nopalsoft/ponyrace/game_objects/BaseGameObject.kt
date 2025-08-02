package com.nopalsoft.ponyrace.game_objects

import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector3

open class BaseGameObject {
    @JvmField
    val position: Vector3
    val bounds: Polygon

    constructor(x: Float, y: Float, z: Float, vertices: FloatArray) {
        this.position = Vector3(x, y, z)
        this.bounds = Polygon(vertices)
    }

    constructor(x: Float, y: Float) {
        this.position = Vector3(x, y, 0f)
        this.bounds = Polygon(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f))
    }
}
