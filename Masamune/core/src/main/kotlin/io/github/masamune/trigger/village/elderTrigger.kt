package io.github.masamune.trigger.village

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.hasQuest
import io.github.masamune.quest.MainQuest
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.elderTrigger(name: String, triggeringEntity: Entity): TriggerScript {
    return when {
        hasQuest<MainQuest>(triggeringEntity) -> {
            trigger(name, this, triggeringEntity) {
                actionDialog("elder_10")
            }
        }

        else -> {
            trigger(name, this, triggeringEntity) {
                actionDialog("elder_00")
                actionAddItem(triggeringEntity, ItemType.ELDER_SWORD)
                actionAddQuest(triggeringEntity, MainQuest())
            }
        }
    }
}
