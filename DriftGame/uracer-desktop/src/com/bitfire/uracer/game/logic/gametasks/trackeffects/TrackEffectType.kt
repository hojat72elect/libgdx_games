package com.bitfire.uracer.game.logic.gametasks.trackeffects

/**
 * Defines the type of special effect, it also describes their rendering order.
 */
enum class TrackEffectType(@JvmField val id: Int) {
    CarSkidMarks(1),
    CarSmokeTrails(2)
}
