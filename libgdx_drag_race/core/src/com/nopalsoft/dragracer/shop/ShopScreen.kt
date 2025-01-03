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
import com.nopalsoft.dragracer.screens.BaseScreen

class ShopScreen(game: MainStreet) : BaseScreen(game) {
    private var buttonCharacters: Button? = null
    private var buttonPowerUps: Button? = null
    private var buttonCoins: Button? = null
    private var buttonNoAds: Button? = null
    private var buttonBack: Button? = null
    private var labelCoin: Label
    private var scrollPane: ScrollPane
    var container: Table

    init {
        val labelShop = Label("Shop", Assets.labelStyleLarge)
        labelShop.setSize(135f, 50f)
        labelShop.setPosition(3f, 747f)

        val imageCoin = Image(Assets.coinFront)

        labelCoin = Label("0", Assets.labelStyleLarge)
        labelCoin.setFontScale(.8f)

        val tableScores = Table()
        tableScores.width = SCREEN_WIDTH.toFloat()
        tableScores.setPosition(0f, SCREEN_HEIGHT - labelCoin.height / 2)
        tableScores.padLeft(5f).padRight(5f)

        tableScores.add(labelCoin).right().expand().padRight(5f)
        tableScores.add(imageCoin).right()

        val imageHorizontalSeparator = Image(Assets.horizontalSeparator)
        imageHorizontalSeparator.setSize(SCREEN_WIDTH.toFloat(), 5f)
        imageHorizontalSeparator.color = Color.LIGHT_GRAY
        imageHorizontalSeparator.setPosition(0f, 740f)

        val imageVerticalSeparator = Image(Assets.verticalSeparator)
        imageVerticalSeparator.setSize(5f, 745f)
        imageVerticalSeparator.color = Color.LIGHT_GRAY
        imageVerticalSeparator.setPosition(90f, 0f)

        initializeButtons()

        container = Table()

        scrollPane = ScrollPane(container, Assets.styleScrollPane)
        scrollPane.setSize((SCREEN_WIDTH - 95).toFloat(), (SCREEN_HEIGHT - 62).toFloat())
        scrollPane.setPosition(95f, 0f)

        stage.addActor(tableScores)
        stage.addActor(labelShop)
        stage.addActor(imageVerticalSeparator)
        stage.addActor(imageHorizontalSeparator)
        stage.addActor(buttonCharacters)

        stage.addActor(buttonCoins)
        stage.addActor(buttonNoAds)
        stage.addActor(buttonBack)
        stage.addActor(scrollPane)

        CharactersSubMenu(game, container)
    }

    private fun initializeButtons() {
        buttonCharacters = Button(TextureRegionDrawable(Assets.carTornado))
        buttonCharacters!!.setSize(45f, 65f)
        buttonCharacters!!.setPosition(23f, 660f)
        addPressEffect(buttonCharacters!!)
        buttonCharacters!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                CharactersSubMenu(game, container)
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
                GetCoinsSubMenu(game, container)
            }
        })

        buttonNoAds = Button(TextureRegionDrawable(Assets.buttonNoAds))
        buttonNoAds!!.setSize(55f, 55f)
        buttonNoAds!!.setPosition(17f, 390f)
        addPressEffect(buttonNoAds!!)
        buttonNoAds!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                NoAdsSubMenu(game, container)
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
