package io.github.masamune.ui

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.configureWorld
import com.ray3k.stripe.FreeTypeSkin
import io.github.masamune.Masamune.Companion.uiViewport
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.component.MonsterBook
import io.github.masamune.component.Player
import io.github.masamune.event.EventService
import io.github.masamune.event.MenuBeginEvent
import io.github.masamune.gdxTest
import io.github.masamune.input.ControllerStateUI
import io.github.masamune.input.KeyboardController
import io.github.masamune.tiledmap.TiledObjectType
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.ui.model.MenuType
import io.github.masamune.ui.model.MonsterBookViewModel
import io.github.masamune.ui.view.MonsterBookView
import io.github.masamune.ui.view.monsterBookView
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.assets.toClasspathFile
import ktx.scene2d.actors

/**
 * Test for [MonsterBookView].
 * It shows a known monster BUTTERFLY and other UNKNOWN monster entries.
 */

fun main() {
    // we misuse the VILLAGE constant for this test to avoid adding the test maps to our
    // TiledMapAsset enum. By mocking the VILLAGE's path value we can transition to an arbitrary map.
    mockkObject(TiledMapAsset.VILLAGE)
    every { TiledMapAsset.VILLAGE.path } returns "maps/test.tmx"
    gdxTest("UI Monster Book Test", UiMonsterBookTest())
}

private class UiMonsterBookTest : KtxApplicationAdapter {
    private val uiViewport = uiViewport()
    private val batch by lazy { SpriteBatch() }
    private val stage by lazy { Stage(uiViewport, batch) }
    private val uiAtlas by lazy { TextureAtlas("ui/skin.atlas".toClasspathFile()) }
    private val skin by lazy { FreeTypeSkin("ui/skin.json".toClasspathFile(), uiAtlas) }
    private val eventService by lazy { EventService() }
    private val assetService by lazy { AssetService(ClasspathFileHandleResolver()) }
    private val tiledService by lazy { TiledService(assetService, eventService) }
    private val world = configureWorld {
        injectables {
            add(mockk<World>(relaxed = true))
        }
    }
    private val bundle by lazy { I18NBundle.createBundle("ui/messages".toClasspathFile(), Charsets.ISO_8859_1.name()) }
    private val audioService by lazy { AudioService(assetService) }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        assetService.load(AtlasAsset.CHARS_AND_PROPS)
        assetService.finishLoading()

        world.entity {
            it += Player()
            it += MonsterBook(knownTypes = mutableSetOf(
                TiledObjectType.BUTTERFLY,
//                TiledObjectType.LARVA,
//                TiledObjectType.MUSHROOM,
//                TiledObjectType.CYCLOPS,
//                TiledObjectType.SLIME,
//                TiledObjectType.AXOLOT,
//                TiledObjectType.SPIDER,
            ))
        }
        val tiledMap = tiledService.loadMap(TiledMapAsset.VILLAGE)
        tiledService.setMap(tiledMap, world, false)

        stage.actors {
            monsterBookView(
                MonsterBookViewModel(
                    bundle,
                    audioService,
                    world,
                    assetService[AtlasAsset.CHARS_AND_PROPS],
                    eventService,
                    tiledService
                ), skin
            )
        }
        eventService += stage
        eventService += audioService
        Gdx.input.inputProcessor = KeyboardController(eventService, ControllerStateUI::class).also {
            eventService += it
        }

        eventService.fire(MenuBeginEvent(MenuType.MONSTER_BOOK))
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)
        uiViewport.apply()
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        batch.dispose()
        skin.dispose()
        world.dispose()
        assetService.dispose()
    }
}
