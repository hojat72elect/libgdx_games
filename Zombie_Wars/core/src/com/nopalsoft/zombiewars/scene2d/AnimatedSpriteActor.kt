package com.nopalsoft.zombiewars.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.zombiewars.AnimationSprite

class AnimatedSpriteActor(var animation: AnimationSprite) : Actor() {
    var stateTime: Float = 0f

    override fun act(delta: Float) {
        stateTime += delta
        super.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val spriteframe = animation.getKeyFrame(stateTime, true)
        spriteframe!!.setPosition(getX(), getY())
        spriteframe.setSize(getWidth(), getHeight())
        spriteframe.draw(batch)
    }
}
