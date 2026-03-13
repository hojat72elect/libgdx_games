package com.salvai.centrum.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.salvai.centrum.CentrumGameClass
import com.salvai.centrum.enums.GameState
import com.salvai.centrum.enums.GameType
import com.salvai.centrum.input.GameInputProcessor
import com.salvai.centrum.utils.Constants
import com.salvai.centrum.utils.GameFlowManager

class GameScreen(var game: CentrumGameClass) : ScreenAdapter() {
    var countdownTime: Int
    var gameFlowManager: GameFlowManager
    private val stage: Stage
    private var fadeOutTime: Int
    private val scoreContainer: Container<Label?>
    private val scoreLabel: Label
    private val pauseTexture: Texture
    private val adVisible = false


    init {
        stage = Stage(game.viewport)

        game.gameState = GameState.RUNNING
        game.score = 0

        gameFlowManager = GameFlowManager(game)

        countdownTime = 199
        fadeOutTime = 20
        scoreLabel = Label("3", game.skin, "score")

        scoreContainer = Container<Label?>(scoreLabel)
        scoreContainer.isTransform = true
        scoreContainer.setSize(50f, 50f)
        scoreContainer.setOrigin(scoreContainer.getWidth() / 2, scoreContainer.getHeight() / 2)
        scoreContainer.setPosition(Constants.WIDTH_CENTER - scoreContainer.getWidth() / 2, Constants.HEIGHT_CENTER - scoreContainer.getHeight() / 2)
        stage.addActor(scoreContainer)

        pauseTexture = game.assetsManager.manager.get<Texture>(Constants.PAUSE_BUTTON_IMAGE_NAME, Texture::class.java)


        //to catch back key
        Gdx.input.setInputProcessor(GameInputProcessor(this))
    }

    override fun render(delta: Float) {
        setupScreen()

        if (countdownTime > 0 && game.gameState == GameState.RUNNING) {
            if (countdownTime < 180) {
                gameFlowManager.ball.sprite.setAlpha(1f)
                scoreLabel.setText("" + ((countdownTime / 60) + 1))
                countdownTime = (countdownTime - delta).toInt()
            } else {
                //fade in style
                countdownTime = (countdownTime - delta).toInt()
                gameFlowManager.ball.sprite.setAlpha((199 - countdownTime).toFloat() / 20)
            }
        } else if (game.gameState == GameState.RUNNING) {
            gameFlowManager.update(delta)
        }

        drawGame(delta)


        if (gameFlowManager.gameOver && gameFlowManager.explosions.size == 0) {
            if (game.levelSucceed && fadeOutTime > 1) {
                fadeOutTime = (fadeOutTime - delta).toInt()
                gameFlowManager.ball.sprite.setAlpha(fadeOutTime.toFloat() / 20)
            } else {
                game.setScreen(GameOverScreen(game))
                dispose()
            }
        }
    }


    private fun setupScreen() {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOR.r, Constants.BACKGROUND_COLOR.g, Constants.BACKGROUND_COLOR.b, Constants.BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }


    private fun drawGame(delta: Float) {
        game.batch.begin()
        if (game.gameState == GameState.RUNNING) game.drawBackground(delta)
        else game.drawPause()

        //draw enemies
        for (enemy in gameFlowManager.enemies) enemy.sprite.draw(game.batch)


        if (!gameFlowManager.gameOver)  //draw missiles
            for (missile in gameFlowManager.missiles) missile.sprite.draw(game.batch)

        if (countdownTime == 0) scoreLabel.setText("" + game.score)

        if (game.gameType == GameType.ENDLESS) {
            if (!gameFlowManager.gameOver) {
                gameFlowManager.ball.sprite.draw(game.batch)
            }
        } else if (gameFlowManager.gameOver) {
            if (game.levelSucceed) {
                gameFlowManager.ball.sprite.draw(game.batch)
            }
        } else {
            gameFlowManager.ball.sprite.draw(game.batch)
        }


        //PAUSED
        if (game.gameState == GameState.PAUSED) {
            game.batch.draw(pauseTexture, Constants.WIDTH_CENTER - pauseTexture.width * 0.5f, Constants.SCREEN_HEIGHT * 0.70f)
        }

        //explosions
        for (explosion in gameFlowManager.explosions) {
            if (game.gameState == GameState.RUNNING) explosion.particleEffect.draw(game.batch, delta)
            else explosion.particleEffect.draw(game.batch)
        }
        game.batch.end()

        stage.act(delta)
        stage.draw()
    }


    override fun pause() {
        //save score
        game.preferences.flush()
        if (game.gameState == GameState.RUNNING) game.gameState = GameState.PAUSED
    }

    override fun resize(width: Int, height: Int) {
        // change the stage's viewport when teh screen size is changed
        game.viewport.update(width, height, true)
        stage.viewport.update(width, height, true)
    }

    override fun resume() {
        Gdx.input.setInputProcessor(GameInputProcessor(this))
    }


    override fun dispose() {
        stage.dispose()
    }
}
