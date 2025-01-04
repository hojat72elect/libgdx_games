package com.nopalsoft.impossibledial.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.impossibledial.Assets;


public class ArrowEasy extends Arrow {
    private final float VELOCIDAD_INICIAL = 4;
    private final float VELOCIDAD_MAXIMA = .925f;


    public ArrowEasy(float x, float y) {
        super(x, y);
        velocidadActual = VELOCIDAD_INICIAL;
    }

    @Override
    public void didScore() {
        if (velocidadActual > 2.5f) {
            velocidadActual -= .4f;
        }
        else if (velocidadActual > 1.5f) {
            velocidadActual -= .025f;
        }
        else if (velocidadActual > VELOCIDAD_MAXIMA) {
            velocidadActual -= .005f;
        }

        if (velocidadActual < VELOCIDAD_MAXIMA) {
            velocidadActual = VELOCIDAD_MAXIMA;
        }
        init();
    }


    public void init() {
        //siempre que se inicializa se modifica el lado al que gira
        isGoingLeft = !isGoingLeft;
        int colorAnterior = colorActual;

        //Previene que al presionar no vuelva a aparecer el mismo color 2 veces
        do {
            colorActual = MathUtils.random(1, 4);
        } while (colorAnterior == colorActual);

        switch (colorActual) {
            case COLOR_GREEN:
                setDrawable(Assets.arrowGreen);
                break;
            case COLOR_RED:
                setDrawable(Assets.arrowRed);
                break;
            case COLOR_BLUE:
                setDrawable(Assets.arrowBlue);
                break;
            case COLOR_YELLOW:
                setDrawable(Assets.arrowYellow);
                break;

        }

        float angulo = 360;
        if (!isGoingLeft) {
            angulo = -angulo;
        }

        getActions().clear();
        addAction(Actions.rotateBy(angulo, velocidadActual));


    }

    /**
     * Los cuadrantes se representan de la siguiente manera
     * ##########
     * # 2 # 1 #
     * #########
     * # 3 # 4 #
     * #########
     *
     * @return
     */
    public int getFlechaApuntandoAlCuadrante() {

        if ((getRotation() >= 0 && getRotation() < 90) || (getRotation() >= -360 && getRotation() < -270)) {
            return COLOR_GREEN;
        }
        else if ((getRotation() >= 90 && getRotation() < 180) || (getRotation() >= -270 && getRotation() < -180)) {
            return COLOR_RED;
        }
        else if ((getRotation() >= 180 && getRotation() < 270) || (getRotation() >= -180 && getRotation() < -90)) {
            return COLOR_BLUE;
        }
        else {
            return COLOR_YELLOW;
        }
    }


}
