package com.esotericsoftware.spine;

import com.badlogic.gdx.graphics.Color;

public class BoneData {
    final BoneData parent;
    final String name;
    // Nonessential.
    final Color color = new Color(1, 1, 1, 1);
    float length;
    float x, y;
    float rotation;
    float scaleX = 1, scaleY = 1;
    boolean inheritScale = true, inheritRotation = true;

    /**
     * @param parent May be null.
     */
    public BoneData(String name, BoneData parent) {
        if (name == null) throw new IllegalArgumentException("name cannot be null.");
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return name;
    }
}
