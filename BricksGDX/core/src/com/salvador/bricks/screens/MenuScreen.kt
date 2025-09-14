package com.salvador.bricks.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.salvador.bricks.BrickBreaker
import com.salvador.bricks.game_objects.Background
import com.salvador.bricks.game_objects.Constants.BUTTON_EXIT
import com.salvador.bricks.game_objects.Constants.BUTTON_INFO
import com.salvador.bricks.game_objects.Constants.BUTTON_PLAY
import com.salvador.bricks.game_objects.Constants.GAME_NAME
import com.salvador.bricks.game_objects.Constants.SCREEN_WIDTH
import com.salvador.bricks.game_objects.MenuButton
import com.salvador.bricks.game_objects.MenuText
import com.salvador.bricks.game_objects.ResourceManager.disposeAssets
import com.salvador.bricks.game_objects.ResourceManager.loadAssets

class MenuScreen(brickBreaker: BrickBreaker) : GameClass(brickBreaker) {

    private var stage: Stage? = null
    private var playButton: MenuButton? = null
    private var exitButton: MenuButton? = null
    private var infoButton: MenuButton? = null

    override fun show() {

        loadAssets()
        stage = Stage()
        Gdx.input.inputProcessor = stage
        val camera = OrthographicCamera()
        stage!!.viewport.camera = camera
        camera.setToOrtho(false, 450F, 800F)
        val background = Background(0F, 0F)
        playButton = MenuButton(BUTTON_PLAY, 225F - 150, 250F, 300F, 90F)
        exitButton = MenuButton(BUTTON_EXIT, 20F, 20F, 80F, 80F)
        infoButton = MenuButton(BUTTON_INFO, SCREEN_WIDTH - 100F, 20F, 80F, 80F)

        val titleText = MenuText(GAME_NAME, "font.ttf", 550F)
        stage!!.addActor(background)
        stage!!.addActor(playButton)
        stage!!.addActor(exitButton)
        stage!!.addActor(infoButton)
        stage!!.addActor(titleText)
    }

    override fun render(delta: Float) {
        stage!!.draw()
        stage!!.act()

        if (playButton!!.touch) {
            playButton!!.touch = false
            game.setScreen(GameScreen(game))
        }
        if (exitButton!!.touch) {
            exitButton!!.touch = false
            disposeAssets()
            Gdx.app.exit()
        }
        if (infoButton!!.touch) {
            infoButton!!.touch = false
            game.setScreen(InfoScreen(game))
        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
