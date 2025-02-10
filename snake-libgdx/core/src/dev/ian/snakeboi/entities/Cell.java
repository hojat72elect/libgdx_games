package dev.ian.snakeboi.entities;

import static dev.ian.snakeboi.game.GameInfo.SCALE;

import com.badlogic.gdx.graphics.g2d.Sprite;

import dev.ian.snakeboi.Direction;

public class Cell extends GameObject {


    public Cell(Sprite sprite, float x, float y) {
        super(sprite, x, y);
        setSize(SCALE, SCALE);
    }


    public void setDirection(Direction dir) {
        if (dir == Direction.UP) {
            sprite.setRotation(90);
            setPosition(x, y + SCALE);
        } else if (dir == Direction.DOWN) {
            sprite.setRotation(-90);
            setPosition(x, y - SCALE);
        } else if (dir == Direction.LEFT) {
            sprite.setRotation(180);
            setPosition(x - SCALE, y);
        } else if (dir == Direction.RIGHT) {
            sprite.setRotation(0);
            setPosition(x + SCALE, y);
        }
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x= " + x +
                ", y= " + y +
                '}';
    }

    public Cell originCenter() {
        sprite.setOriginCenter();
        return this;
    }
}
