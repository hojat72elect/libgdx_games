package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Scaling
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
class ShopItemWidget(
    title: String,
    cost: Int,
    talonsPostfix: String,
    skin: Skin,
) : Table(skin), KTable, SelectableWidget {

    private val image: Image
    private val amountLabel: Label

    init {
        image = image(skin.getDrawable("arrow")) { cell ->
            setScaling(Scaling.contain)
            this += forever(fadeOut(0.5f) then fadeIn(0.25f) then delay(0.25f))
            cell.padRight(5f).padLeft(5f)
        }

        label(title, defaultStyle, skin) {
            this.setEllipsis(true)
            this.setEllipsis("..")
            it.left().minWidth(0f).growX()
        }
        label("${cost}$talonsPostfix", defaultStyle, skin) {
            it.right().padRight(30f).expandX()
        }
        amountLabel = label("0x", defaultStyle, skin) {
            it.right().padRight(5f).minWidth(20f)
        }
    }

    override fun select(value: Boolean) {
        image.isVisible = value
    }

    fun amount(value: Int) {
        amountLabel.txt = "${value}x"
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
fun <S> KWidget<S>.shopItem(
    title: String,
    cost: Int,
    talonsPostfix: String,
    skin: Skin,
    init: (@Scene2dDsl ShopItemWidget).(S) -> Unit = {},
): ShopItemWidget = actor(ShopItemWidget(title, cost, talonsPostfix, skin), init)
