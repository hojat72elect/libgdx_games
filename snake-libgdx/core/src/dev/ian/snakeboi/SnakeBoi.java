package dev.ian.snakeboi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.ian.snakeboi.asset.Asset;
import dev.ian.snakeboi.game.SnakeGame;

public class SnakeBoi extends Game {

    private SpriteBatch batch;
    private SnakeGame game;

    @Override
    public void create() {
        Asset.instance().loadAsset();
        batch = new SpriteBatch();
        game = new SnakeGame();
    }

    @Override
    public void render() {
        game.update(Gdx.graphics.getDeltaTime());
        clearScreen();
        batch.begin();
        game.render(batch);
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        batch.dispose();
        Asset.instance().dispose();
    }
}
