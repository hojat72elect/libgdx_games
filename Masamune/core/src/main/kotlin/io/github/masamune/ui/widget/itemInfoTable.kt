package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import io.github.masamune.ui.view.typingLabel
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.table

@Scene2dDsl
class ItemInfoTable(skin: Skin) : Table(skin), KTable {

    private val image: Image
    private val nameLabel: Label
    private val descriptionLabel: TypingLabel

    init {
        background = skin.getDrawable("dialog_frame")

        table(skin) { headerTblCell ->
            this@ItemInfoTable.image = image { imgCell ->
                setScaling(Scaling.fit)
                imgCell.size(42f, 42f).fill()
            }
            this@ItemInfoTable.nameLabel = label("", "dialog_content", skin) { lblCell ->
                setAlignment(Align.left)
                color = skin.getColor("highlight_blue")
                lblCell.growX().padLeft(20f)
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

    fun item(name: String, description: String, drawable: Drawable?) {
        this.nameLabel.txt = name
        this.descriptionLabel.restart("{FAST}$description")
        this.image.drawable = drawable
    }

    fun clearItem() {
        this.nameLabel.txt = ""
        this.descriptionLabel.txt = ""
        this.image.drawable = null
    }
}

@Scene2dDsl
fun <S> KWidget<S>.itemInfoTable(
    skin: Skin,
    init: (@Scene2dDsl ItemInfoTable).(S) -> Unit = {},
): ItemInfoTable = actor(ItemInfoTable(skin), init)
