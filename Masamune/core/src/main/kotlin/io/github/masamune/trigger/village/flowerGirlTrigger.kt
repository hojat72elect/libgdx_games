package io.github.masamune.trigger.village

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.component.QuestLog
import io.github.masamune.hasItem
import io.github.masamune.quest.FlowerGirlQuest
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.flowerGirlTrigger(name: String, triggeringEntity: Entity): TriggerScript {
    val quest = triggeringEntity[QuestLog].getOrNull<FlowerGirlQuest>()
    val hasTerealisFlower = hasItem(triggeringEntity, ItemType.TEREALIS_FLOWER)

    return when {
        quest != null && !quest.isCompleted() && hasTerealisFlower -> trigger(name, this, triggeringEntity) {
            // quest completed -> give reward
            actionDialog("flower_girl_20")
            actionAddItem(triggeringEntity, ItemType.SMALL_STRENGTH_POTION)
            actionRemoveItem(triggeringEntity, ItemType.TEREALIS_FLOWER, 1)
            actionCompleteQuest(quest)
            actionHeal(triggeringEntity, healLife = true, healMana = true)
        }

        quest != null && quest.isCompleted() -> trigger(name, this, triggeringEntity) {
            // quest completed and reward already given
            actionDialog("flower_girl_30")
            actionHeal(triggeringEntity, healLife = true, healMana = true)
        }

        quest != null -> trigger(name, this, triggeringEntity) {
            // quest started but not completed yet
            actionDialog("flower_girl_10")
            actionHeal(triggeringEntity, healLife = true, healMana = true)
        }


        else -> trigger(name, this, triggeringEntity) {
            // first interaction -> no quest yet
            actionDialog("flower_girl_00") { selectedOptionIdx ->
                if (selectedOptionIdx == 0) {
                    actionAddQuest(triggeringEntity, FlowerGirlQuest())
                    actionHeal(triggeringEntity, healLife = true, healMana = true)
                }
            }
        }
    }
}
