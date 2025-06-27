package io.github.masamune.combat.action

import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class AttackSingleAction : Action(ActionType.ATTACK_SINGLE, ActionTargetType.SINGLE) {
    override fun ActionExecutorService.onStart() {
        attack(singleTarget)
    }
}
