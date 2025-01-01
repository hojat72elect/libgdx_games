package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.screens.Screens

/**
 * The base class for all dialogs we create in this game. It has a background, a title and a close button.
 * It also has an animation effect when appearing and disappearing.
 */
open class Dialog(currentScreen: Screens, width: Float, height: Float, positionY: Float) : Group() {

    protected val screen = currentScreen
    private val dim = Image(Assets.pixelBlack)
    private var isShown = false

    init {
        setSize(width, height)
        y = positionY

        dim.setSize(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT)
        setBackGround(Assets.windowBackground)
    }

    protected fun setCloseButton() {
        val btClose = Button(Assets.btClose, Assets.buttonClosePress)
        btClose.setSize(60f, 60f)
        btClose.setPosition(290f, 250f)
        btClose.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })
        addActor(btClose)
    }

    protected fun setTitle(text: String, fontScale: Float) {
        val tableTitle = Table()

        tableTitle.setSize(180f, 50f)
        tableTitle.setPosition(width / 2f - tableTitle.width / 2f, height - tableTitle.height)

        val labelTitle = Label(text, LabelStyle(Assets.fontDefault, Color.WHITE))
        labelTitle.setFontScale(fontScale)
        tableTitle.add(labelTitle)
        addActor(tableTitle)
    }

    private fun setBackGround(imageBackground: TextureRegionDrawable) {
        val img = Image(imageBackground)
        img.setSize(width, height)
        addActor(img)
    }

    fun show(stage: Stage) {
        setOrigin(width / 2f, height / 2f)
        x = Screens.SCREEN_WIDTH / 2f - width / 2f

        setScale(.35f)
        addAction(Actions.scaleTo(1f, 1f, ANIMATION_DURATION))

        dim.color.a = 0f
        dim.addAction(Actions.alpha(.7f, ANIMATION_DURATION))

        isShown = true
        stage.addActor(dim)
        stage.addActor(this)
    }

    fun isShown(): Boolean {
        return isShown
    }

    fun hide() {
        isShown = false

        addAction(
            Actions
                .sequence(Actions.scaleTo(.35f, .35f, ANIMATION_DURATION), Actions.run { this.hideCompleted() }, Actions.removeActor(dim), Actions.removeActor())
        )
        dim.addAction(Actions.alpha(0f, ANIMATION_DURATION))
    }

    /**
     * the callback that will be called when the dialog is closed.
     */
    open fun hideCompleted() {
    }

    companion object {
        private const val ANIMATION_DURATION = .3F
    }
}