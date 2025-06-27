package io.github.masamune

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.ShaderService
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.event.EventService
import io.github.masamune.input.KeyboardController
import io.github.masamune.system.PhysicSystem
import io.github.masamune.system.PlayerInteractSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.tiledmap.MapTransitionService
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.tiledmap.TiledService.Companion.TILED_MAP_ASSET_PROPERTY_KEY
import io.mockk.mockk
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.box2d.createWorld
import ktx.tiled.set

/**
 * Test for [PlayerInteractSystem], [RenderSystem] and [ShaderService].
 * One entity must be rendered with an outline shader. Per default, it is the bottom one.
 * Press 'A', 'D' or 'S' to switch outline entities. Always the closest one to the player
 * within his direction must be picked for interaction (=outline highlighting).
 *
 * Mushroom is rendered with a red outline (=enemy). Other entities are rendered with a white outline.
 */

fun main() = gdxTest("Interact Test", InteractTest())

private class InteractTest : KtxApplicationAdapter {
    private val batch: Batch by lazy { SpriteBatch() }
    private val shaderService: ShaderService by lazy { ShaderService(ClasspathFileHandleResolver()) }
    private val eventService = EventService()
    private val assetService: AssetService by lazy { AssetService(ClasspathFileHandleResolver()) }
    private val tiledService: TiledService by lazy { TiledService(assetService, eventService) }
    private val gameViewport: Viewport = ExtendViewport(16f, 9f)
    private val physicWorld = createWorld(Vector2.Zero, true).apply {
        autoClearForces = false
    }
    private val world by lazy { gameWorld() }

    private fun gameWorld() = configureWorld {
        injectables {
            add(batch)
            add(gameViewport)
            add(physicWorld)
            add(shaderService)
            add(EventService())
            add(mockk<DialogConfigurator>())
            add(mockk<MapTransitionService>())
            add(mockk<Masamune>())
        }

        systems {
            add(PhysicSystem())
            add(PlayerInteractSystem())
            add(RenderSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        shaderService.loadAllShader()

        eventService += world
        physicWorld.setContactListener(PhysicContactHandler(eventService, world))

        assetService.load(AtlasAsset.CHARS_AND_PROPS)
        assetService.finishLoading()
        val tiledMap = TmxMapLoader(ClasspathFileHandleResolver())
            .load("maps/interactTest.tmx", TmxMapLoader.Parameters().apply {
                projectFilePath = "maps/masamune-tiled.tiled-project"
            })
        TiledService.PLAYER_START_ITEMS = emptyMap()
        tiledMap.properties[TILED_MAP_ASSET_PROPERTY_KEY] = TiledMapAsset.VILLAGE
        tiledService.setMap(tiledMap, world)

        Gdx.input.inputProcessor = KeyboardController(eventService)
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
        world.dispose()
        batch.dispose()
        shaderService.dispose()
        assetService.dispose()
        physicWorld.dispose()
    }

}
