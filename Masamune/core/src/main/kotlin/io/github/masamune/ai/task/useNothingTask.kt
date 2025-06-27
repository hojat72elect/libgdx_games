package io.github.masamune.ai.task

import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.combat.action.DefaultAction
import io.github.masamune.component.Combat
import ktx.ai.GdxAiDsl

class UseNothingTask(world: World) : FleksTask(world) {
    override fun World.onExecute() {
        entity[Combat].action = DefaultAction
    }
}

@GdxAiDsl
inline fun Task<Entity>.useNothing(
    world: World,
    init: (@GdxAiDsl UseNothingTask).() -> Unit = {}
): Int {
    val eatTask = UseNothingTask(world)
    eatTask.init()
    return addChild(eatTask)
}
