package com.nopalsoft.donttap.dialogs

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.donttap.Assets
import com.nopalsoft.donttap.Settings
import com.nopalsoft.donttap.game.GameScreen
import com.nopalsoft.donttap.game.WorldGame
import com.nopalsoft.donttap.screens.MainMenuScreen
import com.nopalsoft.donttap.screens.Screens

class GameOverDialog(var screen: GameScreen) : Group() {
    var game = screen.game

    var oWorld: WorldGame = screen.worldGame

    init {

        setSize(430f, 460f)
        setOrigin(getWidth() / 2f, getHeight() / 2f)
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 160f)

        val background = Image(Assets.scoresBackgroundDrawable)
        background.setSize(getWidth(), getHeight())
        addActor(background)
        getColor().a = 0f

        val lbGameOver = Label("Game over!", Assets.labelStyleBlack)
        lbGameOver.setAlignment(Align.center)
        lbGameOver.setFontScale(1.15f)
        lbGameOver.setPosition(
            getWidth() / 2f - lbGameOver.getWidth() / 2f,
            400f
        )

        val txtMode: String?
        val txtScore: String?
        val txtNumScore: String?
        val txtBestNumScore: String


        when (screen.mode) {
            GameScreen.MODE_CLASSIC -> {
                txtMode = "Classic"
                txtScore = "Time"
                txtNumScore = game!!.formatter!!.format("%.1f", oWorld.time) + "s"
                txtBestNumScore = if (Settings.bestTimeClassicMode >= 100100) "X"
                else game!!.formatter!!.format("%.1f", Settings.bestTimeClassicMode) + "s"
            }

            GameScreen.MODE_TIME -> {
                txtMode = "Time"
                txtScore = "Tiles"
                txtNumScore = oWorld.score.toString() + ""
                txtBestNumScore = Settings.bestScoreTimeMode.toString() + ""
            }

            GameScreen.MODE_ENDLESS -> {
                txtMode = "Endless"
                txtScore = "Tiles"
                txtNumScore = oWorld.score.toString() + ""
                txtBestNumScore = Settings.bestScoreEndlessMode.toString() + ""
            }

            else -> {
                txtMode = "Endless"
                txtScore = "Tiles"
                txtNumScore = oWorld.score.toString() + ""
                txtBestNumScore = Settings.bestScoreEndlessMode.toString() + ""
            }
        }

        val lbMode = Label(txtMode, Assets.labelStyleBlack)
        lbMode.setAlignment(Align.center)
        lbMode.setPosition(getWidth() / 2f - lbMode.getWidth() / 2f, 365f)

        val scoreTable = Table()
        scoreTable.setSize(getWidth(), 130f)
        scoreTable.setY(230f)
        scoreTable.padLeft(15f).padRight(15f)

        // scoreTable.debug();

        // ACTUAL SCORE / TIME
        val lbScore = Label(txtScore, Assets.labelStyleBlack)
        lbScore.setAlignment(Align.left)

        val lblNumScore = Label(txtNumScore, Assets.labelStyleBlack)
        lblNumScore.setAlignment(Align.right)

        // BEST TIME
        val lbBestScore = Label("Best", Assets.labelStyleBlack)
        lbBestScore.setAlignment(Align.left)

        val lblNumBestScore = Label(
            txtBestNumScore,
            Assets.labelStyleBlack
        )
        lblNumBestScore.setAlignment(Align.right)

        scoreTable.defaults().padLeft(30f).padRight(30f)
        scoreTable.add<Label?>(lbScore).left()
        scoreTable.add<Label?>(lblNumScore).right().expand()

        scoreTable.row().padTop(20f)
        scoreTable.add<Label?>(lbBestScore).left()
        scoreTable.add<Label?>(lblNumBestScore).right().expand()

        val btShareTwitter = Button(Assets.buttonTwitter)
        btShareTwitter.setSize(55f, 55f)
        btShareTwitter.setPosition(140f, 160f)
        screen.addPressEffect(btShareTwitter)

        // Facebook Twitter
        val btShareFacebook = Button(Assets.buttonFacebook)
        btShareFacebook.setSize(55f, 55f)
        btShareFacebook.setPosition(235f, 160f)
        screen.addPressEffect(btShareFacebook)

        val btTryAgain = TextButton(
            "Try again",
            Assets.textButtonStyleSmall
        )
        btTryAgain
            .setPosition(getWidth() / 2f - btTryAgain.getWidth() / 2f, 90f)
        screen.addPressEffect(btTryAgain)
        btTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(
                    GameScreen::class.java, screen.game!!,
                    screen.mode
                )
            }
        })

        val btMainMenu = TextButton(
            "Main menu",
            Assets.textButtonStyleSmall
        )
        btMainMenu
            .setPosition(getWidth() / 2f - btMainMenu.getWidth() / 2f, 15f)
        screen.addPressEffect(btMainMenu)
        btMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(
                    MainMenuScreen::class.java,
                    screen.game!!
                )
            }
        })

        addActor(lbGameOver)
        addActor(lbMode)
        addActor(scoreTable)
        addAction(
            Actions.sequence(
                Actions.alpha(1f, fadeDuration),
                Actions.run {
                    addActor(btShareTwitter)
                    addActor(btShareFacebook)
                    addActor(btTryAgain)
                    addActor(btMainMenu)

                    btShareFacebook.remove()
                    btShareTwitter.remove()
                }
            )
        )
    }

    fun show(stage: Stage) {
        val dim = Image(Assets.blackPixel)
        dim.setFillParent(true)
        dim.getColor().a = 0f
        dim.addAction(Actions.alpha(.7f, fadeDuration - .5f))

        stage.addActor(dim)

        stage.addActor(this)
    }

    companion object {
        var fadeDuration: Float = 0.25f
    }
}
