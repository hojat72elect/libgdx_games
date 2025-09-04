package com.bitfire.uracer.game.logic.gametasks.trackeffects;

/**
 * Defines the type of special effect, it also describes their rendering order (FIXME)
 */
public enum TrackEffectType {
    CarSkidMarks(1),
    CarSmokeTrails(2);

    public final int id;

    TrackEffectType(int id) {
        this.id = id;
    }
}
