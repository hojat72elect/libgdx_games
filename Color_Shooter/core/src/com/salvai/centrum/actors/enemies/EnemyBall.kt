package com.salvai.centrum.actors.enemies

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Circle
import com.salvai.centrum.utils.Constants

class EnemyBall : Enemy {
    var shape: Circle

    constructor(texture: Texture) : super(texture) {
        shape = Circle(position.x + (Constants.ENEMY_DIAMETER * 0.5F), position.y + (Constants.ENEMY_DIAMETER * 0.5F), Constants.ENEMY_DIAMETER * 0.5F)
    }

    /**
     * for level balls
     */
    constructor(x: Int, y: Int, speed: Int, color: Color, texture: Texture) : super(x, y, speed, color, texture) {
        shape = Circle(position.x + (Constants.ENEMY_DIAMETER * 0.5F), position.y + (Constants.ENEMY_DIAMETER * 0.5F), Constants.ENEMY_DIAMETER * 0.5F)
    }

    override fun move(delta: Float) {
        shape.setPosition(shape.x + (velocity.x * delta * speed), shape.y + (velocity.y * delta * speed))
        sprite.setPosition(sprite.x + (velocity.x * delta * speed), sprite.y + (velocity.y * delta * speed))
        position.set(shape.x + (velocity.x * delta * speed), shape.y + (velocity.y * delta * speed))
    }
}
