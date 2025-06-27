package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class Player : Component<Player> {
    override fun type() = Player

    companion object : ComponentType<Player>()
}
