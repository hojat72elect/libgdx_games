package io.github.masamune.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

data class Move(var speed: Float = 0f, val direction: Vector2 = vec2()) : Component<Move> {

    override fun type() = Move

    companion object : ComponentType<Move>()
}
