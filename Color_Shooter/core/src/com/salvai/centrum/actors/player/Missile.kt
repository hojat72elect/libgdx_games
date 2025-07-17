package com.salvai.centrum.actors.player

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Vector2
import com.salvai.centrum.actors.MovingGameObject
import com.salvai.centrum.utils.Constants
import com.salvai.centrum.utils.GameTheme

class Missile(touch: Vector2, texture: Texture) : MovingGameObject() {
    @JvmField
    var shape: Circle

    init {
        sprite = Sprite(texture)
        position.x = Constants.WIDTH_CENTER - (Constants.MISSILE_RADIUS * 0.5f)
        position.y = Constants.HEIGHT_CENTER - (Constants.MISSILE_RADIUS * 0.5f)
        velocity = touch.sub(position).nor()
        shape = Circle(position.x, position.y, Constants.MISSILE_RADIUS)
        sprite.setBounds(position.x, position.y, Constants.MISSILE_RADIUS, Constants.MISSILE_RADIUS)
        sprite.setColor(GameTheme[0])
    }


    fun move(delta: Float) {
        position.set(shape.x + (velocity.x * delta * Constants.MISSILE_SPEED), shape.y + (velocity.y * delta * Constants.MISSILE_SPEED))
        shape.setPosition(shape.x + (velocity.x * delta * Constants.MISSILE_SPEED), shape.y + (velocity.y * delta * Constants.MISSILE_SPEED))
        sprite.setPosition(sprite.x + (velocity.x * delta * Constants.MISSILE_SPEED), sprite.y + (velocity.y * delta * Constants.MISSILE_SPEED))
    }
}