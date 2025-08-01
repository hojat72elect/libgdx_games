package com.nopalsoft.zombiedash.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.zombiedash.Assets

class ProgressBarUI : Table {
    var maxNum: Float
    var actualNum: Float
    var WIDTH: Float = 180f
    var HEIGHT: Float = 30f
    var BAR_WIDTH: Float = 140f
    var BAR_HEIGHT: Float = 20f
    var bar: AtlasRegion?

    constructor(bar: AtlasRegion?, icon: AtlasRegion?, maxNum: Float, actualNum: Float, x: Float, y: Float) {
        // this.debug();
        this.setBounds(x, y, WIDTH, HEIGHT)
        this.maxNum = maxNum
        this.actualNum = actualNum

        setBackground(Assets.backgroundProgressBar)
        setIcon(icon)
        this.bar = bar
    }

    constructor(bar: AtlasRegion?, icon: AtlasRegion?, maxNum: Float, x: Float, y: Float) {
        // this.debug();
        this.setBounds(x, y, WIDTH, HEIGHT)
        this.maxNum = maxNum
        this.actualNum = maxNum
        setBackground(Assets.backgroundProgressBar)
        setIcon(icon)
        this.bar = bar
    }

    private fun setIcon(icon: AtlasRegion?) {
        val imgIcon = Image(icon)
        // Both height because i want it to be a square
        imgIcon.scaleBy(-.3f)
        imgIcon.setPosition(-15f, getHeight() / 2f - (imgIcon.getPrefHeight() * imgIcon.getScaleY() / 2f))
        addActor(imgIcon)
    }

    fun updateActualNum(actualNum: Float) {
        this.actualNum = actualNum

        if (actualNum > maxNum) maxNum = actualNum
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (actualNum > 0) batch.draw(bar, this.getX() + 34, this.getY() + 6, BAR_WIDTH * (actualNum / maxNum), BAR_HEIGHT)
    }
}
