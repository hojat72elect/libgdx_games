package io.github.masamune.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

/**
 * Component for [position], [size], [scale], [rotation] and optional [offset] values of an entity.
 * The default scale value is one world unit (=1).
 * The rotation is in degrees.
 */
data class Transform(
    val position: Vector3,
    val size: Vector2,
    var scale: Float = 1f,
    var rotation: Float = 0f,
    val offset: Vector2 = vec2(),
) : Component<Transform>, Comparable<Transform> {

    fun center(): Vector2 = vec2(
        position.x + size.x * 0.5f,
        position.y + size.y * 0.5f
    )

    fun centerTo(result: Vector2) {
        result.x = position.x + size.x * 0.5f
        result.y = position.y + size.y * 0.5f
    }

    override fun type() = Transform

    override fun compareTo(other: Transform): Int {
        return when {
            position.z > other.position.z -> 1
            position.z < other.position.z -> -1
            position.y + offset.y > other.position.y + other.offset.y -> -1
            position.y + offset.y < other.position.y + other.offset.y -> 1
            position.x + offset.x > other.position.x + other.offset.x -> 1
            position.x + offset.x < other.position.x + other.offset.x -> -1
            else -> 0
        }
    }

    companion object : ComponentType<Transform>()
}
