package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.World
import io.github.masamune.component.Combat
import io.github.masamune.component.Player
import io.github.masamune.isEntityHurt
import ktx.ai.GdxAiDsl

class HasHurtFriends(
    world: World,
    private val combatEntities: Family = world.family { all(Combat).none(Player) }
) : FleksGuard(world) {
    override fun World.onExecute(): Boolean {
        return combatEntities.count { world.isEntityHurt(it) } > 0
    }
}

@GdxAiDsl
inline fun Task<Entity>.hasHurtFriends(
    world: World,
    init: (@GdxAiDsl HasHurtFriends).() -> Unit = {}
): Int {
    val eatTask = HasHurtFriends(world)
    eatTask.init()
    return addChild(eatTask)
}
