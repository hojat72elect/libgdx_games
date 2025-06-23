package com.salvai.centrum.actors.enemys;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.salvai.centrum.actors.MovingGameObject;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.RandomUtil;

public abstract class Enemy extends MovingGameObject {
    public int speed;


    public Enemy(Texture texture) {
        super();
        speed = RandomUtil.getRandomEnemySpeed(Constants.ENEMY_MAX_SPEED);
        position = RandomUtil.getRandomEnemyCoordinates();
        setDirectionToCentreBall();
        position.x -= Constants.ENEMY_DIAMETER * 0.5f;
        position.y -= Constants.ENEMY_DIAMETER * 0.5f;
        sprite = new Sprite(texture);
        sprite.setBounds(position.x, position.y, Constants.ENEMY_DIAMETER, Constants.ENEMY_DIAMETER);
        sprite.setColor(color);
    }

    //for level balls
    public Enemy(int x, int y, int speed, Color color, Texture texture) {
        super(color);
        this.speed = speed;
        position.set(x, y);
        setDirectionToCentreBall();
        position.x -= Constants.ENEMY_DIAMETER * 0.5f;
        position.y -= Constants.ENEMY_DIAMETER * 0.5f;
        sprite = new Sprite(texture);
        sprite.setBounds(position.x, position.y, Constants.ENEMY_DIAMETER, Constants.ENEMY_DIAMETER);
        sprite.setColor(color);
    }

    public abstract void move(float delta);
}
