package io.github.masamune.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType


enum class FacingDirection {
    UNDEFINED, DOWN, LEFT, RIGHT, UP;

    val atlasKey: String = this.name.lowercase()
}

data class Facing(
    var direction: FacingDirection,
    var lastDirection: FacingDirection = direction,
) : Component<Facing> {

    fun hasChanged(): Boolean = direction != lastDirection

    fun setByDirection(direction: Vector2) = when {
        direction.y > 0f -> this.direction = FacingDirection.UP
        direction.y < 0f -> this.direction = FacingDirection.DOWN
        direction.x > 0f -> this.direction = FacingDirection.RIGHT
        direction.x < 0f -> this.direction = FacingDirection.LEFT
        else -> Unit
    }

    override fun type() = Facing

    companion object : ComponentType<Facing>()
}
