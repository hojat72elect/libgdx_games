package com.nopalsoft.sokoban.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.screens.BaseScreen

class BoardRenderer {

    private val camera = OrthographicCamera(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT)
    private val tiledRenderer = OrthogonalTiledMapRenderer(Assets.map, Board.UNIT_SCALE)
    private val mapStaticLayer = tiledRenderer.map.layers["StaticMap"] as TiledMapTileLayer

    init {
        camera.position.set(BaseScreen.SCREEN_WIDTH / 2F, BaseScreen.SCREEN_HEIGHT / 2F,0F)
    }

     fun render(){
        camera.update()
        tiledRenderer.setView(camera)
        tiledRenderer.batch.begin()
        tiledRenderer.renderTileLayer(mapStaticLayer)
        tiledRenderer.batch.end()
    }
}