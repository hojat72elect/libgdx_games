package com.salvador.towers.Objects;

import static com.salvador.towers.Tool.Values.DISC_HEIGHT;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Disc extends Actor {

    private float x;
    private float y;

    private Texture texture;
    private boolean isTouching = false;

    public Disc(float x, float y, float w) {
        this.x = x;
        this.y = y;
        setWidth(w);

        setPosition(x, y);

        setBounds(x, y, w, DISC_HEIGHT);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("TOuching");

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                setBounds(Disc.this.x, Disc.this.y, Disc.this.getWidth(), DISC_HEIGHT);
                isTouching = false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);

                Disc.this.x = event.getStageX() - (Disc.this.getWidth() / 2);
                Disc.this.y = event.getStageY() - (DISC_HEIGHT / 2);

                isTouching = true;
            }
        });
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (texture != null) {
            if (isTouching)
                batch.draw(texture, x, y, getWidth(), DISC_HEIGHT);
            else
                batch.draw(texture, getX(), getY(), getWidth(), DISC_HEIGHT);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.x = x;
        this.y = y;
    }
}
