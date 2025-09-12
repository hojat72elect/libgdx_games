package com.salvador.towers.Objects;

import com.badlogic.gdx.graphics.Texture;

public class ButtonPlay extends ButtonUI {

    private Texture texturePause;

    public ButtonPlay(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    public void setTexturePause(Texture texturePause) {
        this.texturePause = texturePause;
    }
}
