package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.screens.BaseScreen

open class BaseDialog(val screen: BaseScreen, width: Float, height: Float, positionY: Float) :
    Group() {
    private val imageDim = Image(Assets.pixelBlack)
    protected val game = screen.game
    protected val languages = Assets.languages


    init {
        setSize(width, height)
        y = positionY
        imageDim.setSize(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT)
        setBackGround(Assets.dialogWindow)
    }


    protected fun setCloseButton(positionY: Float) {
        val buttonClose = Button(Assets.buttonFalse)
        buttonClose.setSize(50f, 50f)
        buttonClose.setPosition(400f, positionY)
        screen.addPressEffect(buttonClose)
        buttonClose.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })
        addActor(buttonClose)
    }

    private fun setBackGround(imageBackground: NinePatchDrawable) {
        val img = Image(imageBackground)
        img.setSize(width, height)
        addActor(img)
    }

    open fun show(stage: Stage) {
        setOrigin(width / 2f, height / 2f)
        x = BaseScreen.SCREEN_WIDTH / 2f - width / 2f

        setScale(.5f)
        addAction(Actions.scaleTo(1f, 1f, ANIMATION_DURATION))

        imageDim.color.a = 0f
        imageDim.addAction(Actions.alpha(.7f, ANIMATION_DURATION))

        stage.addActor(imageDim)
        stage.addActor(this)
    }

    fun hide() {
        addAction(
            Actions.sequence(
                Actions.scaleTo(.5f, .5f, ANIMATION_DURATION),
                Actions.removeActor()
            )
        )
        imageDim.addAction(
            Actions.sequence(
                Actions.alpha(0f, ANIMATION_DURATION),
                Actions.removeActor()
            )
        )
    }

    companion object {
        const val ANIMATION_DURATION = .3F
    }
}