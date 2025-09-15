package com.bitfire.uracer.game.logic.gametasks.sounds.effects.engines;

import com.badlogic.gdx.math.MathUtils;
import com.bitfire.uracer.game.logic.helpers.TrackProgressData;
import com.bitfire.uracer.resources.Sounds;
import com.bitfire.uracer.utils.AlgebraMath;
import com.bitfire.uracer.utils.InterpolatedFloat;

public class EngineF40 extends EngineSoundSet {
    private static final boolean UseGears = true;
    private static final int MinGear = 1;
    private static final int MaxGear = 4;
    private final InterpolatedFloat ifactor = new InterpolatedFloat();
    private final InterpolatedFloat ispeed = new InterpolatedFloat();
    private final InterpolatedFloat ivolume = new InterpolatedFloat();
    private final TrackProgressData progressData;
    private float prevSpeed = 0;

    public EngineF40(TrackProgressData progressData) {
        super();
        this.progressData = progressData;
        engine = Sounds.carEngine_f40;
        rpm = 1000;
        gear = MinGear;
        ivolume.fixup = false;
    }

    @Override
    public float getGlobalVolume() {
        float target_vol = 0;
        float scale_mt = 50;
        if (hasPlayer && progressData.hasTarget && !progressData.isWarmUp) {
            float d = progressData.playerDistance.get() - progressData.targetDistance.get();
            d = MathUtils.clamp(d, -scale_mt, scale_mt);
            d /= scale_mt; // normalized range
            float to_target = AlgebraMath.fixup(d);

            if (to_target < 0) {
                // player behind
                // fade out on distance from target (use tensive music meters scaling)
                target_vol = MathUtils.clamp(to_target + 1, 0, 1);
            } else {
                // player heading the race
                target_vol = 1;
            }
        }

        float computedTarget = 0.1f - 0.05f * target_vol * player.carState.currSpeedFactor;
        ivolume.set(computedTarget, 0.005f);
        return ivolume.get();
    }

    @Override
    public boolean hasGears() {
        return UseGears;
    }

    @Override
    public float getGearRatio() {
        if (!UseGears) {
            return 3;
        }

        // {3, 1, 0.7f, 0.5f, 0.3f, 0.2f, 0.1f}

        float res = 1;


        switch (gear) {
            case 0:
            case 1:
                res = 1.4f;
                break;
            case 2:
                break;
            case 3:
                res = 0.66f;
                break;
            case 4:
                res = 0.5f;
                break;
        }

        return res;
    }

    @Override
    public void updatePitches() {
        float lowLimit = 6000f;
        float hiLimit = 10000f;

        // rpm = 1000;
        float rpmLow = rpm / lowLimit;
        float rpmDef = rpm / hiLimit;

        // dbg
        // setVolume(1, 1 * SoundManager.SfxVolumeMul);
        // setVolume(4, 1 * SoundManager.SfxVolumeMul);
        // dbg

        float from = 0.55f;
        float to = 0.05f;
        float amount = from - (from - to) * player.carState.currSpeedFactor;
        // Gdx.app.log("", amount + "");
        // float amount = 0f;

        // sample specific
        if (rpm < lowLimit) {
            setXnaPitch(0, rpmLow - amount);
            setXnaPitch(1, rpmDef + 0.2f - amount);
        }

        setXnaPitch(2, rpmDef - 0.4f - amount);
        setXnaPitch(3, rpmDef - 0.8f - amount);
        setXnaPitch(4, rpmDef - 0.2f - amount);
        setXnaPitch(5, rpmDef - 0.8f - amount);
        setXnaPitch(6, rpmDef - 0.8f - amount);
    }

    @Override
    public float updateRpm(float load) {
        if (UseGears) {
            if (!hasPlayer) {
                rpm = 1000;
                return 1000;
            }

            updateGear();
            ispeed.set(player.carState.currSpeedFactor, 0.85f);
            float sf = ispeed.get();

            float q = 15000;
            float factor = q * sf * getGearRatio();

            if (sf < prevSpeed) {
                factor = q * sf * getGearRatioOff();
            }

            if (sf < prevSpeed) {
                ifactor.set(factor, 0.6f);
            } else {
                ifactor.set(factor, 0.85f);
            }

            rpm = 1000 + ifactor.get() + (load < 0 ? load : load * (22 - 10 * player.carState.currSpeedFactor));

            rpm = MathUtils.clamp(rpm, 1000, 10000);

            prevSpeed = sf;
            return rpm;
        }

        return super.updateRpm(load);
    }

    private void updateGear() {
        float sf = player.carState.currSpeedFactor;

        if (sf > prevSpeed && gear < MaxGear) {
            if (rpm > 9800) {
                shiftUp();
            }
        } else if (sf < prevSpeed && gear > MinGear) {

            if (gear == 2) {
                if (rpm < 2000) {
                    shiftDown();
                }
            } else {
                if (rpm < 7000) {
                    shiftDown();
                }
            }
        }
    }

    @Override
    public void shiftUp() {
        if (UseGears) {
            if (gear > 0 && gear < MaxGear) {
                gear++;
            }

            if (gear == 0) {
                gear++;
            }

            rpm = MathUtils.clamp(rpm, 1000, 10000);
        }

        gear = MathUtils.clamp(gear, MinGear, MaxGear);
    }

    @Override
    public void shiftDown() {
        if (UseGears) {

            if (gear > MinGear && gear <= MaxGear) {
                gear--;

                rpm = MathUtils.clamp(rpm, 1000, 10000);
            }
        }

        gear = MathUtils.clamp(gear, MinGear, MaxGear);
    }

    @Override
    public void reset() {
        super.reset();

        rpm = 1000;
        gear = 1;
        ifactor.reset(true);
        ispeed.reset(true);
        ivolume.reset(true);
    }
}
