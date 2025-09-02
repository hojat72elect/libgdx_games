package box2dLight

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import kotlin.math.max

class PointLight(rayHandler: RayHandler, rays: Int, color: Color, distance: Float, x: Float, y: Float) :
    PositionalLight(rayHandler, rays, color, distance, x, y, 0f) {

    init {
        setEndPoints()
        update()
    }

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

    override fun setDirection(directionDegree: Float) {}

    /**
     * setDistance(float dist) MIN capped to 1cm
     */
    override fun setDistance(dist: Float) {
        var dist = dist
        dist *= RayHandler.gammaCorrectionParameter
        this.distance = max(dist, 0.01f)
        setEndPoints()
        if (staticLight) staticUpdate()
    }
}
