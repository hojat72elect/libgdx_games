package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import kotlinx.serialization.Serializable

@Serializable
data class Name(val name: String) : Component<Name> {
    override fun type() = Name

    companion object : ComponentType<Name>()
}
