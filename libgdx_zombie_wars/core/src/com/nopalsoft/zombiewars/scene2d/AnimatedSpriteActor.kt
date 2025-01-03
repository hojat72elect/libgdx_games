package com.nopalsoft.zombiewars.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.zombiewars.AnimationSprite

class AnimatedSpriteActor(private val animation: AnimationSprite?) : Actor() {

    // used to track the current time within the animation sequence.
    private var stateTime = 0F

    override fun act(delta: Float) {
        stateTime += delta
        super.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val spriteFrame = animation?.getKeyFrame(stateTime, true)
        spriteFrame?.setPosition(x, y)
        spriteFrame?.setSize(width, height)
        spriteFrame?.draw(batch)
    }
}