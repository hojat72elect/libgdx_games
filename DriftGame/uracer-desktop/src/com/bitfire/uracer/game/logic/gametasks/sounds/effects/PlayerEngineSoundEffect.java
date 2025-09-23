package com.bitfire.uracer.game.logic.gametasks.sounds.effects;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.bitfire.uracer.game.GameEvents;
import com.bitfire.uracer.game.Time;
import com.bitfire.uracer.game.events.CarEvent;
import com.bitfire.uracer.game.events.PlayerDriftStateEvent;
import com.bitfire.uracer.game.logic.gametasks.sounds.SoundEffect;
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.engines.EngineF40;
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.engines.EngineSoundSet;
import com.bitfire.uracer.game.logic.helpers.TrackProgressData;
import com.bitfire.uracer.game.player.PlayerCar;
import com.bitfire.uracer.utils.AlgebraMath;
import net.sourceforge.jFuzzyLogic.FIS;
import org.jetbrains.annotations.NotNull;

public final class PlayerEngineSoundEffect extends SoundEffect {

    // throttle autosoftener
    private static final boolean ThrottleAutoSoftener = true;
    private static final int MinSoftnessTicks = 5;
    private static final int MaxSoftnessTicks = 20;
    // inference engine
    private final FIS feLoad;
    private final Time driftTimer = new Time();
    private final EngineSoundSet soundset;
    private float load;
    private float throttle;
    private final CarEvent.Listener carListener = new CarEvent.Listener() {
        @Override
        public void handle(@NotNull Object source, CarEvent.Type type, @NotNull CarEvent.Order order) {
            switch (type) {
                case OnCollision:
                    throttle = 0;
                    break;
                case OnOutOfTrack:
                case OnBackInTrack:
                    break;
            }
        }
    };
    private int softnessTicks = 0;
    private final PlayerDriftStateEvent.Listener playerListener = (source, type, order) -> {
        if (!hasPlayer) return;
        switch (type) {
            case OnBeginDrift:
                if (player.isThrottling) {

                    driftTimer.start();
                    float ratio = player.carState.currSpeedFactor;
                    softnessTicks = (int) (ratio * (float) MaxSoftnessTicks);
                    softnessTicks = MathUtils.clamp(softnessTicks, MinSoftnessTicks, MaxSoftnessTicks);
                }
                break;
            case OnEndDrift:
                driftTimer.stop();
                break;
        }
    };

    public PlayerEngineSoundEffect(TrackProgressData progressData) {
        feLoad = FIS.load(Gdx.files.getFileHandle("data/audio/car-engine/fuzzy/engineLoad.fcl", FileType.Internal).read(), true);
        load = 0;
        throttle = 0;
        soundset = new EngineF40(progressData);
    }

    public EngineSoundSet getSoundSet() {
        return soundset;
    }

    private void attach() {
        GameEvents.driftState.addListener(playerListener, PlayerDriftStateEvent.Type.OnBeginDrift);
        GameEvents.driftState.addListener(playerListener, PlayerDriftStateEvent.Type.OnEndDrift);
        GameEvents.playerCar.addListener(carListener, CarEvent.Type.OnCollision);
        GameEvents.playerCar.addListener(carListener, CarEvent.Type.OnOutOfTrack);
        GameEvents.playerCar.addListener(carListener, CarEvent.Type.OnBackInTrack);
    }

    private void detach() {
        GameEvents.driftState.removeListener(playerListener, PlayerDriftStateEvent.Type.OnBeginDrift);
        GameEvents.driftState.removeListener(playerListener, PlayerDriftStateEvent.Type.OnEndDrift);
        GameEvents.playerCar.removeListener(carListener, CarEvent.Type.OnCollision);
        GameEvents.playerCar.removeListener(carListener, CarEvent.Type.OnOutOfTrack);
        GameEvents.playerCar.removeListener(carListener, CarEvent.Type.OnBackInTrack);
    }

    @Override
    public void dispose() {
        detach();
        stop();
    }

    private float fuzzyLoadCompute(float throttle, float rpm) {
        feLoad.setVariable("throttle", throttle);
        feLoad.setVariable("rpm", rpm);
        feLoad.evaluate();
        return (float) feLoad.getVariable("load").getValue();
    }

    @Override
    public void tick() {
        if (!hasPlayer || isPaused) return;

        if (player.isThrottling) {
            if (soundset.hasGears()) {
                throttle += 8f;
            } else {
                throttle += 10;
            }

            if (!soundset.hasGears() && ThrottleAutoSoftener && !driftTimer.isStopped()
                    && driftTimer.elapsed().ticks < softnessTicks) {
                throttle *= AlgebraMath.damping(0.8f);
                Gdx.app.log("", "ticks=" + driftTimer.elapsed().ticks);
            }
        } else {
            if (soundset.hasGears()) {
                throttle *= AlgebraMath.damping(0.85f);
            } else {
                throttle *= AlgebraMath.damping(0.8f);
            }
        }

        throttle = AlgebraMath.fixup(throttle);
        throttle = MathUtils.clamp(throttle, 0, 100);

        float rpm = soundset.updateRpm(load);
        load = AlgebraMath.fixup(fuzzyLoadCompute(throttle, rpm));

        // compute volumes
        soundset.updateVolumes(load);
        soundset.updatePitches();
    }

    @Override
    public void stop() {
        soundset.stop();
    }

    @Override
    public void gamePause() {
        super.gamePause();
        soundset.stop();
    }

    @Override
    public void gameResume() {
        super.gameResume();
        soundset.start();
    }

    @Override
    public void gameReset() {
        gameRestart();
    }

    @Override
    public void gameRestart() {
        soundset.reset();
        soundset.stop();
        soundset.start();
    }

    @Override
    public void player(PlayerCar player) {
        super.player(player);

        soundset.setPlayer(player);

        if (hasPlayer) {
            attach();
            soundset.start();
        } else {
            soundset.stop();
            detach();
        }
    }
}
