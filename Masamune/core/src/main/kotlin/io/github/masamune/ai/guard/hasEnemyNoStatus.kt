package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.World
import io.github.masamune.combat.buff.Buff
import io.github.masamune.combat.buff.PoisonBuff
import io.github.masamune.combat.buff.SlowBuff
import io.github.masamune.component.Combat
import io.github.masamune.component.Player
import io.github.masamune.component.StatusType
import ktx.ai.GdxAiDsl

class HasEnemyNoStatus(
    world: World,
    type: StatusType,
    private val combatEntities: Family = world.family { all(Combat, Player) }
) : FleksGuard(world) {
    private val predicate: (Buff) -> Boolean = when (type) {
        StatusType.POISON -> { buff -> buff is PoisonBuff }
        StatusType.SLOW -> { buff -> buff is SlowBuff }
    }

    override fun World.onExecute(): Boolean {
        return combatEntities.count { it[Combat].buffs.none(predicate) } > 0
    }
}

@GdxAiDsl
inline fun Task<Entity>.hasEnemyNoStatus(
    world: World,
    status: StatusType,
    init: (@GdxAiDsl HasEnemyNoStatus).() -> Unit = {}
): Int {
    val eatTask = HasEnemyNoStatus(world, status)
    eatTask.init()
    return addChild(eatTask)
}
