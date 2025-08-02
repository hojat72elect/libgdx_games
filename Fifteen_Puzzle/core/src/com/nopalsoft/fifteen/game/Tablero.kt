package com.nopalsoft.fifteen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Array
import com.nopalsoft.fifteen.Assets
import com.nopalsoft.fifteen.Assets.playSoundMove
import com.nopalsoft.fifteen.objetos.Pieza
import com.nopalsoft.fifteen.screens.Screens

class Tablero : Group() {

    var state = 0
    var tiempo = 0f
    var moves = 0
    var moveUp = false
    var moveDown = false
    var moveLeft = false
    var moveRight = false
    var shuffle = false
    var arrPiezasNums: Array<Pieza>
    var oPiezaBlanca: Pieza

    init {
        setSize(480f, 480f)
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200f)
        addBackground()

        // Inicializco el tablero con puros ceros
        for (i in 0..15) {
            addActor(Pieza(i, 0))
        }

        arrPiezasNums = Array<Pieza>()
        for (i in 1..15) {
            val obj = Pieza(i - 1, i)
            addActor(obj)
            arrPiezasNums.add(obj)
        }
        oPiezaBlanca = Pieza(15, -10) // La pieza que no tiene nada vale -10;
        arrPiezasNums.add(oPiezaBlanca)
        addActor(oPiezaBlanca)

        shuffle() // Dentro de shuffle pongo el estado
    }

    private fun addBackground() {
        val background = Image(Assets.fondoTablero)
        background.setSize(getWidth(), getHeight())
        background.setPosition(0f, 0f)
        addActor(background)
    }

    private fun shuffle() {
        state = STATE_SHUFFLE
        val timesToShuffle = 3500

        var i = 0
        while (i < timesToShuffle) {
            val shuffle = MathUtils.random(4)
            when (shuffle) {
                0 -> if (!moveUp()) i--
                1 -> if (!moveDown()) i--
                2 -> if (!moveLeft()) i--
                3 -> if (!moveRight()) i--
                else -> if (!moveRight()) i--
            }
            i++
        }
        moves = 0
        tiempo = 0F
        state = STATE_RUNNING
    }

    // Revisa si la posicion de la izquierda esta en el mismo renglon
    private fun canMoveLeft(nextPos: Int): Boolean {
        return nextPos != 11 && nextPos != 7 && nextPos != 3 && nextPos != -1
    }

    // Revisa si la posicion de la derecha esta en el mismo renglon
    private fun canMoveRight(nextPos: Int): Boolean {
        return nextPos != 4 && nextPos != 8 && nextPos != 12 && nextPos != 16
    }

    private fun canMoveUp(nextPos: Int): Boolean {
        return nextPos >= 0
    }

    private fun canMoveDown(nextPos: Int): Boolean {
        return nextPos <= 15
    }

    // Intercambia la posicion de dos piezas
    private fun swapPieces(a: Pieza, b: Pieza) {
        val posA = a.posicion
        val posB = b.posicion

        if (state == STATE_SHUFFLE) {
            a.moveInstantly(posB)
            b.moveInstantly(posA)
        } else {
            a.moveToPosition(posB)
            b.moveToPosition(posA)
        }
        moves++
        if (state == STATE_RUNNING) playSoundMove()
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (state == STATE_NO_MORE_MOVES) {
            var numActions = 0
            for (arrPiezasNum in arrPiezasNums) {
                numActions += arrPiezasNum.actions.size
            }
            numActions += actions.size
            if (numActions == 0) state = STATE_GAMEOVER
            return
        }

        if (moveUp) {
            moveUp()
        } else if (moveDown) {
            moveDown()
        } else if (moveLeft) {
            moveLeft()
        } else if (moveRight) {
            moveRight()
        }

        if (shuffle) shuffle()

        shuffle = false
        moveUp = false
        moveRight = false
        moveLeft = false
        moveDown = false

        if (checkWinClassic()) {
            Gdx.app.log("WIN CLASSIC", "")
            state = STATE_NO_MORE_MOVES
        }

        tiempo += Gdx.graphics.rawDeltaTime
    }

    private fun getPiezaEnPos(pos: Int): Pieza? {
        val ite = Array.ArrayIterator<Pieza>(arrPiezasNums)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.posicion == pos) return obj
        }
        return null
    }

    /**
     * Verdadero si se logro hacer el movimiento
     */
    fun moveUp(): Boolean {
        val nextPos = oPiezaBlanca.posicion - 4
        if (canMoveUp(nextPos)) {
            swapPieces(oPiezaBlanca, getPiezaEnPos(nextPos)!!)
            return true
        }
        return false
    }

    /**
     * Verdadero si se logro hacer el movimiento
     */
    fun moveDown(): Boolean {
        val nextPos = oPiezaBlanca.posicion + 4
        if (canMoveDown(nextPos)) {
            swapPieces(oPiezaBlanca, getPiezaEnPos(nextPos)!!)
            return true
        }
        return false
    }

    /**
     * Verdadero si se logro hacer el movimiento
     */
    fun moveRight(): Boolean {
        val nextPos = oPiezaBlanca.posicion + 1
        if (canMoveRight(nextPos)) {
            swapPieces(oPiezaBlanca, getPiezaEnPos(nextPos)!!)
            return true
        }
        return false
    }

    /**
     * Verdadero si se logro hacer el movimiento
     */
    fun moveLeft(): Boolean {
        val nextPos = oPiezaBlanca.posicion - 1
        if (canMoveLeft(nextPos)) {
            swapPieces(oPiezaBlanca, getPiezaEnPos(nextPos)!!)
            return true
        }
        return false
    }

    fun checkWinClassic(): Boolean {
        for (i in 0..14) {
            val obj = getPiezaEnPos(i)

            if (i + 1 != obj!!.valor) return false
        }
        return true
    }

    companion object {
        const val STATE_SHUFFLE = 0
        const val STATE_RUNNING = 1
        const val STATE_NO_MORE_MOVES = 2
        const val STATE_GAMEOVER = 3
    }
}
