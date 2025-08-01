package com.salvai.snake.utils

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class MyAssetsManager {
    @JvmField
    val manager: AssetManager = AssetManager()

    fun loadImages() {
        manager.load(Constants.HAND_IMAGE_NAME, Texture::class.java)

        //menu icons
        for (i in 1..Constants.MAX_LEVEL) manager.load(Constants.LEVEL_PREVIEW + i + ".png", Texture::class.java)
    }

    fun loadSplashScreen() {
        manager.load(Constants.BACKGROUND_IMAGE, Texture::class.java)
        manager.load(Constants.BLOCK_IMAGE_NAME, Texture::class.java)
        manager.load(Constants.APPLE_IMAGE_NAME, Texture::class.java)
    }

    fun loadSkin() {
        manager.load(Constants.SKIN_FILE_NAME, Skin::class.java, SkinParameter(Constants.SKIN_ATLAS_FILE_NAME))
    }
}
