package io.github.masamune.system

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import io.github.masamune.component.Graphic
import io.github.masamune.component.Transform
import ktx.graphics.use

class DebugRenderSystem(
    private val shapeRenderer: ShapeRenderer = ShapeRenderer(),
    private val gameViewport: Viewport = inject(),
) : IteratingSystem(family { all(Transform, Graphic) }) {

    override fun onTick() {
        gameViewport.apply()
        shapeRenderer.use(ShapeRenderer.ShapeType.Line, gameViewport.camera) { renderer ->
            renderer.color = Color.RED
            family.forEach { drawEntityBoundary(it, withScale = true) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Line, gameViewport.camera) { renderer ->
            renderer.color = Color.BLUE
            family.forEach { drawEntityBoundary(it, withScale = false) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Point, gameViewport.camera) { renderer ->
            renderer.color = Color.GREEN
            family.forEach { drawEntityPosition(it) }
        }
    }

    private fun drawEntityBoundary(entity: Entity, withScale: Boolean) {
        val (position, size, scale, rotation) = entity[Transform]
        val scl = if (withScale) scale else 1f

        shapeRenderer.rect(
            position.x, position.y,
            size.x * 0.5f, size.y * 0.5f,
            size.x, size.y,
            scl, scl,
            rotation
        )
    }

    private fun drawEntityPosition(entity: Entity) {
        val (position) = entity[Transform]
        shapeRenderer.point(position.x, position.y, position.z)
    }

    override fun onTickEntity(entity: Entity) = Unit

    override fun onDispose() {
        shapeRenderer.dispose()
    }

}
