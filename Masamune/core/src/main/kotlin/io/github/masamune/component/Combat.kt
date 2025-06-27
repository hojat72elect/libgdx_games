package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.collection.MutableEntityBag
import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.action.Action
import io.github.masamune.combat.action.ActionTargetType
import io.github.masamune.combat.action.DefaultAction
import io.github.masamune.combat.buff.Buff
import io.github.masamune.tiledmap.ActionType

data class Combat(
    var availableActionTypes: List<ActionType>,
    var action: Action = DefaultAction,
    val targets: MutableEntityBag = MutableEntityBag(4),
    val attackSnd: SoundAsset = SoundAsset.ATTACK_SWIPE,
    val attackSFX: String = "hit1",
    val buffs: MutableList<Buff> = mutableListOf(),
) : Component<Combat> {

    val attackAction: Action by lazy { availableActionTypes.single { it == ActionType.ATTACK_SINGLE }() }

    val passiveActions: List<Action> by lazy {
        availableActionTypes
            .filter { it != ActionType.ATTACK_SINGLE && it != ActionType.USE_ITEM }
            .map { it() }
            .filter { it.targetType == ActionTargetType.NONE }
    }

    val magicActions: List<Action> by lazy {
        availableActionTypes
            // Attack actions should not show up as "magic" actions in combat.
            // Use item action should also not show up us "magic".
            .filter { it != ActionType.ATTACK_SINGLE && it != ActionType.USE_ITEM }
            .map { it() }
            // Finally, any "ActionTargetType.NONE" actions should also not show up. Those are passive actions.
            .filter { it.targetType != ActionTargetType.NONE }
    }

    override fun type() = Combat

    fun clearAction() {
        action = DefaultAction
    }

    companion object : ComponentType<Combat>() {
        infix fun List<ActionType>.andEquipment(equipmentActionTypes: List<ActionType>): List<ActionType> {
            val result = mutableListOf<ActionType>()
            // get unique actions that are not part of the equipment
            this.filterTo(result) { it !in equipmentActionTypes }
            // combine the previous actions with the one of the equipment
            equipmentActionTypes.filterTo(result) { it != ActionType.UNDEFINED }
            return result
        }
    }
}
