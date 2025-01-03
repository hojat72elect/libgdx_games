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
import com.nopalsoft.sharkadventure.game.WorldGame
import com.nopalsoft.sharkadventure.screens.BaseScreen

class MenuUI(gameScreen: GameScreen, worldGame: WorldGame) : Group() {
    var gameScreen: GameScreen
    private var worldGame: WorldGame
    private var imageTitle: Image? = null
    private var imageGameOver: Image? = null

    private var tableMenu: Table? = null
    private var tableGameOver: Table

    private var labelBestScore: Label
    private var labelScore: Label

    private var buttonPlay: Button? = null
    private var buttonLeaderboard: Button? = null
    private var buttonAchievements: Button? = null
    private var buttonFacebook: Button? = null
    private var buttonTwitter: Button? = null
    var buttonMusic: Button? = null
    var buttonSound: Button? = null

    var showMainMenu: Boolean = false

    init {
        setBounds(0f, 0f, BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT)
        this.gameScreen = gameScreen
        this.worldGame = worldGame

        initialize()

        tableGameOver = Table()
        tableGameOver.setSize(350f, 200f)
        tableGameOver.background = Assets.backgroundWindow
        tableGameOver.setPosition(width / 2f - tableGameOver.width / 2f, 110f)

        labelBestScore = Label("0", Assets.labelStyle)
        labelScore = Label("0", Assets.labelStyle)

        labelScore.setFontScale(.8f)
        labelBestScore.setFontScale(.8f)

        tableGameOver.pad(15f).padTop(30f).padBottom(50f)
        tableGameOver.defaults().expand()

        tableGameOver.add(Label("Score", Assets.labelStyle)).left()
        tableGameOver.add(labelScore).expandX().right()

        tableGameOver.row()
        tableGameOver.add(Label("Best score", Assets.labelStyle)).left()
        tableGameOver.add(labelBestScore).expandX().right()
    }

    private fun initialize() {
        imageTitle = Image(Assets.drawableTitle)
        imageTitle!!.setScale(1f)
        imageTitle!!.setPosition(width / 2f - imageTitle!!.width * imageTitle!!.scaleX / 2f, BaseScreen.SCREEN_HEIGHT + imageTitle!!.height)

        imageGameOver = Image(Assets.drawableGameOver)
        imageGameOver!!.setScale(1.25f)
        imageGameOver!!.setPosition(width / 2f - imageGameOver!!.width * imageGameOver!!.scaleX / 2f, BaseScreen.SCREEN_HEIGHT + imageGameOver!!.height)

        buttonFacebook = Button(Assets.buttonFacebook, Assets.buttonFacebookPress)
        buttonFacebook!!.setSize(60f, 60f)
        buttonFacebook!!.setPosition(BaseScreen.SCREEN_WIDTH + buttonFacebook!!.width, 410f)

        buttonTwitter = Button(Assets.buttonTwitter, Assets.buttonTwitterPress)
        buttonTwitter!!.setSize(60f, 60f)
        buttonTwitter!!.setPosition(BaseScreen.SCREEN_WIDTH + buttonTwitter!!.width, 410f)

        buttonMusic = Button(Assets.buttonMusicOff, Assets.buttonMusicOn, Assets.buttonMusicOn)
        buttonMusic!!.setSize(60f, 60f)
        buttonMusic!!.setPosition(-buttonMusic!!.width, 410f)

        buttonSound = Button(Assets.buttonSoundOff, Assets.buttonSoundOn, Assets.buttonSoundOn)
        buttonSound!!.setSize(60f, 60f)
        buttonSound!!.setPosition(-buttonSound!!.width, 325f)

        tableMenu = Table()
        tableMenu!!.background = Assets.backgroundMenu

        buttonPlay = Button(Assets.buttonRight, Assets.buttonRightPress)
        buttonLeaderboard = Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPress)
        buttonAchievements = Button(Assets.buttonAchievements, Assets.buttonAchievementsPress)

        tableMenu!!.defaults().size(90f).padBottom(20f).padLeft(10f).padRight(10f)
        if (Gdx.app.type != ApplicationType.WebGL) {
            tableMenu!!.setSize(385f, 85f)
            tableMenu!!.add(buttonPlay)
            tableMenu!!.add(buttonLeaderboard)
            tableMenu!!.add(buttonAchievements)
        } else {
            tableMenu!!.setSize(120f, 85f)
            tableMenu!!.add(buttonPlay)
        }
        tableMenu!!.setPosition(BaseScreen.SCREEN_WIDTH / 2f - tableMenu!!.width / 2f, -tableMenu!!.height)

        buttonFacebook!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.game.facebookHandler.showFacebook()
            }
        })

        buttonTwitter!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.game.reqHandler.shareOnTwitter("")
            }
        })

        buttonLeaderboard!!.addListener(object : ClickListener() {
        })

        buttonAchievements!!.addListener(object : ClickListener() {
        })

        buttonPlay!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.game.reqHandler.hideAdBanner()
                if (showMainMenu) gameScreen.setRunning(true)
                else {
                    gameScreen.game.screen = GameScreen(gameScreen.game, false)
                }
            }
        })

        buttonMusic!!.isChecked = Settings.isMusicOn
        buttonMusic!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                buttonMusic!!.isChecked = Settings.isMusicOn
                if (Settings.isMusicOn) Assets.music?.play()
                else Assets.music?.pause()
            }
        })

        buttonSound!!.isChecked = Settings.isSoundOn
        buttonSound!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                buttonSound!!.isChecked = Settings.isSoundOn
            }
        })

        addActor(tableMenu)
        addActor(buttonFacebook)
        addActor(buttonTwitter)
        addActor(buttonMusic)
        addActor(buttonSound)
    }

    private fun addInActions() {
        imageTitle!!.addAction(Actions.moveTo(width / 2f - imageTitle!!.width * imageTitle!!.scaleX / 2f, 300f, ANIMATION_TIME))
        imageGameOver!!.addAction(Actions.moveTo(width / 2f - imageGameOver!!.width * imageGameOver!!.scaleX / 2f, 320f, ANIMATION_TIME))

        tableMenu!!.addAction(Actions.moveTo(BaseScreen.SCREEN_WIDTH / 2f - tableMenu!!.width / 2f, 0f, ANIMATION_TIME))

        buttonFacebook!!.addAction(Actions.moveTo(735f, 410f, ANIMATION_TIME))
        buttonTwitter!!.addAction(Actions.moveTo(735f, 325f, ANIMATION_TIME))
        buttonMusic!!.addAction(Actions.moveTo(5f, 410f, ANIMATION_TIME))
        buttonSound!!.addAction(Actions.moveTo(5f, 325f, ANIMATION_TIME))
    }

    private fun addOutActions() {
        imageTitle!!.addAction(
            Actions.moveTo(
                width / 2f - imageTitle!!.width * imageTitle!!.scaleX / 2f, BaseScreen.SCREEN_HEIGHT + imageTitle!!.height,
                ANIMATION_TIME
            )
        )
        imageGameOver!!.addAction(
            Actions.moveTo(
                width / 2f - imageGameOver!!.width * imageGameOver!!.scaleX / 2f,
                BaseScreen.SCREEN_HEIGHT + imageGameOver!!.height, ANIMATION_TIME
            )
        )

        tableMenu!!.addAction(Actions.moveTo(BaseScreen.SCREEN_WIDTH / 2f - tableMenu!!.width / 2f, -tableMenu!!.height, ANIMATION_TIME))

        buttonFacebook!!.addAction(Actions.moveTo(BaseScreen.SCREEN_WIDTH + buttonFacebook!!.width, 410f, ANIMATION_TIME))
        buttonTwitter!!.addAction(Actions.moveTo(BaseScreen.SCREEN_WIDTH + buttonTwitter!!.width, 325f, ANIMATION_TIME))
        buttonMusic!!.addAction(Actions.moveTo(-buttonMusic!!.width, 410f, ANIMATION_TIME))
        buttonSound!!.addAction(Actions.moveTo(-buttonSound!!.width, 325f, ANIMATION_TIME))
    }

    fun show(stage: Stage, showMainMenu: Boolean) {
        addInActions()
        stage.addActor(this)

        imageTitle!!.remove()
        imageGameOver!!.remove()
        tableGameOver.remove()

        if (showMainMenu) {
            addActor(imageTitle)
        } else {
            labelBestScore.setText(Settings.bestScore.toString() + " m")
            labelScore.setText(gameScreen.punctuation.toString() + " m")

            addActor(imageGameOver)
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
