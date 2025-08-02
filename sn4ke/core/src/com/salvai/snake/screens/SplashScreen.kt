package com.salvai.snake.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.salvai.snake.SnakeIt
import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.levels.LevelReader
import com.salvai.snake.utils.Constants

class SplashScreen(var game: SnakeIt) : ScreenAdapter() {
    var splashSprite: Sprite
    private var countdownTime: Int
    private var toSetUp = true

    init {
        splashSprite = Sprite(game.assetsManager.manager.get<Texture?>(Constants.APPLE_IMAGE_NAME, Texture::class.java))
        splashSprite.setSize(game.worldWidth * 0.6f, game.worldWidth * 0.6f)
        splashSprite.setPosition(game.worldWidth * 0.2f, game.worldHeight * 0.5f - splashSprite.getHeight() * 0.3f)
        splashSprite.setAlpha(1f)
        countdownTime = 41

        game.assetsManager.loadSkin()
        game.assetsManager.loadImages()
    }


    override fun render(delta: Float) {
        setupScreen()
        game.drawBackground(delta, MovingDirection.UP)

        game.backgroundStage.batch.begin()
        splashSprite.draw(game.backgroundStage.batch)
        game.backgroundStage.batch.end()

        if (game.assetsManager.manager.update()) {
            //load levels first
            if (countdownTime < 41) {
                countdownTime = (countdownTime - delta).toInt()
                splashSprite.setAlpha(countdownTime.toFloat() / 40f)
                if (countdownTime == 0) {
                    if (game.firstTimeOpen) {
                        game.setScreen(GameScreen(game))
                        dispose()
                    } else {
                        game.setScreen(MenuScreen(game))
                        dispose()
                    }
                }
            } else {
                if (toSetUp) {
                    game.skin = game.assetsManager.manager.get<Skin?>(Constants.SKIN_FILE_NAME, Skin::class.java)
                    game.setUpTopBar(Constants.SCREEN.MENU)

                    val levelReader = LevelReader()
                    game.levels = levelReader.loadAllLevels()
                    toSetUp = false
                }
                countdownTime -= 1
            }
        }
    }


    private fun setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.camera.update()
    }


    override fun resize(width: Int, height: Int) {
        // change the stage's viewport when teh screen size is changed
        game.viewport.update(width, height, true)
    }
}
