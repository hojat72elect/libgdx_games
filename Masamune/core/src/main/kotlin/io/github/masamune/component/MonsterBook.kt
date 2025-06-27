package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.masamune.tiledmap.TiledObjectType
import kotlinx.serialization.Serializable

@Serializable
data class MonsterBook(
    val knownTypes: MutableSet<TiledObjectType>,
) : Component<MonsterBook> {
    override fun type() = MonsterBook

    companion object : ComponentType<MonsterBook>()
}
