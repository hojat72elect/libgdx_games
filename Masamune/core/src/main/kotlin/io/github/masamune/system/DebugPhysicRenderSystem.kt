package io.github.masamune.system

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import io.github.masamune.PhysicWorld

class DebugPhysicRenderSystem(
    private val physicWorld: PhysicWorld = inject(),
    private val gameViewport: Viewport = inject(),
) : IntervalSystem() {

    private val b2dRenderer = Box2DDebugRenderer()

    override fun onTick() {
        gameViewport.apply()
        b2dRenderer.render(physicWorld, gameViewport.camera.combined)
    }

    override fun onDispose() {
        b2dRenderer.dispose()
    }

}
