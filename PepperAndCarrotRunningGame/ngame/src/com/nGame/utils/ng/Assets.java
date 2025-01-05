package com.nGame.utils.ng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author oli
 *         Date: 01.11.13 - 23:08
 */
public enum Assets {
    I;

    //public TextureAtlas atlasGame;
    //public TextureAtlas atlasGoogle;

    public Label.LabelStyle titleLabelStyle;
    public Label.LabelStyle defaultLabelStyle;
   // public Label.LabelStyle smallLabelStyle;
    public Label.LabelStyle midLabelStyle;

  /*  public String click = "sound/click.mp3";
    public String fail = "sound/negative.mp3";
    public String remove = "sound/positive.mp3";
    public String select = "sound/select.mp3";
    public String end = "sound/win.mp3";*/

    public AssetManager manager;
    private HashMap<String,TextureAtlas> textureAtlasMap = new HashMap<String,TextureAtlas>();

    public Skin skin;
    private HashMap<String, BitmapFont> bmpfonts= new HashMap<String,BitmapFont>();

    Assets() {
        manager = new AssetManager();
        Texture.setAssetManager(manager);
        loadSkin();

        //skin.getFont("default-font").setScale(3f, 3f);
    }

    public void loadSkin() {
        skin = new Skin(Gdx.files.internal("ui/skin.json"));
    }

    public void loadTexture(String texture) {
        if (Conf.Video.MIPMAPS) {
            TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
            param.minFilter = Texture.TextureFilter.Linear;
            param.genMipMaps = true;
            manager.load(texture, Texture.class, param);
        } else {
            manager.load(texture, Texture.class);
        }
    }


    public void loadSound(String asset) {
        manager.load(asset, Sound.class);
    }

    public Sound getSound(String sound) {
        return manager.get(sound, Sound.class);
    }

    public void playSound(String sound) {
        if (Conf.Audio.SOUND) {
            manager.get(sound, Sound.class).play(Conf.Audio.SOUND_VOL);
        }

    }

    public void initFonts() {
        manager.load("ui/font32.fnt", BitmapFont.class);
        manager.load("ui/font64.fnt", BitmapFont.class);
        manager.load("ui/font96.fnt", BitmapFont.class);
    }

    public void setupStyles(){
        titleLabelStyle= new Label.LabelStyle(Assets.I.manager.get("ui/font96.fnt", BitmapFont.class), Color.WHITE);
        defaultLabelStyle= new Label.LabelStyle(Assets.I.manager.get("ui/font32.fnt", BitmapFont.class), Color.WHITE);
        //smallLabelStyle= new Label.LabelStyle(Assets.I.manager.get("sml.ttf", BitmapFont.class), Color.WHITE);
        midLabelStyle= new Label.LabelStyle(Assets.I.manager.get("ui/font64.fnt", BitmapFont.class), Color.WHITE);
    }

    public void dispose() {
        manager.dispose();
    }

    public TextureAtlas getAtlas(String atlasGame) {
        return textureAtlasMap.get(atlasGame);
    }

    public void loadTextureAtlas(String textureAtlas){
        manager.load(textureAtlas,TextureAtlas.class);
        textureAtlasMap.put(textureAtlas, null);
    }

    public void loadBMPFont(String fnt) {
        manager.load(fnt,BitmapFont.class);
        bmpfonts.put(fnt, null);

    }

    public void updateTextureAtlas(String atlas,TextureAtlas object){
        if(textureAtlasMap.containsKey(atlas)){
            textureAtlasMap.remove(atlas);
        }
        textureAtlasMap.put(atlas,object);
    }

    public void updateMaps() {
        String[] keys = textureAtlasMap.keySet().toArray(new String[textureAtlasMap.size()]);
        for(String key: keys) {
            updateTextureAtlas(key,manager.get(key,TextureAtlas.class));
        }
    }
}
