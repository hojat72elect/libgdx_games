package com.salvador.bricks.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.salvador.bricks.BrickBreaker
import com.salvador.bricks.game_objects.Background
import com.salvador.bricks.game_objects.Constants.BUTTON_EXIT
import com.salvador.bricks.game_objects.Constants.GAME_NAME
import com.salvador.bricks.game_objects.MenuButton
import com.salvador.bricks.game_objects.MenuText
import com.salvador.bricks.game_objects.ResourceManager.loadAssets

class InfoScreen(brickBreaker: BrickBreaker) : GameClass(brickBreaker) {

    private val credits = ArrayList<MenuText>()
    private var stage: Stage? = null
    private var exitButton: MenuButton? = null

    override fun show() {
        loadAssets()

        stage = Stage()
        Gdx.input.inputProcessor = stage
        val camera = OrthographicCamera()
        stage!!.viewport.camera = camera
        camera.setToOrtho(false, 450F, 800F)
        val background = Background(0F, 0F)
        exitButton = MenuButton(BUTTON_EXIT, 20F, 20F, 80F, 80F)

        credits.add(newTitle(GAME_NAME, 650F))
        credits.add(newTitle("Developer", 550F))
        credits.add(newTitle("Salvador Valverde", 500F))
        credits.add(newTitle("Graphics", 400F))
        credits.add(newTitle("Kenney", 350F))
        credits.add(newTitle("Audio", 250F))
        credits.add(newTitle("Nadie", 200F))

        stage!!.addActor(background)
        stage!!.addActor(exitButton)
        for (menuText in credits) {
            stage!!.addActor(menuText)
        }
    }

    fun newTitle(title: String, y: Float) = MenuText(title, "font20.ttf", y)

    override fun render(delta: Float) {
        stage!!.draw()
        stage!!.act()

        if (exitButton!!.touch) game.setScreen(MenuScreen(game))
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
