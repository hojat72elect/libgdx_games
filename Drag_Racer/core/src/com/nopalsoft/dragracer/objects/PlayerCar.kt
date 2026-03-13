package com.nopalsoft.dragracer.objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.game.TrafficGame
import com.nopalsoft.dragracer.shop.PlayerSubMenu

class PlayerCar(private val trafficGame: TrafficGame) : Actor() {
    var state: Int
    val bounds: Rectangle = Rectangle()
    private var lane: Int

    private var moveTime: Float = .75f
    private var keyframe: TextureRegion? = null

    private var stateTime: Float

    override fun act(delta: Float) {
        super.act(delta)
        updateBounds()

        if (state == STATE_SPINNING && stateTime >= TIME_SPINNING) {
            state = STATE_EXPLOSION
            stateTime = 0f
        }

        if (state == STATE_EXPLOSION) {
            if (stateTime >= TIME_EXPLOSION) {
                remove()
                state = STATE_DEAD
                stateTime = 0f
            }
        }

        stateTime += delta
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        var drawWidth = width + 10
        var drawHeight = height + 10
        var angle = rotation

        when (state) {
            STATE_NORMAL, STATE_SPINNING -> batch.draw(
                keyframe!!, x, y, drawWidth / 2, drawHeight / 2,
                drawWidth, drawHeight, 1f, 1f, angle
            )

            STATE_EXPLOSION -> {
                drawWidth = height + 20
                drawHeight = height + 20
                angle = 0f
                batch.draw(
                    Assets.newExplosion?.getKeyFrame(stateTime)!!, x
                            - drawWidth / 2 / 2f, y, drawWidth / 2,
                    drawHeight / 2, drawWidth, drawHeight, 1f, 1f, angle
                )
            }

            else -> {
                drawWidth = height + 20
                drawHeight = height + 20
                angle = 0f
                batch.draw(
                    Assets.newExplosion?.getKeyFrame(stateTime)!!, x
                            - drawWidth / 2 / 2f, y, drawWidth / 2,
                    drawHeight / 2, drawWidth, drawHeight, 1f, 1f, angle
                )
            }
        }

        if (Settings.drawDebugLines) {
            batch.end()
            renders.projectionMatrix = batch.getProjectionMatrix()
            renders.begin(ShapeType.Line)
            renders.rect(bounds.x, bounds.y, bounds.width, bounds.height)
            renders.end()
            batch.begin()
        }
    }

    private var renders: ShapeRenderer = ShapeRenderer()

    init {
        val width: Float
        val height: Float

        when (Settings.selectedSkin) {
            PlayerSubMenu.SKIN_DEVIL -> {
                keyframe = Assets.carDiablo
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_BANSHEE -> {
                keyframe = Assets.carBanshee
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_TORNADO -> {
                keyframe = Assets.carTornado
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_TURISMO -> {
                keyframe = Assets.carTurismo
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_AUDI_S5 -> {
                keyframe = Assets.carAudiS5
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_BMW_X6 -> {
                keyframe = Assets.carBmwX6
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_CAMARO -> {
                keyframe = Assets.carCamaro
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_CHEVROLET_CROSSFIRE -> {
                keyframe = Assets.carChevroletCrossfire
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_CITROEN_C4 -> {
                keyframe = Assets.carCitroenC4
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_DODGE_CHARGER -> {
                keyframe = Assets.carDodgeCharger
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_FIAT_500_LOUNGE -> {
                keyframe = Assets.carFiat500Lounge
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_HONDA_CRV -> {
                keyframe = Assets.carHondaCRV
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_MAZDA_6 -> {
                keyframe = Assets.carMazda6
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_MAZDA_RX8 -> {
                keyframe = Assets.carMazdaRx8
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_SEAT_IBIZA -> {
                keyframe = Assets.carSeatIbiza
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            PlayerSubMenu.SKIN_VOLKSWAGEN_SCIROCCO -> {
                keyframe = Assets.carVolkswagenScirocco
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }

            else -> {
                keyframe = Assets.carVolkswagenScirocco
                width = keyframe!!.regionWidth.toFloat()
                height = keyframe!!.regionHeight.toFloat()
            }
        }

        setWidth(width - 10)
        setHeight(height - 10)

        lane = 1
        setPosition(trafficGame.lane1 - getWidth() / 2, 200f)

        state = STATE_NORMAL
        stateTime = 0f
    }

    private fun updateBounds() {
        bounds[x, y, width] = height
    }

    fun tryMoveRight() {
        if ((actions.size == 0) && (lane != 2)) {
            addAction(Actions.rotateTo(-10f))
            moveToLane(lane + 1)
        }
    }

    fun tryMoveLeft() {
        if ((actions.size == 0) && (lane != 0)) {
            addAction(Actions.rotateTo(10f))
            moveToLane(lane - 1)
        }
    }

    private fun moveToLane(lane: Int) {
        this.lane = lane

        when (lane) {
            0 -> addAction(
                Actions.sequence(
                    Actions.moveTo(
                        trafficGame.lane0 - width / 2f, y,
                        moveTime
                    ), Actions.rotateTo(0f)
                )
            )

            1 -> addAction(
                Actions.sequence(
                    Actions.moveTo(
                        trafficGame.lane1 - width / 2f, y,
                        moveTime
                    ), Actions.rotateTo(0f)
                )
            )

            2 -> addAction(
                Actions.sequence(
                    Actions.moveTo(
                        trafficGame.lane2 - width / 2f, y,
                        moveTime
                    ), Actions.rotateTo(0f)
                )
            )
        }

        if (MathUtils.randomBoolean()) Assets.playSound(Assets.soundTurn1!!)
        else Assets.playSound(Assets.soundTurn2!!)
    }

    fun crash(front: Boolean, above: Boolean) {
        clearActions()
        if (state == STATE_NORMAL) {
            state = STATE_SPINNING
            stateTime = 0f
        }

        if (front && above) addAction(-360f, 125f, 125f)

        if (front && !above) addAction(360f, 125f, -125f)

        if (!front && above) addAction(360f, -125f, 125f)

        if (!front && !above) addAction(-360f, -125f, -125f)
    }

    private fun addAction(rotation: Float, posX: Float, posY: Float) {
        addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.rotateBy(rotation, TIME_SPINNING),
                    Actions.moveBy(posX, posY, TIME_SPINNING)
                )
            )
        )
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_SPINNING: Int = 1
        const val STATE_EXPLOSION: Int = 2
        const val STATE_DEAD: Int = 3
        val TIME_EXPLOSION: Float = Assets.newExplosion!!.animationDuration
        const val TIME_SPINNING: Float = 1.5f
    }
}
