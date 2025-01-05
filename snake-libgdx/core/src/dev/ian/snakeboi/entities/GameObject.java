package dev.ian.snakeboi.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.ian.snakeboi.game.GameInfo;

/**
 * Created by: Ian Parcon
 * Date created: Aug 23, 2018
 * Time created: 3:05 PM
 */
public class GameObject {

    protected Sprite sprite;
    protected float x;
    protected float y;

    public GameObject(Sprite sprite, float x, float y) {
        this.sprite = sprite;
        setPosition(x, y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public GameObject(Sprite sprite) {
        this.sprite = sprite;
        setSize(GameInfo.SCALE, GameInfo.SCALE);
    }


    public void setRotation(float degree) {
        sprite.setRotation(degree);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        sprite.setPosition(x,y);
    }
    public void setColor(Color color){
        sprite.setColor(color.r ,color.g ,color.b ,255);
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);
    }

    public boolean isCollide(GameObject object) {
        return x < object.x + object.getWidth() &&
                x + getWidth() > object.x &&
                y < object.y + object.getHeight() &&
                y + getHeight() > object.y;
    }


    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void setRotation(int rotation) {
        sprite.setRotation(rotation);
    }
}
