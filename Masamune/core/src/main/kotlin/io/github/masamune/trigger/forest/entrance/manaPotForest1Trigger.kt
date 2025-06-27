package io.github.masamune.trigger.forest.entrance

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.manaPotForest1Trigger(
    name: String,
    scriptEntity: Entity,
    triggeringEntity: Entity
): TriggerScript = trigger(name, this, triggeringEntity) {
    actionRemove(scriptEntity)
    actionAddItem(triggeringEntity, ItemType.SMALL_MANA_POTION, 1)
}
