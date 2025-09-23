package com.bitfire.uracer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.bitfire.uracer.Input
import com.bitfire.uracer.URacer
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.utils.UIUtils

/**
 * Implements a Screen that can overlay libgdx's UI elements on it.
 */
abstract class UIScreen : Screen() {

    @JvmField
    protected var ui: Stage = createStage()

    @JvmField
    protected var input: Input = URacer.Game.getInputSystem()

    protected abstract fun setupUI(ui: Stage)
    protected abstract fun draw()
    protected fun createStage() = UIUtils.newFittedStage()

    protected fun reload() {
        disable()
        ui.dispose()
        Art.disposeScreensData()
        Art.loadScreensData()
        ui = createStage()
        setupUI(ui)
        enable()
    }

    override fun init(): Boolean {
        setupUI(ui)
        return true
    }

    override fun enable() {
        Gdx.input.inputProcessor = ui
    }

    override fun dispose() {
        disable()
        ui.dispose()
    }

    override fun render(destinationBuffer: FrameBuffer?) {
        val hasDest = (destinationBuffer != null)

        if (hasDest) {
            ui.viewport.update(destinationBuffer.getWidth(), destinationBuffer.getHeight(), true)
            destinationBuffer.begin()
        } else
            ui.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)

        draw()
        if (hasDest) destinationBuffer.end()
    }
}
