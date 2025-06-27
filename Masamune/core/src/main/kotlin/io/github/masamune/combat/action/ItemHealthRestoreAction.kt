package io.github.masamune.combat.action

import io.github.masamune.asset.SoundAsset
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.tiledmap.ActionType

class ItemHealthRestoreAction : Action(ActionType.ITEM_HEALTH_RESTORE, ActionTargetType.SINGLE, defensive = true) {

    override fun ActionExecutorService.onUpdate(): Boolean {
        heal(life = source.itemStats.life, mana = 0f, singleTarget, "restore_green", 1f, 1.5f, SoundAsset.HEAL1, 1.5f)
        return true
    }

}
