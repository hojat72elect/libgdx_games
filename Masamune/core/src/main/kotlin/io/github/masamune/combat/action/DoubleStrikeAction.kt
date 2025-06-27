package io.github.masamune.combat.action

import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class DoubleStrikeAction : Action(ActionType.DOUBLE_STRIKE, ActionTargetType.SINGLE) {
    override fun ActionExecutorService.onStart() {
        attack(singleTarget)
        attack(singleTarget)
    }
}
