package io.github.masamune

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.ShaderService
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.component.Player
import io.github.masamune.component.teleport
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.event.EventService
import io.github.masamune.input.KeyboardController
import io.github.masamune.system.CameraSystem
import io.github.masamune.system.DebugPhysicRenderSystem
import io.github.masamune.system.FadeSystem
import io.github.masamune.system.MoveSystem
import io.github.masamune.system.MoveToSystem
import io.github.masamune.system.PhysicSystem
import io.github.masamune.system.PlayerInteractSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.system.StateSystem
import io.github.masamune.tiledmap.DefaultMapTransitionService
import io.github.masamune.tiledmap.MapTransitionService
import io.github.masamune.tiledmap.TiledService
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.box2d.createWorld
import ktx.math.vec2

/**
 * Test for [MapTransitionService].
 *
 * Change the 'mapTransitionService' value in the test below to switch between DefaultMapTransitionService
 * and ImmediateMapTransitionService.
 *
 * The test has two maps:
 * - one map smaller than the camera viewport without portal offsets
 * - one map bigger than the camera viewport with portal offsets
 *
 * Press 1 to transition to the small map and 2 to transition to the large map. A transition is:
 * - panning the camera
 * - moving the player to the new map and teleport him at the end of the transition to the correct location
 * - render the target map temporarily for a nice transition effect
 */
fun main() {
    // we misuse the VILLAGE constant for this test to avoid adding the test maps to our
    // TiledMapAsset enum. By mocking the VILLAGE's path value we can transition to an arbitrary map.
    mockkObject(TiledMapAsset.VILLAGE)
    every { TiledMapAsset.VILLAGE.path } returns "maps/transition_test_small.tmx"

    gdxTest("Map Transition Test, 1=small map without offset, 2=large map with offset", MapTransitionTest())
}

private class MapTransitionTest : KtxApplicationAdapter {
    private val batch: Batch by lazy { SpriteBatch() }
    private val gameViewport: Viewport = ExtendViewport(16f, 9f)
    private val physicWorld = createWorld(gravity = Vector2.Zero)
    private val world by lazy { gameWorld() }
    private val assetService by lazy { AssetService(ClasspathFileHandleResolver()) }
    private val eventService by lazy { EventService() }
    private val tiledService by lazy { TiledService(assetService, eventService) }

    // private val mapTransitionService by lazy { ImmediateMapTransitionService(tiledService) }
    private val mapTransitionService by lazy { DefaultMapTransitionService(tiledService) }

    private lateinit var tiledMap: TiledMap

    private fun gameWorld() = configureWorld {
        injectables {
            add(batch)
            add(gameViewport)
            add(physicWorld)
            add(ShaderService())
            add(eventService)
            add(tiledService)
            add(mockk<DialogConfigurator>())
            add<MapTransitionService>(mapTransitionService)
            add(mockk<Masamune>())
        }

        systems {
            add(MoveSystem())
            add(MoveToSystem())
            add(PhysicSystem())
            add(PlayerInteractSystem())
            add(CameraSystem())
            add(StateSystem())
            add(FadeSystem())
            add(RenderSystem())
            add(DebugPhysicRenderSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        eventService += world

        physicWorld.setContactListener(PhysicContactHandler(eventService, world))

        assetService.load(AtlasAsset.CHARS_AND_PROPS)
        assetService.finishLoading()
        tiledMap = tiledService.loadMap(TiledMapAsset.VILLAGE)
        tiledService.setMap(tiledMap, world)

        Gdx.input.inputProcessor = KeyboardController(eventService).also {
            eventService += it
        }
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)

        val deltaTime = Gdx.graphics.deltaTime.coerceAtMost(1 / 30f)
        world.update(deltaTime)
        mapTransitionService.update(world, deltaTime)

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            every { TiledMapAsset.VILLAGE.path } returns "maps/transition_test_small.tmx"
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            every { TiledMapAsset.VILLAGE.path } returns "maps/transition_test_large.tmx"
        }

        if (Gdx.input.justTouched()) {
            val touchXY = vec2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            val worldXY = gameViewport.unproject(touchXY)
            world.family { all(Player) }.forEach { player ->
                player.configure {
                    teleport(entity = it, to = worldXY)
                }
            }
        }
    }

    override fun dispose() {
        batch.dispose()
        world.dispose()
        assetService.dispose()
        tiledMap.dispose()
        physicWorld.dispose()
    }

}
