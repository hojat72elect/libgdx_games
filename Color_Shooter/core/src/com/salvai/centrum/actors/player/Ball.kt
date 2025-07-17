package com.salvai.centrum.actors.player

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Circle
import com.salvai.centrum.utils.Constants
import com.salvai.centrum.utils.GameTheme

class Ball(var x: Float, var y: Float, texture: Texture) {
    @JvmField
    var shape = Circle(x, y, Constants.PLANET_DIAMETER * 0.5f)

    @JvmField
    var sprite = Sprite(texture)

    //set color white
    var color = Color(GameTheme[0])
        set(value) {
            field = value
            sprite.setColor(value)
        }

    init {
        x -= Constants.PLANET_DIAMETER * 0.5f
        y -= Constants.PLANET_DIAMETER * 0.5f
        sprite.setBounds(x, y, Constants.PLANET_DIAMETER, Constants.PLANET_DIAMETER)
        sprite.setColor(color)
    }

}
