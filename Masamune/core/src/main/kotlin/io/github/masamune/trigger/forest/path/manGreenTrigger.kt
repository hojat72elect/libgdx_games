package io.github.masamune.trigger.forest.path

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.component.MonsterBook
import io.github.masamune.component.QuestLog
import io.github.masamune.quest.MonsterBookQuest
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.tiledmap.TiledObjectType
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.manGreenTrigger(
    name: String,
    scriptEntity: Entity,
    triggeringEntity: Entity
): TriggerScript = trigger(name, this, triggeringEntity) {
    val monsterBook = triggeringEntity.getOrNull(MonsterBook)
    if (monsterBook == null) {
        // no monster book yet -> add it
        actionPauseEntity(scriptEntity, true)
        actionDialog("man_green_00") {
            actionPauseEntity(scriptEntity, false)
            triggeringEntity.configure {
                it += MonsterBook(knownTypes = mutableSetOf(TiledObjectType.BUTTERFLY, TiledObjectType.LARVA))
            }
            actionAddQuest(triggeringEntity, MonsterBookQuest())
        }
    } else if (triggeringEntity[QuestLog].getOrNull<MonsterBookQuest>()?.completed == true) {
        // quest already completed
        actionPauseEntity(scriptEntity, true)
        actionDialog("man_green_30") {
            actionPauseEntity(scriptEntity, false)
        }
    } else if (monsterBook.knownTypes.size >= 5) {
        // quest completed! -> give reward
        actionPauseEntity(scriptEntity, true)
        actionDialog("man_green_20") {
            actionPauseEntity(scriptEntity, false)
        }
        actionCompleteQuest(triggeringEntity[QuestLog].get<MonsterBookQuest>())
        actionAddItem(triggeringEntity, ItemType.INTELLIGENCE_POTION)
    } else {
        // dialog while quest is not completed
        actionPauseEntity(scriptEntity, true)
        actionDialog("man_green_10") {
            actionPauseEntity(scriptEntity, false)
        }
    }
}
