package com.nopalsoft.sharkadventure.scene2d

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
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
import com.nopalsoft.sharkadventure.Settings
import com.nopalsoft.sharkadventure.game.GameScreen
import com.nopalsoft.sharkadventure.game.GameWorld
import com.nopalsoft.sharkadventure.screens.Screens

class MenuUI(gameScreen: GameScreen, gameWorld: GameWorld?) : Group() {
    var gameScreen: GameScreen
    var gameWorld: GameWorld?
    var titleImage: Image? = null
    var gameOverImage: Image? = null

    var tableMenu: Table? = null
    var tableGameOver: Table

    var labelBestScore: Label
    var labelScore: Label

    var buttonPlay: Button? = null
    var buttonLeaderboard: Button? = null
    var buttonAchievements: Button? = null
    var buttonFacebook: Button? = null
    var buttonTwitter: Button? = null
    var buttonMusic: Button? = null
    var buttonSoundEffect: Button? = null

    var showMainMenu: Boolean = false

    init {
        setBounds(0f, 0f, Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        this.gameScreen = gameScreen
        this.gameWorld = gameWorld

        init()

        tableGameOver = Table()
        tableGameOver.setSize(350f, 200f)
        tableGameOver.setBackground(Assets.windowBackgroundDrawable)
        tableGameOver.setPosition(getWidth() / 2f - tableGameOver.getWidth() / 2f, 110f)

        labelBestScore = Label("0", Assets.lblStyle)
        labelScore = Label("0", Assets.lblStyle)

        labelScore.setFontScale(.8f)
        labelBestScore.setFontScale(.8f)

        tableGameOver.pad(15f).padTop(30f).padBottom(50f)
        tableGameOver.defaults().expand()

        tableGameOver.add<Label?>(Label("Score", Assets.lblStyle)).left()
        tableGameOver.add<Label?>(labelScore).expandX().right()

        tableGameOver.row()
        tableGameOver.add<Label?>(Label("Best score", Assets.lblStyle)).left()
        tableGameOver.add<Label?>(labelBestScore).expandX().right()
    }

    private fun init() {
        titleImage = Image(Assets.titleDrawable)
        titleImage!!.setScale(1f)
        titleImage!!.setPosition(getWidth() / 2f - titleImage!!.getWidth() * titleImage!!.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage!!.getHeight())

        gameOverImage = Image(Assets.gameOverDrawable)
        gameOverImage!!.setScale(1.25f)
        gameOverImage!!.setPosition(getWidth() / 2f - gameOverImage!!.getWidth() * gameOverImage!!.getScaleX() / 2f, Screens.SCREEN_HEIGHT + gameOverImage!!.getHeight())

        buttonFacebook = Button(Assets.buttonFacebook, Assets.buttonFacebookPressed)
        buttonFacebook!!.setSize(60f, 60f)
        buttonFacebook!!.setPosition(Screens.SCREEN_WIDTH + buttonFacebook!!.getWidth(), 410f)

        buttonTwitter = Button(Assets.buttonTwitter, Assets.buttonTwitterPressed)
        buttonTwitter!!.setSize(60f, 60f)
        buttonTwitter!!.setPosition(Screens.SCREEN_WIDTH + buttonTwitter!!.getWidth(), 410f)

        buttonMusic = Button(Assets.buttonMusicOff, Assets.buttonMusicOn, Assets.buttonMusicOn)
        buttonMusic!!.setSize(60f, 60f)
        buttonMusic!!.setPosition(-buttonMusic!!.getWidth(), 410f)

        buttonSoundEffect = Button(Assets.buttonSoundOff, Assets.buttonSoundOn, Assets.buttonSoundOn)
        buttonSoundEffect!!.setSize(60f, 60f)
        buttonSoundEffect!!.setPosition(-buttonSoundEffect!!.getWidth(), 325f)

        tableMenu = Table()
        tableMenu!!.setBackground(Assets.menuBackgroundDrawable)

        buttonPlay = Button(Assets.buttonRight, Assets.buttonRightPressed)
        buttonLeaderboard = Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPressed)
        buttonAchievements = Button(Assets.buttonAchievements, Assets.buttonAchievementsPressed)

        tableMenu!!.defaults().size(90f).padBottom(20f).padLeft(10f).padRight(10f)
        if (Gdx.app.type != ApplicationType.WebGL) {
            tableMenu!!.setSize(385f, 85f)
            tableMenu!!.add<Button?>(buttonPlay)
            tableMenu!!.add<Button?>(buttonLeaderboard)
            tableMenu!!.add<Button?>(buttonAchievements)
        } else {
            tableMenu!!.setSize(120f, 85f)
            tableMenu!!.add<Button?>(buttonPlay)
        }
        tableMenu!!.setPosition(Screens.SCREEN_WIDTH / 2f - tableMenu!!.getWidth() / 2f, -tableMenu!!.getHeight())


        buttonPlay!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (showMainMenu) gameScreen.setRunning(true)
                else {
                    gameScreen.game.setScreen(GameScreen(gameScreen.game, false))
                }
            }
        })

        buttonMusic!!.setChecked(Settings.isMusicOn)
        buttonMusic!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                buttonMusic!!.setChecked(Settings.isMusicOn)
                if (Settings.isMusicOn) Assets.music!!.play()
                else Assets.music!!.pause()
            }
        })

        buttonSoundEffect!!.setChecked(Settings.isSoundOn)
        buttonSoundEffect!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                buttonSoundEffect!!.setChecked(Settings.isSoundOn)
            }
        })

        addActor(tableMenu)
        addActor(buttonFacebook)
        addActor(buttonTwitter)
        addActor(buttonMusic)
        addActor(buttonSoundEffect)
    }

    private fun addInActions() {
        titleImage!!.addAction(Actions.moveTo(getWidth() / 2f - titleImage!!.getWidth() * titleImage!!.getScaleX() / 2f, 300f, ANIMATION_TIME))
        gameOverImage!!.addAction(Actions.moveTo(getWidth() / 2f - gameOverImage!!.getWidth() * gameOverImage!!.getScaleX() / 2f, 320f, ANIMATION_TIME))

        tableMenu!!.addAction(Actions.moveTo(Screens.SCREEN_WIDTH / 2f - tableMenu!!.getWidth() / 2f, 0f, ANIMATION_TIME))

        buttonFacebook!!.addAction(Actions.moveTo(735f, 410f, ANIMATION_TIME))
        buttonTwitter!!.addAction(Actions.moveTo(735f, 325f, ANIMATION_TIME))
        buttonMusic!!.addAction(Actions.moveTo(5f, 410f, ANIMATION_TIME))
        buttonSoundEffect!!.addAction(Actions.moveTo(5f, 325f, ANIMATION_TIME))
    }

    private fun addOutActions() {
        titleImage!!.addAction(
            Actions.moveTo(
                getWidth() / 2f - titleImage!!.getWidth() * titleImage!!.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage!!.getHeight(),
                ANIMATION_TIME
            )
        )
        gameOverImage!!.addAction(
            Actions.moveTo(
                getWidth() / 2f - gameOverImage!!.getWidth() * gameOverImage!!.getScaleX() / 2f,
                Screens.SCREEN_HEIGHT + gameOverImage!!.getHeight(), ANIMATION_TIME
            )
        )

        tableMenu!!.addAction(Actions.moveTo(Screens.SCREEN_WIDTH / 2f - tableMenu!!.getWidth() / 2f, -tableMenu!!.getHeight(), ANIMATION_TIME))

        buttonFacebook!!.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + buttonFacebook!!.getWidth(), 410f, ANIMATION_TIME))
        buttonTwitter!!.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + buttonTwitter!!.getWidth(), 325f, ANIMATION_TIME))
        buttonMusic!!.addAction(Actions.moveTo(-buttonMusic!!.getWidth(), 410f, ANIMATION_TIME))
        buttonSoundEffect!!.addAction(Actions.moveTo(-buttonSoundEffect!!.getWidth(), 325f, ANIMATION_TIME))
    }

    fun show(stage: Stage, showMainMenu: Boolean) {
        addInActions()
        stage.addActor(this)

        titleImage!!.remove()
        gameOverImage!!.remove()
        tableGameOver.remove()

        if (showMainMenu) {
            addActor(titleImage)
        } else {
            labelBestScore.setText(Settings.bestScore.toString() + " m")
            labelScore.setText(gameScreen.score.toString() + " m")

            addActor(gameOverImage)
            addActor(tableGameOver)
        }

        this.showMainMenu = showMainMenu
    }

    fun removeWithAnimations() {
        addOutActions()
        addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()))
    }

    companion object {
        const val ANIMATION_TIME: Float = .35f
    }
}
