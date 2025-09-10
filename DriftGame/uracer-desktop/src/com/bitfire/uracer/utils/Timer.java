package com.bitfire.uracer.utils;

import com.badlogic.gdx.utils.TimeUtils;

public final class Timer {

    private static final float oneOnOneBillion = 1.0f / 1000000000.0f;
    private long nsStartTime;
    private boolean stopped;
    private long nsStopTime;
    private float elapsedSecs;

    public Timer() {
        reset();
    }

    public void pause() {
        stopped = true;
        nsStopTime = TimeUtils.nanoTime();
    }

    public void resume() {
        stopped = false;
    }

    public void reset() {
        stopped = false;

        // abs
        nsStartTime = TimeUtils.nanoTime();
        nsStopTime = 0;
    }

    public void update() {
        long now = (stopped ? nsStopTime : TimeUtils.nanoTime());
        elapsedSecs = (float) (now - nsStartTime) * oneOnOneBillion;
    }

    /**
     * Returns the elapsed time in seconds.
     */
    public float elapsed() {
        return elapsedSecs;
    }
}
