package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import io.github.masamune.ui.view.typingLabel
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.label
import ktx.scene2d.table

@Scene2dDsl
class QuestInfoTable(skin: Skin) : Table(skin), KTable {

    private val nameLabel: Label
    private val descriptionLabel: TypingLabel

    init {
        background = skin.getDrawable("dialog_frame")

        table(skin) { headerTblCell ->
            this@QuestInfoTable.nameLabel = label("", "dialog_content", skin) { lblCell ->
                setAlignment(Align.center)
                color = skin.getColor("highlight_blue")
                lblCell.growX()
            }
            headerTblCell.growX().row()
        }

        descriptionLabel = typingLabel("", "dialog_content", skin) { lblCell ->
            wrap = true
            val defaultTextColor = skin.getColor("dark_grey")
            defaultToken = "{COLOR=#$defaultTextColor}"
            clearColor.set(defaultTextColor)
            setAlignment(Align.topLeft)
            lblCell.padTop(10f).grow()
        }
    }

    fun quest(name: String, description: String) {
        this.nameLabel.txt = name
        this.descriptionLabel.restart("{FAST}$description")
    }

    fun clearQuest() {
        this.nameLabel.txt = ""
        this.descriptionLabel.txt = ""
    }
}

@Scene2dDsl
fun <S> KWidget<S>.questInfoTable(
    skin: Skin,
    init: (@Scene2dDsl QuestInfoTable).(S) -> Unit = {},
): QuestInfoTable = actor(QuestInfoTable(skin), init)
