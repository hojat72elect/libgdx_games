package io.github.masamune.combat.action

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.collection.MutableEntityBag
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class UseItemAction(
    private val item: Entity = Entity.NONE,
    targetType: ActionTargetType = ActionTargetType.NONE,
    defensive: Boolean = false,
) : Action(ActionType.USE_ITEM, targetType, defensive = defensive) {
    override fun ActionExecutorService.onUpdate(): Boolean {
        // copy the current targets over into a new bag because targets
        // are coming from the ActionExecutorService and if we don't do that
        // then they get cleared inside 'performItem' below.
        val newTargets = MutableEntityBag(allTargets.size)
        newTargets += allTargets
        performItemAction(source, item, item.itemAction, newTargets)
        // return false to continue item action that gets initialized above
        return false
    }
}
