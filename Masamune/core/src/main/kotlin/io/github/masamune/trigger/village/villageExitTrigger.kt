package io.github.masamune.trigger.village

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.component.QuestLog
import io.github.masamune.quest.MainQuest
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.villageExitTrigger(name: String, scriptEntity: Entity, triggeringEntity: Entity): TriggerScript {
    val mainQuest = triggeringEntity[QuestLog].getOrNull<MainQuest>()

    return when (mainQuest) {
        null -> trigger(name, this, triggeringEntity) {
            // push player away to not trigger the same map trigger again and again
            actionMoveBack(triggeringEntity, distance = 0.75f, timeInSeconds = 0.25f, wait = true)
            actionDialog("villageExit")
        }

        else -> trigger(name, this, triggeringEntity) {
            actionRemove(scriptEntity)
        }
    }
}
