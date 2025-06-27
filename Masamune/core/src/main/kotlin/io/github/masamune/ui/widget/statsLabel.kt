package io.github.masamune.ui.widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import io.github.masamune.ui.view.StatsView
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.label

/**
 * A table with two labels:
 * - One [valueLabel] for the current stats value of a character
 * - One [detailLabel] that shows the base value plus any bonus value
 *
 * This is used in [StatsView] to let the player know what is the default value of a stat and what is the
 * bonus via equipment.
 */
class StatsLabel(
    skin: Skin,
    initValue: String,
    valueColor: Color,
) : KTable, Table(skin) {

    private val valueLabel: Label
    private val detailLabel: Label

    init {
        valueLabel = label(initValue, "dialog_content", skin) { cell ->
            this.color = valueColor
            this.setAlignment(Align.left)
            cell.growX()
        }
        detailLabel = label("", "dialog_content", skin) { cell ->
            this.setAlignment(Align.right)
            cell.growX().padLeft(10f).minWidth(40f)
        }
    }

    fun valueTxt(value: String) {
        valueLabel.txt = value
    }

    fun detailTxt(base: Int, bonus: Int) {
        if (bonus == 0) {
            detailLabel.txt = ""
            return
        }

        val defaultColor = skin.getColor("dark_grey")
        if (bonus > 0) {
            val color = skin.getColor("green")
            detailLabel.txt = "[#$defaultColor]($base[][#$color]+$bonus[][#$defaultColor])[]"
        } else {
            val color = skin.getColor("red")
            detailLabel.txt = "[#$defaultColor]($base[][#$color]$bonus[][#$defaultColor])[]"
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.statsLabel(
    skin: Skin,
    initValue: String,
    valueColor: Color = skin.getColor("dark_grey"),
    init: (@Scene2dDsl StatsLabel).(S) -> Unit = {},
): StatsLabel = actor(StatsLabel(skin, initValue, valueColor), init)
