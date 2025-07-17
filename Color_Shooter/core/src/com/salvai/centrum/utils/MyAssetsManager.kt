package com.salvai.centrum.utils

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class MyAssetsManager {
    @JvmField
    val manager: AssetManager = AssetManager()


    fun loadImages() {
        manager.load(Constants.PAUSE_BUTTON_IMAGE_NAME, Texture::class.java)
        manager.load(Constants.BALL_IMAGE_NAME, Texture::class.java)
        manager.load(Constants.LEVEL_STARS_IMAGE_NAME, Texture::class.java)
    }

    fun loadSounds() {
        manager.load(Constants.COIN_SOUND_NAME, Sound::class.java)
        manager.load(Constants.POP_SOUND_NAME, Sound::class.java)
        manager.load(Constants.GAME_OVER_SOUND_NAME, Sound::class.java)
        manager.load(Constants.COMBO_RESET_SOUND_NAME, Sound::class.java)
        manager.load(Constants.SUCCESS_SOUND_NAME, Sound::class.java)
    }

    fun loadSplashScreen() {
        //background
        manager.load(Constants.STAR_IMAGE_NAME, Texture::class.java)
        manager.load(Constants.SPLASH_IMAGE_NAME, Texture::class.java)
        manager.finishLoading()
    }

    fun loadParticleEffect() {
        manager.load(Constants.PARTICLE_EFFECT_FILE_NAME, ParticleEffect::class.java)
    }

    fun loadSkin() {
        manager.load(Constants.SKIN_FILE_NAME, Skin::class.java, SkinParameter(Constants.SKIN_ATLAS_FILE_NAME))
    }
}
