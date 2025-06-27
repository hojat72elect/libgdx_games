package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import io.github.masamune.tiledmap.ItemCategory
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.image
import ktx.scene2d.label

@Scene2dDsl
class EquipmentItemWidget(
    val category: ItemCategory,
    categoryName: String,
    skin: Skin,
) : Table(skin), KTable, SelectableWidget {

    private val image: Image
    val categoryLabelCell: Cell<*>
    private val itemLabel: Label

    init {
        image = image(skin.getDrawable("arrow")) { cell ->
            setScaling(Scaling.contain)
            this += forever(fadeOut(0.5f) then fadeIn(0.25f) then delay(0.25f))
            cell.padRight(5f)
        }

        label(categoryName, defaultStyle, skin) {
            this.color = skin.getColor("dark_grey")
            this@EquipmentItemWidget.categoryLabelCell = it
            it.align(Align.left)
        }
        itemLabel = label("", defaultStyle, skin) {
            this.color = skin.getColor("dark_grey")
            this.setEllipsis(true)
            this.setEllipsis("..")
            it.padLeft(20f).align(Align.left).minWidth(0f).growX().row()
        }
    }

    override fun select(value: Boolean) {
        image.isVisible = value
    }

    fun item(name: String) {
        itemLabel.txt = name
    }

    fun stopSelectAnimation() {
        image.clearActions()
        image.alpha = 1f
    }

    fun resumeSelectAnimation() {
        image.clearActions()
        image.alpha = 1f
        image += forever(fadeOut(0.5f) then fadeIn(0.25f) then delay(0.25f))
    }

}

@Scene2dDsl
fun <S> KWidget<S>.equipmentItem(
    category: ItemCategory,
    categoryName: String,
    skin: Skin,
    init: (@Scene2dDsl EquipmentItemWidget).(S) -> Unit = {},
): EquipmentItemWidget = actor(EquipmentItemWidget(category, categoryName, skin), init)
