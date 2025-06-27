package io.github.masamune.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Dialog(var dialogName: String) : Component<Dialog> {
    override fun type() = Dialog

    companion object : ComponentType<Dialog>()
}
