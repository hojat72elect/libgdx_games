package io.github.masamune.combat.buff

import com.github.quillraven.fleks.Entity
import io.github.masamune.combat.ActionExecutorService

class SlowBuff(
    override val owner: Entity,
    private val amount: Int,
    private var duration: Int
) : OnTurnBuff {

    override fun ActionExecutorService.onApply() {
        owner.stats.agility -= amount
    }

    override fun ActionExecutorService.onRemove() {
        owner.stats.agility += amount
    }

    override fun ActionExecutorService.onTurnBegin() {
        --duration
        if (duration <= 0) {
            removeBuff()
            return
        }
    }

}
