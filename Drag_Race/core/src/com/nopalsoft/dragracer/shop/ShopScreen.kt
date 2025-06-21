package com.nopalsoft.dragracer.shop

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.MainStreet
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.screens.MainMenuScreen
import com.nopalsoft.dragracer.screens.Screens

class ShopScreen(game: MainStreet) : Screens(game) {
    private var buttonPlayers: Button? = null
    private var buttonPowerUps: Button? = null
    private var buttonCoins: Button? = null
    private var buttonNoAds: Button? = null
    private var buttonBack: Button? = null

    private var labelCoin: Label

    private var scroll: ScrollPane
    var tableContainer: Table

    init {
        val labelShop = Label("Shop", Assets.labelStyleLarge)
        labelShop.setSize(135f, 50f)
        labelShop.setPosition(3f, 747f)

        val coinImage = Image(Assets.coinFront)

        labelCoin = Label("0", Assets.labelStyleLarge)
        labelCoin.setFontScale(.8f)

        val tbScores = Table()
        tbScores.width = SCREEN_WIDTH.toFloat()
        tbScores.setPosition(0f, SCREEN_HEIGHT - labelCoin.height / 2)
        tbScores.padLeft(5f).padRight(5f)

        tbScores.add(labelCoin).right().expand().padRight(5f)
        tbScores.add(coinImage).right()

        val horizontalSeparatorImage = Image(Assets.horizontalSeparatorDrawable)
        horizontalSeparatorImage.setSize(SCREEN_WIDTH.toFloat(), 5f)
        horizontalSeparatorImage.color = Color.LIGHT_GRAY
        horizontalSeparatorImage.setPosition(0f, 740f)

        val verticalSeparatorImage = Image(Assets.verticalSeparatorDrawable)
        verticalSeparatorImage.setSize(5f, 745f)
        verticalSeparatorImage.color = Color.LIGHT_GRAY
        verticalSeparatorImage.setPosition(90f, 0f)

        initializeButtons()

        tableContainer = Table()
        scroll = ScrollPane(tableContainer, Assets.styleScrollPane)
        scroll.setSize((SCREEN_WIDTH - 95).toFloat(), (SCREEN_HEIGHT - 62).toFloat())
        scroll.setPosition(95f, 0f)

        stage?.addActor(tbScores)
        stage?.addActor(labelShop)
        stage?.addActor(verticalSeparatorImage)
        stage?.addActor(horizontalSeparatorImage)
        stage?.addActor(buttonPlayers)

        stage?.addActor(buttonCoins)
        stage?.addActor(buttonNoAds)
        stage?.addActor(buttonBack)
        stage?.addActor(scroll)

        PlayerSubMenu(game, tableContainer)
    }

    private fun initializeButtons() {
        buttonPlayers = Button(
            TextureRegionDrawable(Assets.carTornado)
        )
        buttonPlayers!!.setSize(45f, 65f)
        buttonPlayers!!.setPosition(23f, 660f)
        addPressEffect(buttonPlayers!!)
        buttonPlayers!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                PlayerSubMenu(game, tableContainer)
            }
        })

        buttonPowerUps = Button(TextureRegionDrawable(Assets.carTornado))
        buttonPowerUps!!.setSize(55f, 55f)
        buttonPowerUps!!.setPosition(17f, 570f)
        addPressEffect(buttonPowerUps!!)

        buttonCoins = Button(TextureRegionDrawable(Assets.coinFront))
        buttonCoins!!.setSize(55f, 55f)
        buttonCoins!!.setPosition(17f, 480f)
        addPressEffect(buttonCoins!!)
        buttonCoins!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                GetCoinsSubMenu(game, tableContainer)
            }
        })

        buttonNoAds = Button(TextureRegionDrawable(Assets.buttonNoAds))
        buttonNoAds!!.setSize(55f, 55f)
        buttonNoAds!!.setPosition(17f, 390f)
        addPressEffect(buttonNoAds!!)
        buttonNoAds!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                NoAdsSubMenu(game, tableContainer)
            }
        })

        buttonBack = Button(TextureRegionDrawable(Assets.buttonBack))
        buttonBack!!.setSize(55f, 55f)
        buttonBack!!.setPosition(17f, 10f)
        addPressEffect(buttonBack!!)
        buttonBack!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })
    }

    override fun draw(delta: Float) {
    }

    override fun update(delta: Float) {
        labelCoin.setText(Settings.coinsTotal.toString() + "")
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            return true
        }
        return false
    }
}
