package com.nopalsoft.ponyrace.game_objects

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.game.TileMapHandler

class OpponentPony(x: Float, y: Float, skinName: String?, tileMapHandler: TileMapHandler) : Pony(x, y, skinName, tileMapHandler) {
    private val pastPosition: Vector3

    @JvmField
    var didTouchFlag: Boolean = false

    @JvmField
    var hasToJump: Boolean = false
    var timeSamePosition: Float = 0f
    private var jumpSuccessProbability = .5f
    private var timeSinceLastShot = 0f

    init {
        when (Settings.difficultyLevel) {
            Settings.DIFFICULTY_EASY -> jumpSuccessProbability = .35f
            Settings.DIFFICULTY_NORMAL -> jumpSuccessProbability = .5f
            Settings.DIFFICULTY_HARD -> jumpSuccessProbability = .7f
            Settings.DIFFICULTY_VERY_HARD -> jumpSuccessProbability = 1f
        }
        pastPosition = Vector3()
    }

    fun hitSimpleJump(newPosition: Int) {
        if (didTouchFlag) {
            val pro = random!!.nextFloat()
            if (pro < jumpSuccessProbability) {
                hasToJump = true
                state = newPosition
            }
        } else {
            hasToJump = true
            state = newPosition
        }

        didTouchFlag = false
    }

    fun hitCaminarOtraDireccion(nuevaDireccion: Int) {
        if (didTouchFlag) {
            val pro = random!!.nextFloat()

            if (pro < jumpSuccessProbability) {
                state = nuevaDireccion
            }
        } else {
            state = nuevaDireccion
        }
        didTouchFlag = false
    }

    override fun update(delta: Float, obj: Body, accelX: Float) {
        timeSinceLastShot += delta

        // A veces disparan en medio de un salto y la banana queda en el objeto saltar, entonces los otros ponis q tocan el cuadrito tocan la banana y se atoran
        // por eso pongo !hasToJump || !isJumping.. aun asi el problema continuaaa =(
        val TIEMPO_DISPARO = 3f
        if (timeSinceLastShot >= TIEMPO_DISPARO && (!hasToJump || !isJumping)) {
            timeSinceLastShot -= TIEMPO_DISPARO
            if (random!!.nextInt(10) < 2 && !pasoLaMeta) {
                fireWood = true
            }
        }

        // Checo si a estado mucho tiempo en la misma posicion en caso de que si lo pongo a saltar, esto es porque a veces se atoran
        if (pastPosition.x != position.x) pastPosition.x = position.x
        else {
            timeSamePosition += delta
            if (timeSamePosition >= 2.5f) {
                timeSamePosition = 0f
                hasToJump = true
            }
        }

        super.update(delta, obj, accelX)
    }
}
