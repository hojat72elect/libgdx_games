package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.nopalsoft.zombiekiller.Assets

class NumGemsBar(icon: AtlasRegion?, x: Float, y: Float) : Table() {
    var actualNum: Int = 0

    var lbNum: Label

    init {
        this.setBounds(x, y, WIDTH, HEIGHT)
        setBackground(Assets.backgroundProgressBar)
        setIcon(icon)
        lbNum = Label("0", Assets.labelStyleChico)
        lbNum.setPosition(50f, 3.5f)
        lbNum.setAlignment(Align.center)
        addActor(lbNum)
    }

    private fun setIcon(icon: AtlasRegion?) {
        val imgIcon = Image(icon)
        // Both height because i want it to be a square
        imgIcon.scaleBy(-.3f)
        imgIcon.setPosition(-15f, getHeight() / 2f - (imgIcon.getPrefHeight() * imgIcon.getScaleY() / 2f))
        addActor(imgIcon)
    }

    fun updateNumGems(gems: Int) {
        this.actualNum = gems
        lbNum.setText(actualNum.toString() + "")
    }

    companion object {
        const val WIDTH = 100f
        const val HEIGHT = 30f
    }
}
