package io.github.masamune.component

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class AI(
    val behaviorTree: BehaviorTree<Entity>,
) : Component<AI> {
    override fun type() = AI

    companion object : ComponentType<AI>()
}
