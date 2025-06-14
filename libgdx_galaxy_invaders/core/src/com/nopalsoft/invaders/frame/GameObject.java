package com.nopalsoft.invaders.frame;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
    public final Vector2 position;
    public final Rectangle boundsRectangle;
    public Circle boundsCircle;


    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.boundsRectangle = new Rectangle(x - width / 2, y - height / 2, width, height);
        boundsCircle = null;
    }

    public GameObject(float x, float y, float radio) {
        this.position = new Vector2(x, y);
        this.boundsRectangle = null;
        boundsCircle = new Circle(x, y, radio);
    }
}
