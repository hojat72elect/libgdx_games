package com.nopalsoft.clumsy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.nopalsoft.clumsy.parallax.ParallaxBackground;
import com.nopalsoft.clumsy.parallax.ParallaxLayer;

public class Assets {

    public static boolean LOAD_FLAPPY_PENIS = false;

    public static Animation<TextureRegion> bird;

    public static AtlasRegion btPlayArcade;
    public static AtlasRegion btPlayClassic;
    public static AtlasRegion btLeaderboard;

    public static AtlasRegion btFacebook;
    public static AtlasRegion btTwitter;
    public static AtlasRegion arcoiris;

    public static NinePatchDrawable blanco;
    public static NinePatchDrawable negro;

    public static AtlasRegion btRate;
    public static AtlasRegion btNoAds;
    public static AtlasRegion btAchievements;
    public static AtlasRegion btRestorePurchases;

    public static AtlasRegion meteor1;
    public static AtlasRegion meteor2;
    public static AtlasRegion meteor3;
    public static AtlasRegion meteor4;
    public static AtlasRegion meteor5;
    public static AtlasRegion meteor6;

    public static AtlasRegion num0Grande;
    public static AtlasRegion num1Grande;
    public static AtlasRegion num2Grande;
    public static AtlasRegion num3Grande;
    public static AtlasRegion num4Grande;
    public static AtlasRegion num5Grande;
    public static AtlasRegion num6Grande;
    public static AtlasRegion num7Grande;
    public static AtlasRegion num8Grande;
    public static AtlasRegion num9Grande;

    public static AtlasRegion num0Chico;
    public static AtlasRegion num1Chico;
    public static AtlasRegion num2Chico;
    public static AtlasRegion num3Chico;
    public static AtlasRegion num4Chico;
    public static AtlasRegion num5Chico;
    public static AtlasRegion num6Chico;
    public static AtlasRegion num7Chico;
    public static AtlasRegion num8Chico;
    public static AtlasRegion num9Chico;

    public static AtlasRegion tituloApp;
    public static AtlasRegion tuboArriba;
    public static AtlasRegion tuboAbajo;

    public static ParallaxBackground parallaxFondo;

    public static AtlasRegion fondo;
    public static AtlasRegion fondo1;
    public static AtlasRegion fondo2;
    public static AtlasRegion fondo3;

    public static AtlasRegion medallsFondo;

    public static AtlasRegion med1;
    public static AtlasRegion med2;
    public static AtlasRegion med3;
    public static AtlasRegion med4;

    public static AtlasRegion getReady;
    public static AtlasRegion gameover;
    public static AtlasRegion tapCat;

    public static Sound die;
    public static Sound hit;
    public static Sound point;
    public static Sound wing;
    public static Sound swooshing;

    public static void load() {
        String path = "data";
        if (LOAD_FLAPPY_PENIS) {
            path = "data_flappy_penis";
        }

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path + "/atlasMap.txt"));

        AtlasRegion b1 = atlas.findRegion("nave1");
        AtlasRegion b2 = atlas.findRegion("nave2");
        AtlasRegion b3 = atlas.findRegion("nave3");
        AtlasRegion b4 = atlas.findRegion("nave4");
        bird = new Animation<TextureRegion>(.1f, b1, b2, b3, b4);

        arcoiris = atlas.findRegion("luzA");

        tituloApp = atlas.findRegion("titulo");

        tuboArriba = atlas.findRegion("tubosEspacioArriba");
        tuboAbajo = atlas.findRegion("tubosEspacio");

        gameover = atlas.findRegion("gameover");
        getReady = atlas.findRegion("getready");
        tapCat = atlas.findRegion("tap");

        btPlayArcade = atlas.findRegion("btArcade");
        btPlayClassic = atlas.findRegion("btClassic");
        btLeaderboard = atlas.findRegion("btLeaderboard");
        btRate = atlas.findRegion("btRate");
        btNoAds = atlas.findRegion("btNoAds");
        btAchievements = atlas.findRegion("btAchievements");
        btRestorePurchases = atlas.findRegion("btRestore");

        // TODO: Add btRestore to flappy penis. Workaround initialize with another button
        if (btRestorePurchases == null) btRestorePurchases = atlas.findRegion("btNoAds");


        btFacebook = atlas.findRegion("btFacebook");
        btTwitter = atlas.findRegion("btTwitter");

        fondo1 = atlas.findRegion("fondo1");
        fondo2 = atlas.findRegion("fondo1");
        fondo3 = atlas.findRegion("fondo1");

        num0Grande = atlas.findRegion("num0");
        num1Grande = atlas.findRegion("num1");
        num2Grande = atlas.findRegion("num2");
        num3Grande = atlas.findRegion("num3");
        num4Grande = atlas.findRegion("num4");
        num5Grande = atlas.findRegion("num5");
        num6Grande = atlas.findRegion("num6");
        num7Grande = atlas.findRegion("num7");
        num8Grande = atlas.findRegion("num8");
        num9Grande = atlas.findRegion("num9");

        num0Chico = atlas.findRegion("0");
        num1Chico = atlas.findRegion("1");
        num2Chico = atlas.findRegion("2");
        num3Chico = atlas.findRegion("3");
        num4Chico = atlas.findRegion("4");
        num5Chico = atlas.findRegion("5");
        num6Chico = atlas.findRegion("6");
        num7Chico = atlas.findRegion("7");
        num8Chico = atlas.findRegion("8");
        num9Chico = atlas.findRegion("9");

        meteor1 = atlas.findRegion("meteoro1");
        meteor2 = atlas.findRegion("meteoro2");
        meteor3 = atlas.findRegion("meteoro3");
        meteor4 = atlas.findRegion("meteoro4");
        meteor5 = atlas.findRegion("meteoro5");
        meteor6 = atlas.findRegion("meteoro6");

        blanco = new NinePatchDrawable(new NinePatch(atlas.findRegion("luz"), 1, 1, 0, 0));
        negro = new NinePatchDrawable(new NinePatch(atlas.findRegion("oscuridad"), 1, 1, 0, 0));

        medallsFondo = atlas.findRegion("medallsFondo");
        med1 = atlas.findRegion("monedaOro");
        med2 = atlas.findRegion("monedaPlata");
        med3 = atlas.findRegion("monedaBronce");
        med4 = atlas.findRegion("monedaAluminio");

        ParallaxLayer floor = new ParallaxLayer(atlas.findRegion("floor"),
                new Vector2(24, 0), new Vector2(0, 0), new Vector2(-1, 700),
                336, 140);
        ParallaxLayer[] as = new ParallaxLayer[]{floor};

        parallaxFondo = new ParallaxBackground(as, 480, 800, new Vector2(10, 0));

        die = Gdx.audio.newSound(Gdx.files.internal(path + "/sonidos/sfx_die.mp3"));
        hit = Gdx.audio.newSound(Gdx.files.internal(path + "/sonidos/sfx_hit.mp3"));
        point = Gdx.audio.newSound(Gdx.files.internal(path + "/sonidos/sfx_point.mp3"));
        swooshing = Gdx.audio.newSound(Gdx.files.internal(path + "/sonidos/sfx_swooshing.mp3"));
        wing = Gdx.audio.newSound(Gdx.files.internal(path + "/sonidos/sfx_wing.mp3"));

        Settings.load();
    }

    public static void playSound(Sound sound) {
        sound.play(1);
    }
}