package com.salvador.bricks.game_objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.Actor
import com.salvador.bricks.game_objects.Constants.SCREEN_WIDTH
import com.salvador.bricks.game_objects.ResourceManager.getTexture

class Score(var positionY: Float) : Actor() {

    @JvmField
    var score = 0

    @JvmField
    var lives = 3

    @JvmField
    var level = 1
    var font: BitmapFont
    var ball = getTexture("ball.png")
    var textWidth = 0F
    var layout = GlyphLayout()

    init {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("font.ttf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 15
        font = generator.generateFont(parameter)
        generator.dispose()
    }

    override fun act(delta: Float) {
        super.act(delta)
        layout.setText(font, score.toString())
        textWidth = layout.width
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        font.draw(batch, score.toString(), SCREEN_WIDTH / 2F - (textWidth / 2), positionY)
        batch.draw(ball, 360F, 755F, 25F, 25F)
        font.draw(batch, "x$lives", 390F, 770F)
        font.draw(batch, "Level $level", 20F, 770F)
    }
}
