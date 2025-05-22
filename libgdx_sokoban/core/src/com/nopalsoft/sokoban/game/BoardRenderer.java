package com.nopalsoft.sokoban.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.screens.Screens;

public class BoardRenderer {

    SpriteBatch batcher;
    OrthogonalTiledMapRenderer renderer;
    TiledMapTileLayer mapStaticLayer;
    OrthographicCamera camera;

    public BoardRenderer(SpriteBatch batch) {
        batcher = batch;
        camera = new OrthographicCamera(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        camera.position.set(Screens.SCREEN_WIDTH / 2f, Screens.SCREEN_HEIGHT / 2f, 0);
        renderer = new OrthogonalTiledMapRenderer(Assets.map, GameBoard.UNIT_SCALE);
        mapStaticLayer = (TiledMapTileLayer) renderer.getMap().getLayers().get("StaticMap");
    }

    public void render() {
        camera.update();
        renderer.setView(camera);
        renderer.getBatch().begin();
        renderer.renderTileLayer(mapStaticLayer);
        renderer.getBatch().end();
    }
}
