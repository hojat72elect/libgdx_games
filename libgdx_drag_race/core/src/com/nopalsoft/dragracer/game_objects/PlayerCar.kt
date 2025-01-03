package com.nopalsoft.dragracer.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.Assets.playSound
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.game.TrafficGame
import com.nopalsoft.dragracer.shop.CharactersSubMenu

class PlayerCar(private val trafficGame: TrafficGame) : Actor() {

    val bounds = Rectangle()


    var state = 0
    private var stateTime = 0f
    private var moveTime = .75f
    private var keyframe: TextureRegion
    private var renders = ShapeRenderer()
    private var lane = 0

    init {
        val width: Float
        val height: Float

        when (Settings.selectedSkin) {
            CharactersSubMenu.SKIN_CAR_DEVIL -> {
                keyframe = Assets.carDevil
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_BANSHEE -> {
                keyframe = Assets.carBanshee
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_TORNADO -> {
                keyframe = Assets.carTornado
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_TURISM -> {
                keyframe = Assets.carTourism
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_AUDI_S5 -> {
                keyframe = Assets.carAudiS5
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_BMW_X6 -> {
                keyframe = Assets.carBmwX6
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_BULLET -> {
                keyframe = Assets.carBullet
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_CHEVROLET_CROSSFIRE -> {
                keyframe = Assets.carChevroletCrossfire
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_CITROEN_C4 -> {
                keyframe = Assets.carCitroenC4
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_DODGE_CHARGER -> {
                keyframe = Assets.carDodgeCharger
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_FIAT_500_LOUNGE -> {
                keyframe = Assets.carFiat500Lounge
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_HONDA_CRV -> {
                keyframe = Assets.carHondaCRV
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_MAZDA_6 -> {
                keyframe = Assets.carMazda6
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_MAZDA_RX8 -> {
                keyframe = Assets.carMazdaRX8
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_SEAT_IBIZA -> {
                keyframe = Assets.carSeatIbiza
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            CharactersSubMenu.SKIN_CAR_VOLKSWAGEN_SCIROCCO -> {
                keyframe = Assets.carVolkswagenScirocco
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }

            else -> {
                keyframe = Assets.carVolkswagenScirocco
                width = keyframe.regionWidth.toFloat()
                height = keyframe.regionHeight.toFloat()
            }
        }

        setWidth(width - 10)
        setHeight(height - 10)

        lane = 1
        setPosition(trafficGame.lane1 - width / 2, 200f)

        state = STATE_NORMAL
        stateTime = 0f
    }

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
                keyframe,
                x, y, drawWidth / 2, drawHeight / 2,
                drawWidth, drawHeight, 1f, 1f, angle
            )

            STATE_EXPLOSION -> {
                drawWidth = height + 20
                drawHeight = height + 20
                angle = 0f
                batch.draw(
                    Assets.newExplosion.getKeyFrame(stateTime), x
                            - drawWidth / 2 / 2f, y, drawWidth / 2,
                    drawHeight / 2, drawWidth, drawHeight, 1f, 1f, angle
                )
            }

            else -> {
                drawWidth = height + 20
                drawHeight = height + 20
                angle = 0f
                batch.draw(
                    Assets.newExplosion.getKeyFrame(stateTime), x
                            - drawWidth / 2 / 2f, y, drawWidth / 2,
                    drawHeight / 2, drawWidth, drawHeight, 1f, 1f, angle
                )
            }
        }
        if (Settings.DRAW_DEBUG_LINES) {
            batch.end()
            renders.projectionMatrix = batch.projectionMatrix
            renders.begin(ShapeType.Line)
            renders.rect(bounds.x, bounds.y, bounds.width, bounds.height)
            renders.end()
            batch.begin()
        }
    }

    private fun updateBounds() {
        bounds.set(x, y, width, height)
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
        if (MathUtils.randomBoolean()) {
            playSound(Assets.soundTurn1)
        } else playSound(Assets.soundTurn2)
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

    private fun addAction(rotation: Float, positionX: Float, positionY: Float) {
        addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.rotateBy(rotation, TIME_SPINNING),
                    Actions.moveBy(positionX, positionY, TIME_SPINNING)
                )
            )
        )
    }


    companion object {
        const val STATE_NORMAL = 0
        const val STATE_SPINNING = 1
        const val STATE_EXPLOSION = 2
        const val STATE_DEAD = 3
        val TIME_EXPLOSION = Assets.newExplosion.animationDuration
        const val TIME_SPINNING = 1.5f
    }
}