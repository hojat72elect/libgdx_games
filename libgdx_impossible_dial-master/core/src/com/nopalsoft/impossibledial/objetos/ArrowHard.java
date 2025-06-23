package com.nopalsoft.impossibledial.objetos;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.impossibledial.Assets;


public class ArrowHard extends Arrow {
    private final float VELOCIDAD_INICIAL = 4;
    private final float VELOCIDAD_MAXIMA = 1.5f;

    public ArrowHard(float x, float y) {
        super(x, y);
        velocidadActual = VELOCIDAD_INICIAL;
    }

    @Override
    public void didScore() {
        if (velocidadActual > 2.5f) {
            velocidadActual -= .25f;
        }
        else if (velocidadActual > 1.5f) {
            velocidadActual -= .015f;
        }
        else if (velocidadActual > VELOCIDAD_MAXIMA) {
            velocidadActual -= .0025f;
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
            colorActual = MathUtils.random(1, 8);
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
            case COLOR_BLACK:
                setDrawable(Assets.arrowBlack);
                break;
            case COLOR_PURPLE:
                setDrawable(Assets.arrowPurple);
                break;
            case COLOR_BROWN:
                setDrawable(Assets.arrowBrown);
                break;
            case COLOR_CYAN:
                setDrawable(Assets.arrowCyan);
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

        if ((getRotation() >= 0 && getRotation() <= 45) || (getRotation() >= -360 && getRotation() <= -315)) {
            return COLOR_GREEN;
        }
        else if ((getRotation() >= 45 && getRotation() <= 90) || (getRotation() >= -315 && getRotation() <= -270)) {
            return COLOR_YELLOW;
        }
        else if ((getRotation() >= 90 && getRotation() <= 135) || (getRotation() >= -270 && getRotation() <= -225)) {
            return COLOR_BROWN;
        }
        else if ((getRotation() >= 135 && getRotation() <= 180) || (getRotation() >= -225 && getRotation() <= -180)) {
            return COLOR_PURPLE;
        }
        else if ((getRotation() >= 180 && getRotation() <= 225) || (getRotation() >= -180 && getRotation() <= -135)) {
            return COLOR_BLUE;
        }
        else if ((getRotation() >= 225 && getRotation() <= 270) || (getRotation() >= -135 && getRotation() <= -90)) {
            return COLOR_BLACK;
        }
        else if ((getRotation() >= 270 && getRotation() <= 315) || (getRotation() >= -90 && getRotation() <= -45)) {
            return COLOR_RED;
        }
        else {
            return COLOR_CYAN;
        }
    }




}
