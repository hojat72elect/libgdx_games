package com.bitfire.uracer.game.actors

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.bitfire.uracer.entities.EntityRenderState
import com.bitfire.uracer.utils.AlgebraMath

abstract class Box2DEntity(@JvmField protected var box2dWorld: World) : SubframeInterpolableEntity() {

    @JvmField
    protected var body: Body? = null

    override fun dispose() {
        super.dispose()
        box2dWorld.destroyBody(body)
    }

    fun getBody() = body!!

    override fun saveStateTo(state: EntityRenderState) {
        state.position.set(body!!.position)
        state.orientation = body!!.angle
    }

    override val  isSubframeInterpolated = true

    override fun onBeforePhysicsSubstep() {
        toNormalRelativeAngle()
        super.onBeforePhysicsSubstep()
    }

    fun getWorldPosMt(): Vector2 = body!!.position

    open fun setWorldPosMt(worldPosition: Vector2) {
        body!!.setTransform(worldPosition, body!!.angle)
        resetState()
    }

    fun getWorldOrientRads() = body!!.angle

    open fun setWorldPosMt(worldPosition: Vector2, orientationRads: Float) {
        body!!.setTransform(worldPosition, orientationRads)
        resetState()
    }

    protected fun toNormalRelativeAngle() {
        // normalize body angle since it can grow unbounded
        val angle = AlgebraMath.normalRelativeAngle(body!!.angle)
        body!!.setTransform(body!!.position, angle)
    }
}
