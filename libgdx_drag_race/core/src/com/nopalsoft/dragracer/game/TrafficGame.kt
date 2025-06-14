package com.nopalsoft.dragracer.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.objects.Coin
import com.nopalsoft.dragracer.objects.EnemyCar
import com.nopalsoft.dragracer.objects.InfiniteScrollBackground
import com.nopalsoft.dragracer.objects.PlayerCar
import com.nopalsoft.dragracer.screens.Screens

class TrafficGame : Table() {
    var state: Int

    val WIDTH: Float = Screens.WORLD_WIDTH
    val HEIGHT: Float = Screens.WORLD_HEIGHT

    var numCoinsForSuperSpeed: Int = 0
    var canSuperSpeed: Boolean = false

    val TIME_TO_SPAWN_CAR: Float = 2f
    var timeToSpawnCar: Float = 0f

    val TIME_TO_SPAWN_COIN: Float = 1f
    var timeToSpawnCoin: Float = 0f

    val DURATION_SUPER_SPEED: Float = 5f
    var durationSuperSpeed: Float = 0f
    var isSuperSpeed: Boolean = false
    var currentSpeed: Float = 5f

    var score: Float = 0f
    var coins: Int = 0

    private val backgroundRoad: InfiniteScrollBackground
    var car: PlayerCar
    private val arrayEnemyCars: Array<EnemyCar>
    private val arrayCoins: Array<Coin>

    val lane2: Float = 390f
    val lane1: Float = 240f
    val lane0: Float = 90f

    init {
        setBounds(0f, 0f, WIDTH, HEIGHT)
        clip = true
        backgroundRoad = InfiniteScrollBackground(width, height)
        addActor(backgroundRoad)

        car = PlayerCar(this)
        addActor(car)
        arrayEnemyCars = Array()
        arrayCoins = Array()

        state = STATE_RUNNING
    }

    override fun act(delta: Float) {
        super.act(delta)

        durationSuperSpeed += delta
        if (durationSuperSpeed >= DURATION_SUPER_SPEED) {
            stopSuperSpeed()
        }

        if (numCoinsForSuperSpeed >= NUM_COINS_FOR_SUPER_SPEED) {
            canSuperSpeed = true
        }

        updateCar()
        updateEnemyCar(delta)
        updateCoins(delta)
        score += delta * currentSpeed

        if (car.state == PlayerCar.STATE_DEAD) {
            state = STATE_GAMEOVER
        }
    }

    private fun updateCar() {}

    private fun updateEnemyCar(delta: Float) {
        // First I create a car if necessary

        timeToSpawnCar += delta
        if (timeToSpawnCar >= TIME_TO_SPAWN_CAR) {
            timeToSpawnCar -= TIME_TO_SPAWN_CAR
            spawnCar()
        }

        var iterator: MutableIterator<EnemyCar> = arrayEnemyCars.iterator()
        while (iterator.hasNext()) {
            val enemyCar = iterator.next()
            if (enemyCar.bounds.y + enemyCar.height <= 0) {
                iterator.remove()
                removeActor(enemyCar)
                continue
            }

            if (isSuperSpeed) enemyCar.setSpeed()
        }

        // Then I check the collisions with the player
        iterator = arrayEnemyCars.iterator()
        while (iterator.hasNext()) {
            val enemyCar = iterator.next()
            if (enemyCar.bounds.overlaps(car.bounds)) {
                iterator.remove()

                if (enemyCar.x > car.x) {
                    enemyCar.crash(true, enemyCar.y > car.y)
                    if (!isSuperSpeed) car.crash(false, true)
                } else {
                    enemyCar.crash(false, enemyCar.y > car.y)
                    if (!isSuperSpeed) car.crash(true, true)
                }
                Assets.soundCrash?.stop()
                Assets.playSound(Assets.soundCrash!!)
            }
        }
    }

    private fun updateCoins(delta: Float) {
        timeToSpawnCoin += delta

        if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
            timeToSpawnCoin -= TIME_TO_SPAWN_COIN
            spawnCoin()
        }

        val iterator: MutableIterator<Coin> = arrayCoins.iterator()
        while (iterator.hasNext()) {
            val obj = iterator.next()
            if (obj.bounds.y + obj.height <= 0) {
                iterator.remove()
                removeActor(obj)
                continue
            }
            // I see if they are touching my car
            if (car.bounds.overlaps(obj.bounds)) {
                iterator.remove()
                removeActor(obj)
                coins++
                numCoinsForSuperSpeed++
                continue
            }

            // I see if it's touching an enemy
            for (enemyCar in arrayEnemyCars) {
                if (obj.bounds.overlaps(enemyCar.bounds)) {
                    iterator.remove()
                    removeActor(obj)
                    break
                }
            }

            if (isSuperSpeed) obj.setSpeed()
        }
    }

    fun setSuperSpeed() {
        canSuperSpeed = false
        durationSuperSpeed = 0f
        isSuperSpeed = true
        currentSpeed = 30f
        numCoinsForSuperSpeed = 0
        backgroundRoad.setSpeed()
    }

    fun stopSuperSpeed() {
        isSuperSpeed = false
        currentSpeed = 5f
        backgroundRoad.stopSpeed()
    }

    private fun spawnCar() {
        val lane = MathUtils.random(0, 2)
        var x = 0f
        if (lane == 0) x = lane0
        if (lane == 1) x = lane1
        if (lane == 2) x = lane2
        val enemyCar = EnemyCar(x, height)
        arrayEnemyCars.add(enemyCar)
        addActor(enemyCar)
    }

    private fun spawnCoin() {
        val lane = MathUtils.random(0, 2)
        var x = 0f
        if (lane == 0) x = lane0
        if (lane == 1) x = lane1
        if (lane == 2) x = lane2
        val obj = Coin(x, height)
        arrayCoins.add(obj)
        addActor(obj)
    }

    companion object {
        const val STATE_RUNNING: Int = 0
        const val STATE_GAMEOVER: Int = 1
        const val NUM_COINS_FOR_SUPER_SPEED: Int = 10
    }
}
