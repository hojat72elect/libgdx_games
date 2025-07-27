package com.nopalsoft.ponyrace.game_objects;

public class Pisable extends GameObject {

    public final float ancho, alto;

    public Pisable(float x, float y, float ancho, float alto) {
        super(x, y);
        this.ancho = ancho;
        this.alto = alto;
    }
}
