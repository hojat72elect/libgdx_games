package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.gamestudio24.martianrun.utils.AssetsManager.getSmallFont
import com.gamestudio24.martianrun.utils.Constants

class AboutLabel(private val bounds: Rectangle) : Actor() {
    private val font: BitmapFont

    init {
        setWidth(bounds.width)
        setHeight(bounds.height)
        font = getSmallFont()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        font.drawWrapped(batch, Constants.ABOUT_TEXT, bounds.x, bounds.y, bounds.width, BitmapFont.HAlignment.CENTER)
    }
}
