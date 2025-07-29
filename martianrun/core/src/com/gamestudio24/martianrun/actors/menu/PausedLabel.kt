package com.gamestudio24.martianrun.actors.menu

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.AssetsManager.getSmallFont
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager

class PausedLabel(private val bounds: Rectangle) : Actor() {
    private val font: BitmapFont

    init {
        setWidth(bounds.width)
        setHeight(bounds.height)
        font = getSmallFont()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (GameManager.instance.gameState == GameState.PAUSED) {
            font.drawWrapped(
                batch, Constants.PAUSED_LABEL, bounds.x, bounds.y, bounds.width,
                BitmapFont.HAlignment.CENTER
            )
        }
    }
}
