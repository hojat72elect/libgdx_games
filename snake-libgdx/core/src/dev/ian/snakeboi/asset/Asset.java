package dev.ian.snakeboi.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * Created by: Ian Parcon
 * Date created: Aug 23, 2018
 * Time created: 2:57 PM
 */
public class Asset {

    private static Asset instance = new Asset();
    private AssetManager assetManager = new AssetManager();

    public static final String SNAKE_PACK = "images/snake.pack";
    public static final String PIXEL_FONT = "fonts/pixel.ttf";
    public static final String MEMO_SOUND = "sounds/8bit_bg.mp3";
    public static final String GAME_OVER_SOUND = "sounds/gameover.mp3";
    public static final String EAT_FOOD_SOUND = "sounds/eat_food.mp3";
    public static final String CRASH_SOUND = "sounds/crash.ogg";

    private Asset() {

    }

    public static Asset instance() {
        return instance;
    }

    public void loadAsset() {
        loadFont();
        loadSounds();
        loadImages();
        assetManager.finishLoading();
    }

    private void loadFont() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter mySmallFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mySmallFont.fontFileName = PIXEL_FONT;
        mySmallFont.fontParameters.size = 16;
        mySmallFont.fontParameters.color = Color.WHITE;
        assetManager.load(PIXEL_FONT, BitmapFont.class, mySmallFont);
    }

    private void loadImages() {
        assetManager.load(SNAKE_PACK, TextureAtlas.class);
    }

    private void loadSounds() {
        assetManager.load(MEMO_SOUND, Music.class);
        assetManager.load(GAME_OVER_SOUND, Music.class);
        assetManager.load(EAT_FOOD_SOUND, Sound.class);
        assetManager.load(CRASH_SOUND, Sound.class);

    }

    public <T> T get(String filename) {
        return assetManager.get(filename);
    }

    public Sprite getSprite(String name) {
        TextureAtlas atlas = get(Asset.SNAKE_PACK);
        return atlas.createSprite(name);
    }

    public void dispose() {
        assetManager.dispose();
    }

}
