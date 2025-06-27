package io.github.masamune.ai.guard

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.system.CombatSystem
import ktx.ai.GdxAiDsl

class IsTurnMultipleOf(
    world: World,
    private val multiple: Int,
    private val ignoreFirstTurn: Boolean,
) : FleksGuard(world) {
    override fun World.onExecute(): Boolean {
        val turn = world.system<CombatSystem>().turn
        if (turn == 0 && ignoreFirstTurn) {
            return false
        }

        return turn % multiple == 0
    }
}

@GdxAiDsl
inline fun Task<Entity>.isTurnMultipleOf(
    world: World,
    multiple: Int,
    ignoreFirstTurn: Boolean = false,
    init: (@GdxAiDsl IsTurnMultipleOf).() -> Unit = {}
): Int {
    val eatTask = IsTurnMultipleOf(world, multiple, ignoreFirstTurn)
    eatTask.init()
    return addChild(eatTask)
}
