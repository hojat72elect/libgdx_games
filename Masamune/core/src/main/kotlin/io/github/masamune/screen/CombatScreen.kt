package io.github.masamune.screen

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.MutableEntityBag
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.Masamune
import io.github.masamune.Masamune.Companion.uiViewport
import io.github.masamune.asset.AssetService
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.I18NAsset
import io.github.masamune.asset.MusicAsset
import io.github.masamune.asset.ShaderService
import io.github.masamune.asset.ShaderService.Companion.useShader
import io.github.masamune.asset.SkinAsset
import io.github.masamune.asset.SoundAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.component.Animation
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.Combat
import io.github.masamune.component.Combat.Companion.andEquipment
import io.github.masamune.component.Equipment
import io.github.masamune.component.Experience
import io.github.masamune.component.Facing
import io.github.masamune.component.FacingDirection
import io.github.masamune.component.Graphic
import io.github.masamune.component.Inventory
import io.github.masamune.component.Item
import io.github.masamune.component.ItemStats
import io.github.masamune.component.MonsterBook
import io.github.masamune.component.Name
import io.github.masamune.component.Player
import io.github.masamune.component.ScreenBgd
import io.github.masamune.component.Transform
import io.github.masamune.event.CombatStartEvent
import io.github.masamune.event.EventService
import io.github.masamune.input.ControllerStateUI
import io.github.masamune.input.KeyboardController
import io.github.masamune.removeItem
import io.github.masamune.system.AnimationSystem
import io.github.masamune.system.CombatSystem
import io.github.masamune.system.DissolveSystem
import io.github.masamune.system.FadeSystem
import io.github.masamune.system.GrayscaleSystem
import io.github.masamune.system.MoveBySystem
import io.github.masamune.system.PlayerStatusAilmentSystem
import io.github.masamune.system.RemoveSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.system.ScaleSystem
import io.github.masamune.system.ScreenBgdRenderSystem
import io.github.masamune.system.SelectorSystem
import io.github.masamune.system.ShakeSystem
import io.github.masamune.tiledmap.AnimationType
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.tiledmap.TiledObjectType
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.ui.model.CombatFinishViewModel
import io.github.masamune.ui.model.CombatViewModel
import io.github.masamune.ui.view.combatFinishView
import io.github.masamune.ui.view.combatView
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.log.logger
import ktx.math.vec3
import ktx.scene2d.actors
import kotlin.collections.component1
import kotlin.collections.component2

class CombatScreen(
    private val masamune: Masamune,
    private val batch: Batch = masamune.batch,
    private val inputProcessor: InputMultiplexer = masamune.inputProcessor,
    private val eventService: EventService = masamune.event,
    private val shaderService: ShaderService = masamune.shader,
    private val assetService: AssetService = masamune.asset,
    private val audioService: AudioService = masamune.audio,
    private val actionExecutorService: ActionExecutorService = masamune.actionExecutor,
    private val tiledService: TiledService = masamune.tiled,
) : KtxScreen {
    // viewports and stage
    private val gameViewport: Viewport = ExtendViewport(8f, 8f)
    private val uiViewport = uiViewport()
    private val stage = Stage(uiViewport, batch)
    private val skin = assetService[SkinAsset.DEFAULT]

    // other stuff
    private val bundle: I18NBundle = assetService[I18NAsset.MESSAGES]
    private val keyboardController = KeyboardController(eventService, initialState = ControllerStateUI::class)

    // ecs world
    val world = combatWorld()
    private val enemyEntities = world.family { none(Player).all(Combat) }

    // things to remember to restart combat
    private lateinit var gameScreenWorld: World
    private lateinit var gameScreenPlayer: Entity
    private lateinit var enemiesMap: Map<TiledObjectType, Int>
    lateinit var gameScreenEnemy: Entity
        private set
    private lateinit var combatPlayer: Entity
    private var combatMusic: MusicAsset? = null

    private fun combatWorld(): World {
        return configureWorld {
            injectables {
                add(batch)
                add(gameViewport)
                add(shaderService)
                add(eventService)
                add(assetService)
                add(masamune)
                add(audioService)
                add(actionExecutorService)
                add(AtlasAsset.CHARS_AND_PROPS.name, assetService[AtlasAsset.CHARS_AND_PROPS])
                add(AtlasAsset.SFX.name, assetService[AtlasAsset.SFX])
                add(tiledService)
            }

            systems {
                add(CombatSystem())
                add(MoveBySystem())
                add(AnimationSystem())
                add(DissolveSystem())
                add(ScaleSystem())
                add(ShakeSystem())
                add(SelectorSystem())
                add(PlayerStatusAilmentSystem())
                // grayscale system must be before any other render system
                add(GrayscaleSystem())
                add(ScreenBgdRenderSystem())
                add(FadeSystem())
                add(RenderSystem())
                add(RemoveSystem())
            }
        }
    }

    override fun show() {
        // set action executor entity world
        actionExecutorService withWorld world

        // set controller
        inputProcessor.clear()
        inputProcessor.addProcessor(keyboardController)

        // setup UI views
        setupUI()

        // register all event listeners
        registerEventListeners()

        // special screen background entity to render GameScreen as blurred background
        spawnScreenBgdEntity()
    }

    fun playCombatMusic(musicAsset: MusicAsset) {
        combatMusic = musicAsset
        audioService.play(musicAsset)
    }

    private fun spawnScreenBgdEntity() {
        world.entity {
            it += ScreenBgd(alpha = 0.4f) { batch, fbo ->
                shaderService.useBlurShader(batch, 6f, fbo) {
                    val gameScreen = masamune.getScreen<GameScreen>()
                    gameScreen.resize(fbo.width, fbo.height)
                    gameScreen.render(0f)
                }
            }
        }
    }

    private fun setupUI() {
        val model = CombatViewModel(
            bundle,
            audioService,
            world,
            eventService,
            gameViewport,
            uiViewport,
            actionExecutorService
        )
        stage.clear()
        stage.actors {
            combatView(model, skin)
            combatFinishView(CombatFinishViewModel(bundle, masamune, world), skin) {
                isVisible = false
            }
        }
    }

    fun spawnPlayer(gameScreenWorld: World, gameScreenPlayer: Entity) {
        this.gameScreenWorld = gameScreenWorld
        this.gameScreenPlayer = gameScreenPlayer

        val aniCmp = with(gameScreenWorld) { gameScreenPlayer[Animation] }
        val nameCmp = with(gameScreenWorld) { gameScreenPlayer[Name] }
        val playerCmp = with(gameScreenWorld) { gameScreenPlayer[Player] }
        val statsCmp = with(gameScreenWorld) { gameScreenPlayer[CharacterStats] }
        val equipmentActionTypes = with(gameScreenWorld) { gameScreenPlayer[Equipment].items.map { it[Item].actionType } }
        val combatCmp = with(gameScreenWorld) { gameScreenPlayer[Combat] }
        val xpCmp = with(gameScreenWorld) { gameScreenPlayer[Experience] }
        val monsterBookCmp = with(gameScreenWorld) { gameScreenPlayer.getOrNull(MonsterBook) }
        // clone OTHER ItemCategory items; they are the only ones that can be used during combat
        val clonedItems = MutableEntityBag()
        with(gameScreenWorld) {
            gameScreenPlayer[Inventory].items
                .filter { it[Item].category == ItemCategory.OTHER }
                .forEach {
                    val clonedItem = tiledService.loadItem(world, it[Item].type, it[Item].amount)
                    clonedItems += clonedItem
                }
        }

        combatPlayer = this.world.entity {
            it += nameCmp
            it += playerCmp
            it += xpCmp
            it += statsCmp.copy()
            it += Facing(FacingDirection.UP)
            val animationCmp = Animation.ofAnimation(aniCmp, FacingDirection.UP)
            animationCmp.changeTo = AnimationType.WALK
            animationCmp.speed = 0.4f
            it += animationCmp
            val graphicCmp = Graphic(animationCmp.gdxAnimation.getKeyFrame(0f))
            it += graphicCmp
            it += Transform(vec3(gameViewport.worldWidth * 0.5f + 1f, 1f, 0f), graphicCmp.regionSize)
            it += Combat(
                availableActionTypes = combatCmp.availableActionTypes andEquipment equipmentActionTypes,
                attackSnd = SoundAsset.SWORD_SWIPE,
                attackSFX = "slash1",
            )
            it += Inventory(clonedItems)
            monsterBookCmp?.let { cmp -> it += cmp }
        }

        eventService.fire(CombatStartEvent(combatPlayer, enemyEntities.entities))
    }

    fun updatePlayerAfterVictory(
        xpGained: Int,
        lvlUpStats: ItemStats,
        talonsGained: Int,
        monsterTypesToAdd: MutableList<TiledObjectType>
    ) {
        // update life and mana
        val combatStats = with(world) { combatPlayer[CharacterStats] }
        val gameStats = with(gameScreenWorld) { gameScreenPlayer[CharacterStats] }
        gameStats.life = combatStats.life
        gameStats.mana = combatStats.mana
        gameStats += lvlUpStats

        // update consumable items
        updatePlayerItemsAfterCombat()

        // update xp and talons
        with(gameScreenWorld) {
            gameScreenPlayer[Experience].gainXp(xpGained)
            gameScreenPlayer[Inventory].talons += talonsGained
        }

        // update monster book
        with(gameScreenWorld) {
            gameScreenPlayer.getOrNull(MonsterBook)?.knownTypes?.addAll(monsterTypesToAdd)
        }
    }

    fun updatePlayerAfterDefeat() {
        // update life and mana
        val combatStats = with(world) { combatPlayer[CharacterStats] }
        val gameStats = with(gameScreenWorld) { gameScreenPlayer[CharacterStats] }
        gameStats.life = 1f
        gameStats.mana = combatStats.mana

        // update consumable items
        updatePlayerItemsAfterCombat()
    }

    private fun updatePlayerItemsAfterCombat() {
        val combatItemCmps = with(world) {
            combatPlayer[Inventory].items.map { it[Item] }
        }
        with(gameScreenWorld) {
            val toRemove = MutableEntityBag(4)
            gameScreenPlayer[Inventory].items
                .filter { it[Item].category == ItemCategory.OTHER }
                .forEach { itemEntity ->
                    val itemCmp = itemEntity[Item]
                    val matchingItemCmp = combatItemCmps.firstOrNull { it.type == itemCmp.type }
                    if (matchingItemCmp == null || matchingItemCmp.amount == 0) {
                        // item was completely consumed during combat
                        toRemove += itemEntity
                    } else {
                        // item is still available -> update amount
                        itemCmp.amount = matchingItemCmp.amount
                    }
                }
            toRemove.forEach { this.removeItem(it, gameScreenPlayer) }
        }
    }

    fun spawnEnemies(gameScreenEnemy: Entity, enemiesMap: Map<TiledObjectType, Int>) {
        if (enemiesMap.isEmpty()) {
            gdxError("Cannot start a combat without enemies")
        }
        this.enemiesMap = enemiesMap
        this.gameScreenEnemy = gameScreenEnemy

        val totalEnemies = enemiesMap.values.sum()
        val diffX = gameViewport.worldWidth / (1 + totalEnemies)
        val y = gameViewport.worldHeight - 2
        var x = diffX

        enemiesMap.forEach { (type, amount) ->
            repeat(amount) {
                val spawnX = x + MathUtils.random(-0.5f, 0.5f)
                val spawnY = y + MathUtils.random(-2f, 0.5f)
                val enemy = tiledService.loadCombatEnemy(world, type, spawnX, spawnY)
                // adjust animation speed and position, if enemy is outside screen (e.g. boss enemies are
                // sometimes larger than 1 game unit)
                with(world) {
                    enemy[Animation].speed = 0.4f
                    val transformCmp = enemy[Transform]
                    val sizeY = transformCmp.size.y
                    if (spawnY + sizeY > gameViewport.worldHeight) {
                        transformCmp.position.y -= ((spawnY + sizeY) - gameViewport.worldHeight)
                    }
                }
                x += diffX
            }
        }
    }

    fun restartCombat() {
        batch.shader = null
        world.removeAll(clearRecycled = true)
        spawnScreenBgdEntity()
        spawnEnemies(gameScreenEnemy, enemiesMap)
        spawnPlayer(gameScreenWorld, gameScreenPlayer)
        combatMusic?.let { musicAsset ->
            audioService.play(musicAsset, keepPrevious = true)
        }
    }

    private fun registerEventListeners() {
        eventService += world
        eventService += stage
        eventService += keyboardController
        eventService += masamune.audio
    }

    override fun hide() {
        // in case of defeat we set a grayscale shader -> remove that shader if it was set
        batch.shader = null
        eventService.clearListeners()
        world.removeAll(clearRecycled = true)
        audioService.playPrevMusic()
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)

        // we need to call useShader because there might be a grayscale shader set for world rendering,
        // and we need to restore to that shader after UI rendering is done
        batch.useShader(null) {
            uiViewport.apply()
            stage.act(delta)
            stage.draw()
            batch.setColor(1f, 1f, 1f, 1f)
        }
    }

    override fun dispose() {
        log.debug { "Disposing world with '${world.numEntities}' entities" }
        world.dispose()
        stage.dispose()
    }

    companion object {
        private val log = logger<CombatScreen>()
    }
}
