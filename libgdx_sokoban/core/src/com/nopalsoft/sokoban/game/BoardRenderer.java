package com.nopalsoft.sokoban.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.screens.Screens;

public class BoardRenderer {

    SpriteBatch batcher;
    OrthogonalTiledMapRenderer tiledRenderer;
    TiledMapTileLayer mapStaticLayer;
    OrthographicCamera camera;

    public BoardRenderer(SpriteBatch batch) {
        batcher = batch;
        camera = new OrthographicCamera(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        camera.position.set(Screens.SCREEN_WIDTH / 2f, Screens.SCREEN_HEIGHT / 2f, 0);
        tiledRenderer = new OrthogonalTiledMapRenderer(Assets.map, Board.UNIT_SCALE);
        mapStaticLayer = (TiledMapTileLayer) tiledRenderer.getMap().getLayers().get("StaticMap");

    }

    public void render() {
        camera.update();
        tiledRenderer.setView(camera);
        tiledRenderer.getBatch().begin();
        tiledRenderer.renderTileLayer(mapStaticLayer);
        tiledRenderer.getBatch().end();
    }

}
