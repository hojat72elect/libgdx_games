package com.salvador.bricks.game_objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class ResourceManager {

    public static AssetManager assetManager;

    public static void loadAssets() {

        assetManager = new AssetManager();
        //Background
        assetManager.load("background.png", Texture.class);

        //Bricks
        assetManager.load("brick1.png", Texture.class);
        assetManager.load("brick2.png", Texture.class);
        assetManager.load("brick3.png", Texture.class);
        assetManager.load("brick4.png", Texture.class);
        assetManager.load("brick5.png", Texture.class);
        assetManager.load("brick6.png", Texture.class);

        //Power Up's
        assetManager.load("powerupBlue.png", Texture.class);
        assetManager.load("powerupGreen.png", Texture.class);
        assetManager.load("powerupRed.png", Texture.class);
        assetManager.load("powerupYellow.png", Texture.class);

        //Buttons
        assetManager.load("button.png", Texture.class);
        assetManager.load("btn_exit.png", Texture.class);
        assetManager.load("btn_info.png", Texture.class);
        assetManager.load("btn_reset.png", Texture.class);

        //Paddle
        assetManager.load("paddle.png", Texture.class);

        //Balls
        assetManager.load("ball.png", Texture.class);
        assetManager.load("ball_fire.png", Texture.class);

        //Sounds
        assetManager.load("sound.ogg", Sound.class);
        assetManager.load("music.mp3", Music.class);

        loadFont("font.ttf", "font.ttf", 40);
        loadFont("font.ttf", "font20.ttf", 20);

        assetManager.finishLoading();
    }

    public static void disposeAssets() {
        assetManager.dispose();
    }

    public static Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);
    }

    public static BitmapFont getFont(String name) {
        return assetManager.get(name, BitmapFont.class);
    }

    public static Sound getSound(String name) {
        return assetManager.get(name, Sound.class);
    }

    public static Music getMusic(String name) {
        return assetManager.get(name, Music.class);
    }

    public static void loadFont(String name, String finalName, int size) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter parms = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parms.fontFileName = name;    // path of .ttf file where that exist
        parms.fontParameters.size = size;
        assetManager.load(finalName, BitmapFont.class, parms);   // fileName with extension, sameName will use to get from manager
    }
}
