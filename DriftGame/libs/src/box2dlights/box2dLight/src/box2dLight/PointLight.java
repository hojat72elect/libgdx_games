package box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class PointLight extends PositionalLight {

    public PointLight(RayHandler rayHandler, int rays, Color color,
                      float distance, float x, float y) {
        super(rayHandler, rays, color, distance, x, y, 0f);
        setEndPoints();
        update();
    }

    final void setEndPoints() {
        float angleNum = 360f / (rayNum - 1);
        for (int i = 0; i < rayNum; i++) {
            final float angle = angleNum * i;
            sin[i] = MathUtils.sinDeg(angle);
            cos[i] = MathUtils.cosDeg(angle);
            endX[i] = distance * cos[i];
            endY[i] = distance * sin[i];
        }
    }

    @Override
    public void setDirection(float directionDegree) {
    }

    /**
     * setDistance(float dist) MIN capped to 1cm
     */
    @Override
    public void setDistance(float dist) {
        dist *= RayHandler.gammaCorrectionParameter;
        this.distance = Math.max(dist, 0.01f);
        setEndPoints();
        if (staticLight)
            staticUpdate();
    }
}
