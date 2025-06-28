package ca.hojat.sokoban.scene2d

import ca.hojat.sokoban.Assets
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class CounterTable(backgroundDrawable: TextureRegionDrawable, x: Float, y: Float) : Table() {


    private val displayLabel = Label("", LabelStyle(Assets.fontRed, Color.WHITE))

    init {
        this.setBounds(x, y, WIDTH, HEIGHT)
        background = backgroundDrawable

        displayLabel.setFontScale(.8f)
        add(displayLabel)

        center()
        padLeft(25f)
        padBottom(5f)
    }

    fun updateDisplayedNumber(newNumber: Int) {
        displayLabel.setText(newNumber)
    }

    companion object {
        private const val WIDTH = 125F
        private const val HEIGHT = 42F
    }
}