package com.nopalsoft.zombiedash.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.MainZombieDash
import com.nopalsoft.zombiedash.screens.Screens

open class Ventana(@JvmField protected var screen: Screens, width: Float, height: Float, positionY: Float, imageBackgroun: TextureRegionDrawable?) : Group() {
    @JvmField
    protected var idiomas: I18NBundle?

    @JvmField
    protected var game: MainZombieDash

    private var isVisible = false
    private var imgBackground: Image? = null

    init {
        game = screen.game
        idiomas = game.idiomas
        setSize(width, height)
        setY(positionY)

        setBackGround(imageBackgroun)
    }

    protected fun setCloseButton(positionX: Float, positionY: Float, size: Float) {
        val btClose = Button(Assets.btClose)
        btClose.setSize(size, size)
        btClose.setPosition(positionX, positionY)
        screen.addEfectoPress(btClose)
        btClose.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })
        addActor(btClose)
    }

    private fun setBackGround(imageBackground: TextureRegionDrawable?) {
        imgBackground = Image(imageBackground)
        imgBackground!!.setSize(getWidth(), getHeight())
        addActor(imgBackground)
    }

    override fun sizeChanged() {
        super.sizeChanged()
        if (imgBackground != null) imgBackground!!.setSize(getWidth(), getHeight())
    }

    open fun show(stage: Stage) {
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f)

        setScale(.5f)
        addAction(Actions.sequence(Actions.scaleTo(1f, 1f, DURACION_ANIMATION), Actions.run(object : Runnable {
            override fun run() {
                endResize()
            }
        })))

        isVisible = true
        stage.addActor(this)
    }

    override fun isVisible(): Boolean {
        return isVisible
    }

    open fun hide() {
        isVisible = false
        remove()
    }

    protected fun endResize() {
    }

    companion object {
        const val DURACION_ANIMATION: Float = .3f
    }
}
