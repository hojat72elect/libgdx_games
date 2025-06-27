package io.github.masamune.component

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class ScreenBgd(
    val alpha: Float,
    val renderBlock: (Batch, FrameBuffer) -> Unit,
) : Component<ScreenBgd> {
    override fun type() = ScreenBgd

    companion object : ComponentType<ScreenBgd>()
}
