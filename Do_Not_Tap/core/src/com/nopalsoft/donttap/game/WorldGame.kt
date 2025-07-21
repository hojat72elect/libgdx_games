package com.nopalsoft.donttap.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.donttap.game_objects.Tile
import com.nopalsoft.donttap.screens.Screens

class WorldGame(var mode: Int) : Table() {
    @JvmField
    var state: Int

    var TIME_TO_SPAWN_ROW: Float = .4f
    var timeToSpawnRow: Float = 0f

    val WIDTH: Float = Screens.WORLD_WIDTH
    val HEIGHT: Float = Screens.WORLD_HEIGHT

    var arrTiles: Array<Tile>

    @JvmField
    var time: Float = 0f

    @JvmField
    var score: Int = 0

    init {
        setSize(WIDTH, HEIGHT)
        setPosition(0f, 0f)
        debug()

        arrTiles = Array<Tile>()
        addRow(true)
        addRow()
        addRow()
        addRow()

        state = STATE_READY

        when (mode) {
            MODE_CLASSIC -> {
                score = 100
                time = 0f
            }

            MODE_TIME -> {
                score = 0
                time = 60f
            }

            MODE_ENDLESS -> {
                score = 0
                time = 0f
                timeToSpawnRow = TIME_TO_SPAWN_ROW
            }
        }
    }

    fun addRow() {
        if (state == STATE_GAME_OVER_1 || state == STATE_GAME_OVER_2) return
        addRow(false)
    }

    fun addRow(isFirstRow: Boolean) {
        val tileCanStep = MathUtils.random(3)

        // Agrego toda una fila en Y= -1
        // para despues bajar todos los tiles 1 renglon
        for (col in 0..3) {
            val obj = Pools.obtain<Tile>(Tile::class.java)

            obj.init(this, col, tileCanStep == col, isFirstRow)

            addActor(obj)
            arrTiles.add(obj)
        }

        moveRowsDown()
    }

    fun moveRowsDown() {
        for (col in 23 downTo 0) {
            val obj = getTileAtPosition(col)
            if (obj != null) obj.moveDown()
        }
    }

    fun moveRowsUp() {
        for (col in 0..23) {
            val obj = getTileAtPosition(col)
            if (obj != null) obj.moveUp()
        }
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (state == STATE_GAME_OVER_1) {
            var moveActions = 0
            for (arrTile in arrTiles) {
                for (action in arrTile.actions) {
                    if (action is MoveToAction) moveActions++
                }
            }
            if (moveActions == 0) state = STATE_GAME_OVER_2
            return
        }

        if (state == STATE_RUNNING) {
            if (mode == MODE_TIME) {
                time -= delta
                if (time <= 0) time = 0f
            } else {
                time += delta
            }
            if (mode == MODE_ENDLESS) {
                timeToSpawnRow += delta
                if (timeToSpawnRow >= TIME_TO_SPAWN_ROW) {
                    timeToSpawnRow -= TIME_TO_SPAWN_ROW

                    TIME_TO_SPAWN_ROW -= (delta / 8f)
                    if (TIME_TO_SPAWN_ROW <= .2f) TIME_TO_SPAWN_ROW = .2f
                    addRow()
                }
            }
        }

        deleteOld()
        updatesTiles()
        if (checkIsGameover()) state = STATE_GAME_OVER_1

        if (checkGameWin()) state = STATE_GAME_WIN
    }

    // Actualizo los tiles que ya se pueden tocar
    private fun updatesTiles() {
        for (col in 0..23) {
            val obj = getTileAtPosition(col)
            if (obj != null) {
                if (obj.state == Tile.STATE_TAP) {
                    val previosObj = findPreviosTapTile(col - 1)
                    if (previosObj != null) previosObj.canBeTap = true
                }
            }
        }
    }

    private fun findPreviosTapTile(posActual: Int): Tile? {
        for (col in posActual downTo 0) {
            val obj = getTileAtPosition(col)
            if (obj != null) {
                if (obj.type == Tile.TYPE_GOOD) return obj
            }
        }
        return null
    }

    private fun deleteOld() {
        for (obj in arrTiles) {
            if (obj.tablePosition < 0 || obj.tablePosition > 23) {
                removeActor(obj)
                arrTiles.removeValue(obj, true)
                Pools.free(obj)
            }
        }
    }

    private fun checkIsGameover(): Boolean {
        for (obj in arrTiles) {
            if (obj.type == Tile.TYPE_BAD && obj.state == Tile.STATE_TAP) return true

            if (obj.tablePosition > 19 && obj.getY() < -90 && obj.type == Tile.TYPE_GOOD && obj.state == Tile.STATE_NORMAL) {
                moveRowsUp()
                return true
            }
        }
        if (mode == MODE_TIME) return time <= 0
        return false
    }

    private fun checkGameWin(): Boolean {
        when (mode) {
            MODE_CLASSIC -> if (score == 0) return true
            MODE_TIME -> if (time == 0f) return true
            MODE_ENDLESS -> return false
            else -> return false
        }
        return false
    }

    private fun getTileAtPosition(tablePosition: Int): Tile? {
        val ite = Array.ArrayIterator<Tile>(arrTiles)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.tablePosition == tablePosition) return obj
        }
        return null
    }

    companion object {
        const val STATE_READY: Int = 0
        const val STATE_RUNNING: Int = 1
        const val STATE_GAME_OVER_1: Int = 2
        const val STATE_GAME_OVER_2: Int = 3
        const val STATE_GAME_WIN: Int = 4
        const val MODE_CLASSIC: Int = 0 // Record 50 tiles in the shortest time
        const val MODE_TIME: Int = 1 // Walk for 1 minute
        const val MODE_ENDLESS: Int = 2 // Don't let any tile go by
    }
}
