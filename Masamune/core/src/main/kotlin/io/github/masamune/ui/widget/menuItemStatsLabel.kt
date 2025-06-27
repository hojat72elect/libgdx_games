package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.label

/**
 * A table with two labels:
 * - One [titleLabel]
 * - One [statsLabel]
 */
class MenuItemStatsLabel(
    skin: Skin,
    title: String,
    value: String,
) : KTable, Table(skin) {

    private val titleLabel: Label
    private val statsLabel: StatsLabel

    init {
        titleLabel = label(title, "dialog_content", skin) { cell ->
            this.color = skin.getColor("dark_grey")
            cell.growX()
        }
        statsLabel = statsLabel(skin, value) { cell ->
            this.color = skin.getColor("dark_grey")
            cell.right().minWidth(180f)
        }
    }

    fun valueAndDetail(total: String, base: Int, bonus: Int) {
        statsLabel.valueTxt(total)
        statsLabel.detailTxt(base, bonus)
    }

}

@Scene2dDsl
fun <S> KWidget<S>.menuItemStatsLabel(
    skin: Skin,
    title: String,
    value: String,
    init: (@Scene2dDsl MenuItemStatsLabel).(S) -> Unit = {},
): MenuItemStatsLabel = actor(MenuItemStatsLabel(skin, title, value), init)
