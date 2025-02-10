package dev.ian.snakeboi

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import dev.ian.snakeboi.asset.Asset
import dev.ian.snakeboi.game.SnakeGame

class SnakeBoi : Game() {

    private lateinit var batch: SpriteBatch
    private lateinit var game: SnakeGame

    override fun create() {
        Asset.instance().loadAsset()
        batch = SpriteBatch()
        game = SnakeGame()
    }

    override fun render() {
        game.update(Gdx.graphics.deltaTime)
        clearScreen()
        batch.begin()
        game.render(batch)
        batch.end()
    }

    private fun clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun dispose() {
        batch.dispose()
        Asset.instance().dispose()
    }
}