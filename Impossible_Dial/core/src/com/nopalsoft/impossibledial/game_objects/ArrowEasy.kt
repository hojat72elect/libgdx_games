package com.nopalsoft.impossibledial.game_objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.impossibledial.Assets

class ArrowEasy(x: Float, y: Float) : Arrow(x, y) {
    init {
        velocidadActual = 4f
    }

    override fun didScore() {
        val VELOCIDAD_MAXIMA = .925f
        if (velocidadActual > 2.5f) {
            velocidadActual -= .4f
        } else if (velocidadActual > 1.5f) {
            velocidadActual -= .025f
        } else if (velocidadActual > VELOCIDAD_MAXIMA) {
            velocidadActual -= .005f
        }

        if (velocidadActual < VELOCIDAD_MAXIMA) {
            velocidadActual = VELOCIDAD_MAXIMA
        }
        init()
    }


    override fun init() {
        //siempre que se inicializa se modifica el lado al que gira
        isGoingLeft = !isGoingLeft
        val colorAnterior = colorActual

        //Previene que al presionar no vuelva a aparecer el mismo color 2 veces
        do {
            colorActual = MathUtils.random(1, 4)
        } while (colorAnterior == colorActual)

        when (colorActual) {
            COLOR_GREEN -> setDrawable(Assets.arrowGreen)
            COLOR_RED -> setDrawable(Assets.arrowRed)
            COLOR_BLUE -> setDrawable(Assets.arrowBlue)
            COLOR_YELLOW -> setDrawable(Assets.arrowYellow)
        }

        var angulo = 360f
        if (!isGoingLeft) {
            angulo = -angulo
        }

        actions.clear()
        addAction(Actions.rotateBy(angulo, velocidadActual))
    }

    override val flechaApuntandoAlCuadrante: Int
        /**
         * Los cuadrantes se representan de la siguiente manera
         * ##########
         * # 2 # 1 #
         * #########
         * # 3 # 4 #
         * #########
         */
        get() {
            return if ((getRotation() >= 0 && getRotation() < 90) || (getRotation() >= -360 && getRotation() < -270)) {
                COLOR_GREEN
            } else if ((getRotation() >= 90 && getRotation() < 180) || (getRotation() >= -270 && getRotation() < -180)) {
                COLOR_RED
            } else if ((getRotation() >= 180 && getRotation() < 270) || (getRotation() >= -180 && getRotation() < -90)) {
                COLOR_BLUE
            } else {
                COLOR_YELLOW
            }
        }
}
