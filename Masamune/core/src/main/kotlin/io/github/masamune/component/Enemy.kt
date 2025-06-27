package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.masamune.tiledmap.TiledObjectType

data class Enemy(
    val combatEntities: Map<TiledObjectType, Int>
) : Component<Enemy> {
    override fun type() = Enemy

    companion object : ComponentType<Enemy>()
}
