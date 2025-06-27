package io.github.masamune.screen

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.Masamune
import io.github.masamune.Masamune.Companion.uiViewport
import io.github.masamune.PhysicContactHandler
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.I18NAsset
import io.github.masamune.asset.ShaderService
import io.github.masamune.asset.SkinAsset
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.event.EventService
import io.github.masamune.event.GameStartEvent
import io.github.masamune.event.LoadEvent
import io.github.masamune.event.SaveEvent
import io.github.masamune.input.KeyboardController
import io.github.masamune.system.AnimationSystem
import io.github.masamune.system.CameraSystem
import io.github.masamune.system.DissolveSystem
import io.github.masamune.system.FacingSystem
import io.github.masamune.system.FadeSystem
import io.github.masamune.system.FollowPathSystem
import io.github.masamune.system.MoveSystem
import io.github.masamune.system.MoveToSystem
import io.github.masamune.system.PhysicSystem
import io.github.masamune.system.PlayerInteractSystem
import io.github.masamune.system.RemoveSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.system.ScaleSystem
import io.github.masamune.system.StateSystem
import io.github.masamune.system.TriggerSystem
import io.github.masamune.tiledmap.MapTransitionService
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.trigger.TriggerConfigurator
import io.github.masamune.ui.model.DialogViewModel
import io.github.masamune.ui.model.GameMenuViewModel
import io.github.masamune.ui.model.InventoryViewModel
import io.github.masamune.ui.model.MonsterBookViewModel
import io.github.masamune.ui.model.QuestItemViewModel
import io.github.masamune.ui.model.QuestViewModel
import io.github.masamune.ui.model.ShopViewModel
import io.github.masamune.ui.model.StatsViewModel
import io.github.masamune.ui.view.dialogView
import io.github.masamune.ui.view.gameMenuView
import io.github.masamune.ui.view.inventoryView
import io.github.masamune.ui.view.monsterBookView
import io.github.masamune.ui.view.questItemView
import io.github.masamune.ui.view.questView
import io.github.masamune.ui.view.shopView
import io.github.masamune.ui.view.statsView
import ktx.app.KtxScreen
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.scene2d.actors

class GameScreen(
    private val masamune: Masamune,
    private val batch: Batch = masamune.batch,
    private val inputProcessor: InputMultiplexer = masamune.inputProcessor,
    private val eventService: EventService = masamune.event,
    private val tiledService: TiledService = masamune.tiled,
    private val shaderService: ShaderService = masamune.shader,
    private val mapTransitionService: MapTransitionService = masamune.mapTransition,
    private val assetService: AssetService = masamune.asset,
    private val audioService: AudioService = masamune.audio,
) : KtxScreen {
    // viewports and stage
    private val gameViewport: Viewport = ExtendViewport(16f, 9f)
    private val uiViewport = uiViewport()
    private val stage = Stage(uiViewport, batch)
    private val skin = assetService[SkinAsset.DEFAULT]

    // physic world
    private val physicWorld = createWorld(Vector2.Zero, true).apply {
        autoClearForces = false
    }

    // other stuff
    private val bundle: I18NBundle = assetService[I18NAsset.MESSAGES]
    private val keyboardController = KeyboardController(eventService)
    private val dialogConfigurator = DialogConfigurator(bundle)
    private val triggerConfigurator = TriggerConfigurator()

    // ecs world
    val world = gameWorld()

    // view stuff
    private val gmViewModel = GameMenuViewModel(bundle, audioService, world, eventService, masamune)

    private fun gameWorld() = configureWorld {
        injectables {
            add(batch)
            add(gameViewport)
            add(physicWorld)
            add(shaderService)
            add(eventService)
            add(dialogConfigurator)
            add(triggerConfigurator)
            add(tiledService)
            add<MapTransitionService>(mapTransitionService)
            add(assetService)
            add(masamune)
            add(audioService)
            add(AtlasAsset.SFX.name, assetService[AtlasAsset.SFX])
        }

        systems {
            add(FollowPathSystem())
            add(MoveSystem())
            add(MoveToSystem())
            add(PhysicSystem())
            add(PlayerInteractSystem())
            add(CameraSystem())
            add(StateSystem())
            add(DissolveSystem())
            add(ScaleSystem())
            add(FadeSystem())
            add(RenderSystem())
            add(TriggerSystem())
            // animation system must run after trigger to correctly update animation if facing direction gets changed
            add(AnimationSystem())
            // FacingSystem must run at the end of a frame to correctly detect facing changes in any system before
            add(FacingSystem())
            add(RemoveSystem())
        }
    }

    override fun show() {
        // set controller
        inputProcessor.clear()
        inputProcessor.addProcessor(keyboardController)

        // set physic contact handler (needs to be done AFTER ECS world is created)
        physicWorld.setContactListener(PhysicContactHandler(eventService, world))

        // setup UI views
        stage.clear()
        stage.actors {
            dialogView(DialogViewModel(bundle, audioService, eventService), skin) { isVisible = false }
            gameMenuView(gmViewModel, skin) { isVisible = false }
            statsView(StatsViewModel(bundle, audioService, world, eventService), skin) { isVisible = false }
            inventoryView(InventoryViewModel(bundle, audioService, world, eventService), skin) { isVisible = false }
            shopView(ShopViewModel(bundle, audioService, world, tiledService), skin) { isVisible = false }
            questItemView(QuestItemViewModel(bundle, audioService, world, gameViewport, uiViewport), skin) { isVisible = false }
            questView(QuestViewModel(bundle, audioService, world, eventService), skin) { isVisible = false }
            monsterBookView(MonsterBookViewModel(bundle, audioService, world, assetService[AtlasAsset.CHARS_AND_PROPS], eventService, tiledService), skin) { isVisible = false }
        }

        // register all event listeners
        registerEventListeners()
        eventService.fire(GameStartEvent)
    }

    fun startNewGame(initialMap: TiledMapAsset = TiledMapAsset.VILLAGE, ignoreSaveService: Boolean = false) {
        // call this AFTER event listeners are registered
        world.removeAll(true)
        tiledService.unloadActiveMap(world)
        tiledService.loadMap(initialMap).also {
            tiledService.setMap(it, world)
        }

        if (!ignoreSaveService) {
            masamune.save.clearSaveState()
            eventService.fire(SaveEvent(world))
        }
    }

    fun loadSaveState() {
        eventService.fire(LoadEvent(world))
    }

    fun clearGameState() {
        world.removeAll(true)
        tiledService.unloadActiveMap(world)
        world.system<RenderSystem>().clearMapLayer()
    }

    private fun registerEventListeners() {
        eventService += world
        eventService += stage
        eventService += keyboardController
        eventService += masamune.audio
        eventService += masamune.save
        world.system<TriggerSystem>().registerTriggerListeners()
    }

    override fun hide() {
        eventService.clearListeners()
        physicWorld.setContactListener(null)
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, false)
        uiViewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)

        uiViewport.apply()
        stage.act(delta)
        stage.draw()
        batch.setColor(1f, 1f, 1f, 1f)

        mapTransitionService.update(world, delta)
    }

    override fun dispose() {
        log.debug { "Disposing world with '${world.numEntities}' entities" }
        world.dispose()
        physicWorld.dispose()
        stage.dispose()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}
