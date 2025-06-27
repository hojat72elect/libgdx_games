package io.github.masamune.combat.action

import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class FireboltAction : Action(ActionType.FIREBOLT, ActionTargetType.MULTI, manaCost = 3) {
    override fun ActionExecutorService.onUpdate(): Boolean {
        dealMagicDamage(source, 8f / numTargets, allTargets, "fire1", 1f, 2f, SoundAsset.EXPLOSION1, 1f)
        return true
    }
}
