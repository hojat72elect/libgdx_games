package io.github.masamune.ui.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy
import com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Scaling
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import io.github.masamune.ui.model.QuestItemViewModel
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.actors.txt
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.image
import ktx.scene2d.scene2d
import ktx.scene2d.table

/**
 * An image and text popup when the player receives a quest item.
 */
class QuestItemView(
    model: QuestItemViewModel,
    skin: Skin,
) : View<QuestItemViewModel>(skin, model), KGroup {

    private val itemInfoTable: Table
    private val itemImage: Image
    private val label: TypingLabel

    init {
        itemInfoTable = table(skin) {
            // we don't use frameImage here because I did not understand how I can properly scale the inner image
            // AND have a padding top+bottom. A good combination seems to be Scaling.fill for the frame image
            // and Scaling.fit for the inner image with a size where the width is bigger than the height.
            // That's why we use a custom stack below.
            val frameImg = scene2d.image("frame_1", skin).apply { setScaling(Scaling.fill) }
            this@QuestItemView.itemImage = scene2d.image(drawable = null).apply { setScaling(Scaling.fit) }
            stack(frameImg, this@QuestItemView.itemImage).size(32f + 10, 32f)

            this@QuestItemView.label = typingLabel("", defaultStyle, skin) {
                it.growX().padLeft(10f)
            }
            pack()
        }

        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        viewModel.onPropertyChange(QuestItemViewModel::itemPosition) { position ->
            debug = true
            if (position.isZero) {
                label.txt = ""
                itemImage.drawable = null
                isVisible = false
                return@onPropertyChange
            }

            label.txt = "{EASE=6;6;1}{RAINBOW}${viewModel.itemName}"
            label.restart()
            itemImage.drawable = viewModel.itemDrawable
            itemInfoTable.pack()
            itemInfoTable.setPosition(position.x, position.y)

            itemInfoTable.clearActions()
            itemInfoTable += parallel(
                alpha(0.25f, 0f) then alpha(1f, 0.5f),
                moveBy(0f, 15f, 0f) then moveBy(0f, -15f, 1f, Interpolation.bounceOut)
            )

            isVisible = true
        }
    }

}

@Scene2dDsl
fun <S> KWidget<S>.questItemView(
    model: QuestItemViewModel,
    skin: Skin,
    init: (@Scene2dDsl QuestItemView).(S) -> Unit = {},
): QuestItemView = actor(QuestItemView(model, skin), init)
