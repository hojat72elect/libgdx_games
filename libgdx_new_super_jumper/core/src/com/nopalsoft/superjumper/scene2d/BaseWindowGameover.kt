package com.nopalsoft.superjumper.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.Settings
import com.nopalsoft.superjumper.game.GameScreen
import com.nopalsoft.superjumper.game.WorldGame
import com.nopalsoft.superjumper.screens.MainMenuScreen

class BaseWindowGameover(currentScreen: GameScreen) : BaseWindow(currentScreen, 350f, 400f, 250f) {
    private var buttonMenu: TextButton? = null
    private var buttonTryAgain: TextButton? = null
    private var worldGame: WorldGame = currentScreen.oWorld

    init {
        val labelShop = Label("Game over!", Assets.labelStyleLarge)
        labelShop.setFontScale(1.5f)
        labelShop.setAlignment(Align.center)
        labelShop.setPosition(width / 2f - labelShop.width / 2f, 350f)
        addActor(labelShop)

        initializeButtons()

        val scoreTable = Table()
        scoreTable.setSize(width, 130f)
        scoreTable.y = 230f

        val labelScore = Label("Score", Assets.labelStyleSmall)
        labelScore.setAlignment(Align.left)

        val labelNumScore = Label(worldGame.maxDistance.toString() + "", Assets.labelStyleSmall)
        labelNumScore.setAlignment(Align.right)

        val labelBestScore = Label("Best Score", Assets.labelStyleSmall)
        labelScore.setAlignment(Align.left)

        val labelBestNumScore = Label(Settings.bestScore.toString() + "", Assets.labelStyleSmall)
        labelNumScore.setAlignment(Align.right)

        scoreTable.pad(10f)
        scoreTable.add(labelScore).left()
        scoreTable.add(labelNumScore).right().expand()

        scoreTable.row()
        scoreTable.add(labelBestScore).left()
        scoreTable.add(labelBestNumScore).right().expand()

        addActor(scoreTable)

        val content = Table()

        content.defaults().expandX().uniform().fill()

        content.add(buttonTryAgain)
        content.row().padTop(20f)
        content.add(buttonMenu)

        content.pack()
        content.setPosition(width / 2f - content.width / 2f, 60f)

        addActor(content)
    }

    private fun initializeButtons() {
        buttonMenu = TextButton("Menu", Assets.textButtonStyleLarge)
        buttonMenu!!.pad(15f)

        screen.addPressEffect(buttonMenu!!)
        buttonMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        buttonTryAgain = TextButton("Try again", Assets.textButtonStyleLarge)
        buttonTryAgain!!.pad(15f)

        screen.addPressEffect(buttonTryAgain!!)
        buttonTryAgain!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })
    }

}
