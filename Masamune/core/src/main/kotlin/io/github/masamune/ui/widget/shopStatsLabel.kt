package io.github.masamune.ui.widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.label

/**
 * A table with two labels:
 * - One [valueLabel] for the current stats value of a character
 * - One [diffLabel] that can show the difference to the current stats value like (+3) or (-3)
 *
 * This is used in shops to let the player know if a specific e.g. weapon is better than the currently equipped one.
 */
class ShopStatsLabel(
    skin: Skin,
    initValue: String,
    valueColor: Color,
    minDiffWidth: Float = 40f,
) : KTable, Table(skin) {

    private val valueLabel: Label
    private val diffLabel: Label

    init {
        valueLabel = label(initValue, "dialog_content", skin) { cell ->
            this.color = valueColor
            this.setAlignment(Align.left)
            cell.growX()
        }
        diffLabel = label("", "dialog_content", skin) { cell ->
            this.setAlignment(Align.right)
            cell.growX().padLeft(10f).minWidth(minDiffWidth)
        }
    }

    fun valueTxt(value: String) {
        valueLabel.txt = value
    }

    fun diffTxt(value: Int) {
        if (value == 0) {
            diffLabel.txt = ""
            return
        }

        if (value > 0) {
            diffLabel.txt = "+$value"
            diffLabel.color = skin.getColor("green")
        } else {
            diffLabel.txt = "$value"
            diffLabel.color = skin.getColor("red")
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.shopStatsLabel(
    skin: Skin,
    initValue: String,
    valueColor: Color = skin.getColor("dark_grey"),
    minDiffWidth: Float = 40f,
    init: (@Scene2dDsl ShopStatsLabel).(S) -> Unit = {},
): ShopStatsLabel = actor(ShopStatsLabel(skin, initValue, valueColor, minDiffWidth), init)
