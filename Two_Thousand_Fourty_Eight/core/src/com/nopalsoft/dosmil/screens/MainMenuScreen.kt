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
import com.nopalsoft.dosmil.Assets.pauseMusic
import com.nopalsoft.dosmil.Assets.playMusic
import com.nopalsoft.dosmil.MainGame
import com.nopalsoft.dosmil.Settings
import com.nopalsoft.dosmil.game.GameScreen

class MainMenuScreen(game: MainGame) : Screens(game) {
    private var titleImage: Image = Image(Assets.titleAtlasRegion)

    private var labelPlay: Label
    private var labelHelp: Label
    private var labelLeaderboard: Label
    private var labelRate: Label

    var buttonMusic: Button
    var buttonSound: Button
    private var buttonFacebook: Button

    init {
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.width / 2f, 580f)

        labelPlay = Label(Assets.languagesBundle!!["play"], Assets.labelStyleLarge)
        labelPlay.setPosition(SCREEN_WIDTH / 2f - labelPlay.width / 2f, 450f)
        addPressEffect(labelPlay)
        labelPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        // Help
        labelHelp = Label(Assets.languagesBundle!!["help"], Assets.labelStyleLarge)
        labelHelp.setPosition(SCREEN_WIDTH / 2f - labelHelp.width / 2f, 350f)
        addPressEffect(labelHelp)
        labelHelp.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(HelpScreen::class.java, game)
            }
        })

        // Rate
        labelRate = Label(Assets.languagesBundle!!["rate"], Assets.labelStyleLarge)
        labelRate.setPosition(SCREEN_WIDTH / 2f - labelRate.width / 2f, 250f)
        addPressEffect(labelRate)

        // Leaderboard
        labelLeaderboard = Label(Assets.languagesBundle!!["leaderboard"], Assets.labelStyleLarge)
        labelLeaderboard.setFontScale(.85f)
        labelLeaderboard.width = SCREEN_WIDTH.toFloat()
        labelLeaderboard.setPosition(SCREEN_WIDTH / 2f - labelLeaderboard.width / 2f, 150f)
        labelLeaderboard.setAlignment(Align.center)
        labelLeaderboard.wrap = true

        addPressEffect(labelLeaderboard)

        buttonMusic = Button(Assets.buttonStyleMusic)
        buttonMusic.setPosition(5f, 5f)
        buttonMusic.isChecked = !Settings.isMusicOn
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                buttonMusic.isChecked = !Settings.isMusicOn
                if (Settings.isMusicOn) playMusic()
                else pauseMusic()
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

        stage!!.addActor(titleImage)
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
        batch!!.begin()
        batch!!.draw(Assets.backgroundAtlasRegion!!, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batch!!.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) Gdx.app.exit()
        return true
    }
}
