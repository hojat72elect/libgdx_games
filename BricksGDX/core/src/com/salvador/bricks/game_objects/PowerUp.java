package com.salvador.bricks.game_objects;

import static com.salvador.bricks.game_objects.Constants.POWER_UP_ADD_ONE_BALL;
import static com.salvador.bricks.game_objects.Constants.POWER_UP_FIREBALL;
import static com.salvador.bricks.game_objects.Constants.POWER_UP_PADDLE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PowerUp extends Actor {

    private final Texture powerUp;
    public float x, y;
    public float speedX;
    public float speedY;
    public float width, height;
    public boolean live = true;
    public int type;

    public PowerUp(int type, float x, float y) {
        this.type = type;
        switch (type) {
            case POWER_UP_ADD_ONE_BALL:
                powerUp = new Texture(Gdx.files.internal("powerupBlue.png"));
                break;
            case POWER_UP_PADDLE_SIZE:
                powerUp = new Texture(Gdx.files.internal("powerupGreen.png"));
                break;
            case POWER_UP_FIREBALL:
                powerUp = new Texture(Gdx.files.internal("powerupRed.png"));
                break;
            default:
                powerUp = new Texture(Gdx.files.internal("powerupYellow.png"));
                break;
        }
        this.x = x;
        this.y = y;
        speedX = 150f;
        speedY = 150f;
        width = 30;
        height = 30;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        y -= speedX * delta;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.x = x;
        this.y = y;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(powerUp, x, y, 30, 30);
    }
}
