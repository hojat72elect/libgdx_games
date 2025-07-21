package com.nopalsoft.clumsy.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.clumsy.Assets
import com.nopalsoft.clumsy.ClumsyUfoGame
import com.nopalsoft.clumsy.Settings
import com.nopalsoft.clumsy.game.arcade.GameScreenArcade
import com.nopalsoft.clumsy.game.classic.ClassicGameScreen
import com.nopalsoft.clumsy.objects.Ufo

class MainMenuScreen(game: ClumsyUfoGame) : Screens(game) {
    var player: Ufo

    var titleImage: Image

    var buttonPlayClassic: Button
    var buttonPlayArcade: Button
    var buttonScore: Button
    var buttonRate: Button
    var buttonRestorePurchases: Button
    var buttonNoAds: Button

    init {
        player = Ufo(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f + 50)

        titleImage = Image(Assets.appTitle)
        titleImage.setSize(321f, 156f)
        titleImage.setPosition(SCREEN_WIDTH / 2f - 321 / 2f, 500f)

        buttonPlayClassic = Button(TextureRegionDrawable(Assets.buttonPlayClassic))
        buttonPlayClassic.setSize(160f, 95f)
        buttonPlayClassic.setPosition(75f, 280f)
        buttonPlayClassic.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonPlayClassic.setPosition(75f, 277f)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                buttonPlayClassic.setPosition(75f, 280f)
                Assets.playSound(Assets.swooshing)
                changeScreenWithFadeOut(ClassicGameScreen::class.java, game)
            }
        })

        buttonPlayArcade = Button(TextureRegionDrawable(Assets.buttonPlayArcade))
        buttonPlayArcade.setSize(160f, 95f)
        buttonPlayArcade.setPosition(250f, 280f)
        buttonPlayArcade.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonPlayArcade.setPosition(250f, 277f)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                buttonPlayArcade.setPosition(250f, 280f)
                Assets.playSound(Assets.swooshing)
                changeScreenWithFadeOut(GameScreenArcade::class.java, game)
            }
        })

        buttonScore = Button(TextureRegionDrawable(Assets.buttonLeaderboard))
        buttonScore.setSize(160f, 95f)
        buttonScore.setPosition(160f, 180f)
        buttonScore.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonScore.setPosition(160f, 177f)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                buttonScore.setPosition(160f, 180f)
            }
        })

        buttonRate = Button(TextureRegionDrawable(Assets.buttonRate))
        buttonRate.setSize(60f, 60f)
        buttonRate.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonRate.setPosition(buttonRate.getX(), buttonRate.getY() - 3)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                buttonRate.setPosition(buttonRate.getX(), buttonRate.getY() + 3)
            }
        })

        buttonNoAds = Button(TextureRegionDrawable(Assets.buttonNoAds))
        if (Settings.didBuyNoAds) buttonNoAds.isVisible = false
        buttonNoAds.setSize(60f, 60f)
        buttonNoAds.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonNoAds.setPosition(buttonNoAds.getX(), buttonNoAds.getY() - 3)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                buttonNoAds.setPosition(buttonNoAds.getX(), buttonNoAds.getY() + 3)
            }
        })

        buttonRestorePurchases = Button(TextureRegionDrawable(Assets.buttonRestorePurchases))
        buttonRestorePurchases.setSize(60f, 60f)
        buttonRestorePurchases.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                buttonRestorePurchases.setPosition(buttonRestorePurchases.getX(), buttonRestorePurchases.getY() - 3)
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                buttonRestorePurchases.setPosition(buttonRestorePurchases.getX(), buttonRestorePurchases.getY() + 3)
            }
        })

        val bottomMenu = Table()
        bottomMenu.setPosition(1f, 1f)
        bottomMenu.defaults().padRight(2.5f)

        bottomMenu.add<Button?>(buttonRate)
        bottomMenu.add<Button?>(buttonRestorePurchases)
        bottomMenu.add<Button?>(buttonNoAds)
        bottomMenu.pack()

        stage.addActor(bottomMenu)

        stage.addActor(buttonScore)
        stage.addActor(buttonPlayClassic)
        stage.addActor(buttonPlayArcade)
        stage.addActor(titleImage)
    }

    override fun draw(delta: Float) {
        batch.begin()
        batch.draw(Assets.background0, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batch.end()

        Assets.parallaxBackground.render(delta)

        camera.update()
        batch.setProjectionMatrix(camera.combined)

        batch.enableBlending()
        batch.begin()

        player.update(delta, null)
        batch.draw(
            Assets.bird.getKeyFrame(player.stateTime, true), player.position.x - 27, player.position.y - 20, 58f,
            40f
        )
        batch.end()
    }

    override fun update(delta: Float) {
        if (Settings.didBuyNoAds) buttonNoAds.isVisible = false
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) Gdx.app.exit()
        return false
    }
}
