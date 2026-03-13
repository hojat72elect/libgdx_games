package com.nopalsoft.impossibledial.game_objects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Image

abstract class Arrow(x: Float, y: Float) : Image() {
    /**
     * El color de la flecha
     */
    var colorActual: Int
    protected var velocidadActual: Float = 0f
    var isGoingLeft: Boolean = false

    init {
        setSize(WIDTH, HEIGHT)
        setOrigin(WIDTH / 2f, 0f)
        setPosition(x, y)
        setRotation(22.5f)
        colorActual = COLOR_GREEN //Como al inicio esta apuntando al verde, asi nunca se crea inicialmente un verde
    }

    abstract fun didScore()

    abstract fun init()

    abstract val flechaApuntandoAlCuadrante: Int

    override fun act(delta: Float) {
        super.act(delta)

        if (getRotation() >= 360) {
            setRotation(0f)
        } else if (getRotation() <= -360) {
            setRotation(0f)
        }
    }

    fun setGameOver() {
        actions.clear()
    }

    companion object {
        const val WIDTH: Float = 12f
        const val HEIGHT: Float = 179f

        //Easy
        const val COLOR_YELLOW: Int = 1
        const val COLOR_GREEN: Int = 2
        const val COLOR_RED: Int = 3
        const val COLOR_BLUE: Int = 4

        //Hard
        const val COLOR_CYAN: Int = 5
        const val COLOR_BLACK: Int = 6
        const val COLOR_BROWN: Int = 7
        const val COLOR_PURPLE: Int = 8

        fun getColor(colorPalabra: Int): Color {
            val color: Color
            when (colorPalabra) {
                0 -> color = Color.BLUE
                1 -> color = Color.CYAN
                2 -> color = Color.GREEN
                3 -> color = Color.YELLOW
                4 -> color = Color.PINK
                5 -> color = Color(.6f, .3f, 0f, 1f) // Cafe
                6 -> color = Color.PURPLE
                7 -> color = Color.RED
                else -> color = Color.RED
            }
            return color
        }

        val randomColor: Color
            get() = getColor(MathUtils.random(7))
    }
}
