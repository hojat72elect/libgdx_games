package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.MutableEntityBag

data class Equipment(
    val items: MutableEntityBag = MutableEntityBag(8),
) : Component<Equipment> {
    override fun type() = Equipment

    override fun World.onAdd(entity: Entity) {
        if (items.isEmpty()) {
            return
        }

        val entityStats = entity[CharacterStats]
        items.forEach { item ->
            entityStats += item[ItemStats]
        }
    }

    companion object : ComponentType<Equipment>()
}
