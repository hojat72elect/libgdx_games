package com.salvai.centrum.actors.enemies

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.salvai.centrum.actors.MovingGameObject
import com.salvai.centrum.utils.Constants
import com.salvai.centrum.utils.RandomUtil

abstract class Enemy : MovingGameObject {
    var speed: Int

    constructor(texture: Texture) : super() {
        speed = RandomUtil.getRandomEnemySpeed(Constants.ENEMY_MAX_SPEED)
        position = RandomUtil.getRandomEnemyCoordinates()
        setDirectionToCentreBall()
        position.x -= Constants.ENEMY_DIAMETER * 0.5f
        position.y -= Constants.ENEMY_DIAMETER * 0.5f
        sprite = Sprite(texture)
        sprite.setBounds(position.x, position.y, Constants.ENEMY_DIAMETER.toFloat(), Constants.ENEMY_DIAMETER.toFloat())
        sprite.setColor(color)
    }

    //for level balls
    constructor(x: Int, y: Int, speed: Int, color: Color, texture: Texture) : super(color) {
        this.speed = speed
        position.set(x.toFloat(), y.toFloat())
        setDirectionToCentreBall()
        position.x -= Constants.ENEMY_DIAMETER * 0.5f
        position.y -= Constants.ENEMY_DIAMETER * 0.5f
        sprite = Sprite(texture)
        sprite.setBounds(position.x, position.y, Constants.ENEMY_DIAMETER.toFloat(), Constants.ENEMY_DIAMETER.toFloat())
        sprite.setColor(color)
    }

    abstract fun move(delta: Float)
}
