package com.nopalsoft.thetruecolor.objects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.Settings
import com.nopalsoft.thetruecolor.scene2d.DialogHelpSettings.Languages
import com.nopalsoft.thetruecolor.screens.BaseScreen

class Word {

    @JvmField
    val image = Label("", Label.LabelStyle(Assets.fontExtraLarge, Color.WHITE))

    // The color of the word.
    @JvmField
    var color = 0

    // What the word compare says with the table above.
    @JvmField
    var text = 0


    fun initialize() {
        color = MathUtils.random(0, 7)


        // 35% chance that what the word says and its color are the same.
        text = if (MathUtils.randomBoolean(.35f)) {
            color
        } else {
            MathUtils.random(0, 7)
        }

        val textColor = when (text) {
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

        image.remove()
        image.setText(Assets.languages.get(textColor))
        image.color = getCurrentWordColor()
        image.setFontScale(if (Settings.selectedLanguage == Languages.RUSSIAN) 0.68f else 1f)
        image.pack()
        image.setPosition(BaseScreen.SCREEN_WIDTH / 2f - image.width / 2f, 450f)
    }

    /**
     * It is the color of the word.
     */
    fun getCurrentWordColor() = getColor(color)

    private fun getColor(wordColor: Int): Color {
        val color = when (wordColor) {
            0 -> Color.BLUE
            1 -> Color.CYAN
            2 -> Color.GREEN
            3 -> Color.YELLOW
            4 -> Color.PINK
            5 -> Color(.6f, .3f, 0f, 1f) // Cafe
            6 -> Color.PURPLE
            7 -> Color.RED
            else -> Color.RED
        }
        return color
    }

    companion object {

        // all the different colors that the word can have
        const val COLOR_BLUE = 0
        const val COLOR_CYAN = 1
        const val COLOR_GREEN = 2
        const val COLOR_YELLOW = 3

        private fun getColor(wordColor: Int): Color = when (wordColor) {
            0 -> Color.BLUE
            1 -> Color.CYAN
            2 -> Color.GREEN
            3 -> Color.YELLOW
            4 -> Color.PINK
            5 -> Color(.6f, .3f, 0f, 1f) // Cafe
            6 -> Color.PURPLE
            7 -> Color.RED
            else -> Color.RED
        }

        @JvmStatic
        fun getRandomColor() = getColor(MathUtils.random(7))

    }
}