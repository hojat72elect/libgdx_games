package com.salvai.centrum.actors.effects

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.math.Vector2
import com.salvai.centrum.utils.Constants

class Explosion {
    var particleEffect: ParticleEffect
    var position: Vector2
    var sound: Sound

    /**
     * for center ball
     */
    constructor(color: Color, sound: Sound, soundOn: Boolean, particleEffect: ParticleEffect) {
        this.sound = sound
        this.particleEffect = ParticleEffect(particleEffect)
        position = Vector2(Constants.WIDTH_CENTER.toFloat(), Constants.HEIGHT_CENTER.toFloat())
        this.particleEffect.emitters.first().setPosition(position.x, position.y)
        val colorF = floatArrayOf(color.r, color.g, color.b)
        this.particleEffect.emitters.first().tint.colors = colorF
        this.particleEffect.scaleEffect(2f)
        this.particleEffect.start()
        this.particleEffect.emitters.shuffle()
        if (soundOn) this.sound.play()
    }

    /**
     * for enemies
     */
    constructor(position: Vector2, color: Color, sound: Sound, soundOn: Boolean, particleEffect: ParticleEffect) {
        this.sound = sound
        this.position = position
        this.particleEffect = ParticleEffect(particleEffect)
        this.particleEffect.emitters.first().setPosition(position.x, position.y)
        val colorF = floatArrayOf(color.r, color.g, color.b)
        this.particleEffect.emitters.first().tint.colors = colorF
        this.particleEffect.emitters.shuffle()
        this.particleEffect.start()
        if (soundOn) this.sound.play()
    }
}