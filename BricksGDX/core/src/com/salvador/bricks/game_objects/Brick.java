package com.salvador.bricks.Objects;

import static com.salvador.bricks.Objects.Constants.BRICK_HEIGHT;
import static com.salvador.bricks.Objects.Constants.BRICK_WIDTH;
import static com.salvador.bricks.Objects.ResourceManager.getTexture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Brick extends Actor {

    public int x, y;
    public boolean live = true;
    public float width, height;
    public int type;
    Texture brick1;

    public Brick(int type, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = type;
        brick1 = getTexture("brick1.png");

        switch (type) {
            case 1:
            case 11:
                brick1 = getTexture("brick1.png");
                break;
            case 2:
            case 12:
                brick1 = getTexture("brick2.png");
                break;
            case 3:
            case 13:
                brick1 = getTexture("brick3.png");
                break;
            case 4:
            case 14:
                brick1 = getTexture("brick4.png");
                break;
            case 5:
            case 15:
                brick1 = getTexture("brick5.png");
                break;
            case 6:
            case 16:
                brick1 = getTexture("brick6.png");
                break;
        }
        width = 75;
        height = 30;
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, 75, 30);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(brick1, x, y, BRICK_WIDTH, BRICK_HEIGHT);
    }
}
