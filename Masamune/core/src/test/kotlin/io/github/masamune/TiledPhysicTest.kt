package io.github.masamune

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.ShaderService
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.event.EventService
import io.github.masamune.system.AnimationSystem
import io.github.masamune.system.CameraSystem
import io.github.masamune.system.DebugPhysicRenderSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.tiledmap.TiledService.Companion.TILED_MAP_ASSET_PROPERTY_KEY
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.box2d.createWorld
import ktx.tiled.set

/**
 * Test for tiledPhysic.kt.
 *
 * It loads a map object with all possible collision shapes (Tile MapObject):
 * - rect
 * - circle
 * - ellipse
 * - polyline
 * - polygon
 *
 * It also loads trigger map objects with those shapes (=no Tile, no collision editor).
 *
 * Refer to tiledTest.tmx in src/test/resources/maps folder.
 */

fun main() = gdxTest("Tiled Physic Test", TiledPhysicTest())

private class TiledPhysicTest : KtxApplicationAdapter {
    private val batch: Batch by lazy { SpriteBatch() }
    private val gameViewport: Viewport = ExtendViewport(6f, 3.375f)
    private val world by lazy { gameWorld() }
    private val assetService by lazy { AssetService(ClasspathFileHandleResolver()) }
    private val eventService by lazy { EventService() }
    private val tiledService by lazy { TiledService(assetService, eventService) }
    private lateinit var tiledMap: TiledMap

    private fun gameWorld() = configureWorld {
        injectables {
            add(batch)
            add(gameViewport)
            add(createWorld(gravity = Vector2.Zero))
            add(ShaderService())
        }

        systems {
            add(CameraSystem())
            add(AnimationSystem())
            add(RenderSystem())
            add(DebugPhysicRenderSystem())
        }
    }

    override fun create() {
        eventService += world

        assetService.load(AtlasAsset.CHARS_AND_PROPS)
        assetService.finishLoading()
        tiledMap = TmxMapLoader(ClasspathFileHandleResolver())
            .load("maps/tiledTest.tmx", TmxMapLoader.Parameters().apply {
                projectFilePath = "maps/masamune-tiled.tiled-project"
            })
        tiledMap.properties[TILED_MAP_ASSET_PROPERTY_KEY] = TiledMapAsset.VILLAGE
        TiledService.PLAYER_START_ITEMS = emptyMap()
        tiledService.setMap(tiledMap, world)
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)

        val deltaTime = Gdx.graphics.deltaTime.coerceAtMost(1 / 30f)
        world.update(deltaTime)
    }

    override fun dispose() {
        batch.dispose()
        world.dispose()
        assetService.dispose()
        tiledMap.dispose()
    }

}
