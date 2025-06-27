package io.github.masamune.combat.action

import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.buff.SlowBuff
import io.github.masamune.tiledmap.ActionType

class SlowAction : Action(ActionType.SLOW, ActionTargetType.SINGLE, 5) {
    override fun ActionExecutorService.onUpdate(): Boolean {
        addBuff(SlowBuff(singleTarget, 7, 3))
        play(SoundAsset.SLOW)
        addSfx(singleTarget, "slow1", 1.25f, 1.75f)
        wait(1.5f)
        return true
    }
}
