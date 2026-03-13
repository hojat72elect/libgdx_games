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

class MainMenuScreen(game: MainStreet) : Screens(game) {

    private var titleImage: Image = Image(Assets.titleDrawable)
    private var labelShopScreen: Label = Label("Shop screen", Assets.labelStyleLarge)
    private var labelPlay: Label = Label("Play", Assets.labelStyleLarge)
    private var labelLeaderBoard: Label = Label("Leaderboard", Assets.labelStyleLarge)
    private var labelRate: Label = Label("Rate", Assets.labelStyleLarge)

    var buttonMusic: Button = Button(Assets.styleButtonMusic)

    init {
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.width / 2f, 520f)
        titleImage.color.a = 0f
        titleImage.addAction(
            Actions.sequence(
                Actions.fadeIn(.5f),
                Actions.run {
                    stage!!.addActor(labelPlay)
                    stage!!.addActor(labelRate)
                    stage!!.addActor(labelLeaderBoard)
                    stage!!.addActor(labelShopScreen)
                    stage!!.addActor(buttonMusic)
                })
        )


        labelPlay.setPosition(500f, 440f)
        labelPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })


        labelRate.setPosition(500f, 340f)

        labelShopScreen.setPosition(500f, 240f)
        labelShopScreen.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(ShopScreen::class.java, game)
            }
        })


        labelLeaderBoard.setPosition(500f, 140f)

        buttonMusic.setPosition(5f, 5f)
        buttonMusic.isChecked = !Settings.isMusicOn
        Gdx.app.log("Musica", Settings.isMusicOn.toString() + "")
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isMusicOn = !Settings.isMusicOn
                buttonMusic.isChecked = !Settings.isMusicOn
                if (Settings.isMusicOn) Assets.music?.play()
                else Assets.music?.stop()
                super.clicked(event, x, y)
            }
        })

        entranceAction(labelPlay, labelPlay.y, .25f)
        entranceAction(labelRate, labelRate.y, .5f)
        entranceAction(labelShopScreen, labelShopScreen.y, .75f)
        entranceAction(labelLeaderBoard, labelLeaderBoard.y, 1f)

        setAnimationChangeColor(labelShopScreen)
        setAnimationChangeColor(labelRate)
        setAnimationChangeColor(labelLeaderBoard)
        setAnimationChangeColor(labelPlay)

        stage!!.addActor(titleImage)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batch!!.begin()
        batch!!.draw(Assets.street!!, 0f, 0f, SCREEN_WIDTH.toFloat(), (SCREEN_HEIGHT * 2).toFloat())
        batch!!.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) Gdx.app.exit()
        return true
    }
}
