package com.nopalsoft.impossibledial.game_objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.impossibledial.Assets

class ArrowHard(x: Float, y: Float) : Arrow(x, y) {
    init {
        velocidadActual = 4f
    }

    override fun didScore() {
        val VELOCIDAD_MAXIMA = 1.5f
        if (velocidadActual > 2.5f) {
            velocidadActual -= .25f
        } else if (velocidadActual > 1.5f) {
            velocidadActual -= .015f
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
            colorActual = MathUtils.random(1, 8)
        } while (colorAnterior == colorActual)

        when (colorActual) {
            COLOR_GREEN -> setDrawable(Assets.arrowGreen)
            COLOR_RED -> setDrawable(Assets.arrowRed)
            COLOR_BLUE -> setDrawable(Assets.arrowBlue)
            COLOR_YELLOW -> setDrawable(Assets.arrowYellow)
            COLOR_BLACK -> setDrawable(Assets.arrowBlack)
            COLOR_PURPLE -> setDrawable(Assets.arrowPurple)
            COLOR_BROWN -> setDrawable(Assets.arrowBrown)
            COLOR_CYAN -> setDrawable(Assets.arrowCyan)
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
            if ((getRotation() >= 0 && getRotation() <= 45) || (getRotation() >= -360 && getRotation() <= -315)) {
                return COLOR_GREEN
            } else if ((getRotation() >= 45 && getRotation() <= 90) || (getRotation() >= -315 && getRotation() <= -270)) {
                return COLOR_YELLOW
            } else if ((getRotation() >= 90 && getRotation() <= 135) || (getRotation() >= -270 && getRotation() <= -225)) {
                return COLOR_BROWN
            } else if ((getRotation() >= 135 && getRotation() <= 180) || (getRotation() >= -225 && getRotation() <= -180)) {
                return COLOR_PURPLE
            } else if ((getRotation() >= 180 && getRotation() <= 225) || (getRotation() >= -180 && getRotation() <= -135)) {
                return COLOR_BLUE
            } else if ((getRotation() >= 225 && getRotation() <= 270) || (getRotation() >= -135 && getRotation() <= -90)) {
                return COLOR_BLACK
            } else if ((getRotation() >= 270 && getRotation() <= 315) || (getRotation() >= -90 && getRotation() <= -45)) {
                return COLOR_RED
            } else {
                return COLOR_CYAN
            }
        }
}
