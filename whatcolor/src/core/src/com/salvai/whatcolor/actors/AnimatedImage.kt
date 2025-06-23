package com.salvai.whatcolor.actors

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable


open class AnimatedImage(val animation: Animation<TextureRegion>) : Image(animation.getKeyFrame(0f)) {
    private var stateTime = 0f

    override fun act(delta: Float) {
        (drawable as TextureRegionDrawable).region = animation.getKeyFrame(delta.let { stateTime += it; stateTime }, true)
        super.act(delta)
    }

}