package com.nopalsoft.ponyrace.game_objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.esotericsoftware.spine.AnimationState
import com.esotericsoftware.spine.AnimationState.AnimationStateListener
import com.esotericsoftware.spine.AnimationStateData
import com.esotericsoftware.spine.Event
import com.esotericsoftware.spine.Skeleton
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.TileMapHandler
import java.util.Random

open class Pony(x: Float, y: Float, @JvmField var nombreSkin: String?, oWorld: TileMapHandler) {
    val TIEMPO_IS_CHILE: Float = when (Settings.chiliLevel) {
        1 -> 3.5f
        2 -> 5f
        3 -> 7f
        4 -> 9f
        5 -> 11f
        0 -> 3f
        else -> 3f
    }
    val TIEMPO_IS_DULCE: Float = when (Settings.chocolateLevel) {
        1 -> 3.5f
        2 -> 4f
        3 -> 5f
        4 -> 7f
        5 -> 9f
        0 -> 3f
        else -> 3f
    }

    @JvmField
    val position: Vector3

    @JvmField
    val aceleration: Vector3

    @JvmField
    var state: Int

    @JvmField
    var angleRad: Float = 0f
    var canRotate: Boolean = false // Solo cuando toca el piso inclinado puede rotar

    @JvmField
    var animState: AnimationState

    @JvmField
    var ponySkel: Skeleton
    var isJumping: Boolean

    @JvmField
    var isDoubleJump: Boolean

    @JvmField
    var isHurt: Boolean

    @JvmField
    var isChile: Boolean = false // Es true cuando toca un chile

    @JvmField
    var isDulce: Boolean = false // Es true cuando toca un dulce

    @JvmField
    var regresoHoyo: Vector2?

    @JvmField
    var cayoEnHoyo: Boolean = false

    @JvmField
    var tocoElPisoDespuesCaerHoyo: Boolean

    var stateTime: Float
    var chileTime: Float = 0f
    var dulceTime: Float = 0f
    var random: Random? = oWorld.random

    @JvmField
    var monedasRecolectadas: Int

    @JvmField
    var chilesRecolectados: Int = 0

    @JvmField
    var globosRecolectados: Int = 0

    @JvmField
    var dulcesRecolectados: Int = 0

    @JvmField
    var lugarEnLaCarrera: Int = 0 // Variable que dice si vas en primer, segundo, tercer, et. Lugar en la carrera

    @JvmField
    var pasoLaMeta: Boolean

    // Variables para saber si disparo o no
    @JvmField
    var fireBomb: Boolean = false

    @JvmField
    var fireWood: Boolean = false

    init {

        state = STATE_WALK_RIGHT

        position = Vector3(x, y, 0f)
        aceleration = Vector3()
        regresoHoyo = Vector2()
        monedasRecolectadas = 0
        stateTime = 0f
        isJumping = false
        isDoubleJump = false
        isHurt = false
        pasoLaMeta = false
        tocoElPisoDespuesCaerHoyo = true
        ponySkel = Skeleton(oWorld.game.assetsHandler!!.ponySkeletonData)
        ponySkel.setSkin(nombreSkin)
        ponySkel.setToSetupPose()

        val stateData = AnimationStateData(ponySkel.getData())
        stateData.setMix("Jump", "Running", .45f)
        stateData.setMix("Jump", "standing", .45f)
        stateData.setMix("Jump2", "standing", .45f)
        stateData.setMix("Jump2", "Running", .45f)
        stateData.setMix("Jump", "Jump2", -.45f)
        stateData.setMix("hurt", "Running", .3f)
        stateData.setMix("hurt", "standing", .3f)
        // stateData.setMix("hurt", "death", .3f);
        stateData.setMix("hurt", "hurt", .3f)

        animState = AnimationState(stateData)
        animState.addListener(object : AnimationStateListener {
            override fun event(trackIndex: Int, event: Event?) {
                // TODO Auto-generated method stub
            }

            override fun complete(trackIndex: Int, loopCount: Int) {
                // TODO Auto-generated method stub
            }

            override fun start(trackIndex: Int) {
                // TODO Auto-generated method stub
            }

            override fun end(trackIndex: Int) {
                ponySkel.setToSetupPose()
            }
        })
        animState.setAnimation(0, "standing", true)
    }

    open fun update(delta: Float, obj: Body, accelX: Float) {
        stateTime += delta
        position.x = obj.getPosition().x
        position.y = obj.getPosition().y

        val MAX_ANGLE_DEGREES = 18
        val angleLimitRad = Math.toRadians(MAX_ANGLE_DEGREES.toDouble()).toFloat()
        angleRad = obj.angle

        if (!canRotate || isJumping) angleRad = 0f
        else if (angleRad > angleLimitRad) angleRad = angleLimitRad
        else if (angleRad < -angleLimitRad) angleRad = -angleLimitRad

        obj.setTransform(position.x, position.y, angleRad)

        aceleration.set(accelX, 0f, 0f)

        if (state == STATE_DEAD) return

        if (isChile) {
            chileTime += delta
            if (chileTime >= TIEMPO_IS_CHILE) {
                chileTime = 0f
                isChile = false
            }
        }

        if (isDulce) {
            dulceTime += delta
            if (dulceTime >= TIEMPO_IS_DULCE) {
                dulceTime = 0f
                isDulce = false
            }
        }

        if (accelX < 0) state = STATE_WALK_LEFT
        else if (accelX > 0) state = STATE_WALK_RIGHT

        if (isHurt && stateTime < TIEMPO_IS_HURT) return
        else if (isHurt) {
            isHurt = false
            stateTime = 0f
        }

        if (isChile && animState.getCurrent(0).getAnimation().getName() == "Running") animState.setAnimation(0, "Runningchile", true)
        else if (isDulce && animState.getCurrent(0).getAnimation().getName() == "Running") animState.setAnimation(0, "Runningchocolate", true)
        else if (!isDulce && !isChile && (animState.getCurrent(0).getAnimation().getName() == "Runningchile" || animState.getCurrent(0).getAnimation()
                .getName() == "Runningchocolate")
        ) animState.setAnimation(0, "Running", true)

        if ((animState.getCurrent(0).getAnimation().getName() == "standingchocolate" || animState.getCurrent(0).getAnimation().getName() == "standingchile" || animState.getCurrent(0).getAnimation()
                .getName() == "standing" || animState.getCurrent(0).getAnimation().getName() == "hurt") && aceleration.x != 0f
        ) {
            if (isDulce) animState.setAnimation(0, "Runningchocolate", true)
            else if (isChile) animState.setAnimation(0, "Runningchile", true)
            else animState.setAnimation(0, "Running", true)
        }

        // Si no se esta moviendo a la izq o derecha, y si ya toco el suelo lo pongo en stanging
        if ((animState.getCurrent(0).getAnimation().getName() == "Runningchocolate" || animState.getCurrent(0).getAnimation().getName() == "Running" || animState.getCurrent(0).getAnimation()
                .getName() == "Runningchile" || animState.getCurrent(0).getAnimation().getName() == "hurt") && aceleration.x == 0f && !isJumping
        ) {
            if (isDulce) animState.setAnimation(0, "standingchocolate", true)
            else if (isChile) animState.setAnimation(0, "standingchile", true)
            else animState.setAnimation(0, "standing", true)
        }
    }

    fun jump() {
        if (isDoubleJump || isHurt) return

        if (isJumping) {
            if (isDulce) animState.setAnimation(0, "Jump2chocolate", false)
            else if (isChile) animState.setAnimation(0, "Jump2chile", false)
            else animState.setAnimation(0, "Jump2", false)
        } else {
            if (isDulce) animState.setAnimation(0, "Jumpchocolate", false)
            else if (isChile) animState.setAnimation(0, "Jumpchile", false)
            else animState.setAnimation(0, "Jump", false)
        }
        animState.addAnimation(0, "Running", true, 0f)

        if (!isJumping) isJumping = true
        else if (!isDoubleJump) isDoubleJump = true
    }

    fun getHurt(tiempoIsHurt: Float) {
        if (isHurt || isChile) return

        TIEMPO_IS_HURT = tiempoIsHurt
        stateTime = 0f
        isHurt = true

        animState.setAnimation(0, "hurt", true)
    }

    fun die() {
        stateTime = 0f
        state = STATE_DEAD
        animState.setAnimation(0, "death", false)
    }

    fun tocoElPiso() {
        isJumping = false
        isDoubleJump = false
        canRotate = false
        tocoElPisoDespuesCaerHoyo = true
    }

    fun tocoPisoInclinado() {
        tocoElPiso()
        canRotate = true
    }

    fun cayoEnHoyo() {
        tocoElPisoDespuesCaerHoyo = false
        cayoEnHoyo = true
    }

    fun tocoChile() {
        chileTime = 0f
        isChile = true
    }

    fun tocoDulce() {
        dulceTime = 0f
        isDulce = true
    }

    companion object {
        const val STATE_DEAD: Int = 0
        const val STATE_WALK_LEFT: Int = 1
        const val STATE_WALK_RIGHT: Int = 2
        const val STATE_STAND: Int = 3
        const val VEL_JUMP: Float = 3.5f
        const val VEL_RUN: Float = 2.0f
        var TIEMPO_IS_HURT: Float = 0f
    }
}
