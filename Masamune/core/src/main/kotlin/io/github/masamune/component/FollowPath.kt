package io.github.masamune.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class FollowPath(
    val path: List<Vector2>,
    val removeAtEnd: Boolean = false,
    var currentVertexIdx: Int = -1,
) : Component<FollowPath> {
    override fun type() = FollowPath

    companion object : ComponentType<FollowPath>()
}
