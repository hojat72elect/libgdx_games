package ca.hojat.sokoban.game

import ca.hojat.sokoban.Assets
import ca.hojat.sokoban.screens.Screens
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

class BoardRenderer {

    private val camera = OrthographicCamera(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT)
    private val renderer = OrthogonalTiledMapRenderer(Assets.map, GameBoard.UNIT_SCALE)
    private val tileLayer = renderer.map.layers.get("StaticMap") as TiledMapTileLayer?

    init {
        camera.position.set(Screens.SCREEN_WIDTH / 2F, Screens.SCREEN_HEIGHT / 2F, 0F)

    }

    fun render() {
        camera.update()
        renderer.setView(camera)
        renderer.batch.begin()
        renderer.renderTileLayer(tileLayer!!)
        renderer.batch.end()
    }
}