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

import java.util.Locale;

public class Assets {

    public static I18NBundle idiomas;

    public static BitmapFont fontChico;
    public static BitmapFont fontGrande;

    public static AtlasRegion fondo;
    public static AtlasRegion fondoTablero;
    public static AtlasRegion puzzleSolved;

    public static AtlasRegion titulo;

    public static NinePatchDrawable pixelNegro;
    public static AtlasRegion fondoPuntuaciones;

    public static TextureRegionDrawable btAtras;
    public static TextureRegionDrawable btFacebook;
    public static TextureRegionDrawable btTwitter;

    public static AtlasRegion piezaVacia;
    public static AtlasRegion pieza2;
    public static AtlasRegion pieza4;
    public static AtlasRegion pieza8;
    public static AtlasRegion pieza16;
    public static AtlasRegion pieza32;
    public static AtlasRegion pieza64;
    public static AtlasRegion pieza128;
    public static AtlasRegion pieza256;
    public static AtlasRegion pieza512;
    public static AtlasRegion pieza1024;
    public static AtlasRegion pieza2048;

    public static LabelStyle labelStyleChico;
    public static LabelStyle labelStyleGrande;
    public static ButtonStyle styleButtonMusica;
    public static ButtonStyle styleButtonPause;
    public static ButtonStyle styleButtonSonido;

    static TextureAtlas atlas;

    private static Music music2;

    static Sound move1;
    static Sound move2;

    public static void loadFont() {
        fontChico = new BitmapFont(Gdx.files.internal("data/font25.fnt"),
                atlas.findRegion("font25"));

        fontGrande = new BitmapFont(Gdx.files.internal("data/font45.fnt"),
                atlas.findRegion("font45"));
    }

    private static void cargarEstilos() {
        labelStyleChico = new LabelStyle(fontChico, Color.WHITE);
        labelStyleGrande = new LabelStyle(fontGrande, Color.WHITE);

		/* Button Musica */
        TextureRegionDrawable btMusicOn = new TextureRegionDrawable(
                atlas.findRegion("btMusica"));
        TextureRegionDrawable btMusicOff = new TextureRegionDrawable(
                atlas.findRegion("btSinMusica"));
        styleButtonMusica = new ButtonStyle(btMusicOn, null, btMusicOff);

		/* Boton Sonido */
        TextureRegionDrawable botonSonidoOn = new TextureRegionDrawable(
                atlas.findRegion("btSonido"));
        TextureRegionDrawable botonSonidoOff = new TextureRegionDrawable(
                atlas.findRegion("btSinSonido"));
        styleButtonSonido = new ButtonStyle(botonSonidoOn, null, botonSonidoOff);

		/* ImageButton Pause */
        TextureRegionDrawable btPauseUp = new TextureRegionDrawable(
                atlas.findRegion("btPause"));
        TextureRegionDrawable btPauseDown = new TextureRegionDrawable(
                atlas.findRegion("btPauseDown"));
        styleButtonPause = new ButtonStyle(btPauseUp, btPauseDown, null);

    }

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        loadFont();
        cargarEstilos();

        if (MathUtils.randomBoolean())
            fondo = atlas.findRegion("fondo");
        else
            fondo = atlas.findRegion("fondo2");
        fondoTablero = atlas.findRegion("fondoPuntuaciones");

        titulo = atlas.findRegion("titulo");

        pixelNegro = new NinePatchDrawable(new NinePatch(
                atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
        fondoPuntuaciones = atlas.findRegion("fondoPuntuaciones");

        puzzleSolved = atlas.findRegion("puzzleSolved");

        piezaVacia = atlas.findRegion("piezaVacia");

        pieza2 = atlas.findRegion("pieza2");
        pieza4 = atlas.findRegion("pieza4");
        pieza8 = atlas.findRegion("pieza8");
        pieza16 = atlas.findRegion("pieza16");
        pieza32 = atlas.findRegion("pieza32");
        pieza64 = atlas.findRegion("pieza64");
        pieza128 = atlas.findRegion("pieza128");
        pieza256 = atlas.findRegion("pieza256");
        pieza512 = atlas.findRegion("pieza512");
        pieza1024 = atlas.findRegion("pieza1024");
        pieza2048 = atlas.findRegion("pieza2048");

        btAtras = new TextureRegionDrawable(atlas.findRegion("btAtras2"));
        btFacebook = new TextureRegionDrawable(atlas.findRegion("btFacebook"));
        btTwitter = new TextureRegionDrawable(atlas.findRegion("btTwitter"));

        move1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move1.mp3"));
        move2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move2.mp3"));

        music2 = Gdx.audio.newMusic(Gdx.files
                .internal("data/Sounds/music2.mp3"));

        Settings.load();

        music2.setVolume(.1f);

        playMusic();

        idiomas = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));
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
