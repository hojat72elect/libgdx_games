package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.game.GameScreen
import com.nopalsoft.sokoban.screens.Screens

/**
 * The dialog that will be shown whenever you choose a game from the main screen.
 * This dialog shows the best number of  moves and the best time that the player
 * has had in that specific level.
 */
class DialogLevel(currentScreen: Screens) : Dialog(currentScreen, 350F, 300F, 100F) {

    private val buttonPlay = Button(Assets.btPlay, Assets.btPlayPress);
    private val labelBestMoves = Label("0", Label.LabelStyle(Assets.fontRed, Color.WHITE))
    private val labelBestTime = Label("0", Label.LabelStyle(Assets.fontRed, Color.WHITE))

    init {
        setCloseButton()
        setTitle("Puntuaciones", .75f)

        val tableMenu = Table()
        tableMenu.setFillParent(true)

        val imageClock = Image(Assets.clock)
        val imageMoves = Image(Assets.playerStand)

        tableMenu.defaults().expandX()

        tableMenu.padLeft(30f).padRight(30f).padBottom(20f).padTop(50f)
        tableMenu.add(imageMoves).size(45f)
        tableMenu.add(labelBestMoves)

        tableMenu.row().padTop(10f)
        tableMenu.add(imageClock).size(45f)
        tableMenu.add(labelBestTime)

        tableMenu.row().padTop(10f)
        tableMenu.add(buttonPlay).colspan(2).size(60f)

        addActor(tableMenu)
    }

    fun show(stage: Stage?, level: Int, bestMoves: Int, bestTime: Int) {
        labelBestMoves.setText(bestMoves.toString())
        labelBestTime.setText(bestTime.toString())

        buttonPlay.clear()
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(GameScreen::class.java, level, screen.game)
            }
        })

        super.show(stage)
    }
}