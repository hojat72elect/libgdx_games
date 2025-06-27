package io.github.masamune.ui.model

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.World
import io.github.masamune.Masamune
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Experience
import io.github.masamune.component.Experience.Companion.calcLevelUps
import io.github.masamune.component.Experience.Companion.levelUpStats
import io.github.masamune.component.Inventory
import io.github.masamune.component.ItemStats
import io.github.masamune.component.Tiled
import io.github.masamune.event.CombatPlayerDefeatEvent
import io.github.masamune.event.CombatPlayerVictoryEvent
import io.github.masamune.event.CombatStartEvent
import io.github.masamune.event.Event
import io.github.masamune.event.EventService
import io.github.masamune.event.PlayerInteractCombatEndEvent
import io.github.masamune.screen.BlurTransitionType
import io.github.masamune.screen.CombatScreen
import io.github.masamune.screen.DefaultTransitionType
import io.github.masamune.screen.GameScreen
import io.github.masamune.tiledmap.TiledObjectType
import ktx.log.logger
import kotlin.math.roundToInt

enum class UiCombatFinishState {
    UNDEFINED, VICTORY, DEFEAT
}

class CombatFinishViewModel(
    bundle: I18NBundle,
    private val masamune: Masamune,
    private val world: World,
    audioService: AudioService = masamune.audio,
    private val eventService: EventService = masamune.event,
) : ViewModel(bundle, audioService) {

    var xpToGain: Int by propertyNotify(0)
    var talonsToGain: Int by propertyNotify(0)
    var levelsToGain: Int by propertyNotify(0)
    var statsToGain: Map<UIStats, Int> by propertyNotify(emptyMap())
    val combatSummary: MutableMap<String, Int> by propertyNotify(mutableMapOf())
    var state: UiCombatFinishState by propertyNotify(UiCombatFinishState.UNDEFINED)
    private val monsterTypesToAdd = mutableListOf<TiledObjectType>()
    private var lvlUpStats = ItemStats()

    override fun onEvent(event: Event) {
        when (event) {
            is CombatStartEvent -> {
                state = UiCombatFinishState.UNDEFINED
                with(world) {
                    var totalXp = 0
                    var totalTalons = 0
                    monsterTypesToAdd.clear()
                    combatSummary.clear()
                    event.enemies.forEach { enemy ->
                        val (_, xp) = enemy[Experience]
                        totalXp += xp
                        totalTalons += (enemy[Inventory].talons * MathUtils.random(0.9f, 1.1f)).roundToInt()

                        val type = enemy[Tiled].objType
                        val name = bundle["enemy.${type.name.lowercase()}.name"]
                        combatSummary.merge(name, 1, Int::plus)

                        monsterTypesToAdd += enemy[Tiled].objType
                    }

                    xpToGain = totalXp
                    talonsToGain = totalTalons
                    val (playerLevel, playerXp) = event.player[Experience]
                    levelsToGain = calcLevelUps(playerLevel, playerXp, xpToGain)

                    // get level up stat gains
                    lvlUpStats = ItemStats()
                    repeat(levelsToGain) {
                        lvlUpStats.levelUpStats(playerLevel + (it + 1))
                    }
                    statsToGain = lvlUpStats.toUiStatsMap()

                    log.debug { "Total XP/talons to gain: xp=$xpToGain, talons=$talonsToGain, levels=$levelsToGain" }
                    notify(CombatFinishViewModel::combatSummary, combatSummary)
                }
            }

            is CombatPlayerVictoryEvent -> {
                state = UiCombatFinishState.VICTORY
            }

            is CombatPlayerDefeatEvent -> state = UiCombatFinishState.DEFEAT

            else -> Unit
        }
    }

    fun quitCombat() {
        val victory = state == UiCombatFinishState.VICTORY
        val combatScreen = masamune.getScreen<CombatScreen>()
        if (victory) {
            combatScreen.updatePlayerAfterVictory(xpToGain, lvlUpStats, talonsToGain, monsterTypesToAdd)
        } else {
            combatScreen.updatePlayerAfterDefeat()
        }

        masamune.transitionScreen<GameScreen>(
            fromType = DefaultTransitionType,
            toType = BlurTransitionType(
                startBlur = 6f,
                endBlur = 0f,
                time = 2f,
                endAlpha = 1f,
                startAlpha = 0.4f
            )
        ) {
            val gameScreenEntity = combatScreen.gameScreenEnemy
            eventService.fire(PlayerInteractCombatEndEvent(victory, gameScreenEntity))
        }
    }

    fun restartCombat() {
        masamune.getScreen<CombatScreen>().restartCombat()
    }

    companion object {
        private val log = logger<CombatFinishViewModel>()
    }

}
