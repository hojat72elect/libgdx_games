package com.nopalsoft.sharkadventure.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.nopalsoft.sharkadventure.Assets

/**
 * This class defines a visual progress bar with a background image, an icon positioned to its left, and
 * a bar that fills up from left to right based on the actualNum and maxNum values.
 */
class ProgressBarUI(
    private val bar: AtlasRegion,
    icon: AtlasRegion,
    private var maxNum: Float,
    x: Float,
    y: Float
) : Table() {

    private var actualNum = maxNum


    init {
        setBounds(x, y, WIDTH, HEIGHT)
        setBackground(Assets.backgroundProgressBar)
        setIcon(icon)
    }

    private fun setIcon(icon: AtlasRegion) {
        val imageIcon = Image(icon)

        // Both height because I want it to be a square
        imageIcon.scaleBy(-0.3F)
        imageIcon.setPosition(-15F, height / 2F - (imageIcon.prefHeight * imageIcon.scaleY / 2F))
        addActor(imageIcon)
    }

    fun updateActualNum(actualNum: Float) {
        this.actualNum = actualNum

        if (actualNum > maxNum) maxNum = actualNum
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        if (actualNum > 0) {
            batch.draw(
                bar,
                this.x + 34,
                this.y + 6, BAR_WIDTH * (actualNum / maxNum), BAR_HEIGHT
            )
        }
    }


    companion object {
        const val WIDTH = 180F
        private const val HEIGHT = 30F
        private const val BAR_WIDTH = 140F
        private const val BAR_HEIGHT = 20F
    }
}