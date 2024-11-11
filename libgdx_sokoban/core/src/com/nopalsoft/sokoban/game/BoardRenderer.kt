package com.nopalsoft.sokoban.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.screens.Screens

class BoardRenderer(batcher : SpriteBatch) {

    private val camera = OrthographicCamera(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
    private val tiledRenderer = OrthogonalTiledMapRenderer(Assets.map, Board.UNIT_SCALE)
    private val mapStaticLayer = tiledRenderer.map.layers["StaticMap"] as TiledMapTileLayer

    init {
        camera.position.set(Screens.SCREEN_WIDTH / 2F, Screens.SCREEN_HEIGHT / 2F,0F)
    }

     fun render(){
        camera.update()
        tiledRenderer.setView(camera)
        tiledRenderer.batch.begin()
        tiledRenderer.renderTileLayer(mapStaticLayer)
        tiledRenderer.batch.end()
    }
}