package dev.ian.assroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.ian.assroids.asset.Asset;
import dev.ian.assroids.screen.LoadScreen;

public class AssRoids extends Game {

    private Asset asset;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        asset = Asset.instance();
        setScreen(new LoadScreen(this));
    }


    @Override
    public void dispose() {
        batch.dispose();
        asset.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
