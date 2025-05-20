package com.nopalsoft.ninjarunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.ninjarunner.parallax.ParallaxBackground;
import com.nopalsoft.ninjarunner.parallax.ParallaxLayer;

/**
 * Created by Yayo on 1/21/15.
 */
public class Assets {

    public static BitmapFont fontChico;
    public static BitmapFont fontGrande;

    public static com.nopalsoft.ninjarunner.AnimationSprite personajeRun;
    public static com.nopalsoft.ninjarunner.AnimationSprite personajeDash;
    public static com.nopalsoft.ninjarunner.AnimationSprite personajeIdle;
    public static com.nopalsoft.ninjarunner.AnimationSprite personajeDead;
    public static com.nopalsoft.ninjarunner.AnimationSprite personajeJump;
    public static com.nopalsoft.ninjarunner.AnimationSprite personajeHurt;
    public static com.nopalsoft.ninjarunner.AnimationSprite personajeDizzy;
    public static com.nopalsoft.ninjarunner.AnimationSprite personajeSlide;

    public static com.nopalsoft.ninjarunner.AnimationSprite ninjaRun;
    public static com.nopalsoft.ninjarunner.AnimationSprite ninjaDash;
    public static com.nopalsoft.ninjarunner.AnimationSprite ninjaIdle;
    public static com.nopalsoft.ninjarunner.AnimationSprite ninjaDead;
    public static com.nopalsoft.ninjarunner.AnimationSprite ninjaJump;
    public static com.nopalsoft.ninjarunner.AnimationSprite ninjaHurt;
    public static com.nopalsoft.ninjarunner.AnimationSprite ninjaDizzy;
    public static com.nopalsoft.ninjarunner.AnimationSprite ninjaSlide;

    public static com.nopalsoft.ninjarunner.AnimationSprite Mascota1Fly;
    public static com.nopalsoft.ninjarunner.AnimationSprite Mascota1Dash;

    public static com.nopalsoft.ninjarunner.AnimationSprite MascotaBombFly;

    public static com.nopalsoft.ninjarunner.AnimationSprite moneda;
    public static com.nopalsoft.ninjarunner.AnimationSprite pick;

    public static Sprite magnet;
    public static Sprite energy;
    public static Sprite hearth;

    public static Sprite jellyRed;
    public static Sprite beanRed;
    public static Sprite candyCorn;

    public static com.nopalsoft.ninjarunner.AnimationSprite candyExplosionRed;

    public static Sprite plataforma;
    public static Sprite pared;

    // Obstaculos
    public static Sprite cajas4;
    public static Sprite cajas7;

    public static com.nopalsoft.ninjarunner.AnimationSprite missil;
    public static com.nopalsoft.ninjarunner.AnimationSprite explosion;

    public static ParallaxBackground backgroundNubes;

    public static Music musica1;

    public static NinePatchDrawable pixelNegro;

    public static Sound jump;
    public static Sound coin;
    public static Sound popCandy;

    public static ParticleEffectPool cajasEffectPool;

    // UI STUFF

    public static TextureRegionDrawable titulo;

    public static NinePatchDrawable backgroundMenu;
    public static NinePatchDrawable backgroundShop;
    public static NinePatchDrawable backgroundTitleShop;
    public static NinePatchDrawable backgroundItemShop;
    public static NinePatchDrawable backgroundUpgradeBar;

    public static TextureRegionDrawable btShop;
    public static TextureRegionDrawable btShopPress;
    public static TextureRegionDrawable btLeaderboard;
    public static TextureRegionDrawable btLeaderboardPress;
    public static TextureRegionDrawable btAchievement;
    public static TextureRegionDrawable btAchievementPress;
    public static TextureRegionDrawable btSettings;
    public static TextureRegionDrawable btSettingsPress;
    public static TextureRegionDrawable btRate;
    public static TextureRegionDrawable btRatePress;
    public static TextureRegionDrawable btShare;
    public static TextureRegionDrawable btSharePress;
    public static TextureRegionDrawable btUpgrade;
    public static TextureRegionDrawable btUpgradePress;
    public static TextureRegionDrawable btFacebook;
    public static TextureRegionDrawable btFacebookPress;
    public static TextureRegionDrawable photoFrame;
    public static TextureRegionDrawable photoNA;

    public static TextureRegionDrawable imGoogle;
    public static TextureRegionDrawable imAmazon;
    public static TextureRegionDrawable imFacebook;

    public static LabelStyle labelStyleChico;
    public static LabelStyle labelStyleGrande;

    public static TextButtonStyle styleTextButtonPurchased;
    public static TextButtonStyle styleTextButtonBuy;
    public static ButtonStyle styleButtonUpgrade;

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontGrande = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("Font/fontGrande"));
        fontChico = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("Font/fontChico"));
        fontChico.setUseIntegerPositions(false);


        loadUI(atlas);
        loadShanti(atlas);
        loadNinja(atlas);

        Sprite fly1 = atlas.createSprite("Mascota1/fly1");
        Sprite fly2 = atlas.createSprite("Mascota1/fly2");
        Sprite fly3 = atlas.createSprite("Mascota1/fly3");
        Sprite fly4 = atlas.createSprite("Mascota1/fly4");
        Sprite fly5 = atlas.createSprite("Mascota1/fly5");
        Sprite fly6 = atlas.createSprite("Mascota1/fly6");
        Sprite fly7 = atlas.createSprite("Mascota1/fly7");
        Sprite fly8 = atlas.createSprite("Mascota1/fly8");
        Mascota1Fly = new com.nopalsoft.ninjarunner.AnimationSprite(.075f, fly1, fly2, fly3, fly4, fly5, fly6, fly7, fly8);

        fly1 = atlas.createSprite("Mascota2/fly1");
        fly2 = atlas.createSprite("Mascota2/fly2");
        fly3 = atlas.createSprite("Mascota2/fly3");
        fly4 = atlas.createSprite("Mascota2/fly4");
        fly5 = atlas.createSprite("Mascota2/fly5");
        fly6 = atlas.createSprite("Mascota2/fly6");
        fly7 = atlas.createSprite("Mascota2/fly7");
        fly8 = atlas.createSprite("Mascota2/fly8");
        MascotaBombFly = new com.nopalsoft.ninjarunner.AnimationSprite(.075f, fly1, fly2, fly3, fly4, fly5, fly6, fly7, fly8);

        Sprite dash1 = atlas.createSprite("Mascota1/dash1");
        Sprite dash2 = atlas.createSprite("Mascota1/dash2");
        Sprite dash3 = atlas.createSprite("Mascota1/dash3");
        Sprite dash4 = atlas.createSprite("Mascota1/dash4");
        Mascota1Dash = new com.nopalsoft.ninjarunner.AnimationSprite(.04f, dash1, dash2, dash3, dash4);

        Sprite moneda1 = atlas.createSprite("moneda1");
        Sprite moneda2 = atlas.createSprite("moneda2");
        Sprite moneda3 = atlas.createSprite("moneda3");
        Sprite moneda4 = atlas.createSprite("moneda4");
        Sprite moneda5 = atlas.createSprite("moneda5");
        Sprite moneda6 = atlas.createSprite("moneda6");
        Sprite moneda7 = atlas.createSprite("moneda7");
        Sprite moneda8 = atlas.createSprite("moneda8");
        moneda = new com.nopalsoft.ninjarunner.AnimationSprite(.075f, moneda1, moneda2, moneda3, moneda4, moneda5, moneda6, moneda7, moneda8);

        Sprite pick1 = atlas.createSprite("pick1");
        Sprite pick2 = atlas.createSprite("pick2");
        Sprite pick3 = atlas.createSprite("pick3");
        pick = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, pick1, pick2, pick3);

        Sprite missil1 = atlas.createSprite("misil1");
        Sprite missil2 = atlas.createSprite("misil2");
        Sprite missil3 = atlas.createSprite("misil3");
        Sprite missil4 = atlas.createSprite("misil4");
        missil = new com.nopalsoft.ninjarunner.AnimationSprite(.05f, missil1, missil2, missil3, missil4);

        Sprite explosion1 = atlas.createSprite("explosion1");
        Sprite explosion2 = atlas.createSprite("explosion2");
        Sprite explosion3 = atlas.createSprite("explosion3");
        Sprite explosion4 = atlas.createSprite("explosion4");
        Sprite explosion5 = atlas.createSprite("explosion5");
        explosion = new com.nopalsoft.ninjarunner.AnimationSprite(.05f, explosion1, explosion2, explosion3, explosion4, explosion5);

        plataforma = atlas.createSprite("plataforma");
        pared = atlas.createSprite("pared");
        cajas4 = atlas.createSprite("cajas4");
        cajas7 = atlas.createSprite("cajas7");
        magnet = atlas.createSprite("magnet");
        energy = atlas.createSprite("energy");
        hearth = atlas.createSprite("hearth");

        // Candies

        explosion1 = atlas.createSprite("Candy/explosionred01");
        explosion2 = atlas.createSprite("Candy/explosionred02");
        explosion3 = atlas.createSprite("Candy/explosionred03");
        explosion4 = atlas.createSprite("Candy/explosionred04");
        explosion5 = atlas.createSprite("Candy/explosionred05");
        candyExplosionRed = new com.nopalsoft.ninjarunner.AnimationSprite(.05f, explosion1, explosion2, explosion3, explosion4, explosion5);

        jellyRed = atlas.createSprite("Candy/jelly_red");
        beanRed = atlas.createSprite("Candy/bean_red");
        candyCorn = atlas.createSprite("Candy/candycorn");

        // Particulas
        ParticleEffect cajasEffect = new ParticleEffect();
        cajasEffect.load(Gdx.files.internal("data/Particulas/Cajas"), atlas);
        cajasEffectPool = new ParticleEffectPool(cajasEffect, 1, 10);

        /** entre mas chico el numero mas atras */
        ParallaxLayer sol = new ParallaxLayer(atlas.findRegion("Background/sol"), new Vector2(.5f, 0), new Vector2(600, 300), new Vector2(800, 800),
                170, 170);

        ParallaxLayer nubes1 = new ParallaxLayer(atlas.findRegion("Background/nubesLayer1"), new Vector2(1, 0), new Vector2(0, 50), new Vector2(690,
                500), 557, 159);
        ParallaxLayer nubes2 = new ParallaxLayer(atlas.findRegion("Background/nubesLayer2"), new Vector2(3, 0), new Vector2(0, 50), new Vector2(-1,
                500), 1250, 198);
        ParallaxLayer nubes3 = new ParallaxLayer(atlas.findRegion("Background/nubesLayer3"), new Vector2(5, 0), new Vector2(0, 50), new Vector2(-1,
                100), 1250, 397);

        ParallaxLayer arboles1 = new ParallaxLayer(atlas.findRegion("Background/arbolesLayer1"), new Vector2(7, 0), new Vector2(0, 50), new Vector2(
                -1, 500), 1048, 159);
        ParallaxLayer arboles2 = new ParallaxLayer(atlas.findRegion("Background/arbolesLayer2"), new Vector2(8, 0), new Vector2(0, 50), new Vector2(
                1008, 500), 554, 242);

        ParallaxLayer sueloAzul = new ParallaxLayer(atlas.findRegion("Background/sueloAzul"), new Vector2(0, 0), new Vector2(0, -1), new Vector2(-1,
                500), 800, 50);

        backgroundNubes = new ParallaxBackground(new ParallaxLayer[]{sol, nubes1, nubes2, nubes3, arboles1, arboles2, sueloAzul}, 800, 480,
                new Vector2(20, 0));

        jump = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/salto.mp3"));
        coin = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/pickCoin.mp3"));
        popCandy = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/popBubble.mp3"));

        musica1 = Gdx.audio.newMusic(Gdx.files.internal("data/Sonidos/Happy.mp3"));
        musica1.setLooping(true);

    }

    private static void loadShanti(TextureAtlas atlas) {
        Sprite dash1 = atlas.createSprite("dash1");
        Sprite dash2 = atlas.createSprite("dash2");
        Sprite dash3 = atlas.createSprite("dash3");
        personajeDash = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, dash1, dash2, dash3);

        Sprite idle1 = atlas.createSprite("idle1");
        Sprite idle2 = atlas.createSprite("idle2");
        Sprite idle3 = atlas.createSprite("idle3");
        Sprite idle4 = atlas.createSprite("idle4");
        personajeIdle = new com.nopalsoft.ninjarunner.AnimationSprite(.25f, idle1, idle2, idle3, idle4);

        Sprite dead1 = atlas.createSprite("dead1");
        Sprite dead2 = atlas.createSprite("dead2");
        Sprite dead3 = atlas.createSprite("dead3");
        Sprite dead4 = atlas.createSprite("dead4");
        Sprite dead5 = atlas.createSprite("dead5");
        personajeDead = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, dead1, dead2, dead3, dead4, dead5);

        Sprite hurt1 = atlas.createSprite("hurt1");
        Sprite hurt2 = atlas.createSprite("hurt2");
        personajeHurt = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, hurt1, hurt2);

        Sprite dizzy1 = atlas.createSprite("dizzy1");
        Sprite dizzy2 = atlas.createSprite("dizzy2");
        Sprite dizzy3 = atlas.createSprite("dizzy3");
        personajeDizzy = new com.nopalsoft.ninjarunner.AnimationSprite(.18f, dizzy1, dizzy2, dizzy3);

        Sprite jump1 = atlas.createSprite("jump1");
        Sprite jump2 = atlas.createSprite("jump2");
        Sprite jump3 = atlas.createSprite("jump3");
        Sprite jump4 = atlas.createSprite("jump4");
        Sprite jump5 = atlas.createSprite("jump5");
        Sprite jump6 = atlas.createSprite("jump6");
        personajeJump = new com.nopalsoft.ninjarunner.AnimationSprite(.18f, jump1, jump2, jump3, jump4, jump5, jump6);

        Sprite run1 = atlas.createSprite("run1");
        Sprite run2 = atlas.createSprite("run2");
        Sprite run3 = atlas.createSprite("run3");
        Sprite run4 = atlas.createSprite("run4");
        Sprite run5 = atlas.createSprite("run5");
        Sprite run6 = atlas.createSprite("run6");
        personajeRun = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, run1, run2, run3, run4, run5, run6);

        Sprite slide1 = atlas.createSprite("slide1");
        Sprite slide2 = atlas.createSprite("slide2");
        Sprite slide3 = atlas.createSprite("slide3");
        personajeSlide = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, slide1, slide2, slide3);

    }

    private static void loadNinja(TextureAtlas atlas) {

        Sprite run1 = atlas.createSprite("Ninja/run1");
        Sprite run2 = atlas.createSprite("Ninja/run2");
        Sprite run3 = atlas.createSprite("Ninja/run3");
        Sprite run4 = atlas.createSprite("Ninja/run4");
        Sprite run5 = atlas.createSprite("Ninja/run5");
        Sprite run6 = atlas.createSprite("Ninja/run6");
        ninjaRun = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, run1, run2, run3, run4, run5, run6);
        ninjaDash = new com.nopalsoft.ninjarunner.AnimationSprite(.05f, run1, run2, run3, run4, run5, run6);

        Sprite jump1 = atlas.createSprite("Ninja/jump1");
        Sprite jump2 = atlas.createSprite("Ninja/jump2");
        Sprite jump3 = atlas.createSprite("Ninja/jump3");
        Sprite jump4 = atlas.createSprite("Ninja/jump4");
        Sprite jump5 = atlas.createSprite("Ninja/jump5");
        Sprite jump6 = atlas.createSprite("Ninja/jump6");
        Sprite jump7 = atlas.createSprite("Ninja/jump7");
        Sprite jump8 = atlas.createSprite("Ninja/jump8");
        ninjaJump = new com.nopalsoft.ninjarunner.AnimationSprite(.075f, jump1, jump2, jump3, jump4, jump5, jump6, jump7, jump8);

        Sprite slide1 = atlas.createSprite("Ninja/slide1");
        Sprite slide2 = atlas.createSprite("Ninja/slide2");
        Sprite slide3 = atlas.createSprite("Ninja/slide3");
        ninjaSlide = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, slide1, slide2, slide3);

        Sprite idle1 = atlas.createSprite("Ninja/idle1");
        Sprite idle2 = atlas.createSprite("Ninja/idle2");
        Sprite idle3 = atlas.createSprite("Ninja/idle3");
        Sprite idle4 = atlas.createSprite("Ninja/idle4");
        ninjaIdle = new com.nopalsoft.ninjarunner.AnimationSprite(.25f, idle1, idle2, idle3, idle4);

        Sprite dead1 = atlas.createSprite("Ninja/dead1");
        Sprite dead2 = atlas.createSprite("Ninja/dead2");
        Sprite dead3 = atlas.createSprite("Ninja/dead3");
        Sprite dead4 = atlas.createSprite("Ninja/dead4");
        Sprite dead5 = atlas.createSprite("Ninja/dead5");
        ninjaDead = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, dead1, dead2, dead3, dead4, dead5);

        Sprite hurt1 = atlas.createSprite("Ninja/hurt1");
        Sprite hurt2 = atlas.createSprite("Ninja/hurt2");
        ninjaHurt = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, hurt1, hurt2);

        Sprite dizzy1 = atlas.createSprite("Ninja/dizzy1");
        Sprite dizzy2 = atlas.createSprite("Ninja/dizzy2");
        Sprite dizzy3 = atlas.createSprite("Ninja/dizzy3");
        ninjaDizzy = new com.nopalsoft.ninjarunner.AnimationSprite(.18f, dizzy1, dizzy2, dizzy3);

    }

    private static void loadUI(TextureAtlas atlas) {
        titulo = new TextureRegionDrawable(atlas.findRegion("UI/titulo"));

        pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0));

        backgroundMenu = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundMenu"), 40, 40, 40, 40));
        backgroundShop = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundShop"), 140, 40, 40, 40));
        backgroundTitleShop = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundTitleShop"), 40, 40, 40, 30));
        backgroundItemShop = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundItemShop"), 50, 50, 25, 15));
        backgroundUpgradeBar = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundUpgradeBar"), 15, 15, 9, 10));

        btShop = new TextureRegionDrawable(atlas.findRegion("UI/btShop"));
        btShopPress = new TextureRegionDrawable(atlas.findRegion("UI/btShopPress"));
        btLeaderboard = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
        btLeaderboardPress = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"));
        btAchievement = new TextureRegionDrawable(atlas.findRegion("UI/btAchievement"));
        btAchievementPress = new TextureRegionDrawable(atlas.findRegion("UI/btAchievementPress"));
        btSettings = new TextureRegionDrawable(atlas.findRegion("UI/btSettings"));
        btSettingsPress = new TextureRegionDrawable(atlas.findRegion("UI/btSettingsPress"));
        btRate = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        btRatePress = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        btFacebook = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        btFacebookPress = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        btShare = new TextureRegionDrawable(atlas.findRegion("UI/btShare"));
        btSharePress = new TextureRegionDrawable(atlas.findRegion("UI/btSharePress"));
        btUpgrade = new TextureRegionDrawable(atlas.findRegion("UI/btUpgrade"));
        btUpgradePress = new TextureRegionDrawable(atlas.findRegion("UI/btUpgradePress"));
        photoFrame = new TextureRegionDrawable(atlas.findRegion("UI/photoFrame"));
        photoNA = new TextureRegionDrawable(atlas.findRegion("UI/fotoNA"));

        imAmazon = new TextureRegionDrawable(atlas.findRegion("UI/imAmazon"));
        imGoogle = new TextureRegionDrawable(atlas.findRegion("UI/imGoogle"));
        imFacebook = new TextureRegionDrawable(atlas.findRegion("UI/imFacebook"));

        labelStyleChico = new LabelStyle(fontChico, Color.WHITE);
        labelStyleGrande = new LabelStyle(fontGrande, Color.WHITE);

        TextureRegionDrawable txtButton = new TextureRegionDrawable(atlas.findRegion("UI/txtButton"));
        TextureRegionDrawable txtButtonDisabled = new TextureRegionDrawable(atlas.findRegion("UI/txtButtonDisabled"));
        TextureRegionDrawable txtButtonPress = new TextureRegionDrawable(atlas.findRegion("UI/txtButtonPress"));

        styleTextButtonPurchased = new TextButtonStyle(txtButton, txtButtonPress, null, fontChico);
//		styleTextButtonPurchased.fontColor = Color.WHITE;

        styleTextButtonBuy = new TextButtonStyle(txtButtonDisabled, txtButtonPress, null, fontChico);
//		styleTextButtonBuy.fontColor = Color.WHITE;

        styleButtonUpgrade = new ButtonStyle(btUpgrade, btUpgradePress, null);

    }

//    private static BitmapFont createFont(int size) {
//        BitmapFont font;
//
//        FreeTypeFontGenerator generator;
//        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/DroidSansFallback.ttf"));
////        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/arial.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.minFilter = Texture.TextureFilter.Linear;
//        parameter.magFilter = Texture.TextureFilter.Linear;
//        parameter.size = size;
//        parameter.incremental = true;
//        parameter.borderWidth = 1;
//        parameter.shadowColor = Color.DARK_GRAY;
//        parameter.shadowOffsetX = 2;
//
//
//        font = generator.generateFont(parameter);
//
//        return font;
//    }

    public static void playSound(Sound sound, int volume) {
        if (com.nopalsoft.ninjarunner.Settings.isSoundEnabled) {
            sound.play(volume);
        }
    }

}
