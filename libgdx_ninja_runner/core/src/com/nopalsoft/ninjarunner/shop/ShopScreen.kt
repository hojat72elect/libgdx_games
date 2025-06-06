package com.nopalsoft.ninjarunner.shop

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.NinjaRunnerGame
import com.nopalsoft.ninjarunner.Settings
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.screens.Screens

class ShopScreen(game: NinjaRunnerGame) : Screens(game) {
    var tbMenu: Table
    var buttonPlayer: Button? = null
    var buttonMascot: Button? = null
    var buttonUpgrade: Button? = null
    var buttonNoAds: Button? = null
    var buttonMore: Button? = null

    var scroll: ScrollPane
    var tableContainer: Table

    var labelCoins: Label

    init {
        val lbShop = Label("Shop", Assets.labelStyleLarge)

        val tbTitle = Table()
        tbTitle.setSize(400f, 100f)
        tbTitle.setPosition(SCREEN_WIDTH / 2f - tbTitle.getWidth() / 2f, SCREEN_HEIGHT - tbTitle.getHeight())
        tbTitle.setBackground(Assets.backgroundTitleShop)
        tbTitle.padTop(20f).padBottom(5f)

        // tbTitle.debugAll();
        tbTitle.row().colspan(2)
        tbTitle.add<Label?>(lbShop).expand()
        tbTitle.row()

        val imgGem = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        imgGem.setSize(20f, 20f)

        labelCoins = Label("x0", Assets.labelStyleSmall)

        tbTitle.add<Image?>(imgGem).size(20f).right()
        tbTitle.add<Label?>(labelCoins).padLeft(5f).left()

        initButtons()

        tbMenu = Table()
        tbMenu.defaults().size(58f).padBottom(8f)

        tbMenu.row()
        tbMenu.add<Button?>(buttonPlayer)

        tbMenu.row()
        tbMenu.add<Button?>(buttonMascot)

        tbMenu.row()
        tbMenu.add<Button?>(buttonUpgrade)

        tbMenu.row()
        tbMenu.add<Button?>(buttonNoAds)

        tbMenu.row()
        tbMenu.add<Button?>(buttonMore)

        val tbShop = Table()
        tbShop.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT - tbTitle.getHeight())
        tbShop.setBackground(Assets.backgroundShop)
        tbShop.pad(25f, 5f, 15f, 5f)

        // Container for the shop content
        tableContainer = Table()

        scroll = ScrollPane(tableContainer, ScrollPaneStyle(null, null, null, null, null))
        scroll.setFadeScrollBars(false)
        scroll.setSize(SCREEN_WIDTH - tbMenu.getWidth(), 420f)
        scroll.setPosition(tbMenu.getWidth() + 1, 0f)
        scroll.variableSizeKnobs = false

        tbShop.add<Table?>(tbMenu).expandY().width(122f)
        tbShop.add<ScrollPane?>(scroll).expand().fill()

        stage.addActor(tbTitle)
        stage.addActor(tbShop)

        PlayersSubMenu(tableContainer, game)
        buttonPlayer!!.setChecked(true)
    }

    fun initButtons() {
        buttonPlayer = Button(Assets.buttonShop, Assets.buttonShopPress, Assets.buttonShopPress)
        buttonMascot = Button(Assets.buttonLeaderboard, Assets.buttonLeaderBoardPress, Assets.buttonLeaderBoardPress)
        buttonUpgrade = Button(Assets.buttonAchievement, Assets.buttonAchievementPress, Assets.buttonLeaderBoardPress)
        buttonNoAds = Button(Assets.buttonSettings, Assets.buttonSettingsPress, Assets.buttonLeaderBoardPress)
        buttonMore = Button(Assets.buttonRate, Assets.buttonSettingsPress)

        buttonPlayer!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                PlayersSubMenu(tableContainer, game)
            }
        })

        buttonMascot!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                MascotsSubMenu(tableContainer, game)
            }
        })

        buttonUpgrade!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UpgradesSubMenu(tableContainer, game)
            }
        })

        val radioGroup = ButtonGroup<Button?>()
        radioGroup.add(buttonPlayer, buttonMascot, buttonUpgrade, buttonNoAds)
    }

    override fun draw(delta: Float) {
        Assets.cloudsParallaxBackground!!.render(0f)
    }

    override fun update(delta: Float) {
        labelCoins.setText("x" + Settings.totalCoins)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(GameScreen::class.java, game)
            return true
        }
        return super.keyUp(keycode)
    }

    override fun hide() {
    }
}
