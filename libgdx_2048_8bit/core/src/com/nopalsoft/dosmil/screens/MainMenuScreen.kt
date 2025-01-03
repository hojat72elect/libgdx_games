package com.nopalsoft.dosmil.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.MainGame
import com.nopalsoft.dosmil.Settings
import com.nopalsoft.dosmil.game.GameScreen

class MainMenuScreen(game: MainGame) : Screens(game) {
    var imageTitle: Image = Image(Assets.title)

    var labelPlay: Label
    var labelHelp: Label
    var labelLeaderboard: Label
    var labelRate: Label


    var buttonMusic: Button
    var buttonSound: Button
    var buttonFacebook: Button

    init {
        imageTitle.setPosition(SCREEN_WIDTH / 2f - imageTitle.width / 2f, 580f)

        labelPlay = Label(Assets.languages?.get("play"), Assets.labelStyleLarge)
        labelPlay.setPosition(SCREEN_WIDTH / 2f - labelPlay.width / 2f, 450f)
        addPressEffect(labelPlay)
        labelPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        // Help
        labelHelp = Label(Assets.languages?.get("help"), Assets.labelStyleLarge)
        labelHelp.setPosition(SCREEN_WIDTH / 2f - labelHelp.width / 2f, 350f)
        addPressEffect(labelHelp)
        labelHelp.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(HelpScreen::class.java, game)
            }
        })

        // Rate
        labelRate = Label(Assets.languages?.get("rate"), Assets.labelStyleLarge)
        labelRate.setPosition(SCREEN_WIDTH / 2f - labelRate.width / 2f, 250f)
        addPressEffect(labelRate)
        labelRate.addListener(object : ClickListener() {})

        // Leaderboard
        labelLeaderboard = Label(Assets.languages?.get("leaderboard"), Assets.labelStyleLarge)
        labelLeaderboard.setFontScale(.85f)
        labelLeaderboard.width = SCREEN_WIDTH.toFloat()
        labelLeaderboard.setPosition(SCREEN_WIDTH / 2f - labelLeaderboard.width / 2f, 150f)
        labelLeaderboard.setAlignment(Align.center)
        labelLeaderboard.wrap = true

        addPressEffect(labelLeaderboard)
        labelLeaderboard.addListener(object : ClickListener() {
        })

        buttonMusic = Button(Assets.buttonStyleMusic)
        buttonMusic.setPosition(5f, 5f)
        buttonMusic.isChecked = !Settings.isMusicOn
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                buttonMusic.isChecked = !Settings.isMusicOn
                if (Settings.isMusicOn) Assets.playMusic()
                else Assets.pauseMusic()
            }
        })

        buttonSound = Button(Assets.buttonStyleSound)
        buttonSound.setPosition(75f, 5f)
        buttonSound.isChecked = !Settings.isSoundOn
        buttonSound.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                buttonSound.isChecked = !Settings.isSoundOn
            }
        })

        buttonFacebook = Button(Assets.buttonFacebook)
        buttonFacebook.setSize(50f, 50f)
        buttonFacebook.setPosition(SCREEN_WIDTH - buttonFacebook.width - 5, 10f)
        addPressEffect(buttonFacebook)
        buttonFacebook.addListener(object : ClickListener() {})

        stage!!.addActor(imageTitle)
        stage!!.addActor(labelPlay)
        stage!!.addActor(labelHelp)
        stage!!.addActor(labelLeaderboard)
        stage!!.addActor(labelRate)
        stage!!.addActor(buttonMusic)
        stage!!.addActor(buttonSound)
        stage!!.addActor(buttonFacebook)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.background, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher!!.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) Gdx.app.exit()
        return true
    }
}
