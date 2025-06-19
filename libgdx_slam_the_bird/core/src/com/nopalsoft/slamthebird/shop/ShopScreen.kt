package com.nopalsoft.slamthebird.shop

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.SlamTheBirdGame
import com.nopalsoft.slamthebird.game.GameScreen
import com.nopalsoft.slamthebird.screens.BaseScreen

class ShopScreen(game: SlamTheBirdGame) : BaseScreen(game) {
    private var buttonPlayers: Button? = null
    private var buttonPowerUps: Button? = null
    private var buttonCoins: Button? = null
    private var buttonNoAds: Button? = null
    private var buttonBack: Button? = null

    private var scrollPane: ScrollPane
    var tableContainer: Table

    init {
        val shop = Image(Assets.shop)
        shop.setSize(135f, 50f)
        shop.setPosition(3f, 747f)

        val horizontalSeparatorImage = Image(Assets.horizontalSeparator)
        horizontalSeparatorImage.setSize(SCREEN_WIDTH.toFloat(), 5f)
        horizontalSeparatorImage.color = Color.LIGHT_GRAY
        horizontalSeparatorImage.setPosition(0f, 740f)

        val verticalSeparatorImage = Image(Assets.verticalSeparator)
        verticalSeparatorImage.setSize(5f, 745f)
        verticalSeparatorImage.color = Color.LIGHT_GRAY
        verticalSeparatorImage.setPosition(90f, 0f)

        initButtons()

        tableContainer = Table()

        scrollPane = ScrollPane(tableContainer, Assets.styleScrollPane)
        scrollPane.setSize((SCREEN_WIDTH - 95).toFloat(), (SCREEN_HEIGHT - 62).toFloat())
        scrollPane.setPosition(95f, 0f)

        stage.addActor(shop)
        stage.addActor(verticalSeparatorImage)
        stage.addActor(horizontalSeparatorImage)
        stage.addActor(buttonPlayers)
        stage.addActor(buttonPowerUps)
        stage.addActor(buttonCoins)
        stage.addActor(buttonNoAds)
        stage.addActor(buttonBack)
        stage.addActor(scrollPane)

        PlayerSkinsSubMenu(game, tableContainer)

        buttonCoins!!.remove()
    }

    private fun initButtons() {
        buttonPlayers = Button(TextureRegionDrawable(Assets.defaultPlayerSkin))
        buttonPlayers!!.setSize(55f, 55f)
        buttonPlayers!!.setPosition(17f, 660f)
        addPressEffect(buttonPlayers)
        buttonPlayers!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                PlayerSkinsSubMenu(game, tableContainer)
            }
        })

        buttonPowerUps = Button(TextureRegionDrawable(Assets.boosts))
        buttonPowerUps!!.setSize(55f, 55f)
        buttonPowerUps!!.setPosition(17f, 570f)
        addPressEffect(buttonPowerUps)
        buttonPowerUps!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                UpgradesSubMenu(game, tableContainer)
            }
        })

        buttonCoins = Button(TextureRegionDrawable(Assets.coinsRegion))
        buttonCoins!!.setSize(55f, 55f)
        buttonCoins!!.setPosition(17f, 480f)
        addPressEffect(buttonCoins)
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
        addPressEffect(buttonNoAds)
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
        addPressEffect(buttonBack)
        buttonBack!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })
    }

    override fun draw(delta: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined

        batch.begin()
        batch.draw(Assets.coinsRegion, 449f, 764f, 30f, 34f)
        drawSmallScoreRightAligned(445f, 764f, Settings.currentCoins)
        batch.end()
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(tecleada: Int): Boolean {
        if (tecleada == Input.Keys.BACK || tecleada == Input.Keys.ESCAPE) {
            changeScreenWithFadeOut(GameScreen::class.java, game)
            return true
        }
        return false
    }
}
