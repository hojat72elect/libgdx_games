package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

open class BaseDialog(protected var screen: BaseScreen, width: Float, height: Float, positionY: Float) : Group() {
    private val dimImage: Image
    protected var languages: I18NBundle? = Assets.languagesBundle

    init {
        setSize(width, height)
        setY(positionY)

        dimImage = Image(Assets.blackPixelDrawable)
        dimImage.setSize(BaseScreen.SCREEN_WIDTH.toFloat(), BaseScreen.SCREEN_HEIGHT.toFloat())

        setBackGround(Assets.dialogDrawable)
    }

    protected fun setCloseButton(positionY: Float) {
        val btClose = Button(Assets.buttonFalseDrawable)
        btClose.setSize(50f, 50f)
        btClose.setPosition(400f, positionY)
        screen.addPressEffect(btClose)
        btClose.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })
        addActor(btClose)
    }

    private fun setBackGround(imageBackground: NinePatchDrawable?) {
        val img = Image(imageBackground)
        img.setSize(getWidth(), getHeight())
        addActor(img)
    }

    open fun show(stage: Stage) {
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setX(BaseScreen.SCREEN_WIDTH / 2f - getWidth() / 2f)

        setScale(.5f)
        addAction(Actions.scaleTo(1f, 1f, ANIMATION_DURATION))

        dimImage.getColor().a = 0f
        dimImage.addAction(Actions.alpha(.7f, ANIMATION_DURATION))

        stage.addActor(dimImage)
        stage.addActor(this)
    }

    fun hide() {
        addAction(Actions.sequence(Actions.scaleTo(.5f, .5f, ANIMATION_DURATION), Actions.removeActor()))
        dimImage.addAction(Actions.sequence(Actions.alpha(0f, ANIMATION_DURATION), Actions.removeActor()))
    }

    companion object {
        const val ANIMATION_DURATION: Float = .3f
    }
}
