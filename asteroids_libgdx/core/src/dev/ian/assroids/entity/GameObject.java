package dev.ian.assroids.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by: Ian Parcon
 * Date created: Sep 03, 2018
 * Time created: 10:52 AM
 */
public class GameObject implements Entity {

    protected float x, y;
    protected float dx, dy;
    protected Sprite sprite;
    protected Color color;

    public GameObject(Sprite sprite, float x, float y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;

        sprite.setPosition(x, y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public GameObject() {
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy += dy;
    }

    public void setDx(float dx) {
        this.dx += dx;
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);
    }

    public void originCenter() {
        sprite.setOriginCenter();
    }

    @Override
    public void update(float delta) {
        wrap();
    }

    private void wrap() {
        if (y > Gdx.graphics.getHeight()) y = -getHeight();
        if (y + getHeight() < 0) y = Gdx.graphics.getHeight();
        if (x + getWidth() < 0) x = Gdx.graphics.getWidth();
        if (x > Gdx.graphics.getWidth()) x = -getWidth();
    }

    public boolean isCollide(GameObject object) {
        return x < object.x + object.getWidth() &&
                x + getWidth() > object.x &&
                y < object.y + object.getHeight() &&
                y + getHeight() > object.y;
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
