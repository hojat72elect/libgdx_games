package com.bitfire.uracer.game.debug.player

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.bitfire.uracer.game.debug.DebugHelper.RenderFlags
import com.bitfire.uracer.game.debug.DebugRenderable
import com.bitfire.uracer.game.logic.gametasks.GameTasksManager
import com.bitfire.uracer.game.logic.gametasks.trackeffects.TrackEffect
import com.bitfire.uracer.game.logic.gametasks.trackeffects.TrackEffectType
import com.bitfire.uracer.game.player.DriftState
import com.bitfire.uracer.game.player.PlayerCar
import com.bitfire.uracer.game.rendering.GameRenderer
import com.bitfire.uracer.resources.Art
import com.bitfire.uracer.utils.mtSecToKmHour

class DebugPlayer(flag: RenderFlags, manager: GameTasksManager) : DebugRenderable(flag) {

    private val smokeTrails = manager.effects.getEffect(TrackEffectType.CarSmokeTrails)
    private val skidMarks: TrackEffect? = manager.effects.getEffect(TrackEffectType.CarSkidMarks)
    private var driftState: DriftState? = null
    private val meterDriftStrength = DebugMeter(100, 5)
    private var meterSkidMarks: DebugMeter? = null
    private val meterSpeed = DebugMeter(100, 5)   // player speed, km/h
    private var meterSmokeTrails: DebugMeter? = null
    private val meters = Array<DebugMeter>()
    private val pos = Vector2()

    init {
        meterDriftStrength.setLimits(0f, 1f)
        meterDriftStrength.setName("drift strength")
        meters.add(meterDriftStrength)

        // meter skid marks count
        if (skidMarks != null) {
            meterSkidMarks = DebugMeter(100, 5)
            meterSkidMarks!!.setLimits(0f, skidMarks.getMaxParticleCount().toFloat())
            meterSkidMarks!!.setName("skid marks")
            meters.add(meterSkidMarks)
        }

        // meter smoke trails count
        if (smokeTrails != null) {
            meterSmokeTrails = DebugMeter(100, 5)
            meterSmokeTrails!!.setLimits(0f, smokeTrails.getMaxParticleCount().toFloat())
            meterSmokeTrails!!.setName("smoke trails")
            meters.add(meterSmokeTrails)
        }

        meterSpeed.setName("speed km/h")
        meters.add(meterSpeed)
    }

    override fun dispose() {
        for (m in meters) {
            m.dispose()
        }
    }

    override fun player(player: PlayerCar?) {
        super.player(player)
        if (hasPlayer) {
            this.player = player
            driftState = player?.driftState
            meterSpeed.setLimits(0f, mtSecToKmHour(player!!.carModel.max_speed))
        }
    }

    private val isActive: Boolean
        get() = hasPlayer

    override fun tick() {
        if (this.isActive) {
            // lateral forces
            meterDriftStrength.value = driftState?.driftStrength ?: 0F

            if (driftState!!.isDrifting)
                meterDriftStrength.color.set(.3f, 1f, .3f, 1f)
             else
                meterDriftStrength.color.set(1f, 1f, 1f, 1f)


            // skid marks count
            meterSkidMarks?.value = skidMarks?.getParticleCount()?.toFloat() ?: 0F

            // smoke trails count
            meterSmokeTrails?.value = smokeTrails?.getParticleCount()?.toFloat() ?: 0F

            // player's speed
            meterSpeed.value = mtSecToKmHour(player!!.instantSpeed)
        }
    }

    override fun renderBatch(batch: SpriteBatch) {
        if (this.isActive) {
            var prevHeight = 0f
            var index = 0
            for (m in meters) {
                pos.set(GameRenderer.ScreenUtils.worldPxToScreen(player!!.state().position))
                pos.x += 100f
                pos.y += 0f

                // offset by index
                pos.y += index * (prevHeight + Art.DebugFontHeight)

                m.setPosition(pos)
                m.render(batch)

                index++
                prevHeight = m.height.toFloat()
            }
        }
    }
}
