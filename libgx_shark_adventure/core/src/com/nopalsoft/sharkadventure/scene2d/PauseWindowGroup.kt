package com.nopalsoft.sharkadventure.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sharkadventure.Assets
import com.nopalsoft.sharkadventure.MainShark
import com.nopalsoft.sharkadventure.game.GameScreen
import com.nopalsoft.sharkadventure.screens.BaseScreen

class PauseWindowGroup(currentScreen: GameScreen) : Group() {
    protected var screen: GameScreen
    protected var game: MainShark
    private var buttonPlay: Button
    private var buttonRefresh: Button
    private var buttonHome: Button
    private var isVisible = false

    init {
        setSize(300f, 300f)
        setPosition(BaseScreen.SCREEN_WIDTH / 2f - width / 2f, 80f)
        screen = currentScreen
        game = currentScreen.game
        setBackGround()

        val tableTitle = Table()
        tableTitle.setSize(width - 80, 50f)
        tableTitle.setPosition(width / 2f - tableTitle.width / 2f, height - 30)
        tableTitle.background = Assets.backgroundTitle

        val labelTitle = Label("Paused", Assets.labelStyle)

        tableTitle.add(labelTitle).fill().padBottom(10f)
        addActor(tableTitle)

        buttonPlay = Button(Assets.buttonRight, Assets.buttonRightPress)
        buttonPlay.setSize(70f, 70f)
        buttonPlay.setPosition(width / 2f - buttonPlay.width / 2f, 170f)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.setRunning(false)
            }
        })

        buttonRefresh = Button(Assets.buttonRefresh, Assets.buttonRefreshPress)
        buttonRefresh.setSize(70f, 70f)
        buttonRefresh.setPosition(width / 2f + 25, 80f)
        buttonRefresh.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.game.screen = GameScreen(game, false)
            }
        })

        buttonHome = Button(Assets.buttonHome, Assets.buttonHomePress)
        buttonHome.setSize(70f, 70f)
        buttonHome.setPosition(width / 2f - buttonHome.width - 25, 80f)
        buttonHome.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.game.screen = GameScreen(game, true)
            }
        })

        addActor(buttonPlay)
        addActor(buttonRefresh)
        addActor(buttonHome)
    }

    private fun setBackGround() {
        val image = Image(Assets.backgroundWindow)
        image.setSize(width, height)
        addActor(image)
    }

    fun show(stage: Stage) {
        setOrigin(width / 2f, height / 2f)
        x = BaseScreen.SCREEN_WIDTH / 2f - width / 2f

        setScale(.5f)
        addAction(Actions.sequence(Actions.scaleTo(1f, 1f, ANIMATION_DURATION)))

        isVisible = true
        stage.addActor(this)

        game.reqHandler.showAdBanner()
    }

    override fun isVisible(): Boolean {
        return isVisible
    }

    fun hide() {
        isVisible = false
        game.reqHandler.hideAdBanner()
        remove()
    }

    companion object {
        const val ANIMATION_DURATION: Float = .3f
    }
}
