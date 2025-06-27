package io.github.masamune.ai.task

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.ai.GdxAiDsl

class UseAttackTask(world: World) : FleksTask(world) {
    override fun World.onExecute() {
        useAction { attackAction }
    }
}

@GdxAiDsl
inline fun Task<Entity>.useAttack(
    world: World,
    init: (@GdxAiDsl UseAttackTask).() -> Unit = {}
): Int {
    val eatTask = UseAttackTask(world)
    eatTask.init()
    return addChild(eatTask)
}
