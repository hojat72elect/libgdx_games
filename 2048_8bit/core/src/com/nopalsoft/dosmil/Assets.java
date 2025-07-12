package com.nopalsoft.dosmil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

public class Assets {

    public static I18NBundle languagesBundle;

    public static BitmapFont fontSmall;
    public static BitmapFont fontLarge;

    public static AtlasRegion backgroundAtlasRegion;
    public static AtlasRegion backgroundBoardAtlasRegion;
    public static AtlasRegion puzzleSolvedAtlasRegion;

    public static AtlasRegion titleAtlasRegion;

    public static NinePatchDrawable blackPixel;
    public static AtlasRegion scoresBackgroundAtlasRegion;

    public static TextureRegionDrawable buttonBack;
    public static TextureRegionDrawable buttonFacebook;
    public static TextureRegionDrawable buttonTwitter;

    public static AtlasRegion emptyPieceAtlasRegion;
    public static AtlasRegion piece2AtlasRegion;
    public static AtlasRegion piece4AtlasRegion;
    public static AtlasRegion piece8AtlasRegion;
    public static AtlasRegion piece16AtlasRegion;
    public static AtlasRegion piece32AtlasRegion;
    public static AtlasRegion piece64AtlasRegion;
    public static AtlasRegion piece128AtlasRegion;
    public static AtlasRegion piece256AtlasRegion;
    public static AtlasRegion piece512AtlasRegion;
    public static AtlasRegion piece1024AtlasRegion;
    public static AtlasRegion piece2048AtlasRegion;

    public static LabelStyle labelStyleSmall;
    public static LabelStyle labelStyleLarge;
    public static ButtonStyle buttonStyleMusic;
    public static ButtonStyle buttonStylePause;
    public static ButtonStyle buttonStyleSound;

    static TextureAtlas atlas;

    private static Music music2;

    static Sound move1;
    static Sound move2;

    public static void loadFont() {
        fontSmall = new BitmapFont(Gdx.files.internal("data/font25.fnt"),
                atlas.findRegion("font25"));

        fontLarge = new BitmapFont(Gdx.files.internal("data/font45.fnt"),
                atlas.findRegion("font45"));
    }

    private static void loadStyles() {
        labelStyleSmall = new LabelStyle(fontSmall, Color.WHITE);
        labelStyleLarge = new LabelStyle(fontLarge, Color.WHITE);

        TextureRegionDrawable buttonMusicOn = new TextureRegionDrawable(atlas.findRegion("btMusica"));
        TextureRegionDrawable buttonMusicOff = new TextureRegionDrawable(atlas.findRegion("btSinMusica"));
        buttonStyleMusic = new ButtonStyle(buttonMusicOn, null, buttonMusicOff);

        TextureRegionDrawable buttonSoundOn = new TextureRegionDrawable(atlas.findRegion("btSonido"));
        TextureRegionDrawable buttonSoundOff = new TextureRegionDrawable(atlas.findRegion("btSinSonido"));
        buttonStyleSound = new ButtonStyle(buttonSoundOn, null, buttonSoundOff);

        TextureRegionDrawable buttonPauseUp = new TextureRegionDrawable(atlas.findRegion("btPause"));
        TextureRegionDrawable buttonPauseDown = new TextureRegionDrawable(atlas.findRegion("btPauseDown"));
        buttonStylePause = new ButtonStyle(buttonPauseUp, buttonPauseDown, null);
    }

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        loadFont();
        loadStyles();

        if (MathUtils.randomBoolean())
            backgroundAtlasRegion = atlas.findRegion("fondo");
        else
            backgroundAtlasRegion = atlas.findRegion("fondo2");
        backgroundBoardAtlasRegion = atlas.findRegion("fondoPuntuaciones");

        titleAtlasRegion = atlas.findRegion("titulo");

        blackPixel = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
        scoresBackgroundAtlasRegion = atlas.findRegion("fondoPuntuaciones");

        puzzleSolvedAtlasRegion = atlas.findRegion("puzzleSolved");

        emptyPieceAtlasRegion = atlas.findRegion("piezaVacia");

        piece2AtlasRegion = atlas.findRegion("pieza2");
        piece4AtlasRegion = atlas.findRegion("pieza4");
        piece8AtlasRegion = atlas.findRegion("pieza8");
        piece16AtlasRegion = atlas.findRegion("pieza16");
        piece32AtlasRegion = atlas.findRegion("pieza32");
        piece64AtlasRegion = atlas.findRegion("pieza64");
        piece128AtlasRegion = atlas.findRegion("pieza128");
        piece256AtlasRegion = atlas.findRegion("pieza256");
        piece512AtlasRegion = atlas.findRegion("pieza512");
        piece1024AtlasRegion = atlas.findRegion("pieza1024");
        piece2048AtlasRegion = atlas.findRegion("pieza2048");

        buttonBack = new TextureRegionDrawable(atlas.findRegion("btAtras2"));
        buttonFacebook = new TextureRegionDrawable(atlas.findRegion("btFacebook"));
        buttonTwitter = new TextureRegionDrawable(atlas.findRegion("btTwitter"));

        move1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move1.mp3"));
        move2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move2.mp3"));

        music2 = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music2.mp3"));

        Settings.load();

        music2.setVolume(.1f);

        playMusic();

        languagesBundle = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));
    }

    public static void playMusic() {
        if (Settings.isMusicOn)

            music2.play();
    }

    public static void pauseMusic() {
        music2.stop();
    }

    public static void playSoundMove() {
        if (Settings.isSoundOn) {
            if (MathUtils.randomBoolean())
                move1.play(.3f);
            else
                move2.play(.3f);
        }
    }
}
