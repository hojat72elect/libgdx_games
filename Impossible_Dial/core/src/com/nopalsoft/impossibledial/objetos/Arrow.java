package com.nopalsoft.impossibledial.objetos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Arrow extends Image {
    public static final float WIDTH = 12;
    public static final float HEIGHT = 179;

    //Easy
    public static final int COLOR_YELLOW = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_RED = 3;
    public static final int COLOR_BLUE = 4;
    //Hard
    public static final int COLOR_CYAN = 5;
    public static final int COLOR_BLACK = 6;
    public static final int COLOR_BROWN = 7;
    public static final int COLOR_PURPLE = 8;

    /**
     * El color de la flecha
     */
    public int colorActual;
    protected float velocidadActual;

    boolean isGoingLeft;

    public Arrow(float x, float y) {
        setSize(WIDTH, HEIGHT);
        setOrigin(WIDTH / 2f, 0);
        setPosition(x, y);
        setRotation(22.5f);
        colorActual = COLOR_GREEN;//Como al inicio esta apuntando al verde, asi nunca se crea inicialmente un verde
    }

    public abstract void didScore();

    public abstract void init();

    public abstract int getFlechaApuntandoAlCuadrante();

    @Override
    public void act(float delta) {
        super.act(delta);

        if (getRotation() >= 360) {
            setRotation(0);
        }
        else if (getRotation() <= -360) {
            setRotation(0);
        }
    }


    public void setGameOver() {
        getActions().clear();
    }


    public static Color getColor(int colorPalabra) {
        Color color;
        switch (colorPalabra) {
            case 0:
                color = Color.BLUE;
                break;
            case 1:
                color = Color.CYAN;
                break;
            case 2:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.PINK;
                break;
            case 5:
                color = new Color(.6f, .3f, 0, 1);// Cafe
                break;
            case 6:
                color = Color.PURPLE;
                break;
            default:
            case 7:
                color = Color.RED;
                break;
        }
        return color;
    }

    public static Color getRandomColor() {
        return getColor(MathUtils.random(7));
    }

}
