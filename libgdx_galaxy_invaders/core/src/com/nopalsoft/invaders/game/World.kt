package com.nopalsoft.invaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.utils.Array
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.Settings
import com.nopalsoft.invaders.game_objects.AlienShip
import com.nopalsoft.invaders.game_objects.Boost
import com.nopalsoft.invaders.game_objects.Bullet
import com.nopalsoft.invaders.game_objects.Missile
import com.nopalsoft.invaders.game_objects.SpaceShip
import com.nopalsoft.invaders.screens.Screens
import java.util.Random

class World {
    @JvmField
    var state: Int

    @JvmField
    var oSpaceShip: SpaceShip

    @JvmField
    var boosts: Array<Boost> = Array()

    @JvmField
    var missiles: Array<Missile> = Array()

    @JvmField
    var shipBullets: Array<Bullet> = Array()

    @JvmField
    var alienBullets: Array<Bullet> = Array()

    @JvmField
    var alienShips: Array<AlienShip> = Array()

    var oRan: Random
    var score: Int
    var currentLevel: Int = 0
    var missileCount: Int = 5

    var extraChanceDrop: Int

    var maxMissilesRonda: Int
    var maxBalasRonda: Int
    var nivelBala: Int = 0 // It is the level at which the current bullet is located, each time a boost is grabbed it increases
    var probs: Float // This variable will increase with each level to make the game more difficult.
    var aumentoVel: Float

    init {
        oSpaceShip = SpaceShip(WIDTH / 2f, 9.5f)

        oSpaceShip.lives = 5
        extraChanceDrop = 5
        maxMissilesRonda = 5
        maxBalasRonda = 5

        score = 0
        currentLevel = 0
        aumentoVel = 0f
        probs = aumentoVel
        oRan = Random(Gdx.app.graphics.deltaTime.toLong() * 10000)
        state = STATE_RUNNING
        agregarAliens()
    }

    private fun agregarAliens() {
        currentLevel++

        // Every 2 levels the missiles that can be fired increase
        if (currentLevel % 2f == 0f) {
            maxMissilesRonda++
            maxBalasRonda++
        }
        var x: Float
        var y = 21f
        var vida = 1

        var vidaAlterable = false
        if (currentLevel > 2) {
            vidaAlterable = true
            probs += 0.2f
            aumentoVel += .02f
        }

        // I will add 25 aliens 5x5 columns of 5 rows of 5
        for (col in 0..5) {
            y += 3.8.toFloat()
            x = 1.5f
            for (ren in 0..5) {
                if (vidaAlterable) vida = oRan.nextInt(3) + 1 + probs.toInt() //

                alienShips.add(AlienShip(vida, aumentoVel, x, y))
                x += 4.5f
            }
        }
    }

    fun update(deltaTime: Float, accelX: Float, seDisparo: Boolean, seDisparoMissil: Boolean) {
        updateNave(deltaTime, accelX)
        updateAlienShip(deltaTime) // Alien bullets are added right here. They are updated using another method.

        updateBalaNormalYConNivel(deltaTime, seDisparo)
        updateMissil(deltaTime, seDisparoMissil)
        updateBalaAlien(deltaTime)
        // Boosts are added every time an alien ship is hit. They are only updated here.
        updateBoost(deltaTime)

        if (oSpaceShip.state != SpaceShip.SPACESHIP_STATE_EXPLODE) {
            checkCollision()
        }
        checkGameOver()
        checkLevelEnd() // When I've killed all the aliens
    }

    private fun updateNave(deltaTime: Float, accelX: Float) {
        if (oSpaceShip.state != SpaceShip.SPACESHIP_STATE_EXPLODE) {
            oSpaceShip.velocity.x = -accelX / Settings.accelerometerSensitivity * SpaceShip.SPACESHIP_SPEED
        }
        oSpaceShip.update(deltaTime)
    }

    private fun updateAlienShip(deltaTime: Float) {
        val it: MutableIterator<AlienShip> = alienShips.iterator()
        while (it.hasNext()) {
            val oAlienShip = it.next()
            oAlienShip.update(deltaTime)

            // I add bullets to the aliens
            if (oRan.nextInt(5000) < (1 + probs) && oAlienShip.state != AlienShip.EXPLODING) {
                val x = oAlienShip.position.x
                val y = oAlienShip.position.y
                alienBullets.add(Bullet(x, y))
            }

            // I delete if they have already exploded
            if (oAlienShip.state == AlienShip.EXPLODING && oAlienShip.stateTime > AlienShip.EXPLOSION_DURATION) {
                it.remove()
            }

            // If the aliens reach the bottom you automatically lose.
            if (oAlienShip.position.y < 9.5f) {
                state = STATE_GAME_OVER
            }
        }
    }

    private fun updateBalaAlien(deltaTime: Float) {
        // Now I Update. I recalculate len in case a new bullet was fired

        val it: MutableIterator<Bullet> = alienBullets.iterator()
        while (it.hasNext()) {
            val oAlienBullet = it.next()
            if (oAlienBullet.position.y < -2) oAlienBullet.destruirBala()
            oAlienBullet.update(deltaTime)
            if (oAlienBullet.state == Bullet.STATE_EXPLODING) {
                it.remove()
            }
        }
    }

    private fun updateBalaNormalYConNivel(deltaTime: Float, seDisparo: Boolean) {
        val x = oSpaceShip.position.x
        val y = oSpaceShip.position.y + 1

        if (seDisparo && shipBullets.size < maxBalasRonda) {
            shipBullets.add(Bullet(x, y, nivelBala))
        }

        val it1: MutableIterator<Bullet> = shipBullets.iterator()
        while (it1.hasNext()) {
            val oBullet = it1.next()
            if (oBullet.position.y > HEIGHT + 2) oBullet.destruirBala() // so that the missile doesn't get too far

            oBullet.update(deltaTime)
            if (oBullet.state == Bullet.STATE_EXPLODING) {
                it1.remove()
            }
        }
    }

    private fun updateMissil(deltaTime: Float, seDisparoMissil: Boolean) {
        // Limit of max Missiles Round Missiles in a round
        val len = missiles.size
        if (seDisparoMissil && missileCount > 0 && len < maxMissilesRonda) {
            val x = oSpaceShip.position.x
            val y = oSpaceShip.position.y + 1
            missiles.add(Missile(x, y))
            missileCount--
            playSound(Assets.missileFiringSound!!, 0.15f)
        }

        // Now I'm updating. I'm recalculating len in case a new missile is fired.
        val it: MutableIterator<Missile> = missiles.iterator()
        while (it.hasNext()) {
            val oMissile = it.next()
            if (oMissile.position.y > HEIGHT + 2 && oMissile.state != Missile.STATE_EXPLODING) oMissile.hitTarget()
            oMissile.update(deltaTime)
            if (oMissile.state == Missile.STATE_EXPLODING && oMissile.stateTime > Missile.EXPLOSION_DURATION) {
                it.remove()
            }
        }
    }

    private fun updateBoost(deltaTime: Float) {
        val it: MutableIterator<Boost> = boosts.iterator()
        while (it.hasNext()) {
            val oBoost = it.next()
            oBoost.update(deltaTime)
            if (oBoost.position.y < -2) {
                it.remove()
            }
        }
    }

    /**
     * All types of collisions are checked.
     */
    private fun checkCollision() {
        checkColisionNaveBalaAliens() // Primero reviso si le dieron a mi nave =(
        checkColisionAliensBala() // Checo si mis balas les dio a esos weas (Reviso BalaNormal, BalaNivel1, BalaNivel2, BalaNivel3.... etc
        checkColisionAlienMissil()
        checkColisionBoostNave()
    }

    private fun checkColisionNaveBalaAliens() {
        for (oAlienBullet in alienBullets) {
            if (Intersector.overlaps(
                    oSpaceShip.boundsRectangle,
                    oAlienBullet.boundsRectangle
                ) && oSpaceShip.state != SpaceShip.SPACESHIP_STATE_EXPLODE && oSpaceShip.state != SpaceShip.SPACESHIP_STATE_BEING_HIT
            ) {
                oSpaceShip.beingHit()
                oAlienBullet.hitTarget(1)
            }
        }
    }

    private fun checkColisionAliensBala() {
        for (oBala in shipBullets) {
            for (oAlien in alienShips) {
                if (Intersector.overlaps(oAlien.boundsCircle, oBala.boundsRectangle) && (oAlien.state != AlienShip.EXPLODING)) {
                    oBala.hitTarget(oAlien.remainingLives)
                    oAlien.beingHit()
                    if (oAlien.state == AlienShip.EXPLODING) { // It only increases the score and I add boost if it is already exploding, not if I decrease its life
                        score += oAlien.score // I update the score
                        agregarBoost(oAlien.position.x, oAlien.position.y) // Here I'll see if it gives me any boost or not.
                        playSound(Assets.explosionSound!!, 0.6f)
                    }
                }
            }
        }
    }

    private fun checkColisionAlienMissil() {
        for (oMissile in missiles) {
            for (oAlien in alienShips) {
                if (oMissile.state == Missile.STATE_LAUNCHED && Intersector.overlaps(oAlien.boundsCircle, oMissile.boundsRectangle) && oAlien.state != AlienShip.EXPLODING) {
                    oMissile.hitTarget()
                    oAlien.beingHit()
                    if (oAlien.state == AlienShip.EXPLODING) { // It only increases the score and I add boost if it is already exploding, not if I decrease its life
                        score += oAlien.score // I update the score
                        agregarBoost(oAlien.position.x, oAlien.position.y) // Here I'll see if it gives me any boost or not.
                        playSound(Assets.explosionSound!!, 0.6f)
                    }
                }
                // Check with the radius of the explosion
                if (oMissile.state == Missile.STATE_EXPLODING && Intersector.overlaps(oAlien.boundsCircle, oMissile.boundsCircle) && oAlien.state != AlienShip.EXPLODING) {
                    oAlien.beingHit()
                    if (oAlien.state == AlienShip.EXPLODING) { // It only increases the score and I add boost if it is already exploding, not if I decrease its life
                        score += oAlien.score // I update the score
                        agregarBoost(oAlien.position.x, oAlien.position.y) // Here I'll see if it gives me any boost or not.
                        playSound(Assets.explosionSound!!, 0.6f)
                    }
                }
            }
        }
    }

    private fun checkColisionBoostNave() {
        val it: MutableIterator<Boost> = boosts.iterator()
        while (it.hasNext()) {
            val oBoost = it.next()
            if (Intersector.overlaps(oBoost.boundsCircle, oSpaceShip.boundsRectangle) && oSpaceShip.state != SpaceShip.SPACESHIP_STATE_EXPLODE) {
                when (oBoost.type) {
                    Boost.EXTRA_LIFE_BOOST -> oSpaceShip.hitVidaExtra()
                    Boost.EXTRA_MISSILE_BOOST -> nivelBala++
                    Boost.EXTRA_SHIELD_BOOST -> missileCount++
                    Boost.SHIELD -> oSpaceShip.hitEscudo()
                    else -> oSpaceShip.hitEscudo()
                }
                it.remove()
                playSound(Assets.coinSound!!)
            }
        }
    }

    /**
     * Receives the x,y coordinates of the ship that has just been destroyed. The Boost can be a life, weapons, shield, etc.
     *
     * @param x position where the boost will appear
     * @param y position where the boost will appear
     */
    private fun agregarBoost(x: Float, y: Float) {
        if (oRan.nextInt(100) < 5 + extraChanceDrop) { // Chances of a boost appearing
            when (oRan.nextInt(4)) {
                Boost.EXTRA_LIFE_BOOST -> boosts.add(Boost(Boost.EXTRA_LIFE_BOOST, x, y))
                1 -> boosts.add(Boost(Boost.EXTRA_MISSILE_BOOST, x, y))
                Boost.EXTRA_SHIELD_BOOST -> boosts.add(Boost(Boost.EXTRA_SHIELD_BOOST, x, y))
                else -> boosts.add(Boost(Boost.SHIELD, x, y))
            }
        }
    }

    private fun checkGameOver() {
        if (oSpaceShip.state == SpaceShip.SPACESHIP_STATE_EXPLODE && oSpaceShip.stateTime > SpaceShip.EXPLOSION_DURATION) {
            oSpaceShip.position.x = 200f
            state = STATE_GAME_OVER
        }
    }

    private fun checkLevelEnd() {
        if (alienShips.size == 0) {
            shipBullets.clear()
            alienBullets.clear()
            agregarAliens()
        }
    }

    companion object {
        var WIDTH: Float = Screens.WORLD_SCREEN_WIDTH.toFloat()
        const val HEIGHT: Float = Screens.WORLD_SCREEN_HEIGHT.toFloat()

        const val STATE_RUNNING: Int = 0
        const val STATE_GAME_OVER: Int = 1
        const val STATE_PAUSED: Int = 2
    }
}
