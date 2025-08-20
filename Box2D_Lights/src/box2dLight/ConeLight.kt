package box2dLight

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import kotlin.math.max

/**
 * Light shaped as a circle's sector with given radius, direction and angle.
 * Creates light shaped as a circle's sector with given radius, direction and arc angle.
 *
 * @param rayHandler not `null` instance of RayHandler.
 * @param rays number of rays - more rays make light to look more realistic but will decrease performance, can't be less than MIN_RAYS.
 * @param color color, set to `null` to use the default color.
 * @param distance distance of cone light, soft shadow length is set to distance * 0.1f.
 * @param x axis position.
 * @param y axis position.
 * @param directionDegree direction of cone light.
 * @param coneDegree half-size of cone light, centered over direction.
 */
class ConeLight(rayHandler: RayHandler, rays: Int, color: Color?, distance: Float, x: Float, y: Float, directionDegree: Float, var coneDegree: Float) :
    PositionalLight(rayHandler, rays, color, distance, x, y, directionDegree) {


    init {
        coneDegree = MathUtils.clamp(coneDegree, 0f, 180f)
        dirty = true
    }

    public override fun update() {
        if (rayHandler.pseudo3d) {
            prepareFixtureData()
            updateDynamicShadowMeshes()
        }

        updateBody()
        if (dirty) setEndPoints()

        if (cull()) return
        if (staticLight && !dirty) return

        dirty = false
        updateMesh()
    }

    /**
     * Sets light direction
     * Actual recalculations will be done only on [.update] call
     */
    override fun setDirection(direction: Float) {
        this.direction = direction
        dirty = true
    }

    /**
     * Sets light distance
     * MIN value capped to 0.1f meter
     * Actual recalculations will be done only on [.update] call
     */
    override fun setDistance(dist: Float) {
        var dist = dist
        dist *= RayHandler.gammaCorrectionParameter
        this.distance = max(dist, 0.01f)
        dirty = true
    }

    /**
     * Updates lights sector basing on distance, direction and coneDegree
     */
    private fun setEndPoints() {
        for (i in 0..<rayNum) {
            val angle = (direction + coneDegree - 2f * coneDegree * i
                    / (rayNum - 1f))
            sin[i] = MathUtils.sinDeg(angle)
            val s = sin[i]
            cos[i] = MathUtils.cosDeg(angle)
            val c = cos[i]
            endX[i] = distance * c
            endY[i] = distance * s
        }
    }
}
