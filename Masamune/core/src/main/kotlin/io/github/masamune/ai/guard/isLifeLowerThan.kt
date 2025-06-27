package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.component.CharacterStats
import ktx.ai.GdxAiDsl

class IsLifeLowerThan(world: World, private val threshold: Float) : FleksGuard(world) {
    override fun World.onExecute(): Boolean {
        val stats = entity[CharacterStats]
        if (threshold < 1f) {
            // percentage based threshold
            return stats.life <= stats.lifeMax * threshold
        }

        // absolute based threshold
        return stats.life <= threshold
    }
}

@GdxAiDsl
inline fun Task<Entity>.isLifeLowerThan(
    world: World,
    threshold: Float,
    init: (@GdxAiDsl IsLifeLowerThan).() -> Unit = {}
): Int {
    val eatTask = IsLifeLowerThan(world, threshold)
    eatTask.init()
    return addChild(eatTask)
}
