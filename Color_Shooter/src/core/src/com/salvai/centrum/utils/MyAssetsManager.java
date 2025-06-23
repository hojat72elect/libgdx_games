package com.salvai.centrum.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;

public class MyAssetsManager {

    public final AssetManager manager;

    public MyAssetsManager() {
        manager = new AssetManager();
    }


    public void loadImages() {
        manager.load(Constants.PAUSE_BUTTON_IMAGE_NAME, Texture.class);
        manager.load(Constants.BALL_IMAGE_NAME, Texture.class);
        manager.load(Constants.LEVEL_STARS_IMAGE_NAME, Texture.class);
    }

    public void loadSounds() {
        manager.load(Constants.COIN_SOUND_NAME, Sound.class);
        manager.load(Constants.POP_SOUND_NAME, Sound.class);
        manager.load(Constants.GAME_OVER_SOUND_NAME, Sound.class);
        manager.load(Constants.COMBO_RESET_SOUND_NAME, Sound.class);
        manager.load(Constants.SUCCESS_SOUND_NAME, Sound.class);
    }

    public void loadSplashScreen() {
        //background
        manager.load(Constants.STAR_IMAGE_NAME, Texture.class);
        manager.load(Constants.SPLASH_IMAGE_NAME, Texture.class);
        manager.finishLoading();
    }

    public void loadParticleEffect() {
        manager.load(Constants.PARTICLE_EFFECT_FILE_NAME, ParticleEffect.class);
    }

    public void loadSkin(){
        manager.load(Constants.SKIN_FILE_NAME, Skin.class, new SkinLoader.SkinParameter(Constants.SKIN_ATLAS_FILE_NAME));
    }

}
