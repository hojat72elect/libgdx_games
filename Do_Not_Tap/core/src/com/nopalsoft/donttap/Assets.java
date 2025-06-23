package com.nopalsoft.donttap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

    public static BitmapFont font;

    public static NinePatchDrawable pixelNegro;
    public static NinePatchDrawable fondoPuntuaciones;

    public static AtlasRegion tileAmarillo;
    public static AtlasRegion tileAzul;
    public static AtlasRegion tileMorado;
    public static AtlasRegion tileNaranja;
    public static AtlasRegion tileBlanco;
    public static AtlasRegion tileRojo;
    public static AtlasRegion wrong;
    public static AtlasRegion step1;

    public static AtlasRegion titulo;

    public static LabelStyle labelStyleChico;
    public static LabelStyle labelStyleBlack;
    public static TextButtonStyle textButtonStyleChico;

    public static TextureRegionDrawable btFacebook;
    public static TextureRegionDrawable btTwitter;
    public static TextureRegionDrawable btAtras;

    private static void cargarEstilos(TextureAtlas atlas) {
        labelStyleChico = new LabelStyle(font, Color.WHITE);
        labelStyleBlack = new LabelStyle(font, Color.GRAY);

        fondoPuntuaciones = new NinePatchDrawable(atlas.createPatch("fondoPuntuaciones"));

        textButtonStyleChico = new TextButtonStyle(fondoPuntuaciones, null, null, font);
        textButtonStyleChico.fontColor = Color.WHITE;
    }

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        titulo = atlas.findRegion("titulo");

        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), atlas.findRegion("font"));

        pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));

        cargarEstilos(atlas);

        tileAmarillo = atlas.findRegion("amarillo");
        tileAzul = atlas.findRegion("azul");
        tileRojo = atlas.findRegion("rojo");
        tileBlanco = atlas.findRegion("blanco");
        tileMorado = atlas.findRegion("morado");
        tileNaranja = atlas.findRegion("naranja");
        wrong = atlas.findRegion("wrong");
        step1 = atlas.findRegion("step");

        btTwitter = new TextureRegionDrawable(atlas.findRegion("btTwitter"));
        btFacebook = new TextureRegionDrawable(atlas.findRegion("btFacebook"));
        btAtras = new TextureRegionDrawable(atlas.findRegion("btAtras"));

        loadSounds();

        Settings.load();
    }

    private static void loadSounds() {
        pianoSounds = new Sound[26];
        String ruta = "data/Sounds/piano";
        for (int i = 0; i < pianoSounds.length; i++) {
            Sound sonido = Gdx.audio.newSound(Gdx.files.internal(ruta + i + ".mp3"));
            pianoSounds[i] = sonido;
        }

        soundTap1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/tap1.mp3"));
        soundTap2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/tap2.mp3"));
        soundWrong = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/wrong.mp3"));
    }

    static Sound[] pianoSounds;
    static Sound soundTap1;
    static Sound soundTap2;
    public static Sound soundWrong;

    public static void playTapSound() {
        if (!Settings.isSoundEnabled)
            return;

        // if (MathUtils.randomBoolean())
        // soundTap1.play();
        // else
        soundTap2.play();

        // int sonido = MathUtils.random(pianoSounds.length - 1);
        // pianoSounds[1].play();

    }
}
