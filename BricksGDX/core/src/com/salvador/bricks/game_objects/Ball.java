package com.salvador.bricks.game_objects;

import static com.salvador.bricks.game_objects.Constants.BALL_HEIGHT;
import static com.salvador.bricks.game_objects.Constants.BALL_SPEED;
import static com.salvador.bricks.game_objects.Constants.BALL_WIDTH;
import static com.salvador.bricks.game_objects.ResourceManager.getTexture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;

public class Ball extends Actor {

    public Vector2 position;
    public float speedX;
    public float speedY;

    public float width, height;

    public boolean state_fire;
    public boolean velocity_changed;
    public boolean pause = false;
    private Texture ball;
    private long startx;
    private long start;


    public Ball(float x, float y) {
        ball = getTexture("ball.png");
        position = new Vector2(x, y);

        speedX = 300f;
        speedY = 300f;
        width = 30;
        height = 30;
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, BALL_WIDTH, BALL_HEIGHT);
    }


    public void setVelocity(float speed) {
        if (speedY > 0) {
            this.speedY = speed;
        }
        if (speedY < 0) {
            this.speedY = -speed;
        }
        if (speedX > 0) {
            this.speedX = speed;
        }
        if (speedY < 0) {
            this.speedX = -speed;
        }
        velocity_changed = true;
        startx = TimeUtils.millis();
    }

    public void setDefaultSpeed() {
        if (speedY > 0) {
            this.speedY = BALL_SPEED;
        } else {
            this.speedY = -BALL_SPEED;
        }
        if (speedX > 0) {
            this.speedX = BALL_SPEED;
        } else {
            this.speedX = -BALL_SPEED;
        }
        velocity_changed = true;
        startx = TimeUtils.millis();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!pause) {
            position.x += speedX * delta;
            position.y += speedY * delta;
        }
        if (state_fire) {
            long diffInMillis = TimeUtils.timeSinceMillis(start);
            if (diffInMillis > 2000) {
                state_fire = false;
                ball = getTexture("ball.png");
            }
        }
        if (velocity_changed) {
            long diffInMillis = TimeUtils.timeSinceMillis(startx);
            if (diffInMillis > 5000) {
                velocity_changed = false;
                setDefaultSpeed();
            }
        }
    }


    public void setFireBall() {
        state_fire = true;
        ball = getTexture("ball_fire.png");
        start = TimeUtils.millis();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        position.x = x;
        position.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(ball, position.x, position.y, BALL_WIDTH, BALL_HEIGHT);
    }
}
