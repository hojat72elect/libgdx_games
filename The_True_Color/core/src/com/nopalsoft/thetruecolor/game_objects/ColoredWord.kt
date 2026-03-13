package com.nopalsoft.thetruecolor.game_objects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.Settings
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings.Languages
import com.nopalsoft.thetruecolor.screens.BaseScreen

class ColoredWord {
    // The color of the word
    var color: Int = 0

    // What the word compare says with the table above
    var wordText: Int = 0
    var wordLabel: Label = Label("", LabelStyle(Assets.fontLarge, Color.WHITE))

    fun initialize() {
        color = MathUtils.random(0, 7)

        // 35% chance that what the word says and its color are the same
        wordText = if (MathUtils.randomBoolean(.35f)) {
            color
        } else {
            MathUtils.random(0, 7)
        }
        val textColor = when (wordText) {
            COLOR_BLUE -> "blue"
            COLOR_CYAN -> "cyan"
            COLOR_GREEN -> "green"
            COLOR_YELLOW -> "yellow"
            4 -> "pink"
            5 -> "brown"
            6 -> "purple"
            7 -> "red"
            else -> "red"
        }

        wordLabel.remove()
        wordLabel.setText(Assets.languagesBundle!!.get(textColor))
        wordLabel.setColor(this.colorActualPalabra)
        if (Settings.selectedLanguage == Languages.RUSSIAN) wordLabel.setFontScale(.68f)
        else wordLabel.setFontScale(1f)
        wordLabel.pack()
        wordLabel.setPosition(BaseScreen.SCREEN_WIDTH / 2f - wordLabel.getWidth() / 2f, 450f)
    }

    val colorActualPalabra: Color
        /**
         * It is the color of the word
         */
        get() = getColor(color)

    companion object {
        const val COLOR_BLUE: Int = 0
        const val COLOR_CYAN: Int = 1
        const val COLOR_GREEN: Int = 2
        const val COLOR_YELLOW: Int = 3

        fun getColor(colorId: Int): Color {
            val color: Color
            when (colorId) {
                0 -> color = Color.BLUE
                1 -> color = Color.CYAN
                2 -> color = Color.GREEN
                3 -> color = Color.YELLOW
                4 -> color = Color.PINK
                5 -> color = Color(.6f, .3f, 0f, 1f) // Brown
                6 -> color = Color.PURPLE
                7 -> color = Color.RED
                else -> color = Color.RED
            }
            return color
        }

        val randomColor: Color
            get() = getColor(MathUtils.random(7))
    }
}
