package com.salvai.snake.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.salvai.snake.actors.Apple
import com.salvai.snake.actors.Block
import com.salvai.snake.actors.GameObjectMap
import com.salvai.snake.actors.snake.Snake
import com.salvai.snake.actors.snake.SnakeBody
import com.salvai.snake.enums.MovingDirection
import com.salvai.snake.screens.GameScreen

class GameFlowManager(private val gameScreen: GameScreen) {
    private val gameObjectMap: GameObjectMap

    @JvmField
    var snake: Snake? = null

    @JvmField
    var baseBlocks: Array<Block?>?

    @JvmField
    var blocks: Array<Block>

    @JvmField
    var apple: Apple? = null


    init {
        this.gameScreen.game.savePreferences()

        val blockTexture = gameScreen.game.assetsManager!!.manager.get<Texture?>(Constants.BLOCK_IMAGE_NAME, Texture::class.java)

        val boundariesCreator = BoundariesCreator(blockTexture, gameScreen.game.worldUtils!!)

        baseBlocks = boundariesCreator.fullBoundaries()
        blocks = Array<Block>()

        //load blocks
        blocks = this.gameScreen.game.currentLevel!!.getBlocks(blockTexture, gameScreen.game.worldUtils!!)

        //Load blocks first other wise start X is aways -1. See getBocks method
        if (gameScreen.game.firstTimeOpen) snake = Snake(blockTexture, 8, 19, gameScreen.game.selectedColor, gameScreen.game.worldUtils!!)
        else if (gameScreen.game.currentLevel!!.snakeStartX > 0) snake =
            Snake(blockTexture, gameScreen.game.currentLevel!!.snakeStartX, gameScreen.game.currentLevel!!.snakeStartY, gameScreen.game.selectedColor, gameScreen.game.worldUtils!!)
        else snake = Snake(blockTexture, gameScreen.game.selectedColor, gameScreen.game.worldUtils!!)

        gameObjectMap = GameObjectMap(blocks, gameScreen.game.worldUtils!!)


        if (gameScreen.game.firstTimeOpen) apple = Apple(Vector2(11f, 19f), blockTexture, gameScreen.game.worldUtils!!, gameScreen.game.selectedColor)
        else apple = Apple(gameObjectMap.getFreePositions(snake!!, null), blockTexture, gameScreen.game.worldUtils!!, gameScreen.game.selectedColor)
    }

    fun update(userDirection: MovingDirection?): SnakeBody? {
        var newSnakeBody: SnakeBody? = null

        if (userDirection != null && userDirection != snake!!.snakeHead.direction) {
            snake!!.setDirection(userDirection)
        }

        snake!!.moveWorldPosition()

        if (snake!!.checkGameOver(blocks)) {
            gameScreen.gameOver = true
            if (gameScreen.game.vibrationOn) {
                Gdx.input.vibrate(Constants.VIBRATION_DURATION_GAME_OVER)
            }
            assignHighscore()
        } else {
            snake!!.move(gameScreen.game.worldUtils!!.blockSize)
            checkScore()
            newSnakeBody = addBody()
            snake!!.updateBodyAndTailDirections()
        }
        return newSnakeBody
    }

    private fun addBody(): SnakeBody? {
        var newSnakeBody: SnakeBody? = null
        if (snake!!.addBody) {
            if (gameScreen.game.vibrationOn) Gdx.input.vibrate(Constants.VIBRATION_DURATION)
            newSnakeBody = snake!!.addBody()
        }
        return newSnakeBody
    }

    private fun checkScore() {
        if (snake!!.eats(apple!!)) {
            gameScreen.game.score += Constants.POINT
            gameScreen.updateScoreLabel()

            apple!!.reset(gameObjectMap.getFreePositions(snake!!, apple!!))
        }
    }

    private fun assignHighscore() {
        if (gameScreen.game.highScores[gameScreen.game.level] < gameScreen.game.score) {
            gameScreen.game.highScores[gameScreen.game.level] = gameScreen.game.score
            gameScreen.newHighscore = true
        }
        gameScreen.game.savePreferences()
    }
}
