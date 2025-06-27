package io.github.masamune.combat.action

import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.buff.PoisonBuff
import io.github.masamune.tiledmap.ActionType

class PoisonAttackAction : Action(ActionType.POISON_ATTACK, ActionTargetType.SINGLE) {
    override fun ActionExecutorService.onStart() {
        addBuff(PoisonBuff(singleTarget, 0.05f, 2))
        play(SoundAsset.POISON1)
        addSfx(singleTarget, "poison1", 0.75f, 1.5f)
        wait(0.9f)
    }
}
