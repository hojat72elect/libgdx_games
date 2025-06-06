package com.nopalsoft.ninjarunner.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.ninjarunner.AnimationSprite

class AnimatedSpriteActor(var anim: AnimationSprite) : Actor() {
    var stateTime: Float = 0f

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val sprite = anim.getKeyFrame(stateTime, true)
        sprite.setSize(getWidth(), getHeight())
        sprite.setPosition(getX(), getY())
        sprite.draw(batch)
    }
}
