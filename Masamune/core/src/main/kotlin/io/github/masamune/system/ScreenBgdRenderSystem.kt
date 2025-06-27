package io.github.masamune.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.HdpiUtils
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.FamilyOnAdd
import com.github.quillraven.fleks.FamilyOnRemove
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import io.github.masamune.asset.ShaderService
import io.github.masamune.asset.ShaderService.Companion.resize
import io.github.masamune.component.ScreenBgd
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import io.github.masamune.event.GameResizeEvent
import ktx.graphics.component1
import ktx.graphics.component2
import ktx.graphics.component3
import ktx.graphics.component4
import ktx.graphics.use
import ktx.log.logger

class ScreenBgdRenderSystem(
    private val batch: Batch = inject(),
) : IteratingSystem(family { all(ScreenBgd) }), FamilyOnAdd, FamilyOnRemove, EventListener {
    private var fbo = FrameBuffer(ShaderService.FBO_FORMAT, Gdx.graphics.width, Gdx.graphics.height, false)
    private var alpha = 0f

    override fun onAddEntity(entity: Entity) {
        log.debug { "onAddEntity screen bgd fbo" }
        val (alpha, renderBlock) = entity[ScreenBgd]
        renderBlock(batch, fbo)
        this.alpha = alpha
    }

    override fun onRemoveEntity(entity: Entity) {
        log.debug { "onRemoveEntity screen bgd fbo" }
        this.alpha = 0f
    }

    override fun onTick() {
        if (alpha == 0f) {
            return
        }

        HdpiUtils.glViewport(0, 0, fbo.width, fbo.height)
        batch.use(batch.projectionMatrix.idt()) {
            val (r, g, b, a) = batch.color
            batch.setColor(r, g, b, alpha)
            it.draw(fbo.colorBufferTexture, -1f, 1f, 2f, -2f)
            batch.setColor(r, g, b, a)
        }
    }

    override fun onTickEntity(entity: Entity) = Unit

    override fun onEvent(event: Event) {
        when (event) {
            is GameResizeEvent -> {
                fbo = fbo.resize(event.width, event.height)
                family.singleOrNull()?.let { entity ->
                    log.debug { "onResize screen bgd fbo" }
                    val (_, renderBlock) = entity[ScreenBgd]
                    renderBlock(batch, fbo)
                }
            }

            else -> Unit
        }
    }

    override fun onDispose() {
        fbo.dispose()
    }

    companion object {
        private val log = logger<ScreenBgdRenderSystem>()
    }
}
