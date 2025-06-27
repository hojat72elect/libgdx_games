package io.github.masamune.combat.action

import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.buff.StrBoosterBuff
import io.github.masamune.tiledmap.ActionType

class StrBoosterAction : Action(ActionType.STR_BOOSTER, ActionTargetType.NONE, defensive = true) {
    override fun ActionExecutorService.onCombatStart() {
        addBuff(StrBoosterBuff(source, 0.25f))
    }
}
