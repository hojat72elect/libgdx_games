package io.github.masamune.trigger.forest.path

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger

fun World.snowyTrigger(
    name: String,
    triggeringEntity: Entity
): TriggerScript = trigger(name, this, triggeringEntity) {
    actionDialog("snowy")
}
