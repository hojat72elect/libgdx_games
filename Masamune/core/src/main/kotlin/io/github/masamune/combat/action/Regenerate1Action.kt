package io.github.masamune.combat.action

import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class Regenerate1Action : Action(ActionType.REGENERATE1, ActionTargetType.NONE, defensive = true) {
    override fun ActionExecutorService.onUpdate(): Boolean {
        heal(life = source.stats.lifeMax * 0.2f, mana = 0f, source, "restore_green", 1f, 1.5f, SoundAsset.HEAL1, 1.5f)
        return true
    }
}
