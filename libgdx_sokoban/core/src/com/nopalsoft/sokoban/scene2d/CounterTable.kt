package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.sokoban.Assets

/**
 * We'll have a table in top-left of the game screen showing 2 numbers; One of them shows
 * the number of seconds passed, and the other one shows number of steps that player has
 * taken so far. Each one of the rows in that table is a [CounterTable].
 */
class CounterTable(background: TextureRegionDrawable, x: Float, y: Float) : Table() {

    private val labelDisplay = Label("", LabelStyle(Assets.fontRed, Color.WHITE))

    init {
        setBounds(x, y, WIDTH, HEIGHT)
        setBackground(background)

        labelDisplay.setFontScale(.8F)
        add(labelDisplay)

        center()
        padLeft(25F)
        padBottom(5F)
    }

    fun updateActualNum(actualNum: Int) {
        labelDisplay.setText(actualNum)
    }


    companion object {
       private const val WIDTH = 125F
       private const val HEIGHT = 42F
    }
}