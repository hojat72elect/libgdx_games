package io.github.masamune.ui

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.mutableEntityBagOf
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.Masamune.Companion.uiViewport
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.Equipment
import io.github.masamune.component.Inventory
import io.github.masamune.component.Name
import io.github.masamune.component.Player
import io.github.masamune.event.EventService
import io.github.masamune.event.MenuBeginEvent
import io.github.masamune.gdxTest
import io.github.masamune.input.ControllerStateUI
import io.github.masamune.input.KeyboardController
import io.github.masamune.testSkin
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.ui.model.InventoryViewModel
import io.github.masamune.ui.model.MenuType
import io.github.masamune.ui.view.InventoryView
import io.github.masamune.ui.view.inventoryView
import io.mockk.every
import io.mockk.mockkObject
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.assets.toClasspathFile
import ktx.scene2d.actors

/**
 * Test for [InventoryView].
 * It loads a player with at least two items of each item category (=equipment test), and also
 * a consumable that can be used in the inventory view, and a consumable that can only be used in combat.
 * When selecting the "Quit" option then the UI gets invisible and the test must be restarted.
 */

fun main() {
    // we misuse the VILLAGE constant for this test to avoid adding the test maps to our
    // TiledMapAsset enum. By mocking the VILLAGE's path value we can transition to an arbitrary map.
    mockkObject(TiledMapAsset.VILLAGE)
    every { TiledMapAsset.VILLAGE.path } returns "maps/inventoryTest.tmx"
    gdxTest("UI Inventory Test", UiInventoryTest())
}

private class UiInventoryTest : KtxApplicationAdapter {
    private val uiViewport = uiViewport()
    private val batch by lazy { SpriteBatch() }
    private val stage by lazy { Stage(uiViewport, batch) }
    private val skin by lazy { testSkin() }
    private val eventService by lazy { EventService() }
    private val assetService by lazy { AssetService(ClasspathFileHandleResolver()) }
    private val tiledService by lazy { TiledService(assetService, eventService) }
    private val bundle by lazy { I18NBundle.createBundle("ui/messages".toClasspathFile(), Charsets.ISO_8859_1.name()) }
    private val audioService by lazy { AudioService(assetService) }
    private val world = configureWorld {
        injectables {
            add(audioService)
        }
    }
    private val viewModel by lazy { InventoryViewModel(bundle, audioService, world, eventService) }

    private fun createItem(world: World, type: ItemType, amount: Int = 2): Entity {
        return tiledService.loadItem(world, type, amount)
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        stage.actors {
            inventoryView(viewModel, skin)
        }
        eventService += stage
        eventService += audioService
        Gdx.input.inputProcessor = KeyboardController(eventService, ControllerStateUI::class).also {
            eventService += it
        }

        assetService.load(AtlasAsset.CHARS_AND_PROPS)
        assetService.finishLoading()

        // create player
        val map = tiledService.loadMap(TiledMapAsset.VILLAGE)
        tiledService.setMap(map, world, withBoundaries = false)
        world.entity {
            it += Name("Alexxius")
            it += Player()
            it += Inventory(
                items = mutableEntityBagOf(
                    createItem(world, ItemType.ELDER_SWORD),
                    createItem(world, ItemType.STUDDED_LEATHER),
                    createItem(world, ItemType.HELMET, 1),
                    createItem(world, ItemType.BOOTS),
                    createItem(world, ItemType.RING, 1),
                    createItem(world, ItemType.SMALL_MANA_POTION),
                    createItem(world, ItemType.SMALL_HEALTH_POTION),
                    createItem(world, ItemType.SCROLL_INFERNO),
                )
            )
            it += Equipment()
            it += CharacterStats(
                strength = 10f,
                agility = 1f,
                constitution = 2f,
                intelligence = 5f,
                baseDamage = 0f,
                armor = 0f,
                resistance = 0f,
                baseLife = 10f,
                baseMana = 15f,
            )
            it[CharacterStats].life = 1f
            it[CharacterStats].mana = 1f
        }

        eventService.fire(MenuBeginEvent(MenuType.INVENTORY))

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
