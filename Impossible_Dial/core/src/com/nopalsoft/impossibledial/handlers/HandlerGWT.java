package com.nopalsoft.impossibledial.handlers;

import com.badlogic.gdx.graphics.Texture;

public interface HandlerGWT {
    void getTextureFromFacebook(String base64, OnTextureLoaded onTextureLoaded);

    interface OnTextureLoaded {
        void onTextureLoaded(Texture texture);
    }
}
