package com.salvador.bricks.game_objects;

import static com.salvador.bricks.game_objects.Constants.BUTTON_EXIT;
import static com.salvador.bricks.game_objects.Constants.BUTTON_INFO;
import static com.salvador.bricks.game_objects.Constants.BUTTON_PLAY;
import static com.salvador.bricks.game_objects.Constants.BUTTON_RESET;
import static com.salvador.bricks.game_objects.ResourceManager.getTexture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MenuButton extends Actor {

    public float x, y;
    public float w, h;
    public boolean touch;
    private Texture menuButton;

    public MenuButton(int type, float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        switch (type) {
            case BUTTON_PLAY:
                menuButton = getTexture("button.png");
                break;
            case BUTTON_EXIT:
                menuButton = getTexture("btn_exit.png");
                break;
            case BUTTON_INFO:
                menuButton = getTexture("btn_info.png");
                break;
            case BUTTON_RESET:
                menuButton = getTexture("btn_reset.png");
                break;
        }
        setBounds(x, y, w, h);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                touch = true;
                return true;
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(menuButton, x, y, w, h);
    }
}
