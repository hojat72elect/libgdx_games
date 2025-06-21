package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.superjumper.screens.Screens

class Coin : Poolable {
    @JvmField
    var state = 0

    @JvmField
    val position = Vector2()

    var stateTime = 0f

    fun initialize(x: Float, y: Float) {
        position[x] = y
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float) {
        stateTime += delta
    }

    fun take() {
        state = STATE_TAKEN
        stateTime = 0f
    }

    override fun reset() {
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_TAKEN = 1
        const val DRAW_WIDTH = .27f
        const val DRAW_HEIGHT = .34f
        private const val WIDTH = .25f
        private const val HEIGHT = .32f

        private const val SPACE_BETWEEN_COINS = .025f // Variable so that the coins are not stuck together

        @JvmStatic
        fun createCoin(worldBox: World, coins: Array<Coin>, y: Float) {
            createCoinCube(worldBox, coins, y)
        }

        @JvmStatic
        fun createOneCoin(worldBox: World, coins: Array<Coin>, y: Float) {
            createCoin(worldBox, coins, generatePositionX(1), y)
        }

        private fun createCoinCube(worldBox: World, coins: Array<Coin>, y: Float) {
            val maxRows = MathUtils.random(25) + 1
            val maxColumns = MathUtils.random(6) + 1

            val x = generatePositionX(maxColumns)
            for (col in 0..<maxColumns) {
                for (ren in 0..<maxRows) {
                    createCoin(worldBox, coins, x + (col * (WIDTH + SPACE_BETWEEN_COINS)), y + (ren * (HEIGHT + SPACE_BETWEEN_COINS)))
                }
            }
        }

        /**
         * Generates an X position depending on the number of coins in the row so
         * that they do not go off the screen to the right or left.
         */
        private fun generatePositionX(numCoinsInRow: Int): Float {
            var x = MathUtils.random(Screens.WORLD_WIDTH) + WIDTH / 2f
            if (x + (numCoinsInRow * (WIDTH + SPACE_BETWEEN_COINS)) > Screens.WORLD_WIDTH) {
                x -= (x + (numCoinsInRow * (WIDTH + SPACE_BETWEEN_COINS))) - Screens.WORLD_WIDTH // Take the difference in width and what is passed
                x += WIDTH / 2f // Add half to make it stick
            }
            return x
        }

        private fun createCoin(worldBox: World, arrayCoins: Array<Coin>, x: Float, y: Float) {
            val coin = Pools.obtain(Coin::class.java)
            coin.initialize(x, y)

            val bodyDefinition = BodyDef()
            bodyDefinition.position.x = x
            bodyDefinition.position.y = y
            bodyDefinition.type = BodyType.StaticBody
            val body = worldBox.createBody(bodyDefinition)

            val shape = PolygonShape()
            shape.setAsBox(WIDTH / 2f, HEIGHT / 2f)

            val fixtureDefinition = FixtureDef()
            fixtureDefinition.shape = shape
            fixtureDefinition.isSensor = true
            body.createFixture(fixtureDefinition)
            body.userData = coin
            shape.dispose()
            arrayCoins.add(coin)
        }
    }
}
