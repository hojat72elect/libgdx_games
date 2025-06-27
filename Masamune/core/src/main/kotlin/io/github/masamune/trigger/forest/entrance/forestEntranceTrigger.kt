package io.github.masamune.trigger.forest.entrance

import com.github.quillraven.fleks.World
import io.github.masamune.component.Graphic
import io.github.masamune.component.Physic
import io.github.masamune.component.Player
import io.github.masamune.component.QuestLog
import io.github.masamune.component.Trigger
import io.github.masamune.getEntityByTiledIdOrNull
import io.github.masamune.quest.FlowerGirlQuest
import io.github.masamune.quest.MainQuest
import io.github.masamune.quest.SpiritQuest
import io.github.masamune.screen.CutSceneScreen
import io.github.masamune.screen.FadeTransitionType
import io.github.masamune.teleportEntity
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.forestEntranceTrigger(name: String): TriggerScript? {
    val player = this.family { all(Player) }.single()

    // the next code never happens in a real game but is useful if we need to test the forest entrance
    // map separately. In that case the player doesn't have the MainQuest and the game crashes.
    // In the real game this is impossible because the player always has the MainQuest at this point.
    if (player[QuestLog].quests.isEmpty()) {
        player[QuestLog].quests += MainQuest()
    }

    // check main quest progress: if boss fight is completed then demo is over
    val mainQuest = player[QuestLog].get<MainQuest>()
    if (mainQuest.progress >= 50) {
        return trigger(name, this, player) {
            actionChangeScreen {
                transitionScreen<CutSceneScreen>(
                    FadeTransitionType(1f, 0f, 3f),
                    FadeTransitionType(0f, 1f, 1f)
                ) {
                    it.startCutScene("outro")
                }
            }
        }
    }

    val flowerQuest = player[QuestLog].getOrNull<FlowerGirlQuest>()
    if (flowerQuest == null) {
        // player doesn't have flower quest -> make flower non interactable and hide it
        getEntityByTiledIdOrNull(32)?.let { flower ->
            flower.configure {
                it -= Trigger
                it -= Physic
                it[Graphic].color.a = 0f
            }
        }
    }

    val spiritQuest = player[QuestLog].getOrNull<SpiritQuest>()
    if (spiritQuest != null && !spiritQuest.isCompleted()) {
        // player has non-finished spirit quest -> adjust spirit location
        val questProgress = spiritQuest.progress
        getEntityByTiledIdOrNull(44)?.let { spirit ->
            val targetPosition = when (questProgress) {
                0 -> this.inject<TiledService>().loadPoint("spirit_pos_2")
                25 -> this.inject<TiledService>().loadPoint("spirit_pos_3")
                else -> this.inject<TiledService>().loadPoint("spirit_pos_4")
            }
            this.teleportEntity(spirit, targetPosition)
        }
    }

    return null
}
