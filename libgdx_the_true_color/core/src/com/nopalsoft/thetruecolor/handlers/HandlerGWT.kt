package com.nopalsoft.thetruecolor.handlers

import com.badlogic.gdx.graphics.Texture

interface HandlerGWT {
    fun getTextureFromFacebook(url: String?, onTextureLoaded: OnTextureLoaded?)

    interface OnTextureLoaded {
        fun onTextureLoaded(texture: Texture?)
    }
}
