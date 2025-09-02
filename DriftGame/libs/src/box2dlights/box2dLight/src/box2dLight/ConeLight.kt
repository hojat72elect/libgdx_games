package box2dLight

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import kotlin.math.max

/**
 * Light is a data container for all the light parameters.
 * You can create instances of Light also with help of rayHandler addLight method.
 */
class ConeLight(
    rayHandler: RayHandler,
    rays: Int,
    color: Color,
    distance: Float,
    x: Float,
    y: Float,
    directionDegree: Float,
    private var coneDegree: Float
) : PositionalLight(rayHandler, rays, color, distance, x, y, directionDegree) {

    init {
        setConeDegree(coneDegree)
        setDirection(direction)
        update()
    }

    override fun setDirection(direction: Float) {
        this.direction = direction
        for (i in 0..<rayNum) {
            val angle = direction + coneDegree - 2f * coneDegree * i / (rayNum - 1f)
            sin[i] = MathUtils.sinDeg(angle)
            val s = sin[i]
            cos[i] = MathUtils.cosDeg(angle)
            val c = cos[i]
            endX[i] = distance * c
            endY[i] = distance * s
        }
        if (staticLight) staticUpdate()
    }

    /**
     * How big is the arc of cone. Arc angle = coneDegree * 2
     * @param coneDegree the coneDegree to set.
     */
    fun setConeDegree(coneDegree: Float) {
        var coneDegree = coneDegree
        if (coneDegree < 0) coneDegree = 0f
        if (coneDegree > 180) coneDegree = 180f
        this.coneDegree = coneDegree
        setDirection(direction)
    }

    /**
     * setDistance(float dist) MIN capped to 1cm
     */
    override fun setDistance(dist: Float) {
        var dist = dist
        dist *= RayHandler.gammaCorrectionParameter
        this.distance = max(dist, 0.01f)
        setDirection(direction)
    }
}
