package com.nopalsoft.superjumper.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.superjumper.SuperJumperGame
import com.nopalsoft.superjumper.screens.Screens

open class BaseWindow(@JvmField protected var screen: Screens, width: Float, height: Float, positionY: Float) : Group() {
    protected var languagesBundle: I18NBundle?

    @JvmField
    protected var game: SuperJumperGame = screen.game

    private var isVisible = false

    init {
        languagesBundle = game.languagesBundle
        setSize(width, height)
        y = positionY
    }

    open fun show(stage: Stage) {
        setOrigin(width / 2f, height / 2f)
        x = Screens.SCREEN_WIDTH / 2f - width / 2f

        setScale(.5f)
        addAction(Actions.sequence(Actions.scaleTo(1f, 1f, ANIMATION_DURATION), Actions.run { this.endResize() }))

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
        const val ANIMATION_DURATION: Float = .3f
    }
}
