package com.nopalsoft.flappy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion

object Assets {
    var font: BitmapFont? = null
    private val glyphLayout = GlyphLayout()

    var bird: Animation<AtlasRegion>? = null

    var background: TextureRegion? = null
    var gameOver: TextureRegion? = null
    var getReady: TextureRegion? = null
    var tap: TextureRegion? = null
    var downPipe: TextureRegion? = null
    var upPipe: TextureRegion? = null

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        background = atlas.findRegion("fondo")
        gameOver = atlas.findRegion("gameOver")
        getReady = atlas.findRegion("getReady")
        tap = atlas.findRegion("tap")
        downPipe = atlas.findRegion("tuberiaDown")
        upPipe = atlas.findRegion("tuberiaUp")

        bird = Animation(
            .3f,
            atlas.findRegion("bird1"),
            atlas.findRegion("bird2"),
            atlas.findRegion("bird3")
        )

        // Use default libGDX font
        font = BitmapFont()
        font!!.data.scale(7f)
    }

    /**
     * Get the text width in order to center in the screen
     */
    fun getTextWidth(text: String?): Float {
        glyphLayout.setText(font, text)
        return glyphLayout.width
    }
}
