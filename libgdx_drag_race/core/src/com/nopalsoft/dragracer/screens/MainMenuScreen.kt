package com.nopalsoft.dragracer.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.MainStreet
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.game.GameScreen
import com.nopalsoft.dragracer.shop.ShopScreen

class MainMenuScreen(game: MainStreet) : BaseScreen(game) {

    private var imageTitle = Image(Assets.title)
    private var labelShopScreen = Label("Shop screen", Assets.labelStyleLarge)
    private var labelPlay = Label("Play", Assets.labelStyleLarge)
    private var labelLeaderboard = Label("Leaderboard", Assets.labelStyleLarge)
    private var labelRate = Label("Rate", Assets.labelStyleLarge)

    var buttonMusic = Button(Assets.styleButtonMusic)

    init {


        imageTitle.setPosition(SCREEN_WIDTH / 2f - imageTitle.width / 2f, 520f)
        imageTitle.color.a = 0f
        imageTitle.addAction(
            Actions.sequence(
                Actions.fadeIn(.5f),
                Actions.run {
                    stage.addActor(labelPlay)
                    stage.addActor(labelRate)
                    stage.addActor(labelLeaderboard)
                    stage.addActor(labelShopScreen)
                    stage.addActor(buttonMusic)
                })
        )


        labelPlay.setPosition(500f, 440f)
        labelPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })


        labelRate.setPosition(500f, 340f)
        labelRate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {}
        })


        labelShopScreen.setPosition(500f, 240f)
        labelShopScreen.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(ShopScreen::class.java, game)
            }
        })


        labelLeaderboard.setPosition(500f, 140f)
        labelLeaderboard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {

            }
        })


        buttonMusic.setPosition(5f, 5f)
        buttonMusic.isChecked = Settings.isMusicOn.not()
        Gdx.app.log("Music", Settings.isMusicOn.toString())
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isMusicOn = Settings.isMusicOn.not()
                buttonMusic.isChecked = Settings.isMusicOn.not()
                if (Settings.isMusicOn) Assets.music.play()
                else Assets.music.stop()
                super.clicked(event, x, y)
            }
        })

        entranceAction(labelPlay, labelPlay.y, .25f)
        entranceAction(labelRate, labelRate.y, .5f)
        entranceAction(labelShopScreen, labelShopScreen.y, .75f)
        entranceAction(labelLeaderboard, labelLeaderboard.y, 1f)

        setAnimationChangeColor(labelShopScreen)
        setAnimationChangeColor(labelRate)
        setAnimationChangeColor(labelLeaderboard)
        setAnimationChangeColor(labelPlay)

        stage.addActor(imageTitle)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batcher.begin()
        batcher.draw(Assets.street, 0F, 0F, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT * 2F)
        batcher.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) Gdx.app.exit()
        return true
    }
}
