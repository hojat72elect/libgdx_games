package com.nopalsoft.ponyrace.objetos;

public class Pisable extends GameObject {

    public final float ancho, alto;

    public Pisable(float x, float y, float ancho, float alto) {
        super(x, y, 0);
        this.ancho = ancho;
        this.alto = alto;
    }
}
