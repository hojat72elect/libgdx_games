package io.github.masamune.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Facing
import io.github.masamune.component.FollowPath
import io.github.masamune.component.Move
import io.github.masamune.component.Transform
import ktx.log.logger
import ktx.math.vec2
import kotlin.math.cos
import kotlin.math.sin

class FollowPathSystem : IteratingSystem(family { all(FollowPath, Transform, Move) }) {
    private val tmpCenter = vec2()

    override fun onTickEntity(entity: Entity) {
        val pathCmp = entity[FollowPath]
        entity[Transform].centerTo(tmpCenter)

        if (pathCmp.currentVertexIdx == -1) {
            pathCmp.currentVertexIdx = closestVertexIdx(pathCmp.path, tmpCenter)
        }

        var targetVertex = pathCmp.path[pathCmp.currentVertexIdx]
        if (targetVertex.distanceTo(tmpCenter) < 0.05f) {
            pathCmp.currentVertexIdx = (pathCmp.currentVertexIdx + 1) % pathCmp.path.size
            if (pathCmp.removeAtEnd && pathCmp.currentVertexIdx == 0) {
                entity.configure { it -= FollowPath }
                entity[Move].direction.setZero()
                return
            }
            log.debug { "Reached target vertex $targetVertex. Going to next vertex ${pathCmp.path[pathCmp.currentVertexIdx]}." }
            targetVertex = pathCmp.path[pathCmp.currentVertexIdx]
        }

        val angle = MathUtils.atan2(targetVertex.y - tmpCenter.y, targetVertex.x - tmpCenter.x)
        val moveDirection = entity[Move].direction
        moveDirection.set(cos(angle), sin(angle))
        entity.getOrNull(Facing)?.setByDirection(moveDirection)
    }

    private fun Vector2.distanceTo(other: Vector2): Float {
        val diffX = x - other.x
        val diffY = y - other.y
        return diffX * diffX + diffY * diffY
    }

    private fun closestVertexIdx(path: List<Vector2>, entityPosition: Vector2): Int {
        var vertexIdx = 0
        var closestDistance = Float.MAX_VALUE

        path.forEachIndexed { idx, vertex ->
            val distance = vertex.distanceTo(entityPosition)
            if (distance < closestDistance) {
                closestDistance = distance
                vertexIdx = idx
            }
        }

        return vertexIdx
    }

    companion object {
        private val log = logger<FollowPathSystem>()
    }

}
