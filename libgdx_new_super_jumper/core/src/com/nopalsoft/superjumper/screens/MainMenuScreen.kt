package com.nopalsoft.superjumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.Settings
import com.nopalsoft.superjumper.SuperJumperGame
import com.nopalsoft.superjumper.game.GameScreen

class MainMenuScreen(game: SuperJumperGame) : Screens(game) {
    var imageTitle: Image = Image(Assets.title)

    var buttonShop: TextButton
    var buttonPlay: TextButton
    var buttonLeaderBoard: TextButton
    var buttonRate: TextButton
    var labelBestScore: Label = Label("Best score " + Settings.bestScore, Assets.labelStyleSmall)

    init {
        imageTitle.setPosition(SCREEN_WIDTH / 2f - imageTitle.width / 2f, 800f)

        imageTitle.addAction(Actions.sequence(Actions.moveTo(imageTitle.x, 600f, 1f, Interpolation.bounceOut), Actions.run {
            stage!!.addActor(
                labelBestScore
            )
        }))


        labelBestScore.setPosition(SCREEN_WIDTH / 2f - labelBestScore.width / 2f, 570f)
        labelBestScore.color.a = 0f
        labelBestScore.addAction(Actions.alpha(1f, .25f))

        buttonPlay = TextButton("Play", Assets.textButtonStyleLarge)
        buttonPlay.setPosition(SCREEN_WIDTH / 2f - buttonPlay.width / 2f, 440f)
        buttonPlay.pad(10f)
        buttonPlay.pack()
        addPressEffect(buttonPlay)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        buttonShop = TextButton("Shop", Assets.textButtonStyleLarge)
        buttonShop.setPosition(SCREEN_WIDTH / 2f - buttonShop.width / 2f, 340f)
        buttonShop.pad(10f)
        buttonShop.pack()
        addPressEffect(buttonShop)

        buttonRate = TextButton("Rate", Assets.textButtonStyleLarge)
        buttonRate.setPosition(SCREEN_WIDTH / 2f - buttonRate.width / 2f, 340f)
        buttonRate.pad(10f)
        buttonRate.pack()
        addPressEffect(buttonRate)

        buttonLeaderBoard = TextButton("Leaderboard", Assets.textButtonStyleLarge)
        buttonLeaderBoard.pad(10f)
        buttonLeaderBoard.pack()
        buttonLeaderBoard.setPosition(SCREEN_WIDTH / 2f - buttonLeaderBoard.width / 2f, 240f)

        addPressEffect(buttonLeaderBoard)

        stage!!.addActor(imageTitle)
        stage!!.addActor(buttonPlay)
        stage!!.addActor(buttonRate)
        stage!!.addActor(buttonLeaderBoard)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batch!!.begin()
        batch!!.draw(Assets.background, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batch!!.draw(Assets.platformBeigeBroken, 100f, 100f, 125f, 45f)
        batch!!.draw(Assets.platformBlue, 350f, 280f, 125f, 45f)
        batch!!.draw(Assets.platformRainbow, 25f, 430f, 125f, 45f)
        batch!!.draw(Assets.playerJump, 25f, 270f, 75f, 80f)
        batch!!.draw(Assets.happyCloud, 350f, 500f, 95f, 60f)
        batch!!.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            Gdx.app.exit()
        }
        return super.keyDown(keycode)
    }
}
