package com.salvai.centrum.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.salvai.centrum.CentrumGameClass
import com.salvai.centrum.levels.LevelReader
import com.salvai.centrum.utils.Constants

class SplashScreen(private val game: CentrumGameClass) : ScreenAdapter() {
    var levelReader: LevelReader
    private val splashTexture: Texture
    private val splashSprite: Sprite
    private var countdownTime: Int
    private var levelLoadCount: Int

    init {
        splashTexture = game.assetsManager.manager.get<Texture>(Constants.SPLASH_IMAGE_NAME, Texture::class.java)
        splashSprite = Sprite(splashTexture)
        splashSprite.setScale(0.9f)
        splashSprite.setPosition(Constants.WIDTH_CENTER - splashSprite.getWidth() * 0.5f, Constants.HEIGHT_CENTER - splashSprite.getHeight() * 0.5f)
        splashSprite.setAlpha(1f)
        countdownTime = 40
        levelLoadCount = 1
        levelReader = LevelReader()
    }

    override fun render(delta: Float) {
        setupScreen()
        game.batch.begin()

        if (countdownTime == 0) {
            game.skin = game.assetsManager.manager.get<Skin?>(Constants.SKIN_FILE_NAME, Skin::class.java)
            game.setScreen(MenuScreen(game))
            dispose()
        }

        if (game.assetsManager.manager.update()) {
            if (levelLoadCount <= Constants.MAX_LEVEL) loadLevels(levelLoadCount)
            else {
                countdownTime = (countdownTime - delta).toInt()
                splashSprite.setAlpha(countdownTime.toFloat() / 40f)
            }
        }
        game.drawBackground(delta)
        splashSprite.draw(game.batch)
        game.batch.end()
    }

    private fun loadLevels(levelLoadCount: Int) {
        game.levels.add(levelReader.loadLevel(levelLoadCount))
        this.levelLoadCount++
    }


    private fun setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.camera.update()
        game.batch.setProjectionMatrix(game.camera.combined)
    }


    override fun resize(width: Int, height: Int) {
        // change the stage's viewport when teh screen size is changed
        game.viewport.update(width, height, true)
    }
}
