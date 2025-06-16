package com.nopalsoft.invaders.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.GalaxyInvadersGame
import com.nopalsoft.invaders.Settings
import com.nopalsoft.invaders.game.GameScreen


class MainMenuScreen(game: GalaxyInvadersGame) : Screens(game) {
    var buttonPlay: TextButton
    var buttonSettings: TextButton
    var buttonLeaderBoard: TextButton
    var buttonMore: TextButton
    var buttonFacebook: TextButton

    var labelHighestScore: Label

    var buttonSound: ImageButton
    var buttonMusic: ImageButton
    var leftEllipse: Image

    init {
        val tableTitle = Table()
        tableTitle.background = Assets.titleMenuBox
        val labelTitle = Label(Assets.languagesBundle!!["titulo_app"], LabelStyle(Assets.font60, Color.GREEN))
        labelTitle.setAlignment(Align.center)
        tableTitle.setSize(265f, 100f)
        tableTitle.setPosition((SCREEN_WIDTH - 265) / 2f, (SCREEN_HEIGHT - 110).toFloat())
        tableTitle.add(labelTitle).expand().center()

        // I'll put the text in the update.
        labelHighestScore = Label("", LabelStyle(Assets.font10, Color.GREEN))
        labelHighestScore.width = SCREEN_WIDTH.toFloat()
        labelHighestScore.setAlignment(Align.center)
        labelHighestScore.setPosition(0f, (SCREEN_HEIGHT - 120).toFloat())

        buttonPlay = TextButton(Assets.languagesBundle!!["play"], Assets.styleTextButtonMenu)
        buttonPlay.setSize(250f, 50f)
        buttonPlay.setPosition(0f, 280f)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = GameScreen(game)
            }
        })

        buttonSettings = TextButton(Assets.languagesBundle!!["settings"], Assets.styleTextButtonMenu)
        buttonSettings.setSize(300f, 50f)
        buttonSettings.setPosition(0f, 210f)
        buttonSettings.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = SettingsScreen(game)
            }
        })

        buttonLeaderBoard = TextButton(Assets.languagesBundle!!["leaderboard"], Assets.styleTextButtonMenu)
        buttonLeaderBoard.setSize(310f, 50f)
        buttonLeaderBoard.setPosition(0f, 140f)
        buttonLeaderBoard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = LeaderboardScreen(game)
            }
        })

        buttonMore = TextButton(Assets.languagesBundle!!["more"], Assets.styleTextButtonMenu)
        buttonMore.setSize(250f, 50f)
        buttonMore.setPosition(0f, 70f)
        buttonMore.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
            }
        })

        buttonFacebook = TextButton(Assets.languagesBundle!!["like_us_to_get_lastest_news"], Assets.styleTextButtonFacebook)
        buttonFacebook.label.wrap = true
        buttonFacebook.width = 170f
        buttonFacebook.setPosition(SCREEN_WIDTH - buttonFacebook.width - 2, 2f)
        buttonFacebook.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                Gdx.net.openURI("https://www.facebook.com/yayo28")
            }
        })

        buttonSound = ImageButton(Assets.buttonSoundOn, Assets.buttonSoundOff, Assets.buttonSoundOff)
        buttonSound.setSize(40f, 40f)
        buttonSound.setPosition(2f, 2f)
        if (!Settings.soundEnabled) buttonSound.isChecked = true
        buttonSound.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.soundEnabled = !Settings.soundEnabled
                playSound(Assets.clickSound!!)
                buttonSound.isChecked = !Settings.soundEnabled
            }
        })

        buttonMusic = ImageButton(Assets.buttonMusicOn, Assets.buttonMusicOff, Assets.buttonMusicOff)
        buttonMusic.setSize(40f, 40f)
        buttonMusic.setPosition(44f, 2f)
        if (!Settings.musicEnabled) buttonMusic.isChecked = true
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.musicEnabled = !Settings.musicEnabled
                playSound(Assets.clickSound!!)
                if (!Settings.musicEnabled) {
                    buttonMusic.isChecked = true
                    Assets.music!!.pause()
                } else {
                    buttonMusic.isChecked = false
                    Assets.music!!.play()
                }
            }
        })

        // The measurements are taken with a formula of 3 if 480 / 960 x 585 where 585 is the size,
        // 960 is the size for what they were made and 480 is the size of the camera
        leftEllipse = Image(Assets.leftMenuEllipse)
        leftEllipse.setSize(18.5f, 292.5f)
        leftEllipse.setPosition(0f, 60f)

        stage?.addActor(tableTitle)
        stage?.addActor(labelHighestScore)

        stage?.addActor(buttonPlay)
        stage?.addActor(buttonSettings)
        stage?.addActor(buttonLeaderBoard)
        stage?.addActor(buttonMore)
        stage?.addActor(leftEllipse)
        stage?.addActor(buttonSound)
        stage?.addActor(buttonMusic)
        stage?.addActor(buttonFacebook)


        if (Settings.numberOfTimesPlayed == 0) {
            game.dialog!!.showDialogSignIn()
        }
    }

    override fun update(delta: Float) {
        labelHighestScore.setText(Assets.languagesBundle!!.format("local_highest_score", Settings.highScores[0].toString()))
    }

    override fun draw(delta: Float) {
        camera.update()
        batch?.projectionMatrix = camera.combined

        batch?.disableBlending()
        Assets.backgroundLayer!!.render(delta)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            playSound(Assets.clickSound!!)
            if (game.dialog!!.isDialogShown) {
                game.dialog!!.dismissAll()
            } else {
                Gdx.app.exit()
            }
            return true
        }
        return false
    }
}
