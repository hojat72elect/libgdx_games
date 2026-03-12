package com.nopalsoft.donttap.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.donttap.Assets
import com.nopalsoft.donttap.game.WorldGame

class Tile : Actor(), Poolable {
    @JvmField
    var state: Int = 0

    @JvmField
    var type: Int = 0

    @JvmField
    var canBeTap: Boolean = false // You can't touch it until you touch the one below it

    @JvmField
    var tablePosition: Int = 0
    var keyframe: TextureRegion? = null
    var worldGame: WorldGame? = null

    fun init(oWorld: WorldGame, tablePosition: Int, canStep: Boolean, isFirstRow: Boolean) {
        this.tablePosition = tablePosition
        this.worldGame = oWorld
        setPosition(mapPositions.get(tablePosition)!!.x, mapPositions.get(tablePosition)!!.y)

        clearActions()
        getColor().a = 1f
        if (!canStep) {
            type = TYPE_BAD
            keyframe = Assets.whiteTile
        } else {
            when (MathUtils.random(4)) {
                0 -> keyframe = Assets.redTile
                1 -> keyframe = Assets.yellowTile
                2 -> keyframe = Assets.blueTile
                3 -> keyframe = Assets.purpleTile
                4 -> keyframe = Assets.orangeTile
            }
            type = TYPE_GOOD
            addAction(Actions.forever(Actions.sequence(Actions.alpha(.6f, .5f), Actions.alpha(1f, .35f))))
        }

        if (isFirstRow && canStep) {
            canBeTap = true
            state = STATE_TAP
        } else {
            setScale(1f)
            canBeTap = false
            state = STATE_NORMAL
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, getColor().a)
        batch.draw(keyframe!!, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

        if (state == STATE_TAP) {
            val step = if (type == TYPE_GOOD) Assets.step1
            else Assets.wrong
            batch.draw(step!!, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())
        }

        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f)
    }

    var inputListener: InputListener = object : InputListener() {
        override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            if (worldGame!!.state == WorldGame.STATE_READY) {
                worldGame!!.state = WorldGame.STATE_RUNNING
            }

            if (worldGame!!.state == WorldGame.STATE_RUNNING) {
                if (state == STATE_NORMAL && canBeTap || type == TYPE_BAD) {
                    state = STATE_TAP

                    when (worldGame!!.mode) {
                        WorldGame.MODE_CLASSIC -> {
                            worldGame!!.addRow()
                            if (type == TYPE_GOOD) {
                                worldGame!!.score--
                                Assets.playTapSound()
                            } else {
                                Assets.soundWrong?.play()
                            }
                        }

                        WorldGame.MODE_TIME -> {
                            worldGame!!.addRow()
                            if (type == TYPE_GOOD) {
                                worldGame!!.score++
                                Assets.playTapSound()
                            } else {
                                Assets.soundWrong?.play()
                            }
                        }

                        WorldGame.MODE_ENDLESS -> if (type == TYPE_GOOD) {
                            worldGame!!.score++
                            Assets.playTapSound()
                        } else {
                            Assets.soundWrong?.play()
                        }
                    }
                }
            }
            return true
        }
    }

    init {
        setSize(WIDTH, HEIGHT)
        setOrigin(WIDTH / 2f, HEIGHT / 2f)
        addListener(inputListener)
    }

    fun moveUp() {
        tablePosition -= 4
        if (tablePosition < 0) {
            return
        }
        addAction(Actions.moveTo(mapPositions.get(tablePosition)!!.x, mapPositions.get(tablePosition)!!.y, .75f))
    }

    fun moveDown() {
        tablePosition += 4
        if (tablePosition > 23) {
            return
        }

        var time = .1f
        if (worldGame!!.mode == WorldGame.MODE_ENDLESS) time = worldGame!!.timeStep
        addAction(Actions.moveTo(mapPositions.get(tablePosition)!!.x, mapPositions.get(tablePosition)!!.y, time))
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_TAP: Int = 1
        var WIDTH: Float = 120f
        var HEIGHT: Float = 180f

        const val TYPE_BAD: Int = 0
        const val TYPE_GOOD: Int = 1

        val mapPositions: LinkedHashMap<Int?, Vector2?> = LinkedHashMap<Int?, Vector2?>()

        init {
            // The ones that have -1 are the ones that are further down where they cannot be seen.
            mapPositions.put(0, Vector2(0f, 720f))
            mapPositions.put(1, Vector2(120f, 720f))
            mapPositions.put(2, Vector2(240f, 720f))
            mapPositions.put(3, Vector2(360f, 720f))
            mapPositions.put(4, Vector2(0f, 540f))
            mapPositions.put(5, Vector2(120f, 540f))
            mapPositions.put(6, Vector2(240f, 540f))
            mapPositions.put(7, Vector2(360f, 540f))
            mapPositions.put(8, Vector2(0f, 360f))
            mapPositions.put(9, Vector2(120f, 360f))
            mapPositions.put(10, Vector2(240f, 360f))
            mapPositions.put(11, Vector2(360f, 360f))
            mapPositions.put(12, Vector2(0f, 180f))
            mapPositions.put(13, Vector2(120f, 180f))
            mapPositions.put(14, Vector2(240f, 180f))
            mapPositions.put(15, Vector2(360f, 180f))
            mapPositions.put(16, Vector2(0f, 0f))
            mapPositions.put(17, Vector2(120f, 0f))
            mapPositions.put(18, Vector2(240f, 0f))
            mapPositions.put(19, Vector2(360f, 0f))
            mapPositions.put(20, Vector2(0f, -180f))
            mapPositions.put(21, Vector2(120f, -180f))
            mapPositions.put(22, Vector2(240f, -180f))
            mapPositions.put(23, Vector2(360f, -180f))
        }
    }
}
