package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import kotlinx.serialization.Serializable

@Serializable
data class ItemStats(
    var agility: Float = 0f,
    var arcaneStrike: Float = 0f,
    var armor: Float = 0f,
    var constitution: Float = 0f,
    var criticalStrike: Float = 0f,
    var damage: Float = 0f,
    var intelligence: Float = 0f,
    var life: Float = 0f,
    var lifeMax: Float = 0f,
    var magicalEvade: Float = 0f,
    var mana: Float = 0f,
    var manaMax: Float = 0f,
    var physicalEvade: Float = 0f,
    var resistance: Float = 0f,
    var strength: Float = 0f,
) : Component<ItemStats> {
    override fun type() = ItemStats

    companion object : ComponentType<ItemStats>()
}
