package com.bitfire.uracer.game.collisions

import kotlin.experimental.or

/**
 * Defines entities' behavior upon collision: this follows the box2d rules (see `b2WorldCallbacks.cpp`)
 *
 * ```
 * bool b2ContactFilter::ShouldCollide(b2Fixture* fixtureA, b2Fixture* fixtureB) {
 *
 * const b2Filter& filterA = fixtureA -> GetFilterData();
 * const b2Filter& filterB = fixtureB->GetFilterData();
 *
 * if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0) {
 *  return filterA.groupIndex > 0;
 * }
 *
 * bool collide = (filterA.maskBits & filterB.categoryBits) != 0 && (filterA.categoryBits & filterB.maskBits) != 0;
 *
 * return collide;
 * }
 * ```
 */
object CollisionFilters {
    const val GroupNoCollisions: Short = -1
    const val GroupPlayer: Short = 0x0001
    const val GroupReplay: Short = -0x0002
    const val GroupTrackWalls: Short = 0x0003

    const val CategoryPlayer: Short = 0x0001
    const val CategoryReplay: Short = 0x0002
    const val CategoryTrackWalls: Short = 0x0004

    const val MaskPlayer: Short = CategoryTrackWalls
    const val MaskReplay: Short = CategoryTrackWalls
    val MaskWalls: Short = CategoryPlayer or CategoryReplay
}
