package com.bitfire.uracer.game.player;

import com.bitfire.uracer.game.GameEvents;
import com.bitfire.uracer.game.Time;
import com.bitfire.uracer.game.actors.Car;
import com.bitfire.uracer.game.events.PlayerDriftStateEvent.Type;
import com.bitfire.uracer.utils.AlgebraMath;

public final class DriftState {
    public boolean isDrifting = false;
    public float lateralForcesFront = 0, lateralForcesRear = 0;
    public float driftStrength;
    private final Car car;
    private boolean hasCollided = false;
    private final float invMaxGrip;
    private float lastRear = 0;
    private float lastFront = 0;
    private final Time time;
    private final Time collisionTime;

    public DriftState(Car car) {
        this.car = car;
        this.time = new Time();
        this.collisionTime = new Time();
        this.invMaxGrip = car.getCarModel().inv_max_grip;
        reset();
    }

    public void dispose() {
        time.dispose();
        collisionTime.dispose();
        GameEvents.driftState.removeAllListeners();
    }

    public void reset() {
        time.reset();
        collisionTime.reset();

        lastFront = 0;
        lastRear = 0;
        hasCollided = false;
        isDrifting = false;
        lateralForcesFront = 0;
        lateralForcesRear = 0;
        driftStrength = 0;
    }

    // onCollision?
    public void invalidateByCollision() {
        if (!isDrifting) {
            return;
        }

        isDrifting = false;
        hasCollided = true;
        collisionTime.start();
        time.stop();
        GameEvents.driftState.trigger(car, Type.OnEndDrift);
    }

    public void update(float latForceFrontY, float latForceRearY, float velocityLength) {

        // lateral forces are in the range [-max_grip, max_grip]
        lateralForcesFront = AlgebraMath.lowpass(lastFront, latForceFrontY, 0.2f);
        lastFront = lateralForcesFront;
        lateralForcesFront = AlgebraMath.clamp(Math.abs(lateralForcesFront) * invMaxGrip, 0f, 1f); // normalize

        lateralForcesRear = AlgebraMath.lowpass(lastRear, latForceRearY, 0.2f);
        lastRear = lateralForcesRear;
        lateralForcesRear = AlgebraMath.clamp(Math.abs(lateralForcesRear) * invMaxGrip, 0f, 1f); // normalize

        // compute strength
        driftStrength = AlgebraMath.fixup((lateralForcesFront + lateralForcesRear) * 0.5f);

        if (hasCollided) {
            // ignore drifts for a couple of seconds
            if (collisionTime.elapsed().tickSeconds >= 0.5f) {
                collisionTime.stop();
                hasCollided = false;
            }
        } else {
            if (!isDrifting) {
                // search for onBeginDrift
                if (driftStrength > 0.4f && velocityLength > 20) {
                    isDrifting = true;
                    time.start();
                    GameEvents.driftState.trigger(car, Type.OnBeginDrift);
                }
            } else {
                // search for onEndDrift
                if (driftStrength < 0.2f || velocityLength < 15f) {
                    time.stop();
                    isDrifting = false;
                    GameEvents.driftState.trigger(car, Type.OnEndDrift);
                }
            }
        }
    }
}
