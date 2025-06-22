package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.screens.BaseScreen

open class Dialog(var screen: BaseScreen) : Group() {

    var game = screen.game

    private var isVisible = false

    fun setBackGround() {
        val img = Image(Assets.scoresBackground)
        img.setSize(width, height)
        addActor(img)
    }

    fun show(stage: Stage) {
        setOrigin(width / 2f, height / 2f)
        x = BaseScreen.SCREEN_WIDTH / 2f - width / 2f

        setScale(.5f)
        addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, ANIMATION_DURATION),
                Actions.run { this.endResize() })
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

    private fun endResize() {
    }

    companion object {
        const val ANIMATION_DURATION: Float = .3f
    }
}
