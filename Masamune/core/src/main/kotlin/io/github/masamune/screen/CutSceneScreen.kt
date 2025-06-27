package io.github.masamune.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
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
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Tag
import io.github.masamune.component.Trigger
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.event.CutSceneAbortEvent
import io.github.masamune.event.EventService
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
import io.github.masamune.system.RemoveSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.system.ScaleSystem
import io.github.masamune.system.StateSystem
import io.github.masamune.system.TriggerSystem
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.trigger.TriggerConfigurator
import io.github.masamune.ui.model.CutSceneViewModel
import io.github.masamune.ui.view.CutSceneView
import io.github.masamune.ui.view.cutSceneView
import ktx.actors.alpha
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.scene2d.actors

class CutSceneScreen(
    private val masamune: Masamune,
    private val batch: Batch = masamune.batch,
    private val inputProcessor: InputMultiplexer = masamune.inputProcessor,
    private val eventService: EventService = masamune.event,
    private val tiledService: TiledService = masamune.tiled,
    private val shaderService: ShaderService = masamune.shader,
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
    private var alphaDeltaTime = 0f
    private val cutSceneWorldColor = Color(1f, 1f, 1f, 1f)

    // cancel cutscene stuff
    private var cancelHoldTimer = 0f
    private var canCancel = false

    // ecs world
    val world = gameWorld()

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
            add(CameraSystem(camFollowSpeed = 20f, camStationaryRange = 0f))
            add(StateSystem())
            add(DissolveSystem())
            add(ScaleSystem())
            add(AnimationSystem())
            add(FadeSystem())
            add(RenderSystem())
            add(TriggerSystem())
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
        stage.actors {
            cutSceneView(CutSceneViewModel(bundle, audioService), skin) {
                this.name = "cutSceneView"
            }
        }

        // register all event listeners
        registerEventListeners()
    }

    fun startCutScene(name: String) {
        canCancel = false
        cancelHoldTimer = 0f

        when (name) {
            "intro" -> {
                canCancel = true
                cutSceneWorldColor.set(0.5f, 0.3f, 0.3f, 1f)
                world.entity {
                    it += Trigger("cut_scene_intro")
                    it += Tag.EXECUTE_TRIGGER
                }
            }
            "outro" -> {
                world.entity {
                    it += Trigger("cut_scene_outro")
                    it += Tag.EXECUTE_TRIGGER
                }
            }

            else -> gdxError("Unsupported cut scene $name")
        }

        if (canCancel) {
            val view = stage.root.findActor<CutSceneView>("cutSceneView")
            view.showCancelInfo()
        }
    }

    private fun registerEventListeners() {
        eventService += world
        eventService += stage
        eventService += keyboardController
        eventService += masamune.audio
    }

    override fun hide() {
        stage.clear()
        alphaDeltaTime = 0f
        eventService.clearListeners()
        physicWorld.setContactListener(null)
        world.removeAll(true)
        tiledService.unloadActiveMap(world)
        batch.color.set(1f, 1f, 1f, 1f)
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, false)
        uiViewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        batch.color = cutSceneWorldColor
        if (alphaDeltaTime < 1f) {
            // cut scene screen fades in slowly
            alphaDeltaTime = (alphaDeltaTime + delta * 0.33f).coerceAtMost(1f)
            batch.color.a = Interpolation.fade.apply(0f, 1f, alphaDeltaTime)
        }
        world.update(delta)
        batch.setColor(1f, 1f, 1f, 1f)

        uiViewport.apply()
        stage.alpha = batch.color.a
        stage.act(delta)
        stage.draw()
        batch.setColor(1f, 1f, 1f, 1f)

        if (canCancel && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            cancelHoldTimer += delta
            if (cancelHoldTimer > 1f) {
                // hold for one second to abort
                canCancel = false
                eventService.fire(CutSceneAbortEvent)
            }
        } else {
            cancelHoldTimer = 0f
        }
    }

    override fun dispose() {
        log.debug { "Disposing cut-scene world with '${world.numEntities}' entities" }
        world.dispose()
        physicWorld.dispose()
        stage.dispose()
    }

    companion object {
        private val log = logger<CutSceneScreen>()
    }
}
