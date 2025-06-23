package com.nopalsoft.zombiedash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.zombiedash.parallax.ParallaxBackground;
import com.nopalsoft.zombiedash.parallax.ParallaxLayer;

public class Assets {

    public static BitmapFont fontChico;
    public static BitmapFont fontGrande;

    public static TextureRegionDrawable zombieDashTitulo;

    public static AtlasRegion weapon;
    public static AtlasRegion itemJump;

    public static AtlasRegion itemGem;
    public static AtlasRegion itemHeart;
    public static AtlasRegion itemShield;
    public static AtlasRegion itemSkull;
    public static AtlasRegion crate;
    public static AtlasRegion saw;
    public static AtlasRegion sawDialog;
    public static AtlasRegion spike;
    public static AtlasRegion weaponSmall;

    public static AtlasRegion redBar;
    public static AtlasRegion whiteBar;

    public static TextureRegionDrawable upgradeOff;
    /**
     * Heros
     */
    public static AnimationSprite heroSwatRun;
    public static AnimationSprite heroSwatDie;
    public static AnimationSprite heroSwatJump;
    public static Sprite heroSwatHurt;

    public static AnimationSprite heroForceRun;
    public static AnimationSprite heroForceDie;
    public static AnimationSprite heroForceJump;
    public static Sprite heroForceHurt;

    public static AnimationSprite heroRamboRun;
    public static AnimationSprite heroRamboDie;
    public static AnimationSprite heroRamboJump;
    public static Sprite heroRamboHurt;

    public static AnimationSprite heroSoldierRun;
    public static AnimationSprite heroSoldierDie;
    public static AnimationSprite heroSoldierJump;
    public static Sprite heroSoldierHurt;

    public static AnimationSprite heroVaderRun;
    public static AnimationSprite heroVaderDie;
    public static AnimationSprite heroVaderJump;
    public static Sprite heroVaderHurt;

    /**
     * Zombies
     */
    public static AnimationSprite zombieKidWalk;
    public static AnimationSprite zombieKidRise;
    public static AnimationSprite zombieKidDie;
    public static Sprite zombieKidHurt;

    public static AnimationSprite zombiePanWalk;
    public static AnimationSprite zombiePanRise;
    public static AnimationSprite zombiePanDie;
    public static Sprite zombiePanHurt;

    public static AnimationSprite zombieCuasyWalk;
    public static AnimationSprite zombieCuasyRise;
    public static AnimationSprite zombieCuasyDie;
    public static Sprite zombieCuasyHurt;

    public static AnimationSprite zombieFrankWalk;
    public static AnimationSprite zombieFrankRise;
    public static AnimationSprite zombieFrankDie;
    public static Sprite zombieFrankHurt;

    public static AnimationSprite zombieMummyWalk;
    public static AnimationSprite zombieMummyRise;
    public static AnimationSprite zombieMummyDie;
    public static Sprite zombieMummyHurt;

    /**
     * Bullet
     */
    public static AnimationSprite bullet1;
    public static AnimationSprite bullet2;
    public static AnimationSprite bullet3;
    public static AnimationSprite bullet4;
    public static AnimationSprite bullet5;
    public static AnimationSprite muzzle;

    public static TextureRegionDrawable containerButtons;
    public static TextureRegionDrawable btPlay;
    public static TextureRegionDrawable btLeaderboard;
    public static TextureRegionDrawable btAchievement;
    public static TextureRegionDrawable btSettings;
    public static TextureRegionDrawable btFacebook;

    public static TextureRegionDrawable btTwitter;
    public static TextureRegionDrawable btPause;
    public static TextureRegionDrawable btFire;
    public static TextureRegionDrawable btJump;
    public static TextureRegionDrawable btClose;
    public static TextureRegionDrawable btMenu;
    public static TextureRegionDrawable btTryAgain;
    public static TextureRegionDrawable btShop;
    public static TextureRegionDrawable btPlayer;
    public static TextureRegionDrawable btGems;
    public static TextureRegionDrawable btMore;

    public static AtlasRegion pisoRojo;
    public static AtlasRegion pisoVerde;

    public static ParallaxBackground parallaxBackground;

    public static TextureRegionDrawable backgroundProgressBar;
    public static TextureRegionDrawable backgroundBigWindow;
    public static TextureRegionDrawable backgroundSmallWindow;
    public static NinePatchDrawable storeTableBackground;

    public static NinePatchDrawable helpDialog;
    public static NinePatchDrawable helpDialogInverted;

    public static LabelStyle labelStyleChico;
    public static LabelStyle labelStyleGrande;
    public static LabelStyle labelStyleHelpDialog;
    public static SliderStyle sliderStyle;

    public static ButtonStyle styleButtonMusic;
    public static ButtonStyle styleButtonSound;

    public static TextButtonStyle styleTextButtonBuy;
    public static TextButtonStyle styleTextButtonShare;
    public static TextButtonStyle styleTextButtonPurchased;
    public static TextButtonStyle styleTextButtonFacebook;
    public static ScrollPaneStyle styleScrollPane;

    public static NinePatchDrawable pixelNegro;

    public static Sound shoot1;
    public static Sound noBullet;
    public static Sound zombiePan;
    public static Sound zombieKid;
    public static Sound zombieCuasy;
    public static Sound zombieMummy;
    public static Sound zombieFrank;

    public static Sound hurt1;
    public static Sound hurt2;
    public static Sound hurt3;
    public static Sound gem;
    public static Sound jump;
    public static Sound shield;
    public static Sound hearth;

    public static void loadStyles(TextureAtlas atlas) {
        // Label Style
        labelStyleChico = new LabelStyle(fontChico, Color.WHITE);
        labelStyleGrande = new LabelStyle(fontGrande, Color.WHITE);
        labelStyleHelpDialog = new LabelStyle(fontChico, Color.BLACK);

        /* Button Buy */
        TextureRegionDrawable btBuy = new TextureRegionDrawable(atlas.findRegion("UI/btBuy"));
        styleTextButtonBuy = new TextButtonStyle(btBuy, null, null, fontChico);

        /* Button Purchased */
        TextureRegionDrawable btPurchased = new TextureRegionDrawable(atlas.findRegion("UI/btPurchased"));
        styleTextButtonPurchased = new TextButtonStyle(btPurchased, null, null, fontChico);

        /* Button Share */
        styleTextButtonShare = new TextButtonStyle(btBuy, null, null, fontChico);

        /* Button Musica */
        TextureRegionDrawable btMusicOn = new TextureRegionDrawable(atlas.findRegion("UI/btMusic"));
        TextureRegionDrawable btMusicOff = new TextureRegionDrawable(atlas.findRegion("UI/btMusicOff"));
        styleButtonMusic = new ButtonStyle(btMusicOn, null, btMusicOff);

        /* Button FacebooLogin */
        NinePatchDrawable btFacebookLogin = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/btFacebookLogin"), 50, 26, 5, 5));
        styleTextButtonFacebook = new TextButtonStyle(btFacebookLogin, null, null, fontChico);

        /* Boton Sonido */
        TextureRegionDrawable botonSonidoOn = new TextureRegionDrawable(atlas.findRegion("UI/btSound"));
        TextureRegionDrawable botonSonidoOff = new TextureRegionDrawable(atlas.findRegion("UI/btSoundOff"));
        styleButtonSound = new ButtonStyle(botonSonidoOn, null, botonSonidoOff);

        // Scrollpane
        TextureRegionDrawable separadorVertical = new TextureRegionDrawable(atlas.findRegion("UI/separadorVertical"));
        TextureRegionDrawable knobScroll = new TextureRegionDrawable(atlas.findRegion("knob"));
        styleScrollPane = new ScrollPaneStyle(null, null, null, separadorVertical, knobScroll);

        // sliderStyle
        TextureRegionDrawable separadorHorizontal = new TextureRegionDrawable(atlas.findRegion("UI/separadorHorizontal"));
        sliderStyle = new SliderStyle(separadorHorizontal, knobScroll);

    }

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontChico = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"));
        fontGrande = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"));

        loadStyles(atlas);

        zombieDashTitulo = new TextureRegionDrawable(atlas.findRegion("UI/titulo"));

        backgroundProgressBar = new TextureRegionDrawable(atlas.findRegion("backgroundProgressBar"));
        backgroundBigWindow = new TextureRegionDrawable(atlas.findRegion("UI/ventanaGrande"));
        backgroundSmallWindow = new TextureRegionDrawable(atlas.findRegion("UI/ventanaChica"));
        storeTableBackground = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/storeTableBackground"), 50, 40, 50, 40));

        helpDialog = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/helpDialog"), 56, 17, 15, 50));
        helpDialogInverted = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/helpDialogInverted"), 56, 17, 50, 15));

        pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0));

        pisoRojo = atlas.findRegion("Plataformas/pisoRojo");
        pisoVerde = atlas.findRegion("Plataformas/pisoVerde");

        containerButtons = new TextureRegionDrawable(atlas.findRegion("UI/containerButtons"));
        btPlay = new TextureRegionDrawable(atlas.findRegion("UI/btPlay"));
        btLeaderboard = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
        btAchievement = new TextureRegionDrawable(atlas.findRegion("UI/btAchievement"));
        btSettings = new TextureRegionDrawable(atlas.findRegion("UI/btSettings"));
        btFacebook = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        btTwitter = new TextureRegionDrawable(atlas.findRegion("UI/btTwitter"));

        btPause = new TextureRegionDrawable(atlas.findRegion("btPause"));
        btFire = new TextureRegionDrawable(atlas.findRegion("btFire"));
        btJump = new TextureRegionDrawable(atlas.findRegion("btUp"));
        btClose = new TextureRegionDrawable(atlas.findRegion("UI/btClose"));
        btMenu = new TextureRegionDrawable(atlas.findRegion("UI/btMenu"));
        btTryAgain = new TextureRegionDrawable(atlas.findRegion("UI/btTryAgain"));
        btShop = new TextureRegionDrawable(atlas.findRegion("UI/btShop"));
        btPlayer = new TextureRegionDrawable(atlas.findRegion("UI/btPlayer"));
        btGems = new TextureRegionDrawable(atlas.findRegion("UI/btGems"));
        btMore = new TextureRegionDrawable(atlas.findRegion("UI/btMore"));
        upgradeOff = new TextureRegionDrawable(atlas.findRegion("UI/upgradeOff"));
        weapon = atlas.findRegion("UI/weapon");

        /**
         * Items
         */
        itemGem = atlas.findRegion("gem");
        itemHeart = atlas.findRegion("heart");
        itemShield = atlas.findRegion("shield");
        itemSkull = atlas.findRegion("skull");
        itemJump = atlas.findRegion("jump");

        crate = atlas.findRegion("crate");
        saw = atlas.findRegion("saw");
        sawDialog = atlas.findRegion("sawDialog");
        spike = atlas.findRegion("spike");
        weaponSmall = atlas.findRegion("weaponSmall");

        redBar = atlas.findRegion("redBar");
        whiteBar = atlas.findRegion("whiteBar");

        /**
         * Hero Swat
         */
        heroSwatRun = loadAnimationRun(atlas, "HeroSwat/");
        heroSwatJump = loadAnimationJump(atlas, "HeroSwat/");
        heroSwatDie = loadAnimationDie(atlas, "HeroSwat/");
        heroSwatHurt = atlas.createSprite("HeroSwat/hurt");

        /**
         * Hero Force
         */
        heroForceRun = loadAnimationRun(atlas, "HeroForce/");
        heroForceJump = loadAnimationJump(atlas, "HeroForce/");
        heroForceDie = loadAnimationDie(atlas, "HeroForce/");
        heroForceHurt = atlas.createSprite("HeroForce/hurt");

        /**
         * Hero Rambo
         */
        heroRamboRun = loadAnimationRun(atlas, "HeroRambo/");
        heroRamboJump = loadAnimationJump(atlas, "HeroRambo/");
        heroRamboDie = loadAnimationDie(atlas, "HeroRambo/");
        heroRamboHurt = atlas.createSprite("HeroRambo/hurt");

        /**
         * Hero Soldier
         */
        heroSoldierRun = loadAnimationRun(atlas, "HeroSoldier/");
        heroSoldierJump = loadAnimationJump(atlas, "HeroSoldier/");
        heroSoldierDie = loadAnimationDie(atlas, "HeroSoldier/");
        heroSoldierHurt = atlas.createSprite("HeroSoldier/hurt");

        /**
         * Hero Vader
         */
        heroVaderRun = loadAnimationRun(atlas, "HeroVader/");
        heroVaderJump = loadAnimationJump(atlas, "HeroVader/");
        heroVaderDie = loadAnimationDie(atlas, "HeroVader/");
        heroVaderHurt = atlas.createSprite("HeroVader/hurt");

        /**
         * Zombie kid
         */
        zombieKidWalk = loadAnimationWalk(atlas, "ZombieKid/");
        zombieKidRise = loadAnimationRise(atlas, "ZombieKid/");
        zombieKidDie = loadAnimationDie(atlas, "ZombieKid/");
        zombieKidHurt = atlas.createSprite("ZombieKid/die1");

        /**
         * Zombie pan
         */
        zombiePanWalk = loadAnimationWalk(atlas, "ZombiePan/");
        zombiePanRise = loadAnimationRise(atlas, "ZombiePan/");
        zombiePanDie = loadAnimationDie(atlas, "ZombiePan/");
        zombiePanHurt = atlas.createSprite("ZombiePan/die1");

        /**
         * Zombie Cuasy
         */
        zombieCuasyWalk = loadAnimationWalk(atlas, "ZombieCuasy/");
        zombieCuasyRise = loadAnimationRise(atlas, "ZombieCuasy/");
        zombieCuasyDie = loadAnimationDie(atlas, "ZombieCuasy/");
        zombieCuasyHurt = atlas.createSprite("ZombieCuasy/die1");

        /**
         * Zombie Frank
         */
        zombieFrankWalk = loadAnimationWalk(atlas, "ZombieFrank/");
        zombieFrankRise = loadAnimationRise(atlas, "ZombieFrank/");
        zombieFrankDie = loadAnimationDie(atlas, "ZombieFrank/");
        zombieFrankHurt = atlas.createSprite("ZombieFrank/die1");

        /**
         * Zombie mummy
         */
        zombieMummyWalk = loadAnimationWalk(atlas, "ZombieMummy/");
        zombieMummyRise = loadAnimationRise(atlas, "ZombieMummy/");
        zombieMummyDie = loadAnimationDie(atlas, "ZombieMummy/");
        zombieMummyHurt = atlas.createSprite("ZombieMummy/die1");

        /**
         * Bullets
         */
        bullet1 = loadAnimationBullet(atlas, "Bullet/bullet1");
        bullet2 = loadAnimationBullet(atlas, "Bullet/bullet2");
        bullet3 = loadAnimationBullet(atlas, "Bullet/bullet3");
        bullet4 = loadAnimationBullet(atlas, "Bullet/bullet4");
        bullet5 = loadAnimationBullet(atlas, "Bullet/bullet5");
        muzzle = loadAnimationMuzzle(atlas, "Bullet/");

        // Parallax stuff
        ParallaxLayer layerFondo = new ParallaxLayer(atlas.findRegion("background"), new Vector2(1.5f, 0), new Vector2(798, 0));
        AtlasRegion fondoFlipped = atlas.findRegion("backgroundFlipped");
        fondoFlipped.flip(true, false);
        ParallaxLayer layerFondo2 = new ParallaxLayer(fondoFlipped, new Vector2(1.5f, 0), new Vector2(799, 0), new Vector2(798, 0));
        ParallaxLayer layerMoon = new ParallaxLayer(atlas.findRegion("moon"), new Vector2(2f, 0), new Vector2(799, 260), new Vector2(2500, 300));

        ParallaxLayer as[] = new ParallaxLayer[]{layerFondo, layerFondo2, layerMoon};
        parallaxBackground = new ParallaxBackground(as, 800, 480f, new Vector2(10, 0));

        Settings.load();

        shoot1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/shoot2.mp3"));
        noBullet = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/noBullet.mp3"));
        zombiePan = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombiePan.mp3"));
        zombieKid = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieKid.mp3"));
        zombieCuasy = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieCuasy.mp3"));
        zombieMummy = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieMummy.mp3"));
        zombieFrank = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieFrank.mp3"));

        hurt1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt.mp3"));
        hurt2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt2.mp3"));
        hurt3 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt3.mp3"));

        gem = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/gem.mp3"));
        jump = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/jump.mp3"));
        shield = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/pick.mp3"));
        hearth = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hearth.mp3"));
    }

    private static AnimationSprite loadAnimationRun(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "run" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .004f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationDie(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "die" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .03f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationJump(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "jump" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .035f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationWalk(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "walk" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .009f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationRise(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "rise" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .00575f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationBullet(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .03f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationMuzzle(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "muzzle" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .009f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    public static void playSound(Sound sonido, float volume) {
        if (Settings.isSoundOn) {
            sonido.play(volume);
        }
    }

}
