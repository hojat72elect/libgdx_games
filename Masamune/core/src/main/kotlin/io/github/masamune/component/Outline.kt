package io.github.masamune.component

import com.badlogic.gdx.graphics.Color
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Outline(val color: Color) : Component<Outline> {
    override fun type() = Outline

    companion object : ComponentType<Outline>() {
        val COLOR_NEUTRAL = Color(1f, 1f, 1f, 1f)
        val COLOR_ENEMY = Color(0.9f, 0.2f, 0f, 0.9f)
    }
}
