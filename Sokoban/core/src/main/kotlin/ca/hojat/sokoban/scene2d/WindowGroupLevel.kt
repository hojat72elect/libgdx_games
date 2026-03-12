package ca.hojat.sokoban.scene2d

import ca.hojat.sokoban.Assets
import ca.hojat.sokoban.game.GameScreen
import ca.hojat.sokoban.screens.Screens
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class WindowGroupLevel(currentScreen: Screens) : WindowGroup(currentScreen, 350f, 300f, 100f) {
    private var buttonPlay = Button(Assets.buttonPlayDrawable, Assets.buttonPlayPressedDrawable)
    private var labelBestMoves = Label("0", LabelStyle(Assets.fontRed, Color.WHITE))
    private var labelBestTime = Label("0", LabelStyle(Assets.fontRed, Color.WHITE))

    init {
        setCloseButton()
        setTitle("Puntuaciones", .75f)

        val tableMenu = Table()
        tableMenu.setFillParent(true)

        val imageClock = Image(Assets.clockDrawable)
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

    fun show(stage: Stage, level: Int, bestMoves: Int, bestTime: Int) {
        labelBestMoves.setText(bestMoves.toString() + "")
        labelBestTime.setText(bestTime.toString() + "")

        buttonPlay.clear()
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(GameScreen::class.java, level, screen.game)
            }
        })

        super.show(stage)
    }
}
