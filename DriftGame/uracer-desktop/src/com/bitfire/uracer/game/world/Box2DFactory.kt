package com.bitfire.uracer.game.world

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.bitfire.uracer.configuration.DebugUtils
import com.bitfire.uracer.game.collisions.CollisionFilters
import com.bitfire.uracer.game.collisions.CollisionFilters.MaskWalls
import kotlin.math.atan2
import kotlin.math.sqrt

object Box2DFactory {
    /**
     * Creates a wall by constructing a rectangle whose corners are (xmin,ymin) and (xmax,ymax), and rotating the box
     * counterclockwise through the given angle, with specified restitution.
     */
    private fun createWall(world: World, xmin: Float, ymin: Float, xmax: Float, ymax: Float, angle: Float, restitution: Float) {
        val cx = (xmin + xmax) / 2F
        val cy = (ymin + ymax) / 2F
        var hx = (xmax - xmin) / 2F
        var hy = (ymax - ymin) / 2F

        if (hx < 0) hx = -hx
        if (hy < 0) hy = -hy

        val wallshape = PolygonShape()
        wallshape.setAsBox(hx, hy, Vector2(0F, 0F), angle)
        val fdef = FixtureDef()
        fdef.shape = wallshape
        fdef.density = 1.0F
        fdef.friction = 0.02F
        fdef.filter.groupIndex = CollisionFilters.GroupTrackWalls
        fdef.filter.categoryBits = CollisionFilters.CategoryTrackWalls
        fdef.filter.maskBits = MaskWalls

        if (DebugUtils.TraverseWalls) fdef.filter.groupIndex = CollisionFilters.GroupNoCollisions
        if (restitution > 0) fdef.restitution = restitution

        val bd = BodyDef()
        bd.position.set(cx, cy)
        val wall = world.createBody(bd)
        wall.createFixture(fdef)
        wall.type = BodyDef.BodyType.StaticBody
    }

    /**
     * Creates a segment-like thin wall with 0.05 thickness going from (x1,y1) to (x2,y2)
     */
    @JvmStatic
    fun createWall(world: World, from: Vector2, to: Vector2, size: Float, restitution: Float) {
        // determine center point and rotation angle for createWall
        val halfSize = size / 2F
        val cx = (from.x + to.x) / 2F
        val cy = (from.y + to.y) / 2F
        val angle = atan2(to.y - from.y, to.x - from.x)
        val mag = sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y))
        createWall(world, cx - mag / 2F, cy - halfSize, cx + mag / 2F, cy + halfSize, angle, restitution)
    }
}
