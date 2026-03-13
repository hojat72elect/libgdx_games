package com.nopalsoft.dosmil.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.Assets.playSoundMove
import com.nopalsoft.dosmil.game_objects.BoardPiece
import com.nopalsoft.dosmil.screens.Screens
import com.badlogic.gdx.utils.Array as GdxArray

class GameBoard : Group() {

    var state = STATE_RUNNING
    private var boardPieces: GdxArray<BoardPiece> = GdxArray(16)
    var elapsedTime = 0f
    var score = 0L
    var moveUp = false
    var moveDown = false
    var moveLeft = false
    var moveRight = false
    var didWin = false

    init {
        setSize(480f, 480f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 200f)
        addBackground()

        // I initialize the board with all zeros
        for (i in 0..15)
            addActor(BoardPiece(i, 0))


        addPiece()
        addPiece()
    }

    private fun addBackground() {
        val background = Image(Assets.backgroundBoardAtlasRegion)
        background.setSize(width, height)
        background.setPosition(0f, 0f)
        addActor(background)
    }

    private fun addPiece() {
        if (isBoardFull) return
        var isEmpty = false
        var num = 0
        while (!isEmpty) {
            num = MathUtils.random(15)
            isEmpty = isSpaceEmpty(num)
        }
        val valor = if (MathUtils.random(1) == 0) 2 else 4 // The initial value can be 2 or 4
        val obj = BoardPiece(num, valor)
        boardPieces.add(obj)
        addActor(obj)
    }

    override fun act(delta: Float) {
        super.act(delta)

        // If there are no pending actions now, I'll put myself in gameover.
        if (state == STATE_NO_MORE_MOVES) {
            var numActions = 0
            for (arrBoardPiece in boardPieces) {
                numActions += arrBoardPiece.actions.size
            }
            numActions += actions.size
            if (numActions == 0) state = STATE_GAMEOVER
            return
        }

        var didPieceMove = false

        if (moveUp) {
            for (con in 0..3) {
                val i: MutableIterator<BoardPiece> = boardPieces.iterator()
                while (i.hasNext()) {
                    val obj = i.next()
                    val nextPos = obj.currentPosition - 4
                    // First I check if it can be put together
                    if (canMergeUp(obj.currentPosition, nextPos)) {
                        val objNext = getPieceAtPosition(nextPos)
                        if (!objNext!!.justChanged && !obj.justChanged) {
                            i.remove()
                            removePiece(obj)
                            objNext.worth *= 2
                            score += objNext.worth.toLong()
                            objNext.justChanged = true
                            didPieceMove = true
                            continue
                        }
                    }
                    if (isSpaceAboveEmpty(nextPos)) {
                        obj.moveToPosition(nextPos)
                        didPieceMove = true
                    }
                }
            }
        } else if (moveDown) {
            for (con in 0..3) {
                val i: MutableIterator<BoardPiece> = boardPieces.iterator()
                while (i.hasNext()) {
                    val obj = i.next()
                    val nextPos = obj.currentPosition + 4
                    // First I check if it can be put together
                    if (canMergeUp(obj.currentPosition, nextPos)) {
                        val objNext = getPieceAtPosition(nextPos)
                        if (!objNext!!.justChanged && !obj.justChanged) {
                            i.remove()
                            removePiece(obj)
                            objNext.worth *= 2
                            score += objNext.worth.toLong()
                            objNext.justChanged = true
                            didPieceMove = true
                            continue
                        }
                    }
                    if (isSpaceUnderEmpty(nextPos)) {
                        obj.moveToPosition(nextPos)
                        didPieceMove = true
                    }
                }
            }
        } else if (moveLeft) {
            for (con in 0..3) {
                val i: MutableIterator<BoardPiece> = boardPieces.iterator()
                while (i.hasNext()) {
                    val obj = i.next()
                    val nextPos = obj.currentPosition - 1
                    // First I check if it can be put together
                    if (canMergeTiles(obj.currentPosition, nextPos)) {
                        val objNext = getPieceAtPosition(nextPos)
                        if (!objNext!!.justChanged && !obj.justChanged) {
                            i.remove()
                            removePiece(obj)
                            objNext.worth *= 2
                            score += objNext.worth.toLong()
                            objNext.justChanged = true
                            didPieceMove = true
                            continue
                        }
                    }
                    if (isSpaceLeftEmpty(nextPos)) {
                        obj.moveToPosition(nextPos)
                        didPieceMove = true
                    }
                }
            }
        } else if (moveRight) {
            for (con in 0..3) {
                val i: MutableIterator<BoardPiece> = boardPieces.iterator()
                while (i.hasNext()) {
                    val obj = i.next()
                    val nextPos = obj.currentPosition + 1
                    // First I check if it can be put together
                    if (canMergeTiles(obj.currentPosition, nextPos)) {
                        val objNext = getPieceAtPosition(nextPos)
                        if (!objNext!!.justChanged && !obj.justChanged) {
                            i.remove()
                            removePiece(obj)
                            objNext.worth *= 2
                            score += objNext.worth.toLong()
                            objNext.justChanged = true
                            didPieceMove = true
                            continue
                        }
                    }
                    if (isSpaceRightEmpty(nextPos)) {
                        obj.moveToPosition(nextPos)
                        didPieceMove = true
                    }
                }
            }
        }

        if (didWin()) {
            state = STATE_NO_MORE_MOVES
            didWin = true
        }

        if ((moveUp || moveDown || moveRight || moveLeft) && didPieceMove) {
            addPiece()
            playSoundMove()
        }

        if (isBoardFull && !isPossibleToMove) {
            state = STATE_NO_MORE_MOVES
        }

        moveUp = false
        moveRight = moveUp
        moveLeft = moveRight
        moveDown = moveLeft

        elapsedTime += Gdx.graphics.getRawDeltaTime()
    }

    /**
     * Checks to see if two tiles can be merged together.
     */
    private fun canMergeTiles(posActual: Int, nextPosition: Int): Boolean {
        if ((posActual == 3 || posActual == 7 || posActual == 11) && nextPosition > posActual)  // Only those of the same rank can be merged together.
            return false
        if ((posActual == 12 || posActual == 8 || posActual == 4) && nextPosition < posActual) return false
        val obj1 = getPieceAtPosition(posActual)
        val obj2 = getPieceAtPosition(nextPosition)

        return if (obj1 == null || obj2 == null) false
        else obj1.worth == obj2.worth
    }

    /**
     * If it's possible to merge this tile with the tile above it.
     */
    private fun canMergeUp(posActual: Int, nextPosition: Int): Boolean {
        val obj1 = getPieceAtPosition(posActual)
        val obj2 = getPieceAtPosition(nextPosition)

        return if (obj1 == null || obj2 == null) false
        else obj1.worth == obj2.worth
    }

    /**
     * Check if the space at this position is empty.
     */
    private fun isSpaceEmpty(pos: Int): Boolean {
        val ite = GdxArray.ArrayIterator(boardPieces)
        while (ite.hasNext()) {
            if (ite.next().currentPosition == pos) return false
        }
        return true
    }

    /**
     * Checks to see if this tile can move upwards.
     */
    private fun isSpaceAboveEmpty(pos: Int): Boolean {
        if (pos < 0) return false
        return isSpaceEmpty(pos)
    }

    /**
     * Checks to see if this tile can move downwards.
     */
    private fun isSpaceUnderEmpty(pos: Int): Boolean {
        if (pos > 15) return false
        return isSpaceEmpty(pos)
    }

    /**
     * Checks to see if this tile can move to the right.
     */
    private fun isSpaceRightEmpty(pos: Int): Boolean {
        if (pos == 4 || pos == 8 || pos == 12 || pos == 16) return false
        return isSpaceEmpty(pos)
    }

    /**
     * Checks to see if this tile can move to the left.
     */
    private fun isSpaceLeftEmpty(pos: Int): Boolean {
        if (pos == 11 || pos == 7 || pos == 3 || pos == -1) return false
        return isSpaceEmpty(pos)
    }

    /**
     * Get access to the  board piece at this position.
     */
    private fun getPieceAtPosition(pos: Int): BoardPiece? {
        val ite = GdxArray.ArrayIterator(boardPieces)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.currentPosition == pos) return obj
        }
        return null
    }

    private val isBoardFull: Boolean
        /**
         * Checks to see if the game board is full.
         */
        get() = boardPieces.size == (16)

    private fun didWin(): Boolean {
        val ite = GdxArray.ArrayIterator(boardPieces)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.worth >= 2000)  // If there is a piece worth more than 15 thousand, you win.
                return true
        }
        return false
    }

    private val isPossibleToMove: Boolean
        get() {
            var canMove = isPossibleToMoveRightLeft

            if (isPossibleToMoveUpDown) {
                canMove = true
            }
            return canMove
        }

    private val isPossibleToMoveRightLeft: Boolean
        get() {
            var ren = 0
            while (ren < 16) {
                for (col in ren..<ren + 3) {
                    if (canMergeTiles(col, col + 1)) return true
                }
                ren += 4
            }
            return false
        }

    private val isPossibleToMoveUpDown: Boolean
        get() {
            for (col in 0..3) {
                var ren = col
                while (ren < col + 16) {
                    if (canMergeUp(ren, ren + 4)) return true
                    ren += 4
                }
            }
            return false
        }

    private fun removePiece(obj: BoardPiece) {
        removeActor(obj)
        boardPieces.removeValue(obj, true)
    }

    companion object {
        const val STATE_RUNNING: Int = 1
        const val STATE_NO_MORE_MOVES: Int = 2
        const val STATE_GAMEOVER: Int = 3
    }
}
