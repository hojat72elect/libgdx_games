package com.nopalsoft.thetruecolor.handlers;

import com.badlogic.gdx.graphics.Texture;

public interface HandlerGWT {

    void getTextureFromFacebook(String url, OnTextureLoaded onTextureLoaded);

    interface OnTextureLoaded {
        void onTextureLoaded(Texture texture);
    }
}
