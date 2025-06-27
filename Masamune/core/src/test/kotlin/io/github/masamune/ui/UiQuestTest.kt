package io.github.masamune.ui

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.Masamune.Companion.uiViewport
import io.github.masamune.asset.AssetService
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Player
import io.github.masamune.component.QuestLog
import io.github.masamune.event.EventService
import io.github.masamune.event.MenuBeginEvent
import io.github.masamune.gdxTest
import io.github.masamune.input.ControllerStateUI
import io.github.masamune.input.KeyboardController
import io.github.masamune.quest.FlowerGirlQuest
import io.github.masamune.quest.MainQuest
import io.github.masamune.testSkin
import io.github.masamune.ui.model.MenuType
import io.github.masamune.ui.model.QuestViewModel
import io.github.masamune.ui.view.QuestView
import io.github.masamune.ui.view.questView
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.assets.toClasspathFile
import ktx.scene2d.actors

/**
 * Test for [QuestView].
 * It loads a player with two active quests and one completed quest.
 * When selecting the "Quit" option then the UI gets invisible and the test must be restarted.
 */

fun main() = gdxTest("UI Quest Test 1=Quests,2=No Quests", UiQuestTest())

private class UiQuestTest : KtxApplicationAdapter {
    private val uiViewport = uiViewport()
    private val batch by lazy { SpriteBatch() }
    private val stage by lazy { Stage(uiViewport, batch) }
    private val skin by lazy { testSkin() }
    private val eventService by lazy { EventService() }
    private val assetService by lazy { AssetService(ClasspathFileHandleResolver()) }
    private val bundle by lazy { I18NBundle.createBundle("ui/messages".toClasspathFile(), Charsets.ISO_8859_1.name()) }
    private val audioService by lazy { AudioService(assetService) }
    private val world = configureWorld {}
    private val viewModel by lazy { QuestViewModel(bundle, audioService, world, eventService) }
    private val testQuests = listOf(
        MainQuest(0),
        FlowerGirlQuest(0),
        FlowerGirlQuest(100),
    )

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        stage.actors {
            questView(viewModel, skin)
        }
        eventService += stage
        eventService += audioService
        Gdx.input.inputProcessor = KeyboardController(eventService, ControllerStateUI::class).also {
            eventService += it
        }

        // create player
        world.entity {
            it += Player()
            it += QuestLog(quests = testQuests.toMutableList())
        }

        eventService.fire(MenuBeginEvent(MenuType.QUEST))

    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)
        uiViewport.apply()
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            with(world) {
                family { all(Player) }.first()[QuestLog].quests.apply {
                    clear()
                    addAll(testQuests)
                }
            }
            eventService.fire(MenuBeginEvent(MenuType.QUEST))
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            with(world) {
                family { all(Player) }.first()[QuestLog].quests.clear()
            }
            eventService.fire(MenuBeginEvent(MenuType.QUEST))
        }
    }

    override fun dispose() {
        stage.dispose()
        batch.dispose()
        skin.dispose()
        world.dispose()
        assetService.dispose()
    }
}
