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

    public static AtlasRegion buttonPlayArcade;
    public static AtlasRegion buttonPlayClassic;
    public static AtlasRegion buttonLeaderboard;

    public static AtlasRegion buttonFacebook;
    public static AtlasRegion buttonTwitter;
    public static AtlasRegion rainbowLight;

    public static NinePatchDrawable whiteDrawable;
    public static NinePatchDrawable blackDrawable;

    public static AtlasRegion buttonRate;
    public static AtlasRegion buttonNoAds;
    public static AtlasRegion buttonAchievements;
    public static AtlasRegion buttonRestorePurchases;

    public static AtlasRegion meteor1;
    public static AtlasRegion meteor2;
    public static AtlasRegion meteor3;
    public static AtlasRegion meteor4;
    public static AtlasRegion meteor5;
    public static AtlasRegion meteor6;

    public static AtlasRegion num0Large;
    public static AtlasRegion num1Large;
    public static AtlasRegion num2Large;
    public static AtlasRegion num3Large;
    public static AtlasRegion num4Large;
    public static AtlasRegion num5Large;
    public static AtlasRegion num6Large;
    public static AtlasRegion num7Large;
    public static AtlasRegion num8Large;
    public static AtlasRegion num9Large;

    public static AtlasRegion num0Small;
    public static AtlasRegion num1Small;
    public static AtlasRegion num2Small;
    public static AtlasRegion num3Small;
    public static AtlasRegion num4Small;
    public static AtlasRegion num5Small;
    public static AtlasRegion num6Small;
    public static AtlasRegion num7Small;
    public static AtlasRegion num8Small;
    public static AtlasRegion num9Small;

    public static AtlasRegion appTitle;
    public static AtlasRegion upperTube;
    public static AtlasRegion lowerTube;

    public static ParallaxBackground parallaxBackground;

    public static AtlasRegion background0;
    public static AtlasRegion background1;
    public static AtlasRegion background2;
    public static AtlasRegion background3;

    public static AtlasRegion medalsBackground;

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

        rainbowLight = atlas.findRegion("luzA");

        appTitle = atlas.findRegion("titulo");

        upperTube = atlas.findRegion("tubosEspacioArriba");
        lowerTube = atlas.findRegion("tubosEspacio");

        gameover = atlas.findRegion("gameover");
        getReady = atlas.findRegion("getready");
        tapCat = atlas.findRegion("tap");

        buttonPlayArcade = atlas.findRegion("btArcade");
        buttonPlayClassic = atlas.findRegion("btClassic");
        buttonLeaderboard = atlas.findRegion("btLeaderboard");
        buttonRate = atlas.findRegion("btRate");
        buttonNoAds = atlas.findRegion("btNoAds");
        buttonAchievements = atlas.findRegion("btAchievements");
        buttonRestorePurchases = atlas.findRegion("btRestore");

        if (buttonRestorePurchases == null) buttonRestorePurchases = atlas.findRegion("btNoAds");

        buttonFacebook = atlas.findRegion("btFacebook");
        buttonTwitter = atlas.findRegion("btTwitter");

        background1 = atlas.findRegion("fondo1");
        background2 = atlas.findRegion("fondo1");
        background3 = atlas.findRegion("fondo1");

        num0Large = atlas.findRegion("num0");
        num1Large = atlas.findRegion("num1");
        num2Large = atlas.findRegion("num2");
        num3Large = atlas.findRegion("num3");
        num4Large = atlas.findRegion("num4");
        num5Large = atlas.findRegion("num5");
        num6Large = atlas.findRegion("num6");
        num7Large = atlas.findRegion("num7");
        num8Large = atlas.findRegion("num8");
        num9Large = atlas.findRegion("num9");

        num0Small = atlas.findRegion("0");
        num1Small = atlas.findRegion("1");
        num2Small = atlas.findRegion("2");
        num3Small = atlas.findRegion("3");
        num4Small = atlas.findRegion("4");
        num5Small = atlas.findRegion("5");
        num6Small = atlas.findRegion("6");
        num7Small = atlas.findRegion("7");
        num8Small = atlas.findRegion("8");
        num9Small = atlas.findRegion("9");

        meteor1 = atlas.findRegion("meteoro1");
        meteor2 = atlas.findRegion("meteoro2");
        meteor3 = atlas.findRegion("meteoro3");
        meteor4 = atlas.findRegion("meteoro4");
        meteor5 = atlas.findRegion("meteoro5");
        meteor6 = atlas.findRegion("meteoro6");

        whiteDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("luz"), 1, 1, 0, 0));
        blackDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("oscuridad"), 1, 1, 0, 0));

        medalsBackground = atlas.findRegion("medallsFondo");
        med1 = atlas.findRegion("monedaOro");
        med2 = atlas.findRegion("monedaPlata");
        med3 = atlas.findRegion("monedaBronce");
        med4 = atlas.findRegion("monedaAluminio");

        ParallaxLayer floor = new ParallaxLayer(atlas.findRegion("floor"),
                new Vector2(24, 0), new Vector2(0, 0), new Vector2(-1, 700),
                336, 140);
        ParallaxLayer[] as = new ParallaxLayer[]{floor};

        parallaxBackground = new ParallaxBackground(as, 480, 800, new Vector2(10, 0));

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