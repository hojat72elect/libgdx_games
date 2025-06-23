package com.salvai.snake.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MyAssetsManager {

    public final AssetManager manager;

    public MyAssetsManager() {
        manager = new AssetManager();
    }


    public void loadImages() {
        manager.load(Constants.HAND_IMAGE_NAME, Texture.class);

        //menu icons
        for (int i = 1; i <= Constants.MAX_LEVEL; i++)
            manager.load(Constants.LEVEL_PREVIEW + i + ".png", Texture.class);
    }

//    public void loadSounds() {
//        manager.load(Constants.POINT_SFX, Sound.class);
//        manager.load(Constants.UP_SFX, Sound.class);
//        manager.load(Constants.LEFT_RIGHT_SFX, Sound.class);
//        manager.load(Constants.DOWN_SFX, Sound.class);
//        manager.load(Constants.GAME_OVER_SFX, Sound.class);
//        manager.load(Constants.NEW_BEST_SFX, Sound.class);
//    }

    public void loadSplashScreen() {
        manager.load(Constants.BACKGROUND_IMAGE, Texture.class);
        manager.load(Constants.BLOCK_IMAGE_NAME, Texture.class);
        manager.load(Constants.APPLE_IMAGE_NAME, Texture.class);
    }

    public void loadSkin() {
        manager.load(Constants.SKIN_FILE_NAME, Skin.class, new SkinLoader.SkinParameter(Constants.SKIN_ATLAS_FILE_NAME));
    }
}
