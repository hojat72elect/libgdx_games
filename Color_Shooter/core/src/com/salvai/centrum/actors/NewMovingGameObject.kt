package com.salvai.centrum.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.salvai.centrum.utils.Constants
import com.salvai.centrum.utils.RandomUtil

abstract class NewMovingGameObject {
    var velocity: Vector2
    var position: Vector2
    lateinit var sprite: Sprite
    var color: Color

    constructor() {
        velocity = Vector2()
        position = Vector2()
        color = Color(RandomUtil.getRandomColor())
    }

    //for level balls
    constructor(color: Color) {
        velocity = Vector2()
        position = Vector2()
        this.color = Color(color)
    }

    protected fun setDirectionToCentreBall() {
        velocity = Vector2(Constants.WIDTH_CENTER.toFloat(), Constants.HEIGHT_CENTER.toFloat()).sub(position).nor()
    }


    fun inScreenBounds() = position.x <= Constants.SCREEN_WIDTH && position.x >= 0 && position.y <= Constants.SCREEN_HEIGHT && position.y >= 0

    fun setColor(color: Color) {
        this.color = Color(color)
        sprite.setColor(color)
    }
}
