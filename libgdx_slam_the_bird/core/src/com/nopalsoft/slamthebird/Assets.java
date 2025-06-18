package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.shop.PlayerSkinsSubMenu;

public class Assets {

    static TextureAtlas textureAtlas;

    public static AtlasRegion title;
    public static AtlasRegion tapToPlay;
    public static AtlasRegion bestScore;
    public static AtlasRegion score;
    public static AtlasRegion combo;
    public static AtlasRegion coinsEarned;
    public static AtlasRegion shop;

    public static NinePatchDrawable horizontalSeparator;
    public static NinePatchDrawable verticalSeparator;

    public static AtlasRegion background;
    public static AtlasRegion gameOverBackground;
    public static AtlasRegion player;

    public static AtlasRegion defaultPlayerSkin;
    public static AtlasRegion redPlayerSkin;
    public static AtlasRegion bluePlayerSkin;

    public static Animation<TextureRegion> playerJumpAnimation;
    public static Animation<TextureRegion> playerSlamAnimation;
    public static Animation<TextureRegion> playerHitAnimation;
    public static Animation<TextureRegion> slamAnimation;

    public static NinePatchDrawable blackPixel;

    public static AtlasRegion platform;
    public static Animation<TextureRegion> platformFireAnimation;
    public static Animation<TextureRegion> platformBreakAnimation;

    public static TextureRegionDrawable buttonAchievements;
    public static TextureRegionDrawable buttonLeaderboard;
    public static TextureRegionDrawable buttonMore;
    public static TextureRegionDrawable buttonRate;
    public static TextureRegionDrawable buttonShop;
    public static TextureRegionDrawable buttonFacebook;
    public static TextureRegionDrawable buttonTwitter;
    public static TextureRegionDrawable buttonBack;
    public static TextureRegionDrawable buttonNoAds;

    public static ButtonStyle buttonStyleMusic;
    public static ButtonStyle buttonStyleSound;

    public static TextureRegionDrawable upgradeOn;
    public static TextureRegionDrawable upgradeOff;

    public static TextureRegionDrawable scoresBackground;

    public static AtlasRegion flapSpawnRegion;
    public static AtlasRegion flapBlueRegion;

    public static Animation<TextureRegion> blueWingsFlapAnimation;
    public static Animation<TextureRegion> redWingsFlapAnimation;
    public static Animation<TextureRegion> evolvingFlapAnimation;
    public static Animation<TextureRegion> coinAnimation;

    public static AtlasRegion coinsRegion;
    public static AtlasRegion transparentPixelRegion;

    public static AtlasRegion invincibilityBoost;
    public static AtlasRegion coinRainBoost;
    public static AtlasRegion freezeBoost;
    public static AtlasRegion superJumpBoost;
    public static AtlasRegion boosts;

    public static Animation<TextureRegion> invincibilityBoostEndAnimation;
    public static Animation<TextureRegion> superJumpBoostEndAnimation;

    public static AtlasRegion largeNum0;
    public static AtlasRegion largeNum1;
    public static AtlasRegion largeNum2;
    public static AtlasRegion largeNum3;
    public static AtlasRegion largeNum4;
    public static AtlasRegion largeNum5;
    public static AtlasRegion largeNum6;
    public static AtlasRegion largeNum7;
    public static AtlasRegion largeNum8;
    public static AtlasRegion largeNum9;

    public static AtlasRegion smallNum0;
    public static AtlasRegion smallNum1;
    public static AtlasRegion smallNum2;
    public static AtlasRegion smallNum3;
    public static AtlasRegion smallNum4;
    public static AtlasRegion smallNum5;
    public static AtlasRegion smallNum6;
    public static AtlasRegion smallNum7;
    public static AtlasRegion smallNum8;
    public static AtlasRegion smallNum9;

    public static BitmapFont font;

    public static TextButtonStyle styleTextButtonBuy;
    public static TextButtonStyle styleTextButtonPurchased;
    public static TextButtonStyle styleTextButtonSelected;

    public static ScrollPaneStyle styleScrollPane;
    public static LabelStyle smallLabelStyle;

    public static Sound soundJump;
    public static Sound soundCoin;
    public static Sound soundBoost;

    static Music music;

    public static void loadStyles() {
        font = new BitmapFont();
        font.getData().setScale(1.15f);

        horizontalSeparator = new NinePatchDrawable(new NinePatch(textureAtlas.findRegion("Shop/separadorHorizontal"), 0, 1, 0, 0));
        verticalSeparator = new NinePatchDrawable(new NinePatch(textureAtlas.findRegion("Shop/separadorVertical"), 0, 1, 0, 0));

        smallLabelStyle = new LabelStyle(font, Color.WHITE);


        TextureRegionDrawable buttonBuy = new TextureRegionDrawable(textureAtlas.findRegion("Shop/btBuy"));
        styleTextButtonBuy = new TextButtonStyle(buttonBuy, null, null, font);


        TextureRegionDrawable buttonPurchase = new TextureRegionDrawable(textureAtlas.findRegion("Shop/btPurchased"));
        styleTextButtonPurchased = new TextButtonStyle(buttonPurchase, null, null, font);


        TextureRegionDrawable buttonSelected = new TextureRegionDrawable(textureAtlas.findRegion("Shop/btSelected"));
        styleTextButtonSelected = new TextButtonStyle(buttonSelected, null, null, font);


        TextureRegionDrawable buttonMusicOn = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btMusica"));
        TextureRegionDrawable buttonMusicOff = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btSinMusica"));
        buttonStyleMusic = new ButtonStyle(buttonMusicOn, null, buttonMusicOff);


        TextureRegionDrawable buttonSoundOn = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btSonido"));
        TextureRegionDrawable buttonSoundOff = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btSinSonido"));
        buttonStyleSound = new ButtonStyle(buttonSoundOn, null, buttonSoundOff);

        styleScrollPane = new ScrollPaneStyle(null, null, null, null, verticalSeparator);
    }

    public static void drawPlayer() {

        String selectedPlayer = "AndroidBot";

        if (Settings.selectedSkin == PlayerSkinsSubMenu.SKIN_RED_ANDROID) {
            selectedPlayer = "AndroidBotRojo";
        } else if (Settings.selectedSkin == PlayerSkinsSubMenu.SKIN_BLUE_ANDROID) {
            selectedPlayer = "AndroidBotAzul";
        }

        player = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeFall");

        AtlasRegion per1 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeSlam1");
        AtlasRegion per2 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeSlam2");
        AtlasRegion per3 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeSlam3");
        AtlasRegion per4 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeSlam4");
        playerSlamAnimation = new Animation<>(.05f, per1, per2, per3, per4);

        per1 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeJump1");
        per2 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeJump1");
        per3 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeJump1");
        playerJumpAnimation = new Animation<>(.1f, per1, per2, per3);

        per1 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeHit");
        per2 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeHit");
        per3 = textureAtlas.findRegion("Personajes/" + selectedPlayer + "/personajeHit");
        playerHitAnimation = new Animation<>(.1f, per1, per2, per3);

        // These are the ones that appear in the store;
        defaultPlayerSkin = textureAtlas.findRegion("Personajes/AndroidBot/personajeFall");
        redPlayerSkin = textureAtlas.findRegion("Personajes/AndroidBotRojo/personajeFall");
        bluePlayerSkin = textureAtlas.findRegion("Personajes/AndroidBotAzul/personajeFall");
    }

    public static void load() {

        textureAtlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        loadStyles();

        title = textureAtlas.findRegion("MenuPrincipal/titulo");
        tapToPlay = textureAtlas.findRegion("MenuPrincipal/tapToPlay");
        bestScore = textureAtlas.findRegion("MenuPrincipal/bestScore");
        score = textureAtlas.findRegion("MenuPrincipal/score");
        combo = textureAtlas.findRegion("MenuPrincipal/combo");
        coinsEarned = textureAtlas.findRegion("MenuPrincipal/coinsEarned");

        background = textureAtlas.findRegion("fondo");
        scoresBackground = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/fondoPuntuaciones"));
        gameOverBackground = textureAtlas.findRegion("fondoGameover");

        transparentPixelRegion = textureAtlas.findRegion("pixelTransparente");

        shop = textureAtlas.findRegion("Shop/Shop");

        buttonBack = new TextureRegionDrawable(textureAtlas.findRegion("Shop/btAtras"));
        buttonNoAds = new TextureRegionDrawable(textureAtlas.findRegion("Shop/btNoAds"));

        upgradeOff = new TextureRegionDrawable(textureAtlas.findRegion("Shop/upgradeOff"));
        upgradeOn = new TextureRegionDrawable(textureAtlas.findRegion("Shop/upgradeOn"));

        blackPixel = new NinePatchDrawable(new NinePatch(textureAtlas.findRegion("MenuPrincipal/pixelNegro"), 1, 1, 0, 0));

        AtlasRegion per1 = textureAtlas.findRegion("moneda1");
        AtlasRegion per2 = textureAtlas.findRegion("moneda2");
        AtlasRegion per3 = textureAtlas.findRegion("moneda3");
        coinsRegion = per1;
        coinAnimation = new Animation<>(.15f, per1, per2, per3, per2);

        flapBlueRegion = textureAtlas.findRegion("InGame/flapAzul");
        flapSpawnRegion = textureAtlas.findRegion("InGame/flapSpawn");

        AtlasRegion flap1 = textureAtlas.findRegion("InGame/flapAzulAlas1");
        AtlasRegion flap2 = textureAtlas.findRegion("InGame/flapAzulAlas2");
        AtlasRegion flap3 = textureAtlas.findRegion("InGame/flapAzulAlas3");
        blueWingsFlapAnimation = new Animation<>(.15f, flap1, flap2, flap3, flap2);

        flap1 = textureAtlas.findRegion("InGame/flapRojoAlas1");
        flap2 = textureAtlas.findRegion("InGame/flapRojoAlas2");
        flap3 = textureAtlas.findRegion("InGame/flapRojoAlas3");
        redWingsFlapAnimation = new Animation<>(.15f, flap1, flap2, flap3, flap2);
        evolvingFlapAnimation = new Animation<>(.075f, flapBlueRegion, flap1, flapBlueRegion, flap2, flapBlueRegion, flap3);

        flap1 = textureAtlas.findRegion("InGame/plataformFire1");
        flap2 = textureAtlas.findRegion("InGame/plataformFire2");
        flap3 = textureAtlas.findRegion("InGame/plataformFire3");
        platformFireAnimation = new Animation<>(.15f, flap1, flap2, flap3, flap2);

        flap1 = textureAtlas.findRegion("InGame/plataforma2");
        flap2 = textureAtlas.findRegion("InGame/plataforma3");
        flap3 = textureAtlas.findRegion("InGame/plataforma4");
        platformBreakAnimation = new Animation<>(.1f, flap1, flap2, flap3);
        platform = textureAtlas.findRegion("InGame/plataforma1");

        flap1 = textureAtlas.findRegion("InGame/slam1");
        flap2 = textureAtlas.findRegion("InGame/slam2");
        flap3 = textureAtlas.findRegion("InGame/slam3");
        slamAnimation = new Animation<>(.1f, flap1, flap2, flap3);

        invincibilityBoost = textureAtlas.findRegion("InGame/boostInvencible");
        coinRainBoost = textureAtlas.findRegion("InGame/boostCoinRain");
        freezeBoost = textureAtlas.findRegion("InGame/boostIce");
        superJumpBoost = textureAtlas.findRegion("InGame/boostSuperSalto");
        boosts = textureAtlas.findRegion("InGame/boosts");

        invincibilityBoostEndAnimation = new Animation<>(.15f, invincibilityBoost, transparentPixelRegion);
        superJumpBoostEndAnimation = new Animation<>(.15f, superJumpBoost, transparentPixelRegion);

        buttonAchievements = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btAchievements"));
        buttonLeaderboard = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btLeaderboard"));
        buttonMore = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btMore"));
        buttonRate = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btRate"));
        buttonShop = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btShop"));
        buttonFacebook = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btFacebook"));
        buttonTwitter = new TextureRegionDrawable(textureAtlas.findRegion("MenuPrincipal/btTwitter"));

        largeNum0 = textureAtlas.findRegion("Numeros/num0");
        largeNum1 = textureAtlas.findRegion("Numeros/num1");
        largeNum2 = textureAtlas.findRegion("Numeros/num2");
        largeNum3 = textureAtlas.findRegion("Numeros/num3");
        largeNum4 = textureAtlas.findRegion("Numeros/num4");
        largeNum5 = textureAtlas.findRegion("Numeros/num5");
        largeNum6 = textureAtlas.findRegion("Numeros/num6");
        largeNum7 = textureAtlas.findRegion("Numeros/num7");
        largeNum8 = textureAtlas.findRegion("Numeros/num8");
        largeNum9 = textureAtlas.findRegion("Numeros/num9");

        smallNum0 = textureAtlas.findRegion("Numeros/0");
        smallNum1 = textureAtlas.findRegion("Numeros/1");
        smallNum2 = textureAtlas.findRegion("Numeros/2");
        smallNum3 = textureAtlas.findRegion("Numeros/3");
        smallNum4 = textureAtlas.findRegion("Numeros/4");
        smallNum5 = textureAtlas.findRegion("Numeros/5");
        smallNum6 = textureAtlas.findRegion("Numeros/6");
        smallNum7 = textureAtlas.findRegion("Numeros/7");
        smallNum8 = textureAtlas.findRegion("Numeros/8");
        smallNum9 = textureAtlas.findRegion("Numeros/9");

        Settings.load();

        // Must be called after loading settings
        drawPlayer();

        soundCoin = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/pickCoin.mp3"));
        soundJump = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/salto.mp3"));
        soundBoost = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/pickBoost.mp3"));

        music = Gdx.audio.newMusic(Gdx.files
                .internal("data/Sounds/musica.mp3"));

        music.setLooping(true);

        if (Settings.isMusicOn)
            playMusic();
    }

    public static void playSound(Sound sound) {
        if (Settings.isSoundOn)
            sound.play(1);
    }

    public static void playMusic() {
        music.play();
    }

    public static void pauseMusic() {
        music.pause();
    }
}
