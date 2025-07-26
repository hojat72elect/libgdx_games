package com.salvai.whatcolor.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.salvai.whatcolor.enums.PatternType
import com.salvai.whatcolor.global.PATTERN_SCREEN_SIZE

class SinglePatternPart(val texture: Texture, val colorz: Color, val patternType: PatternType) : Image(texture) {
    var secret: Boolean = false

    init {
        setSize(PATTERN_SCREEN_SIZE / patternType.factor, PATTERN_SCREEN_SIZE / patternType.factor)

        setOrigin(width * 0.5f, height * 0.5f)
        if (!secret)
            color = Color(colorz)
    }

    fun copy(): SinglePatternPart {
        return SinglePatternPart(texture, Color(colorz), patternType) //for chooser, always visible
    }
}
