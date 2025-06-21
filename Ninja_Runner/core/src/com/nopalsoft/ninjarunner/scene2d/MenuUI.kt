package com.nopalsoft.ninjarunner.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.game.GameWorld
import com.nopalsoft.ninjarunner.leaderboard.LeaderboardScreen
import com.nopalsoft.ninjarunner.screens.Screens
import com.nopalsoft.ninjarunner.screens.SettingsScreen
import com.nopalsoft.ninjarunner.shop.ShopScreen

class MenuUI(gameScreen: GameScreen, gameWorld: GameWorld?) : Group() {
    var gameScreen: GameScreen
    var oWorld: GameWorld?
    var titleImage: Image? = null

    var tableMenu: Table? = null

    var buttonPlay: Button? = null
    var buttonShop: Button? = null
    var buttonLeaderboard: Button? = null
    var buttonAchievements: Button? = null
    var buttonSettings: Button? = null
    var buttonRate: Button? = null
    var buttonShare: Button? = null

    var showMainMenu: Boolean = false

    init {
        setBounds(0f, 0f, Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        this.gameScreen = gameScreen
        this.oWorld = gameWorld

        init()
    }

    private fun init() {
        titleImage = Image(Assets.titleDrawable)
        titleImage!!.setScale(1f)
        titleImage!!.setPosition(getWidth() / 2f - titleImage!!.getWidth() * titleImage!!.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage!!.getHeight())

        tableMenu = Table()
        tableMenu!!.setSize(122f, getHeight())
        tableMenu!!.setBackground(Assets.backgroundMenu)

        initButtons()

        tableMenu!!.pad(25f, 20f, 10f, 0f)
        tableMenu!!.defaults().size(80f).padBottom(15f)

        tableMenu!!.row().colspan(2)
        tableMenu!!.add<Button?>(buttonShop)

        tableMenu!!.row().colspan(2)
        tableMenu!!.add<Button?>(buttonLeaderboard)

        tableMenu!!.row().colspan(2)
        tableMenu!!.add<Button?>(buttonAchievements)

        tableMenu!!.row().colspan(2)
        tableMenu!!.add<Button?>(buttonSettings)

        tableMenu!!.row().size(40f).padRight(5f).padLeft(5f)
        tableMenu!!.add<Button?>(buttonRate)
        tableMenu!!.add<Button?>(buttonShare)

        tableMenu!!.setPosition(Screens.SCREEN_WIDTH + tableMenu!!.getWidth(), 0f)

        addActor(tableMenu)
        addActor(buttonPlay)
    }

    fun initButtons() {
        buttonShop = Button(Assets.buttonShop, Assets.buttonShopPress)
        buttonLeaderboard = Button(Assets.buttonLeaderboard, Assets.buttonLeaderBoardPress)
        buttonAchievements = Button(Assets.buttonAchievement, Assets.buttonAchievementPress)
        buttonSettings = Button(Assets.buttonSettings, Assets.buttonSettingsPress)
        buttonRate = Button(Assets.buttonRate, Assets.buttonRatePress)
        buttonShare = Button(Assets.buttonShare, Assets.buttonSharePress)

        buttonPlay = Button(ButtonStyle(null, null, null))
        buttonPlay!!.setSize(getWidth() - tableMenu!!.getWidth(), getHeight())
        buttonPlay!!.setPosition(0f, 0f)
        buttonPlay!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (showMainMenu) gameScreen.setRunning(true)
                else {
                    gameScreen.game.setScreen(GameScreen(gameScreen.game, false))
                }
            }
        })

        buttonShop!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameScreen.changeScreenWithFadeOut(ShopScreen::class.java, gameScreen.game)
            }
        })

        buttonLeaderboard!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameScreen.game.setScreen(LeaderboardScreen(gameScreen.game))
            }
        })

        buttonSettings!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameScreen.changeScreenWithFadeOut(SettingsScreen::class.java, gameScreen.game)
            }
        })
    }

    private fun addInActions() {
        titleImage!!.addAction(Actions.moveTo(getWidth() / 2f - titleImage!!.getWidth() * titleImage!!.getScaleX() / 2f, 300f, ANIMATION_TIME))
        tableMenu!!.addAction(Actions.moveTo(Screens.SCREEN_WIDTH - tableMenu!!.getWidth(), 0f, ANIMATION_TIME))
    }

    private fun addOutActions() {
        titleImage!!.addAction(
            Actions.moveTo(
                getWidth() / 2f - titleImage!!.getWidth() * titleImage!!.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage!!.getHeight(),
                ANIMATION_TIME
            )
        )

        tableMenu!!.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + tableMenu!!.getWidth(), 0f, ANIMATION_TIME))
    }

    fun show(stage: Stage, showMainMenu: Boolean) {
        addInActions()
        stage.addActor(this)

        titleImage!!.remove()
        addActor(titleImage)
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
