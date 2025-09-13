package com.salvador.bricks.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class MenuText(@JvmField var text: String, fontName: String, var textY: Float) : Actor() {

    var font: BitmapFont = ResourceManager.getFont(fontName)
    var textWidth: Float
    var layout = GlyphLayout()

    init {
        layout.setText(font, text)
        textWidth = layout.width
    }

    override fun act(delta: Float) {
        super.act(delta)
        layout.setText(font, text)
        textWidth = layout.width
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        font.draw(batch, text, 450F / 2 - textWidth / 2, textY)
    }
}
