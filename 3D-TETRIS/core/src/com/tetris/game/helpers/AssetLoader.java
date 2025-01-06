package com.tetris.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by mkabi_000 on 8/1/2015.
 */
//Asset Loader Class to manage the assets
public class AssetLoader {
    //Member Variables
    public BitmapFont font, shadow, smallFont,smallShadow, largeFont;
    public TextureAtlas buttonAtlas, rxA,rzA,aA,ryA,upA,downA,leftA,rightA,misc,others;
    public TextButton.TextButtonStyle buttonStyle,smallStyle,rx,ry,rz,up,down,left,right,a;
    public Skin buttonSkin,smallSkin,rxS,ryS,rzS,upS,downS,leftS,rightS,aS,miscS,other;
    public Music themeSong;
    public Sound interact,score,spin;
    public Preferences prefs;
    public Texture tut1,tut2,tut3;

    //Constructor
    public AssetLoader(){
        //Set a preference to maintain the scores of users
        prefs = Gdx.app.getPreferences("FullGameDemo");
        if(!prefs.contains("highScore")){
            prefs.putInteger("highScore",0);
        }

        //Create the fonts and shadows of fonts
        float size = 1f * Gdx.graphics.getDensity();
        font = new BitmapFont(Gdx.files.internal("fonts/text.fnt"), true);
        font.getData().setScale(size/2,-size/2);
        shadow = new BitmapFont(Gdx.files.internal("fonts/shadow.fnt"),true);
        shadow.getData().setScale(size/2,-size/2);
        smallFont = new BitmapFont(Gdx.files.internal("fonts/text.fnt"), true);
        smallFont.getData().setScale(size/4,-size/4);
        smallShadow = new BitmapFont(Gdx.files.internal("fonts/shadow.fnt"),true);
        smallShadow.getData().setScale(size/4,-size/4);
        largeFont = new BitmapFont(Gdx.files.internal("fonts/text.fnt"), true);
        largeFont.getData().setScale(size,-size);

        //The basic button assets
        buttonAtlas = new TextureAtlas(Gdx.files.internal("Buttons/purple/button.pack"));
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonAtlas);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = buttonSkin.getDrawable("button");
        buttonStyle.over = buttonSkin.getDrawable("buttonpressed");
        buttonStyle.down = buttonSkin.getDrawable("buttonpressed");
        buttonStyle.font = font;

        //The basic button with smaller font assets
        smallSkin = new Skin();
        smallSkin.addRegions(buttonAtlas);
        smallStyle = new TextButton.TextButtonStyle();
        smallStyle.up = buttonSkin.getDrawable("button");
        smallStyle.over = buttonSkin.getDrawable("buttonpressed");
        smallStyle.down = buttonSkin.getDrawable("buttonpressed");
        smallStyle.font = smallFont;

        //Accelerate Button assets
        aA = new TextureAtlas(Gdx.files.internal("Buttons/a/button.pack"));
        aS = new Skin();
        aS.addRegions(aA);
        a = new TextButton.TextButtonStyle();
        a.font = smallFont;
        a.up = aS.getDrawable("button");
        a.down = aS.getDrawable("buttonpressed");
        a.over = aS.getDrawable("buttonpressed");

        //Down Button assets
        downA = new TextureAtlas(Gdx.files.internal("Buttons/down/button.pack"));
        downS = new Skin();
        downS.addRegions(downA);
        down = new TextButton.TextButtonStyle();
        down.font = smallFont;
        down.up = downS.getDrawable("button");
        down.down = downS.getDrawable("buttonpressed");
        down.over = downS.getDrawable("buttonpressed");

        //Left Button assets
        leftA = new TextureAtlas(Gdx.files.internal("Buttons/left/button.pack"));
        leftS = new Skin();
        leftS.addRegions(leftA);
        left = new TextButton.TextButtonStyle();
        left.font = smallFont;
        left.up = leftS.getDrawable("button");
        left.down = leftS.getDrawable("buttonpressed");
        left.over = leftS.getDrawable("buttonpressed");

        //Right Button assets
        rightA = new TextureAtlas(Gdx.files.internal("Buttons/right/button.pack"));
        rightS = new Skin();
        rightS.addRegions(rightA);
        right = new TextButton.TextButtonStyle();
        right.font = smallFont;
        right.up = rightS.getDrawable("button");
        right.down = rightS.getDrawable("buttonpressed");
        right.over = rightS.getDrawable("buttonpressed");

        //Up Button assets
        upA = new TextureAtlas(Gdx.files.internal("Buttons/up/button.pack"));
        upS = new Skin();
        upS.addRegions(upA);
        up = new TextButton.TextButtonStyle();
        up.font = smallFont;
        up.up = upS.getDrawable("button");
        up.down = upS.getDrawable("buttonpressed");
        up.over = upS.getDrawable("buttonpressed");

        //Rx Button Assets
        rxA = new TextureAtlas(Gdx.files.internal("Buttons/rx/button.pack"));
        rxS = new Skin();
        rxS.addRegions(rxA);
        rx = new TextButton.TextButtonStyle();
        rx.font = smallFont;
        rx.up = rxS.getDrawable("button");
        rx.down = rxS.getDrawable("buttonpressed");
        rx.over = rxS.getDrawable("buttonpressed");

        //Ry Button Assets
        ryA = new TextureAtlas(Gdx.files.internal("Buttons/ry/button.pack"));
        ryS = new Skin();
        ryS.addRegions(ryA);
        ry = new TextButton.TextButtonStyle();
        ry.font = smallFont;
        ry.up = ryS.getDrawable("button");
        ry.down = ryS.getDrawable("buttonpressed");
        ry.over = ryS.getDrawable("buttonpressed");

        //Rz Button Assets
        rzA = new TextureAtlas(Gdx.files.internal("Buttons/rz/button.pack"));
        rzS = new Skin();
        rzS.addRegions(rzA);
        rz = new TextButton.TextButtonStyle();
        rz.font = smallFont;
        rz.up = rzS.getDrawable("button");
        rz.down = rzS.getDrawable("buttonpressed");
        rz.over = rzS.getDrawable("buttonpressed");

        //Images
        tut1 = new Texture(Gdx.files.internal("Pictures/Tutorials/tutorial1.png"));
        tut1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tut2 = new Texture(Gdx.files.internal("Pictures/Tutorials/tutorial2.png"));
        tut2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tut3 = new Texture(Gdx.files.internal("Pictures/Tutorials/tutorial3.png"));
        tut3.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //Sounds and Music
        themeSong = Gdx.audio.newMusic(Gdx.files.internal("Sounds/theme.wav"));
        themeSong.setLooping(true);
        interact = Gdx.audio.newSound(Gdx.files.internal("Sounds/interact.wav"));
        score = Gdx.audio.newSound(Gdx.files.internal("Sounds/score.wav"));
        spin = Gdx.audio.newSound(Gdx.files.internal("Sounds/move.wav"));

        //Misc and other assets
        misc = new TextureAtlas(Gdx.files.internal("Buttons/small/button.pack"));
        miscS = new Skin();
        miscS.addRegions(misc);

        others = new TextureAtlas(Gdx.files.internal("Buttons/cc/button.pack"));
        other = new Skin();
        other.addRegions(others);

    }

    //Getters/Accessors
    public int getHighScore(){
        return  prefs.getInteger("highScore");
    }

    //Setters/Mutators/OtherMethods
    public void setHighScore(int val){
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public void playSong(){
        themeSong.play();
    }

    public void stopSong(){
        themeSong.stop();
    }

    public void dispose(){
        //pictures
        tut1.dispose();
        tut2.dispose();
        tut3.dispose();
        //sounds and music
        interact.dispose();
        score.dispose();
        spin.dispose();
        themeSong.dispose();
        //Skin
        buttonSkin.dispose();
        smallSkin.dispose();
        miscS.dispose();
        rxS.dispose();
        ryS.dispose();
        rzS.dispose();
        upS.dispose();
        downS.dispose();
        leftS.dispose();
        rightS.dispose();
        aS.dispose();
        //Atlas
        buttonAtlas.dispose();
        rxA.dispose();
        ryA.dispose();
        rzA.dispose();
        aA.dispose();
        upA.dispose();
        downA.dispose();
        leftA.dispose();
        rightA.dispose();
        misc.dispose();
        other.dispose();
        others.dispose();
    }

}
