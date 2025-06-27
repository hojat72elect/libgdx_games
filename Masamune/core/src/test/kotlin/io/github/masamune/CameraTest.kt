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
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.ShaderService
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.component.Tag
import io.github.masamune.component.Transform
import io.github.masamune.event.EventService
import io.github.masamune.system.AnimationSystem
import io.github.masamune.system.CameraSystem
import io.github.masamune.system.DebugPhysicRenderSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.tiledmap.TiledService.Companion.TILED_MAP_ASSET_PROPERTY_KEY
import ktx.app.KtxApplicationAdapter
import ktx.app.KtxInputAdapter
import ktx.app.clearScreen
import ktx.box2d.createWorld
import ktx.math.vec2
import ktx.tiled.set

/**
 * Test for [CameraSystem].
 * Camera must stay within map boundaries unless camera viewport is too big.
 * Camera must stay focused on player entity.
 *
 * Use mousewheel to zoom and touch/click to teleport the player entity.
 */

fun main() = gdxTest("Camera Test (click to move; mousewheel to zoom)", CameraTest())

private class CameraController(
    private val gameViewport: Viewport,
    private val world: World
) : KtxInputAdapter {
    private val camera = gameViewport.camera as OrthographicCamera
    private val camEntity = world.family { all(Tag.CAMERA_FOCUS) }.first()

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        camera.zoom += amountY
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        with(world) {
            val (position, size) = camEntity[Transform]
            val worldCoords = gameViewport.unproject(vec2(screenX.toFloat(), screenY.toFloat()))
            position.set(worldCoords.sub(size.cpy().scl(0.5f)), 0f)
        }
        return true
    }
}

class CameraTest : KtxApplicationAdapter {
    private val batch: Batch by lazy { SpriteBatch() }
    private val gameViewport: Viewport = ExtendViewport(8f, 4.5f)
    private val world by lazy { gameWorld() }
    private val assetService by lazy { AssetService(ClasspathFileHandleResolver()) }
    private val eventService by lazy { EventService() }
    private val tiledService by lazy { TiledService(assetService, eventService) }
    private lateinit var tiledMap: TiledMap

    private fun gameWorld() = configureWorld {
        injectables {
            add(batch)
            add(gameViewport)
            add(ShaderService())
            add(createWorld(gravity = Vector2.Zero))
        }

        systems {
            add(AnimationSystem())
            add(CameraSystem())
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

        Gdx.input.inputProcessor = CameraController(gameViewport, world)
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
