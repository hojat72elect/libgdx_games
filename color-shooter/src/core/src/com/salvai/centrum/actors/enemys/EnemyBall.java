package com.salvai.centrum.actors.enemys;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.RandomUtil;


public class EnemyBall extends Enemy {
    public Circle shape;

    public EnemyBall(Texture texture) {
        super(texture);
        shape = new Circle(position.x + (Constants.ENEMY_DIAMETER * 0.5f), position.y + (Constants.ENEMY_DIAMETER * 0.5f), Constants.ENEMY_DIAMETER * 0.5f);
    }



    /**
     * for level balls
     */
    public EnemyBall(int x, int y, int speed, Color color, Texture texture) {
        super(x, y, speed,  color, texture);
        shape = new Circle(position.x + (Constants.ENEMY_DIAMETER * 0.5f), position.y + (Constants.ENEMY_DIAMETER * 0.5f), Constants.ENEMY_DIAMETER * 0.5f);
    }

    public void move(float delta) {
        shape.setPosition(shape.x + (velocity.x * delta * speed), shape.y + (velocity.y * delta * speed));
        sprite.setPosition(sprite.getX() + (velocity.x * delta * speed), sprite.getY() + (velocity.y * delta * speed));
        position.set(shape.x + (velocity.x * delta * speed), shape.y + (velocity.y * delta * speed));
    }

}
