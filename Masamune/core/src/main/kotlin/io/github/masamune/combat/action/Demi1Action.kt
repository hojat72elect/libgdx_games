package io.github.masamune.combat.action

import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class Demi1Action : Action(ActionType.DEMI1, ActionTargetType.SINGLE, 12) {
    override fun ActionExecutorService.onUpdate(): Boolean {
        val damage = singleTarget.stats.life * 0.5f
        dealMagicDamage(source, damage, singleTarget, "demi1", 0.9f, 2f, SoundAsset.DEMI1, 1.2f)
        return true
    }
}
