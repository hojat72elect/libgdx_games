package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

enum class StatusType {
    POISON, SLOW
}

data class StatusAilment(val type: StatusType) : Component<StatusAilment> {
    override fun type() = StatusAilment

    companion object : ComponentType<StatusAilment>()
}
