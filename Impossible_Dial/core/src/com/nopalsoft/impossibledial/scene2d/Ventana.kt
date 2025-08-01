package com.nopalsoft.impossibledial.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.nopalsoft.impossibledial.Assets
import com.nopalsoft.impossibledial.MainGame
import com.nopalsoft.impossibledial.screens.Screens

open class Ventana(protected var screen: Screens, width: Float, height: Float, positionY: Float) : Group() {
    private val dim: Image
    protected var game: MainGame? = screen.game

    init {
        setSize(width, height)
        setY(positionY)

        dim = Image(Assets.pixelNegro)
        dim.setSize(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())

        setBackGround(Assets.dialogVentana)
    }

    protected fun setCloseButton() {
        val btClose = Button(Assets.btFalse)
        btClose.setSize(50f, 50f)
        btClose.setPosition(270f, 190f)
        screen.addEfectoPress(btClose)
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

    fun show(stage: Stage) {
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f)

        setScale(.5f)
        addAction(Actions.scaleTo(1f, 1f, DURACION_ANIMATION))

        dim.getColor().a = 0f
        dim.addAction(Actions.alpha(.7f, DURACION_ANIMATION))

        stage.addActor(dim)
        stage.addActor(this)
    }

    fun hide() {
        addAction(Actions.sequence(Actions.scaleTo(.5f, .5f, DURACION_ANIMATION), Actions.removeActor()))
        dim.addAction(Actions.sequence(Actions.alpha(0f, DURACION_ANIMATION), Actions.removeActor()))
    }

    companion object {
        const val DURACION_ANIMATION: Float = .3f
    }
}
