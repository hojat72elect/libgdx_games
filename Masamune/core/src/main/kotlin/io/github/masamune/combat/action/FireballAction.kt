package io.github.masamune.combat.action

import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class FireballAction : Action(ActionType.FIREBALL, ActionTargetType.ALL, manaCost = 5) {
    private var amount = 2

    override fun ActionExecutorService.onUpdate(): Boolean {
        amount--
        dealMagicDamage(source, 3f, allTargets, "fire2", 1f, 2f, SoundAsset.EXPLOSION1, 1f)
        if (amount <= 0) {
            amount = 2
            return true
        }
        return false
    }
}
