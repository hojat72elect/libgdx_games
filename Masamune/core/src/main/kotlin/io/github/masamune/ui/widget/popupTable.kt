package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.label
import ktx.scene2d.table

@Scene2dDsl
class PopupTable(
    message: String,
    options: List<String>,
    skin: Skin
) : Table(skin), KTable {

    private val messageLabel: Label
    private val optionTable: OptionTable

    val selectedOption: Int
        get() = optionTable.selectedOption

    init {
        setFillParent(true)

        table(skin) {
            background = skin.getDrawable("dialog_frame")

            this@PopupTable.messageLabel = label(message, defaultStyle, skin) {
                color = skin.getColor("dark_grey")
                it.grow().colspan(2).pad(2f, 2f, 5f, 2f).row()
            }
            this@PopupTable.optionTable = optionTable(skin) {
                options.forEach { option(it) }
            }

            center()
        }
    }

    fun update(message: String, options: List<String>) {
        messageLabel.txt = message
        optionTable.clearOptions()
        options.forEach { optionTable.option(it) }
    }

    fun message(message: String) {
        messageLabel.txt = message
    }

    fun prevOption(): Boolean = optionTable.prevOption()

    fun nextOption(): Boolean = optionTable.nextOption()

    fun firstOption() = optionTable.firstOption()

}

@Scene2dDsl
fun <S> KWidget<S>.popupTable(
    message: String,
    options: List<String>,
    skin: Skin,
    init: (@Scene2dDsl PopupTable).(S) -> Unit = {},
): PopupTable = actor(PopupTable(message, options, skin), init)
