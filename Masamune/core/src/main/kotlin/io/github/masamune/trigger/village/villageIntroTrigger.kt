package io.github.masamune.trigger.village

import com.github.quillraven.fleks.World
import io.github.masamune.component.Move
import io.github.masamune.component.Player
import io.github.masamune.component.QuestLog
import io.github.masamune.component.Transform
import io.github.masamune.quest.MainQuest
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.villageIntroTrigger(name: String): TriggerScript? {
    val player = family { all(Player) }.single()
    val mainQuest = player[QuestLog].getOrNull<MainQuest>()
    val originalSpeed = player[Move].speed
    val playerLocation = player[Transform].position
    if (!playerLocation.epsilonEquals(5.5f, 17f, playerLocation.z, 0.1f)) {
        // player is not at proper start location -> save state of village was loaded. Therefore, ignore this trigger
        return null
    }

    return when (mainQuest) {
        null -> trigger(name, this, player) {
            val selector = selectEntity { player }
            actionEnableInput(false)
            actionEntitySpeed(selector, 1.5f)
            actionFollowPath(selector, 21, removeAtEnd = true, waitForEnd = true)
            actionEntitySpeed(selector, originalSpeed)
            actionEnableInput(true)
            actionDialog("village_intro")
        }

        else -> null
    }
}
