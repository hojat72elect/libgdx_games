package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.zombiekiller.Assets

class ProgressBarUI : Table {
    var maxNum: Float
    var actualNum: Float

    var bar: AtlasRegion?

    constructor(bar: AtlasRegion?, icon: AtlasRegion?, maxNum: Float, actualNum: Float, x: Float, y: Float) {
        this.setBounds(x, y, WIDTH, HEIGHT)
        this.maxNum = maxNum
        this.actualNum = actualNum
        setBackground(Assets.backgroundProgressBar)
        setIcon(icon)
        this.bar = bar
    }

    constructor(bar: AtlasRegion?, icon: AtlasRegion?, maxNum: Float, x: Float, y: Float) {
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

    companion object {
        const val WIDTH = 180f
        const val HEIGHT = 30f
        const val BAR_WIDTH = 140f
        const val BAR_HEIGHT = 20f
    }
}
