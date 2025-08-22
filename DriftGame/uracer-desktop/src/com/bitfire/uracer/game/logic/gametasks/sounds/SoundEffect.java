package com.bitfire.uracer.game.logic.gametasks.sounds;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.bitfire.uracer.URacer;
import com.bitfire.uracer.game.logic.gametasks.PlayerClient;

public abstract class SoundEffect extends PlayerClient implements Disposable {
    private static final int WaitLimit = 1000;
    // implements a workaround for Android, need to async-wait
    // for sound loaded but libgdx doesn't expose anything for this!
    private static final int ThrottleMs = 100;
    protected boolean isPaused = false;

    public static long play(Sound sound, float volume) {
        if (URacer.Game.isDesktop()) {
            return sound.play(volume);
        } else {
            int waitCounter = 0;
            long soundId = 0;

            boolean ready = false;
            while (!ready && waitCounter < WaitLimit) {
                soundId = sound.play(volume);
                ready = (soundId != 0);
                waitCounter++;
                try {
                    Thread.sleep(ThrottleMs);
                } catch (InterruptedException ignored) {
                }
            }

            return soundId;
        }
    }

    public static long loop(Sound sound, float volume) {
        if (URacer.Game.isDesktop()) {
            return sound.loop(volume);
        } else {
            int waitCounter = 0;
            long soundId = 0;

            boolean ready = false;
            while (!ready && waitCounter < WaitLimit) {
                soundId = sound.loop(volume);
                ready = (soundId != 0);
                waitCounter++;
                try {
                    Thread.sleep(ThrottleMs);
                } catch (InterruptedException ignored) {
                }
            }

            return soundId;
        }
    }

    public void stop() {
    }

    public void gameRestart() {
    }

    public void gameReset() {
    }

    public void tick() {
    }

    public void gamePause() {
        isPaused = true;
    }

    public void gameResume() {
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
