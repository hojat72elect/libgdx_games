package dev.ian.assroids.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.ian.assroids.AssRoids;
import dev.ian.assroids.asset.Asset;

/**
 * Created by: Ian Parcon
 * Date created: Sep 15, 2018
 * Time created: 9:10 AM
 */
public abstract class BaseScreen implements Screen {

    protected SpriteBatch batch;
    protected AssRoids game;
    protected Asset asset;
    protected float width;
    protected float height;

    public BaseScreen(AssRoids game) {
        this.game = game;
        batch = game.getBatch();
        asset = Asset.instance();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public abstract void update(float delta);

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(40f / 255f, 40f/ 255f, 40f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        asset.dispose();
        game.dispose();
    }
}
