package io.github.masamune.combat.action

import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.buff.RegenerateManaBuff
import io.github.masamune.tiledmap.ActionType

class RegenerateManaRingAction : Action(ActionType.REGENERATE_MANA_RING, ActionTargetType.NONE, defensive = true) {
    override fun ActionExecutorService.onCombatStart() {
        addBuff(RegenerateManaBuff(source, 2f))
    }
}
