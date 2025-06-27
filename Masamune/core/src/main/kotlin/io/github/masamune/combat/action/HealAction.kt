package io.github.masamune.combat.action

import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class HealAction : Action(ActionType.HEAL, ActionTargetType.SINGLE, manaCost = 5, defensive = true) {
    override fun ActionExecutorService.onUpdate(): Boolean {
        heal(life = 20f, mana = 0f, singleTarget, "restore_green", 1f, 1.5f, SoundAsset.HEAL1, 1.5f)
        return true
    }
}
