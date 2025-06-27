package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Remove(var time: Float) : Component<Remove> {
    override fun type() = Remove

    companion object : ComponentType<Remove>()
}
