package com.nopalsoft.superjumper.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlatformPiece implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_DESTROY = 1;
    public int state;

    public static final float DRAW_WIDTH_NORMAL = Platform.DRAW_WIDTH_NORMAL / 2f;
    public static final float DRAW_HEIGHT_NORMAL = Platform.DRAW_HEIGHT_NORMAL;
    public static final float WIDTH_NORMAL = Platform.WIDTH_NORMAL / 2f;
    public static final float HEIGHT_NORMAL = Platform.HEIGHT_NORMAL;

    public int color;

    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;
    public int type;

    public final Vector2 position;

    public float stateTime;
    public float angleDegree;

    public PlatformPiece() {
        position = new Vector2();
    }

    public void initialize(float x, float y, int type, int color) {
        position.set(x, y);
        this.type = type;
        this.color = color;
        angleDegree = 0;
        stateTime = 0;
        state = STATE_NORMAL;
    }

    public void update(float delta, Body body) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        angleDegree = MathUtils.radiansToDegrees * body.getAngle();

        if (angleDegree > 90) {
            body.setTransform(position, MathUtils.degreesToRadians * 90);
            angleDegree = 90;
        }

        stateTime += delta;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    public void setDestroy() {
        state = STATE_DESTROY;
    }
}
