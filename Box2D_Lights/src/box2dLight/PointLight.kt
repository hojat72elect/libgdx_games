package box2dLight

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import kotlin.math.max

/**
 * Light shaped as a circle with given radius. It extends [PositionalLight].
 *
 * Creates light shaped as a circle with given radius.
 *
 * @param rayHandler not `null` instance of RayHandler.
 * @param rays number of rays - more rays make light to look more realistic but will decrease performance, can't be less than MIN_RAYS.
 * @param color color, set to `null` to use the default color.
 * @param distance distance of light, soft shadow length is set to distance * 0.1f.
 * @param x horizontal position in world coordinates.
 * @param y vertical position in world coordinates.
 *
 *  Creates light shaped as a circle with default radius (15f), color and position (0f, 0f).
 *  @param rayHandler not `null` instance of RayHandler
 *  @param rays       number of rays - more rays make light to look more realistic
 *  but will decrease performance, can't be less than MIN_RAYS
 */
class PointLight(rayHandler: RayHandler, rays: Int, color: Color? = DefaultColor, distance: Float = 15f, x: Float = 0f, y: Float = 0f) :
    PositionalLight(rayHandler, rays, color, distance, x, y, 0f) {


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
     * Updates light basing on its distance and rayNum
     */
    fun setEndPoints() {
        val angleNum = 360f / (rayNum - 1)
        for (i in 0..<rayNum) {
            val angle = angleNum * i
            sin[i] = MathUtils.sinDeg(angle)
            cos[i] = MathUtils.cosDeg(angle)
            endX[i] = distance * cos[i]
            endY[i] = distance * sin[i]
        }
    }

    /**
     * Not applicable for this light type
     */
    @Deprecated("")
    override fun setDirection(directionDegree: Float) {
    }
}
