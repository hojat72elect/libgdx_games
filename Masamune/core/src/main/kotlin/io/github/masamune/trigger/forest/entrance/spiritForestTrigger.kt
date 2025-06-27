package io.github.masamune.trigger.forest.entrance

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.asset.SoundAsset
import io.github.masamune.component.Combat
import io.github.masamune.component.QuestLog
import io.github.masamune.component.teleport
import io.github.masamune.quest.SpiritQuest
import io.github.masamune.tiledmap.ActionType
import io.github.masamune.tiledmap.TiledService
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.spiritForestTrigger(
    name: String,
    scriptEntity: Entity,
    triggeringEntity: Entity
): TriggerScript = trigger(name, this, triggeringEntity) {
    val spiritQuest = triggeringEntity[QuestLog].getOrNull<SpiritQuest>()
    val tiledService = inject<TiledService>()

    if (spiritQuest == null) {
        // first spirit interaction -> add quest and move to next location
        actionAddQuest(triggeringEntity, SpiritQuest())

        val newLocation = tiledService.loadPoint("spirit_pos_2")
        actionConfigureEntity(scriptEntity) {
            teleport(it, newLocation)
        }

        actionDialog("spiritForest_00")
    } else if (spiritQuest.progress == 0) {
        // move to third location
        val newLocation = tiledService.loadPoint("spirit_pos_3")
        spiritQuest.progress = 25
        actionConfigureEntity(scriptEntity) {
            teleport(it, newLocation)
        }
        actionDialog("spiritForest_10")
    } else if (spiritQuest.progress == 25) {
        // move to last location
        val newLocation = tiledService.loadPoint("spirit_pos_4")
        spiritQuest.progress = 75
        actionConfigureEntity(scriptEntity) {
            teleport(it, newLocation)
        }
        actionDialog("spiritForest_20")
    } else {
        // last location -> complete quest and give reward
        actionCompleteQuest(spiritQuest)
        scriptEntity.remove()
        actionDialog("spiritForest_30")
        actionPlaySound(SoundAsset.QUEST_ITEM)
        actionConfigureEntity(triggeringEntity) {
            it[Combat].availableActionTypes += ActionType.FIREBALL
        }
    }
}
