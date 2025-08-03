package com.nopalsoft.ponyrace.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.ponyrace.AssetsHandler
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.screens.BaseScreen

open class Ventana(currentScreen: BaseScreen) : Group() {
    @JvmField
    var screen: BaseScreen? = currentScreen

    @JvmField
    var game: PonyRacingGame = currentScreen.game!!

    @JvmField
    var oAssetsHandler: AssetsHandler = game.assetsHandler!!

    private var isVisible = false

    fun setBackGround() {
        val img = Image(oAssetsHandler.fondoVentanas)
        img.setSize(getWidth(), getHeight())
        addActor(img)
    }

    fun show(stage: Stage) {
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setX(BaseScreen.SCREEN_WIDTH / 2f - getWidth() / 2f)

        setScale(.5f)
        addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, DURACION_ANIMATION),
                Actions.run { endResize() }
            )
        )

        isVisible = true
        stage.addActor(this)
    }

    override fun isVisible(): Boolean {
        return isVisible
    }

    fun hide() {
        isVisible = false
        remove()
    }

    protected fun endResize() {
    }

    companion object {
        const val DURACION_ANIMATION: Float = .3f
    }
}
