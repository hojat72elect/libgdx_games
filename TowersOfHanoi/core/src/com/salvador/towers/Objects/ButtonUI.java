package com.salvador.towers.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class ButtonUI extends Actor {

    private OnClickListenner onClickListenner;
    private Texture texture;

    public ButtonUI(float x, float y, float w, float h) {
        setBounds(x, y, w, h);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                onClickListenner.onClick();
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
            }
        });
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (texture != null)
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void setOnClickListenner(OnClickListenner onClickListenner) {
        this.onClickListenner = onClickListenner;
    }


    public interface OnClickListenner {
        void onClick();
    }
}


