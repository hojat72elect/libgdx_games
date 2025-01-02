package com.nopalsoft.sokoban.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.game_objects.Box
import com.nopalsoft.sokoban.game_objects.EndPoint
import com.nopalsoft.sokoban.game_objects.Player
import com.nopalsoft.sokoban.game_objects.Tile
import com.nopalsoft.sokoban.game_objects.Wall
import com.badlogic.gdx.utils.Array as GdxArray

class Board : Group() {

    var state = STATE_RUNNING

    /**
     * X previous position, Y current position.
     */
    private val arrayPlayerMoves = GdxArray<Vector2>()

    /**
     * X previous position, Y current position
     */
    private val arrayBoxMoves = GdxArray<Vector2>()

    private val arrayTiles = GdxArray<Tile>(25 * 15)
    private var player: Player? = null
    var moveUp = false
    var moveDown = false
    var moveLeft = false
    var moveRight = false
    var undo = false
    var moves = 0
    var time = 0F

    init {
        setSize(800F, 480F)

        initializeMap("StaticMap")
        initializeMap("Objetos")

        // AFTER initializing the objects I add them to the Board in order so that some are drawn before others.
        addToBoard(Wall::class.java)
        addToBoard(EndPoint::class.java)
        addToBoard(Box::class.java)
        addToBoard(Player::class.java)
    }

    private fun addToBoard(type: Class<*>) {
        for (obj in arrayTiles) {
            if (obj.javaClass == type) {
                addActor(obj)
            }
        }
    }

    private fun initializeMap(layerName: String) {
        val layer = Assets.map!!.layers[layerName] as TiledMapTileLayer?
        if (layer != null) {
            var tilePosition = 0
            for (y in 0..14) {
                for (x in 0..24) {
                    val cell = layer.getCell(x, y)
                    if (cell != null) {
                        val tile = cell.tile
                        if (tile.properties != null) {
                            if (tile.properties.containsKey("tipo")) {
                                val type = tile.properties["tipo"].toString()

                                when (type) {
                                    "startPoint" -> {
                                        createPlayer(tilePosition)
                                    }
                                    "pared" -> {
                                        createWall(tilePosition)
                                    }
                                    "caja" -> {
                                        createBox(tilePosition, tile.properties["color"].toString())
                                    }
                                    "endPoint" -> {
                                        createEndPoint(tilePosition, tile.properties["color"].toString())
                                    }
                                }
                            }
                        }
                    }
                    tilePosition++
                }
            }
        }
    }

    private fun createPlayer(tilePosition: Int) {
        val obj = Player(tilePosition)
        arrayTiles.add(obj)
        player = obj
    }

    private fun createWall(tilePosition: Int) {
        val obj = Wall(tilePosition)
        arrayTiles.add(obj)
    }

    private fun createBox(posTile: Int, color: String) {
        val obj = Box(posTile, color)
        arrayTiles.add(obj)
    }

    private fun createEndPoint(posTile: Int, color: String) {
        val obj = EndPoint(posTile, color)
        arrayTiles.add(obj)
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (state == STATE_RUNNING) {
            if (moves <= 0) undo = false

            if (undo) {

                if (arrayPlayerMoves.size >= moves) {
                    val posAntPersonaje = arrayPlayerMoves.removeIndex(moves - 1)
                    player!!.moveToPosition(posAntPersonaje.x.toInt(), true)
                }
                if (arrayBoxMoves.size >= moves) {
                    val posAntBox = arrayBoxMoves.removeIndex(moves - 1)
                    if (posAntBox != null) {
                        val box = getBoxInPosition(posAntBox.y.toInt())
                        box!!.moveToPosition(posAntBox.x.toInt(), true)
                        box.setIsInEndPoint(getEndPointInPosition(box.position))
                    }
                }
                moves--
                undo = false


            } else {
                var auxMoves = 0
                if (moveUp) {
                    auxMoves = 25
                } else if (moveDown) {
                    auxMoves = -25
                } else if (moveLeft) {
                    auxMoves = -1
                } else if (moveRight) {
                    auxMoves = 1
                }

                if (player!!.canMove() && (moveDown || moveLeft || moveRight || moveUp)) {
                    val nextPos = player!!.position + auxMoves

                    if (checkEmptySpace(nextPos) || (!checkIsBoxInPosition(nextPos) && checkIsEndInPosition(nextPos))) {
                        arrayPlayerMoves.add(Vector2(player!!.position.toFloat(), nextPos.toFloat()))
                        arrayBoxMoves.add(null)
                        player!!.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft)
                        moves++
                    } else {
                        if (checkIsBoxInPosition(nextPos)) {
                            val boxNextPos = nextPos + auxMoves
                            if (checkEmptySpace(boxNextPos) || (!checkIsBoxInPosition(boxNextPos) && checkIsEndInPosition(
                                    boxNextPos
                                ))
                            ) {
                                val box = getBoxInPosition(nextPos)

                                arrayPlayerMoves.add(Vector2(player!!.position.toFloat(), nextPos.toFloat()))
                                arrayBoxMoves.add(Vector2(box!!.position.toFloat(), boxNextPos.toFloat()))
                                moves++

                                box.moveToPosition(boxNextPos, false)
                                player!!.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft)
                                box.setIsInEndPoint(getEndPointInPosition(boxNextPos))
                            }
                        }
                    }
                }

                moveUp = false
                moveRight = false
                moveLeft = false
                moveDown = false

                if (checkBoxesMissingTheRightEndPoint() == 0) state = STATE_GAME_OVER
            }

            if (state == STATE_RUNNING) time += Gdx.graphics.rawDeltaTime
        }
    }

    private fun checkEmptySpace(pos: Int): Boolean {
        val iterator = GdxArray.ArrayIterator(arrayTiles)
        while (iterator.hasNext()) {
            if (iterator.next().position == pos) return false
        }
        return true
    }

    /**
     * Indicates whether the object at position is a box.
     */
    private fun checkIsBoxInPosition(position: Int): Boolean {
        var isBoxInPosition = false
        val ite = GdxArray.ArrayIterator(arrayTiles)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.position == position && obj is Box) isBoxInPosition = true
        }
        return isBoxInPosition
    }

    /**
     * Indicates whether the object at position is endPoint.
     */
    private fun checkIsEndInPosition(position: Int): Boolean {
        var isEndPointInPosition = false
        val ite = GdxArray.ArrayIterator(arrayTiles)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.position == position && obj is EndPoint) isEndPointInPosition = true
        }
        return isEndPointInPosition
    }

    private fun getBoxInPosition(pos: Int): Box? {
        val ite = GdxArray.ArrayIterator(arrayTiles)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.position == pos && obj is Box) return obj
        }
        return null
    }

    private fun getEndPointInPosition(pos: Int): EndPoint? {
        val ite = GdxArray.ArrayIterator(arrayTiles)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.position == pos && obj is EndPoint) return obj
        }
        return null
    }

    private fun checkBoxesMissingTheRightEndPoint(): Int {
        var numBox = 0
        val ite = GdxArray.ArrayIterator(arrayTiles)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj is Box) {
                if (!obj.isInRightEndPoint) numBox++
            }
        }
        return numBox
    }

    companion object {
        const val UNIT_SCALE = 1f
        const val STATE_RUNNING = 1
        const val STATE_GAME_OVER = 2
    }
}