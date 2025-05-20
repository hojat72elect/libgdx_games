package com.nopalsoft.sokoban.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.screens.Screens;

public class TableroRenderer {

	SpriteBatch batcher;
	OrthogonalTiledMapRenderer tiledRender;
	TiledMapTileLayer mapStaticLayer;
	OrthographicCamera oCam;

	public TableroRenderer(SpriteBatch batch) {
		batcher = batch;
		oCam = new OrthographicCamera(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
		oCam.position.set(Screens.SCREEN_WIDTH / 2f, Screens.SCREEN_HEIGHT / 2f, 0);
		tiledRender = new OrthogonalTiledMapRenderer(Assets.map, Tablero.UNIT_SCALE);
		mapStaticLayer = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("StaticMap");

	}

	public void render(float delta) {
		
		
		
		oCam.update();
		tiledRender.setView(oCam);
		tiledRender.getBatch().begin();
		tiledRender.renderTileLayer(mapStaticLayer);
		tiledRender.getBatch().end();
	}

}
