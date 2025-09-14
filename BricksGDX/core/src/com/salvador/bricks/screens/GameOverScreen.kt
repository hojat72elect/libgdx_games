package com.salvador.bricks.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.TimeUtils
import com.salvador.bricks.BrickBreaker
import com.salvador.bricks.game_objects.Background
import com.salvador.bricks.game_objects.Constants.BUTTON_RESET
import com.salvador.bricks.game_objects.MenuButton
import com.salvador.bricks.game_objects.MenuText

class GameOverScreen(brickBreaker: BrickBreaker, private val score: Float) : GameClass(brickBreaker) {

    var scoreI = 1F
    var start = 0L
    private var stage: Stage? = null
    private var menuButton: MenuButton? = null
    private var scoreText: MenuText? = null

    override fun show() {
        stage = Stage()
        stage!!.setDebugAll(true)
        Gdx.input.inputProcessor = stage
        val camera = OrthographicCamera()
        stage!!.viewport.camera = camera
        camera.setToOrtho(false, 450F, 800F)
        val background = Background(0F, 0F)
        menuButton = MenuButton(BUTTON_RESET, 225F - 150, 250F, 300F, 90F)
        val titleText = MenuText("Game Over", "font.ttf", 600F)
        scoreText = MenuText("Score: " + "0", "font20.ttf", 500F)
        stage!!.addActor(background)
        stage!!.addActor(menuButton)
        stage!!.addActor(titleText)
        stage!!.addActor(scoreText)
    }

    override fun render(delta: Float) {
        stage!!.draw()
        stage!!.act()

        if (scoreI < score) {
            val diffInMillis = TimeUtils.timeSinceMillis(start)
            if (diffInMillis > 10) {
                scoreI = scoreI + 5
                scoreText!!.text = scoreI.toString()
                start = 0
            }
        }

        if (menuButton!!.touch) {
            menuButton!!.touch = false
            game.setScreen(GameScreen(game))
        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
