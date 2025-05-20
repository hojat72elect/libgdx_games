package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.shop.PersonajesSubMenu;

public class Assets {

    static TextureAtlas atlas;

    public static AtlasRegion titulo;
    public static AtlasRegion tapToPlay;
    public static AtlasRegion bestScore;
    public static AtlasRegion score;
    public static AtlasRegion combo;
    public static AtlasRegion coinsEarned;
    public static AtlasRegion shop;
    public static NinePatchDrawable separadorHorizontal;
    public static NinePatchDrawable separadorVertical;

    public static AtlasRegion fondo;
    public static AtlasRegion fondoGameover;
    public static AtlasRegion personaje;

    public static AtlasRegion personajeShopDefault;
    public static AtlasRegion personajeShopRojo;
    public static AtlasRegion personajeShopAzul;

    public static Animation<TextureRegion> animPersonajeFall;
    public static Animation<TextureRegion> animPersonajeJump;
    public static Animation<TextureRegion> animPersonajeSlam;
    public static Animation<TextureRegion> animPersonajeHit;
    public static Animation<TextureRegion> slam;

    public static NinePatchDrawable pixelNegro;

    public static AtlasRegion plataforma;
    public static Animation<TextureRegion> animPlataformFire;
    public static Animation<TextureRegion> plataformBreakable;

    public static TextureRegionDrawable btAchievements;
    public static TextureRegionDrawable btLeaderboard;
    public static TextureRegionDrawable btMore;
    public static TextureRegionDrawable btRate;
    public static TextureRegionDrawable btShop;
    public static TextureRegionDrawable btFacebook;
    public static TextureRegionDrawable btTwitter;
    public static TextureRegionDrawable btAtras;
    public static TextureRegionDrawable btNoAds;
    public static ButtonStyle styleButtonMusica;
    public static ButtonStyle styleButtonSonido;

    public static TextureRegionDrawable upgradeOn;
    public static TextureRegionDrawable upgradeOff;

    public static TextureRegionDrawable fondoPuntuaciones;

    public static AtlasRegion flapSpawn;
    public static AtlasRegion flapAzul;
    public static Animation<TextureRegion> animflapAlasAzul;
    public static Animation<TextureRegion> animflapAlasRojo;
    public static Animation<TextureRegion> animEvolving;

    public static Animation<TextureRegion> animMoneda;
    public static AtlasRegion moneda;
    public static AtlasRegion pixelTransparente;

    public static AtlasRegion boostInvencible;
    public static AtlasRegion boostCoinRain;
    public static AtlasRegion boostIce;
    public static AtlasRegion boostSuperSalto;
    public static AtlasRegion boosts;

    public static Animation<TextureRegion> animBoostEndInvencible;
    public static Animation<TextureRegion> animBoostEndSuperSalto;

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

    public static BitmapFont font;

    public static TextButtonStyle styleTextButtonBuy;
    public static TextButtonStyle styleTextButtonPurchased;
    public static TextButtonStyle styleTextButtonSelected;
    public static ScrollPaneStyle styleScrollPane;
    public static LabelStyle styleLabelChico;

    public static Sound soundJump;
    public static Sound soundCoin;
    public static Sound soundBoost;

    static Music musica;

    public static void loadEstilos() {
        font = new BitmapFont();
        font.getData().setScale(1.15f);

        separadorHorizontal = new NinePatchDrawable(new NinePatch(
                atlas.findRegion("Shop/separadorHorizontal"), 0, 1, 0, 0));
        separadorVertical = new NinePatchDrawable(new NinePatch(
                atlas.findRegion("Shop/separadorVertical"), 0, 1, 0, 0));

        styleLabelChico = new LabelStyle(font, Color.WHITE);

        /* Button Buy */
        TextureRegionDrawable btBuy = new TextureRegionDrawable(
                atlas.findRegion("Shop/btBuy"));
        styleTextButtonBuy = new TextButtonStyle(btBuy, null, null, font);

        /* Button Purchased */
        TextureRegionDrawable btPurchased = new TextureRegionDrawable(
                atlas.findRegion("Shop/btPurchased"));
        styleTextButtonPurchased = new TextButtonStyle(btPurchased, null, null,
                font);

        /* Button Selected */
        TextureRegionDrawable btSelected = new TextureRegionDrawable(
                atlas.findRegion("Shop/btSelected"));
        styleTextButtonSelected = new TextButtonStyle(btSelected, null, null,
                font);

        /* Button Musica */
        TextureRegionDrawable btMusicOn = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btMusica"));
        TextureRegionDrawable btMusicOff = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btSinMusica"));
        styleButtonMusica = new ButtonStyle(btMusicOn, null, btMusicOff);

        /* Boton Sonido */
        TextureRegionDrawable botonSonidoOn = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btSonido"));
        TextureRegionDrawable botonSonidoOff = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btSinSonido"));
        styleButtonSonido = new ButtonStyle(botonSonidoOn, null, botonSonidoOff);

        styleScrollPane = new ScrollPaneStyle(null, null, null, null,
                separadorVertical);
    }

    public static void cargarPersonaje() {

        String perSeleccionado = "AndroidBot";

        if (Settings.skinSeleccionada == PersonajesSubMenu.SKIN_ANDROID_ROJO) {
            perSeleccionado = "AndroidBotRojo";
        } else if (Settings.skinSeleccionada == PersonajesSubMenu.SKIN_ANDROID_AZUL) {
            perSeleccionado = "AndroidBotAzul";
        }

        personaje = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeFall");

        AtlasRegion per1 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeSlam1");
        AtlasRegion per2 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeSlam2");
        AtlasRegion per3 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeSlam3");
        AtlasRegion per4 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeSlam4");
        animPersonajeSlam = new Animation<TextureRegion>(.05f, per1, per2, per3, per4);

        per1 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeJump1");
        per2 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeJump1");
        per3 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeJump1");
        animPersonajeJump = new Animation<TextureRegion>(.1f, per1, per2, per3);

        per1 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeHit");
        per2 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeHit");
        per3 = atlas.findRegion("Personajes/" + perSeleccionado
                + "/personajeHit");
        animPersonajeHit = new Animation<TextureRegion>(.1f, per1, per2, per3);

        // Estos son los que aparecen en la tienda;
        personajeShopDefault = atlas
                .findRegion("Personajes/AndroidBot/personajeFall");
        personajeShopRojo = atlas
                .findRegion("Personajes/AndroidBotRojo/personajeFall");
        personajeShopAzul = atlas
                .findRegion("Personajes/AndroidBotAzul/personajeFall");
    }

    public static void load() {

        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        loadEstilos();

        titulo = atlas.findRegion("MenuPrincipal/titulo");
        tapToPlay = atlas.findRegion("MenuPrincipal/tapToPlay");
        bestScore = atlas.findRegion("MenuPrincipal/bestScore");
        score = atlas.findRegion("MenuPrincipal/score");
        combo = atlas.findRegion("MenuPrincipal/combo");
        coinsEarned = atlas.findRegion("MenuPrincipal/coinsEarned");

        fondo = atlas.findRegion("fondo");
        fondoPuntuaciones = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/fondoPuntuaciones"));
        fondoGameover = atlas.findRegion("fondoGameover");

        pixelTransparente = atlas.findRegion("pixelTransparente");

        shop = atlas.findRegion("Shop/Shop");

        btAtras = new TextureRegionDrawable(atlas.findRegion("Shop/btAtras"));
        btNoAds = new TextureRegionDrawable(atlas.findRegion("Shop/btNoAds"));

        upgradeOff = new TextureRegionDrawable(
                atlas.findRegion("Shop/upgradeOff"));
        upgradeOn = new TextureRegionDrawable(
                atlas.findRegion("Shop/upgradeOn"));

        pixelNegro = new NinePatchDrawable(new NinePatch(
                atlas.findRegion("MenuPrincipal/pixelNegro"), 1, 1, 0, 0));

        AtlasRegion per1 = atlas.findRegion("moneda1");
        AtlasRegion per2 = atlas.findRegion("moneda2");
        AtlasRegion per3 = atlas.findRegion("moneda3");
        moneda = per1;
        animMoneda = new Animation<TextureRegion>(.15f, per1, per2, per3, per2);

        flapAzul = atlas.findRegion("InGame/flapAzul");
        flapSpawn = atlas.findRegion("InGame/flapSpawn");

        AtlasRegion flap1 = atlas.findRegion("InGame/flapAzulAlas1");
        AtlasRegion flap2 = atlas.findRegion("InGame/flapAzulAlas2");
        AtlasRegion flap3 = atlas.findRegion("InGame/flapAzulAlas3");
        animflapAlasAzul = new Animation<TextureRegion>(.15f, flap1, flap2, flap3, flap2);

        flap1 = atlas.findRegion("InGame/flapRojoAlas1");
        flap2 = atlas.findRegion("InGame/flapRojoAlas2");
        flap3 = atlas.findRegion("InGame/flapRojoAlas3");
        animflapAlasRojo = new Animation<TextureRegion>(.15f, flap1, flap2, flap3, flap2);
        animEvolving = new Animation<TextureRegion>(.075f, flapAzul, flap1, flapAzul, flap2,
                flapAzul, flap3);

        flap1 = atlas.findRegion("InGame/plataformFire1");
        flap2 = atlas.findRegion("InGame/plataformFire2");
        flap3 = atlas.findRegion("InGame/plataformFire3");
        animPlataformFire = new Animation<TextureRegion>(.15f, flap1, flap2, flap3, flap2);

        flap1 = atlas.findRegion("InGame/plataforma2");
        flap2 = atlas.findRegion("InGame/plataforma3");
        flap3 = atlas.findRegion("InGame/plataforma4");
        plataformBreakable = new Animation<TextureRegion>(.1f, flap1, flap2, flap3);
        plataforma = atlas.findRegion("InGame/plataforma1");

        flap1 = atlas.findRegion("InGame/slam1");
        flap2 = atlas.findRegion("InGame/slam2");
        flap3 = atlas.findRegion("InGame/slam3");
        slam = new Animation<TextureRegion>(.1f, flap1, flap2, flap3);

        boostInvencible = atlas.findRegion("InGame/boostInvencible");
        boostCoinRain = atlas.findRegion("InGame/boostCoinRain");
        boostIce = atlas.findRegion("InGame/boostIce");
        boostSuperSalto = atlas.findRegion("InGame/boostSuperSalto");
        boosts = atlas.findRegion("InGame/boosts");

        animBoostEndInvencible = new Animation<TextureRegion>(.15f, boostInvencible,
                pixelTransparente);
        animBoostEndSuperSalto = new Animation<TextureRegion>(.15f, boostSuperSalto,
                pixelTransparente);

        btAchievements = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btAchievements"));
        btLeaderboard = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btLeaderboard"));
        btMore = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btMore"));
        btRate = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btRate"));
        btShop = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btShop"));
        btFacebook = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btFacebook"));
        btTwitter = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btTwitter"));

        num0Grande = atlas.findRegion("Numeros/num0");
        num1Grande = atlas.findRegion("Numeros/num1");
        num2Grande = atlas.findRegion("Numeros/num2");
        num3Grande = atlas.findRegion("Numeros/num3");
        num4Grande = atlas.findRegion("Numeros/num4");
        num5Grande = atlas.findRegion("Numeros/num5");
        num6Grande = atlas.findRegion("Numeros/num6");
        num7Grande = atlas.findRegion("Numeros/num7");
        num8Grande = atlas.findRegion("Numeros/num8");
        num9Grande = atlas.findRegion("Numeros/num9");

        num0Chico = atlas.findRegion("Numeros/0");
        num1Chico = atlas.findRegion("Numeros/1");
        num2Chico = atlas.findRegion("Numeros/2");
        num3Chico = atlas.findRegion("Numeros/3");
        num4Chico = atlas.findRegion("Numeros/4");
        num5Chico = atlas.findRegion("Numeros/5");
        num6Chico = atlas.findRegion("Numeros/6");
        num7Chico = atlas.findRegion("Numeros/7");
        num8Chico = atlas.findRegion("Numeros/8");
        num9Chico = atlas.findRegion("Numeros/9");

        Settings.load();

        // Se debe llamar despues de cargar settings
        cargarPersonaje();

        soundCoin = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/pickCoin.mp3"));
        soundJump = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/salto.mp3"));
        soundBoost = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/pickBoost.mp3"));

        musica = Gdx.audio.newMusic(Gdx.files
                .internal("data/Sounds/musica.mp3"));

        musica.setLooping(true);

        if (Settings.isMusicOn)
            playMusic();

    }

    public static void playSound(Sound sound) {
        if (Settings.isSoundOn)
            sound.play(1);
    }

    public static void playMusic() {
        musica.play();
    }

    public static void pauseMusic() {
        musica.pause();
    }
}
