package io.github.masamune.trigger.forest.masamune

import com.github.quillraven.fleks.World
import io.github.masamune.component.Player
import io.github.masamune.component.QuestLog
import io.github.masamune.component.Trigger
import io.github.masamune.getEntityByTiledId
import io.github.masamune.quest.MainQuest
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.forestMasamuneMapTrigger(name: String): TriggerScript? {
    val player = this.family { all(Player) }.single()

    // the next code never happens in a real game but is useful if we need to test the forest masamune
    // map separately. In that case the player doesn't have the MainQuest and the game crashes.
    // In the real game this is impossible because the player always has the MainQuest at this point.
    if (player[QuestLog].quests.isEmpty()) {
        player[QuestLog].quests += MainQuest()
    }

    val quest = player[QuestLog].get<MainQuest>()
    val masamuneAlreadyTriggered = quest.progress >= 50

    return when {
        masamuneAlreadyTriggered -> trigger(name, this, player) {
            actionConfigureEntity(getEntityByTiledId(34)) {
                it -= Trigger
            }
        }

        else -> null
    }
}
