package com.nopalsoft.sokoban.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.utils.Array
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.objects.Box
import com.nopalsoft.sokoban.objects.Player
import com.nopalsoft.sokoban.objects.TargetPlatform
import com.nopalsoft.sokoban.objects.Tile
import com.nopalsoft.sokoban.objects.Wall

class GameBoard : Group() {
    @JvmField
    var state: Int

    /**
     * X previous position, Y current position.
     */
    var playerMoves: Array<Vector2>

    /**
     * X previous position, Y current position.
     */
    var boxMoves: Array<Vector2?>

    var tiles: Array<Tile>
    private var player: Player? = null

    @JvmField
    var moveUp: Boolean = false

    @JvmField
    var moveDown: Boolean = false

    @JvmField
    var moveLeft: Boolean = false

    @JvmField
    var moveRight: Boolean = false

    @JvmField
    var undo: Boolean = false

    @JvmField
    var moves: Int

    @JvmField
    var time: Float

    init {
        setSize(800f, 480f)

        tiles = Array(25 * 15)
        playerMoves = Array()
        boxMoves = Array()

        initializeMap("StaticMap")
        initializeMap("Objetos")

        // AFTER initializing the objects I add them to the Board in order so that some are drawn before others
        addByTypeToBoard(Wall::class.java)
        addByTypeToBoard(TargetPlatform::class.java)
        addByTypeToBoard(Box::class.java)
        addByTypeToBoard(Player::class.java)

        state = STATE_RUNNING

        moves = 0
        time = moves.toFloat()
    }

    private fun addByTypeToBoard(tileType: Class<*>) {
        for (tile in tiles) {
            if (tile.javaClass == tileType) {
                addActor(tile)
            }
        }
    }

    private fun initializeMap(layerName: String) {
        val layer = Assets.map!!.layers[layerName] as TiledMapTileLayer
        if (layer != null) {
            var tilePosition = 0
            for (y in 0..14) {
                for (x in 0..24) {
                    val cell = layer.getCell(x, y)
                    if (cell != null) {
                        val tile = cell.tile
                        if (tile.properties != null) {
                            if (tile.properties.containsKey("tipo")) {
                                val tileType = tile.properties["tipo"].toString()

                                when (tileType) {
                                    "startPoint" -> createPlayer(tilePosition)
                                    "pared" -> createWall(tilePosition)
                                    "caja" -> createBox(tilePosition, tile.properties["color"].toString())
                                    "endPoint" -> createTargetPlatform(tilePosition, tile.properties["color"].toString())
                                }
                            }
                        }
                    }
                    tilePosition++
                }
            }
        }
    }

    private fun createPlayer(position: Int) {
        val playerTile = Player(position)
        tiles.add(playerTile)
        player = playerTile
    }

    private fun createWall(position: Int) {
        val wallTile = Wall(position)
        tiles.add(wallTile)
    }

    private fun createBox(position: Int, color: String) {
        val boxTile = Box(position, color)
        tiles.add(boxTile)
    }

    private fun createTargetPlatform(position: Int, color: String) {
        val targetPlatformTile = TargetPlatform(position, color)
        tiles.add(targetPlatformTile)
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (state == STATE_RUNNING) {
            if (moves <= 0) undo = false

            if (undo) {
                undo()
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

                    if (isPositionEmpty(nextPos) || (!isBoxAtPosition(nextPos) && isTargetPlatformAtPosition(nextPos))) {
                        playerMoves.add(Vector2(player!!.position.toFloat(), nextPos.toFloat()))
                        boxMoves.add(null)
                        player!!.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft)
                        moves++
                    } else {
                        if (isBoxAtPosition(nextPos)) {
                            val boxNextPosition = nextPos + auxMoves
                            if (isPositionEmpty(boxNextPosition) || (!isBoxAtPosition(boxNextPosition) && isTargetPlatformAtPosition(boxNextPosition))) {
                                val box = getBoxInPosition(nextPos)

                                playerMoves.add(Vector2(player!!.position.toFloat(), nextPos.toFloat()))
                                boxMoves.add(Vector2(box!!.position.toFloat(), boxNextPosition.toFloat()))
                                moves++

                                box.moveToPosition(boxNextPosition, false)
                                player!!.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft)
                                box.setIsInEndPoint(getEndPointInPosition(boxNextPosition))
                            }
                        }
                    }
                }

                moveUp = false
                moveRight = moveUp
                moveLeft = moveRight
                moveDown = moveLeft

                if (checkBoxesMissingTheRightEndPoint() == 0) state = STATE_GAME_OVER
            }

            if (state == STATE_RUNNING) time += Gdx.graphics.rawDeltaTime
        }
    }

    private fun undo() {
        if (playerMoves.size >= moves) {
            val playerLastPosition = playerMoves.removeIndex(moves - 1)
            player!!.moveToPosition(playerLastPosition.x.toInt(), true)
        }
        if (boxMoves.size >= moves) {
            val boxLastPosition = boxMoves.removeIndex(moves - 1)
            if (boxLastPosition != null) {
                val box = getBoxInPosition(boxLastPosition.y.toInt())
                box!!.moveToPosition(boxLastPosition.x.toInt(), true)
                box.setIsInEndPoint(getEndPointInPosition(box.position))
            }
        }
        moves--
        undo = false
    }

    private fun isPositionEmpty(position: Int): Boolean {
        val iterator = Array.ArrayIterator(tiles)
        while (iterator.hasNext()) {
            if (iterator.next().position == position) return false
        }
        return true
    }

    /**
     * Indicates whether the object at position is a box.
     */
    private fun isBoxAtPosition(position: Int): Boolean {
        var isBoxInPosition = false
        val iterator = Array.ArrayIterator(tiles)
        while (iterator.hasNext()) {
            val obj = iterator.next()
            if (obj.position == position && obj is Box) isBoxInPosition = true
        }
        return isBoxInPosition
    }

    /**
     * Indicates whether the object at position is endPoint.
     */
    private fun isTargetPlatformAtPosition(position: Int): Boolean {
        var isEndPointInPosition = false
        val iterator = Array.ArrayIterator(tiles)
        while (iterator.hasNext()) {
            val obj = iterator.next()
            if (obj.position == position && obj is TargetPlatform) isEndPointInPosition = true
        }
        return isEndPointInPosition
    }

    private fun getBoxInPosition(position: Int): Box? {
        val ite = Array.ArrayIterator(tiles)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.position == position && obj is Box) return obj
        }
        return null
    }

    private fun getEndPointInPosition(position: Int): TargetPlatform? {
        val iterator = Array.ArrayIterator(tiles)
        while (iterator.hasNext()) {
            val obj = iterator.next()
            if (obj.position == position && obj is TargetPlatform) return obj
        }
        return null
    }

    private fun checkBoxesMissingTheRightEndPoint(): Int {
        var numBox = 0
        val iterator = Array.ArrayIterator(tiles)
        while (iterator.hasNext()) {
            val obj = iterator.next()
            if (obj is Box) {
                if (!obj.isInRightEndPoint) numBox++
            }
        }
        return numBox
    }

    companion object {
        const val UNIT_SCALE: Float = 1f

        const val STATE_RUNNING: Int = 1
        const val STATE_GAME_OVER: Int = 2
    }
}
