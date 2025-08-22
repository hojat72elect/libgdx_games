package com.bitfire.uracer.game.tween;

import com.bitfire.uracer.URacer;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.TweenManager;

/**
 * This tweener is a wall-clocked tweener, thus it will NOT take the timeMultiplier modulation into account
 */
public final class SysTweener {
    private static final TweenManager manager = new TweenManager();

    private SysTweener() {
    }

    public static void dispose() {
        clear();
    }

    public static void clear() {
        manager.killAll();
    }

    public static void start(Timeline timeline) {
        timeline.start(manager);
    }

    public static void stop(Object target) {
        manager.killTarget(target);
    }

    public static void update() {
        manager.update(URacer.Game.getLastDeltaMs());
    }
}
