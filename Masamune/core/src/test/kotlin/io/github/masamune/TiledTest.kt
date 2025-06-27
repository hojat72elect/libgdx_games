package io.github.masamune

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
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
import io.github.masamune.component.Graphic
import io.github.masamune.component.Transform
import io.github.masamune.event.EventService
import io.github.masamune.system.AnimationSystem
import io.github.masamune.system.DebugPhysicRenderSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.tiledmap.TiledService.Companion.TILED_MAP_ASSET_PROPERTY_KEY
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.box2d.createWorld
import ktx.tiled.set
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test for [TiledService].
 * It loads a map with background, object and foreground layer.
 * Additionally, it creates map boundary rectangle physic shapes around
 * the edges of the map.
 */

fun main() = gdxTest("Tiled Test", TiledTest())

private class TiledTest : KtxApplicationAdapter {
    private val batch: Batch by lazy { SpriteBatch() }
    private val gameViewport: Viewport = ExtendViewport(16f, 9f)
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
            add(AnimationSystem())
            add(RenderSystem())
            add(DebugPhysicRenderSystem())
        }
    }

    override fun create() {
        eventService += world

        assetService.load(AtlasAsset.CHARS_AND_PROPS)
        assetService.finishLoading()
        TiledService.PLAYER_START_ITEMS = emptyMap()
        tiledMap = TmxMapLoader(ClasspathFileHandleResolver())
            .load("maps/test.tmx", TmxMapLoader.Parameters().apply {
                projectFilePath = "maps/masamune-tiled.tiled-project"
            })
        tiledMap.properties[TILED_MAP_ASSET_PROPERTY_KEY] = TiledMapAsset.VILLAGE
        tiledService.setMap(tiledMap, world)

        // assertions
        with(world) {
            assertEquals(2, numEntities)
            asEntityBag().forEach { entity ->
                assertTrue(entity has Transform)
                assertTrue(entity has Graphic)
            }
        }

        (gameViewport.camera as OrthographicCamera).zoom = 2f
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
