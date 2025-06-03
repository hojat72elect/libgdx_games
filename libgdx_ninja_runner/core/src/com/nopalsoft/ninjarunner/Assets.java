package com.nopalsoft.ninjarunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.ninjarunner.parallax.ParallaxBackground;
import com.nopalsoft.ninjarunner.parallax.ParallaxLayer;


public class Assets {

    public static BitmapFont smallFont;
    public static BitmapFont largeFont;

    // All the animations that are related to the girl character
    public static AnimationSprite girlRunAnimation;
    public static AnimationSprite girlDashAnimation;
    public static AnimationSprite girlIdleAnimation;
    public static AnimationSprite girlDeathAnimation;
    public static AnimationSprite girlJumpAnimation;
    public static AnimationSprite girlHurtAnimation;
    public static AnimationSprite girlDizzyAnimation;
    public static AnimationSprite girlSlideAnimation;

    // All the animations that are related to the ninja character
    public static AnimationSprite ninjaRunAnimation;
    public static AnimationSprite ninjaDashAnimation;
    public static AnimationSprite ninjaIdleAnimation;
    public static AnimationSprite ninjaDeathAnimation;
    public static AnimationSprite ninjaJumpAnimation;
    public static AnimationSprite ninjaHurtAnimation;
    public static AnimationSprite ninjaDizzyAnimation;
    public static AnimationSprite ninjaSlideAnimation;

    public static AnimationSprite mascotBirdFlyAnimation;
    public static AnimationSprite mascotBirdDashAnimation;
    public static AnimationSprite mascotBombFlyAnimation;
    public static AnimationSprite coinAnimation;
    public static AnimationSprite pickUpAnimation;
    public static AnimationSprite candyExplosionRed;

    public static Sprite magnet;
    public static Sprite energy;
    public static Sprite hearth;
    public static Sprite jellyRed;
    public static Sprite beanRed;
    public static Sprite candyCorn;
    public static Sprite platform;
    public static Sprite wall;
    // Obstacles
    public static Sprite boxes4Sprite;
    public static Sprite boxes7Sprite;

    public static AnimationSprite missileAnimation;
    public static AnimationSprite explosionAnimation;

    public static ParallaxBackground cloudsParallaxBackground;

    public static Music music1;

    public static NinePatchDrawable blackPixelDrawable;

    public static Sound jumpSound;
    public static Sound coinSound;
    public static Sound candySound;

    public static ParticleEffectPool boxesEffectPool;

    // UI STUFF

    public static TextureRegionDrawable titleDrawable;

    public static NinePatchDrawable backgroundMenu;
    public static NinePatchDrawable backgroundShop;
    public static NinePatchDrawable backgroundTitleShop;
    public static NinePatchDrawable backgroundItemShop;
    public static NinePatchDrawable backgroundUpgradeBar;

    public static TextureRegionDrawable buttonShop;
    public static TextureRegionDrawable buttonShopPress;
    public static TextureRegionDrawable buttonLeaderboard;
    public static TextureRegionDrawable buttonLeaderBoardPress;
    public static TextureRegionDrawable buttonAchievement;
    public static TextureRegionDrawable buttonAchievementPress;
    public static TextureRegionDrawable buttonSettings;
    public static TextureRegionDrawable buttonSettingsPress;
    public static TextureRegionDrawable buttonRate;
    public static TextureRegionDrawable buttonRatePress;
    public static TextureRegionDrawable buttonShare;
    public static TextureRegionDrawable buttonSharePress;
    public static TextureRegionDrawable buttonUpgrade;
    public static TextureRegionDrawable buttonUpgradePress;
    public static TextureRegionDrawable buttonFacebook;
    public static TextureRegionDrawable buttonFacebookPress;
    public static TextureRegionDrawable photoFrame;
    public static TextureRegionDrawable photoNA;

    public static TextureRegionDrawable imageGoogle;
    public static TextureRegionDrawable imageAmazon;
    public static TextureRegionDrawable imageFacebook;

    public static LabelStyle labelStyleSmall;
    public static LabelStyle labelStyleLarge;

    public static TextButtonStyle styleTextButtonPurchased;
    public static TextButtonStyle styleTextButtonBuy;
    public static ButtonStyle styleButtonUpgrade;

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        largeFont = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("Font/fontGrande"));
        smallFont = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("Font/fontChico"));
        smallFont.setUseIntegerPositions(false);


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
        mascotBirdFlyAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.075f, fly1, fly2, fly3, fly4, fly5, fly6, fly7, fly8);

        fly1 = atlas.createSprite("Mascota2/fly1");
        fly2 = atlas.createSprite("Mascota2/fly2");
        fly3 = atlas.createSprite("Mascota2/fly3");
        fly4 = atlas.createSprite("Mascota2/fly4");
        fly5 = atlas.createSprite("Mascota2/fly5");
        fly6 = atlas.createSprite("Mascota2/fly6");
        fly7 = atlas.createSprite("Mascota2/fly7");
        fly8 = atlas.createSprite("Mascota2/fly8");
        mascotBombFlyAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.075f, fly1, fly2, fly3, fly4, fly5, fly6, fly7, fly8);

        Sprite dash1 = atlas.createSprite("Mascota1/dash1");
        Sprite dash2 = atlas.createSprite("Mascota1/dash2");
        Sprite dash3 = atlas.createSprite("Mascota1/dash3");
        Sprite dash4 = atlas.createSprite("Mascota1/dash4");
        mascotBirdDashAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.04f, dash1, dash2, dash3, dash4);

        Sprite moneda1 = atlas.createSprite("moneda1");
        Sprite moneda2 = atlas.createSprite("moneda2");
        Sprite moneda3 = atlas.createSprite("moneda3");
        Sprite moneda4 = atlas.createSprite("moneda4");
        Sprite moneda5 = atlas.createSprite("moneda5");
        Sprite moneda6 = atlas.createSprite("moneda6");
        Sprite moneda7 = atlas.createSprite("moneda7");
        Sprite moneda8 = atlas.createSprite("moneda8");
        coinAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.075f, moneda1, moneda2, moneda3, moneda4, moneda5, moneda6, moneda7, moneda8);

        Sprite pick1 = atlas.createSprite("pick1");
        Sprite pick2 = atlas.createSprite("pick2");
        Sprite pick3 = atlas.createSprite("pick3");
        pickUpAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, pick1, pick2, pick3);

        Sprite missil1 = atlas.createSprite("misil1");
        Sprite missil2 = atlas.createSprite("misil2");
        Sprite missil3 = atlas.createSprite("misil3");
        Sprite missil4 = atlas.createSprite("misil4");
        missileAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.05f, missil1, missil2, missil3, missil4);

        Sprite explosion1 = atlas.createSprite("explosion1");
        Sprite explosion2 = atlas.createSprite("explosion2");
        Sprite explosion3 = atlas.createSprite("explosion3");
        Sprite explosion4 = atlas.createSprite("explosion4");
        Sprite explosion5 = atlas.createSprite("explosion5");
        explosionAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.05f, explosion1, explosion2, explosion3, explosion4, explosion5);

        platform = atlas.createSprite("plataforma");
        wall = atlas.createSprite("pared");
        boxes4Sprite = atlas.createSprite("cajas4");
        boxes7Sprite = atlas.createSprite("cajas7");
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
        boxesEffectPool = new ParticleEffectPool(cajasEffect, 1, 10);

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

        cloudsParallaxBackground = new ParallaxBackground(new ParallaxLayer[]{sol, nubes1, nubes2, nubes3, arboles1, arboles2, sueloAzul}, 800, 480,
                new Vector2(20, 0));

        jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/salto.mp3"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/pickCoin.mp3"));
        candySound = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/popBubble.mp3"));

        music1 = Gdx.audio.newMusic(Gdx.files.internal("data/Sonidos/Happy.mp3"));
        music1.setLooping(true);
    }

    private static void loadShanti(TextureAtlas atlas) {
        Sprite dash1 = atlas.createSprite("dash1");
        Sprite dash2 = atlas.createSprite("dash2");
        Sprite dash3 = atlas.createSprite("dash3");
        girlDashAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, dash1, dash2, dash3);

        Sprite idle1 = atlas.createSprite("idle1");
        Sprite idle2 = atlas.createSprite("idle2");
        Sprite idle3 = atlas.createSprite("idle3");
        Sprite idle4 = atlas.createSprite("idle4");
        girlIdleAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.25f, idle1, idle2, idle3, idle4);

        Sprite dead1 = atlas.createSprite("dead1");
        Sprite dead2 = atlas.createSprite("dead2");
        Sprite dead3 = atlas.createSprite("dead3");
        Sprite dead4 = atlas.createSprite("dead4");
        Sprite dead5 = atlas.createSprite("dead5");
        girlDeathAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, dead1, dead2, dead3, dead4, dead5);

        Sprite hurt1 = atlas.createSprite("hurt1");
        Sprite hurt2 = atlas.createSprite("hurt2");
        girlHurtAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, hurt1, hurt2);

        Sprite dizzy1 = atlas.createSprite("dizzy1");
        Sprite dizzy2 = atlas.createSprite("dizzy2");
        Sprite dizzy3 = atlas.createSprite("dizzy3");
        girlDizzyAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.18f, dizzy1, dizzy2, dizzy3);

        Sprite jump1 = atlas.createSprite("jump1");
        Sprite jump2 = atlas.createSprite("jump2");
        Sprite jump3 = atlas.createSprite("jump3");
        Sprite jump4 = atlas.createSprite("jump4");
        Sprite jump5 = atlas.createSprite("jump5");
        Sprite jump6 = atlas.createSprite("jump6");
        girlJumpAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.18f, jump1, jump2, jump3, jump4, jump5, jump6);

        Sprite run1 = atlas.createSprite("run1");
        Sprite run2 = atlas.createSprite("run2");
        Sprite run3 = atlas.createSprite("run3");
        Sprite run4 = atlas.createSprite("run4");
        Sprite run5 = atlas.createSprite("run5");
        Sprite run6 = atlas.createSprite("run6");
        girlRunAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, run1, run2, run3, run4, run5, run6);

        Sprite slide1 = atlas.createSprite("slide1");
        Sprite slide2 = atlas.createSprite("slide2");
        Sprite slide3 = atlas.createSprite("slide3");
        girlSlideAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, slide1, slide2, slide3);
    }

    private static void loadNinja(TextureAtlas atlas) {

        Sprite run1 = atlas.createSprite("Ninja/run1");
        Sprite run2 = atlas.createSprite("Ninja/run2");
        Sprite run3 = atlas.createSprite("Ninja/run3");
        Sprite run4 = atlas.createSprite("Ninja/run4");
        Sprite run5 = atlas.createSprite("Ninja/run5");
        Sprite run6 = atlas.createSprite("Ninja/run6");
        ninjaRunAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, run1, run2, run3, run4, run5, run6);
        ninjaDashAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.05f, run1, run2, run3, run4, run5, run6);

        Sprite jump1 = atlas.createSprite("Ninja/jump1");
        Sprite jump2 = atlas.createSprite("Ninja/jump2");
        Sprite jump3 = atlas.createSprite("Ninja/jump3");
        Sprite jump4 = atlas.createSprite("Ninja/jump4");
        Sprite jump5 = atlas.createSprite("Ninja/jump5");
        Sprite jump6 = atlas.createSprite("Ninja/jump6");
        Sprite jump7 = atlas.createSprite("Ninja/jump7");
        Sprite jump8 = atlas.createSprite("Ninja/jump8");
        ninjaJumpAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.075f, jump1, jump2, jump3, jump4, jump5, jump6, jump7, jump8);

        Sprite slide1 = atlas.createSprite("Ninja/slide1");
        Sprite slide2 = atlas.createSprite("Ninja/slide2");
        Sprite slide3 = atlas.createSprite("Ninja/slide3");
        ninjaSlideAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.1f, slide1, slide2, slide3);

        Sprite idle1 = atlas.createSprite("Ninja/idle1");
        Sprite idle2 = atlas.createSprite("Ninja/idle2");
        Sprite idle3 = atlas.createSprite("Ninja/idle3");
        Sprite idle4 = atlas.createSprite("Ninja/idle4");
        ninjaIdleAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.25f, idle1, idle2, idle3, idle4);

        Sprite dead1 = atlas.createSprite("Ninja/dead1");
        Sprite dead2 = atlas.createSprite("Ninja/dead2");
        Sprite dead3 = atlas.createSprite("Ninja/dead3");
        Sprite dead4 = atlas.createSprite("Ninja/dead4");
        Sprite dead5 = atlas.createSprite("Ninja/dead5");
        ninjaDeathAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, dead1, dead2, dead3, dead4, dead5);

        Sprite hurt1 = atlas.createSprite("Ninja/hurt1");
        Sprite hurt2 = atlas.createSprite("Ninja/hurt2");
        ninjaHurtAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.085f, hurt1, hurt2);

        Sprite dizzy1 = atlas.createSprite("Ninja/dizzy1");
        Sprite dizzy2 = atlas.createSprite("Ninja/dizzy2");
        Sprite dizzy3 = atlas.createSprite("Ninja/dizzy3");
        ninjaDizzyAnimation = new com.nopalsoft.ninjarunner.AnimationSprite(.18f, dizzy1, dizzy2, dizzy3);
    }

    private static void loadUI(TextureAtlas atlas) {
        titleDrawable = new TextureRegionDrawable(atlas.findRegion("UI/titulo"));

        blackPixelDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0));

        backgroundMenu = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundMenu"), 40, 40, 40, 40));
        backgroundShop = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundShop"), 140, 40, 40, 40));
        backgroundTitleShop = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundTitleShop"), 40, 40, 40, 30));
        backgroundItemShop = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundItemShop"), 50, 50, 25, 15));
        backgroundUpgradeBar = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundUpgradeBar"), 15, 15, 9, 10));

        buttonShop = new TextureRegionDrawable(atlas.findRegion("UI/btShop"));
        buttonShopPress = new TextureRegionDrawable(atlas.findRegion("UI/btShopPress"));
        buttonLeaderboard = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
        buttonLeaderBoardPress = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"));
        buttonAchievement = new TextureRegionDrawable(atlas.findRegion("UI/btAchievement"));
        buttonAchievementPress = new TextureRegionDrawable(atlas.findRegion("UI/btAchievementPress"));
        buttonSettings = new TextureRegionDrawable(atlas.findRegion("UI/btSettings"));
        buttonSettingsPress = new TextureRegionDrawable(atlas.findRegion("UI/btSettingsPress"));
        buttonRate = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        buttonRatePress = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        buttonFacebook = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        buttonFacebookPress = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        buttonShare = new TextureRegionDrawable(atlas.findRegion("UI/btShare"));
        buttonSharePress = new TextureRegionDrawable(atlas.findRegion("UI/btSharePress"));
        buttonUpgrade = new TextureRegionDrawable(atlas.findRegion("UI/btUpgrade"));
        buttonUpgradePress = new TextureRegionDrawable(atlas.findRegion("UI/btUpgradePress"));
        photoFrame = new TextureRegionDrawable(atlas.findRegion("UI/photoFrame"));
        photoNA = new TextureRegionDrawable(atlas.findRegion("UI/fotoNA"));

        imageAmazon = new TextureRegionDrawable(atlas.findRegion("UI/imAmazon"));
        imageGoogle = new TextureRegionDrawable(atlas.findRegion("UI/imGoogle"));
        imageFacebook = new TextureRegionDrawable(atlas.findRegion("UI/imFacebook"));

        labelStyleSmall = new LabelStyle(smallFont, Color.WHITE);
        labelStyleLarge = new LabelStyle(largeFont, Color.WHITE);

        TextureRegionDrawable txtButton = new TextureRegionDrawable(atlas.findRegion("UI/txtButton"));
        TextureRegionDrawable txtButtonDisabled = new TextureRegionDrawable(atlas.findRegion("UI/txtButtonDisabled"));
        TextureRegionDrawable txtButtonPress = new TextureRegionDrawable(atlas.findRegion("UI/txtButtonPress"));

        styleTextButtonPurchased = new TextButtonStyle(txtButton, txtButtonPress, null, smallFont);
//		styleTextButtonPurchased.fontColor = Color.WHITE;

        styleTextButtonBuy = new TextButtonStyle(txtButtonDisabled, txtButtonPress, null, smallFont);
//		styleTextButtonBuy.fontColor = Color.WHITE;

        styleButtonUpgrade = new ButtonStyle(buttonUpgrade, buttonUpgradePress, null);
    }

//    private static BitmapFont createFont(int size) {
//        BitmapFont font;
//
//        FreeTypeFontGenerator generator;
//        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/DroidSansFallback.ttf"));

    /// /        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/arial.ttf"));
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
