package dev.ian.assroids.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Array;

/**
 * Created by: Ian Parcon
 * Date created: Sept 3, 2018
 * Time created: 10:55 PM
 */
public class Asset {

    private AssetManager assetManager;
    private static Asset instance = new Asset();

    public static final String PIXEL_FONT_SMALL = "fonts/pixel.ttf";
    public static final String LOAD_TEXTURE = "images/load/loading.png";
    private static final String EXPLOSION_PACK = "images/explosion/explosion.pack";
    private static final String ASSROID_PACK = "images/objects/assroids.pack";

    public static final String LASER_SOUND = "sound/laser-sound.wav";
    public static final String NORMAL_GUN_SHOT = "sound/normal-gun-shot.wav";
    public static final String EAGLE_GUN_SHOT = "sound/red-shot.wav";
    public static final String EXPLOSION_B = "sound/explosion-b.wav";
    public static final String EXPLOSION_A = "sound/explosion-a.wav";
    public static final String SCI_FI_MUSIC = "sound/sci-fi-bg.wav";
    public static final String PLASMA_GUN_SHOT = "sound/plasma-shot.ogg";
    public static final String MACHINE_GUN_SHOT = "sound/machine-gun-shot.ogg";
    public static final String POWER_PICK_UP = "sound/pick-up.wav";

    private TextureAtlas asteroidAtlas;
    private TextureAtlas explosionAtlas;

    private Asset() {
        assetManager = new AssetManager();
    }

    public static Asset instance() {
        return instance;
    }

    private void initHandler() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    public static FileHandle load(String name) {
        return Gdx.files.internal(name);
    }

    public void load() {
        loadFont();
        loadSprites();
        loadSounds();
    }

    private void loadSounds() {
        assetManager.load(SCI_FI_MUSIC, Music.class);
        assetManager.load(EXPLOSION_A, Sound.class);
        assetManager.load(EXPLOSION_B, Sound.class);
        assetManager.load(LASER_SOUND, Sound.class);
        assetManager.load(NORMAL_GUN_SHOT, Sound.class);
        assetManager.load(EAGLE_GUN_SHOT, Sound.class);
        assetManager.load(PLASMA_GUN_SHOT, Sound.class);
        assetManager.load(MACHINE_GUN_SHOT, Sound.class);
        assetManager.load(POWER_PICK_UP, Sound.class);

    }

    private void loadSprites() {
        assetManager.load(EXPLOSION_PACK, TextureAtlas.class);
        assetManager.load(ASSROID_PACK, TextureAtlas.class);
    }

    private void loadFont() {
        initHandler();
        FreetypeFontLoader.FreeTypeFontLoaderParameter smallFont = createFont(12);
        FreetypeFontLoader.FreeTypeFontLoaderParameter bigFont = createFont(30);
        assetManager.load(PIXEL_FONT_SMALL, BitmapFont.class, smallFont);
        assetManager.load(PIXEL_FONT_SMALL, BitmapFont.class, bigFont);
    }

    private FreetypeFontLoader.FreeTypeFontLoaderParameter createFont(int size) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = PIXEL_FONT_SMALL;
        font.fontParameters.size = size;
        font.fontParameters.color = Color.WHITE;
        return font;
    }

    public <T> T get(String filename) {
        return assetManager.get(filename);
    }

    public Sprite createSprite(String name) {
        asteroidAtlas = assetManager.get(Asset.ASSROID_PACK);
        return asteroidAtlas.createSprite(name);
    }

    public Array<TextureAtlas.AtlasRegion> createRegion(String name) {
        asteroidAtlas = assetManager.get(Asset.ASSROID_PACK);
        return asteroidAtlas.findRegions(name);
    }

    public Array<TextureAtlas.AtlasRegion> createExplosion(String name) {
        explosionAtlas = assetManager.get(Asset.EXPLOSION_PACK);
        return explosionAtlas.findRegions(name);
    }

    public boolean isLoading() {
        return assetManager.update();
    }

    public void dispose() {
        assetManager.dispose();
    }

}
