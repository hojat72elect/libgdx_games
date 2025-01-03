package com.nopalsoft.dragracer.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.Assets.playSound
import com.nopalsoft.dragracer.game_objects.Coin
import com.nopalsoft.dragracer.game_objects.EnemyCar
import com.nopalsoft.dragracer.game_objects.InfiniteScrollBackground
import com.nopalsoft.dragracer.game_objects.PlayerCar
import com.nopalsoft.dragracer.screens.BaseScreen

class TrafficGame : Table() {
    val lane2 = 390F
    val lane1 = 240F
    val lane0 = 90F

    private val _width = BaseScreen.WORLD_WIDTH
    private val _height = BaseScreen.WORLD_HEIGHT

    private val backgroundRoad: InfiniteScrollBackground
    private val arrayEnemyCars: Array<EnemyCar>
    private val arrCoins: Array<Coin>
    var state: Int
    var numberOfCoinsForSuperSpeed = 0
    var playerCar: PlayerCar
    var canSuperSpeed = false
    private var timeToSpawnCar = 0f
    private var timeToSpawnCoin = 0f
    private var durationSuperSpeed = 0f
    private var isSuperSpeed = false
    private var currentSpeed = 5f
    var score = 0f
    var coins = 0

    init {
        setBounds(0f, 0f, _width, _height)
        clip = true
        backgroundRoad = InfiniteScrollBackground(width, height)
        addActor(backgroundRoad)

        playerCar = PlayerCar(this)
        addActor(playerCar)
        arrayEnemyCars = Array()
        arrCoins = Array()

        state = STATE_RUNNING
    }

    override fun act(delta: Float) {
        super.act(delta)

        durationSuperSpeed += delta
        if (durationSuperSpeed >= DURATION_SUPER_SPEED) {
            stopSuperSpeed()
        }

        if (numberOfCoinsForSuperSpeed >= NUM_COINS_FOR_SUPER_SPEED) {
            canSuperSpeed = true
        }


        updateEnemyCar(delta)
        updateCoins(delta)
        score += delta * currentSpeed

        if (playerCar.state == PlayerCar.STATE_DEAD) {
            state = STATE_GAME_OVER
        }
    }

    private fun updateEnemyCar(delta: Float) {
        // I first create a car if necessary.

        timeToSpawnCar += delta
        if (timeToSpawnCar >= TIME_TO_SPAWN_CAR) {
            timeToSpawnCar -= TIME_TO_SPAWN_CAR
            spawnCar()
        }

        var iteratorEnemyCar = arrayEnemyCars.iterator()
        while (iteratorEnemyCar.hasNext()) {
            val enemyCar = iteratorEnemyCar.next()
            if (enemyCar.bounds.y + enemyCar.height <= 0) {
                iteratorEnemyCar.remove()
                removeActor(enemyCar)
                continue
            }

            if (isSuperSpeed) enemyCar.setSpeed()
        }

        // Then I check the collisions with the player
        iteratorEnemyCar = arrayEnemyCars.iterator()
        while (iteratorEnemyCar.hasNext()) {
            val enemyCar = iteratorEnemyCar.next()
            if (enemyCar.bounds.overlaps(playerCar.bounds)) {
                iteratorEnemyCar.remove()

                if (enemyCar.x > playerCar.x) {
                    enemyCar.crash(true, enemyCar.y > playerCar.y)
                    if (isSuperSpeed.not()) playerCar.crash(front = false, above = true)
                } else {
                    enemyCar.crash(false, enemyCar.y > playerCar.y)
                    if (isSuperSpeed.not()) playerCar.crash(front = true, above = true)
                }
                Assets.soundCrash.stop()
                playSound(Assets.soundCrash)
            }
        }
    }

    private fun updateCoins(delta: Float) {
        timeToSpawnCoin += delta

        if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
            timeToSpawnCoin -= TIME_TO_SPAWN_COIN
            spawnCoin()
        }

        val iterator = arrCoins.iterator()
        while (iterator.hasNext()) {
            val obj = iterator.next()
            if (obj.bounds.y + obj.height <= 0) {
                iterator.remove()
                removeActor(obj)
                continue
            }
            // I see if they are touching my car
            if (playerCar.bounds.overlaps(obj.bounds)) {
                iterator.remove()
                removeActor(obj)
                coins++
                numberOfCoinsForSuperSpeed++
                continue
            }

            // I see if it's touching an enemy
            for (objEnemy in arrayEnemyCars) {
                if (obj.bounds.overlaps(objEnemy.bounds)) {
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
        numberOfCoinsForSuperSpeed = 0
        backgroundRoad.setSpeed()
    }

    private fun stopSuperSpeed() {
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
        arrCoins.add(obj)
        addActor(obj)
    }

    companion object {
        const val STATE_RUNNING = 0
        const val STATE_GAME_OVER = 1
        const val NUM_COINS_FOR_SUPER_SPEED = 10
        private const val TIME_TO_SPAWN_CAR = 2f
        private const val TIME_TO_SPAWN_COIN = 1f
        private const val DURATION_SUPER_SPEED = 5f
    }
}
