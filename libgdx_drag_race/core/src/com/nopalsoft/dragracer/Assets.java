package com.nopalsoft.dragracer;

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

public class Assets {

    public static BitmapFont fontGrande;
    public static BitmapFont fontChico;

    public static Animation<TextureRegion> newExplosion;

    public static NinePatchDrawable blackPixel;
    public static TextureRegionDrawable scoresBackgroundDrawable;

    public static NinePatchDrawable horizontalSeparatorDrawable;
    public static NinePatchDrawable verticalSeparatorDrawable;

    public static AtlasRegion street;
    public static AtlasRegion coin;
    public static AtlasRegion coinFront;

    public static TextureRegionDrawable titleDrawable;

    public static TextureRegionDrawable buttonBack;
    public static TextureRegionDrawable buttonNoAds;
    public static TextureRegionDrawable buttonFacebook;
    public static TextureRegionDrawable buttonTwitter;

    public static TextureRegionDrawable upgradeOn;
    public static TextureRegionDrawable upgradeOff;

    public static TextureRegionDrawable swipeHand;
    public static TextureRegionDrawable swipeHandDown;
    public static TextureRegionDrawable swipeArrows;

    // Cars
    public static AtlasRegion carDiablo;
    public static AtlasRegion carBanshee;
    public static AtlasRegion carTurismo;
    public static AtlasRegion carCamaro;
    public static AtlasRegion carTornado;
    public static AtlasRegion carAudiS5;
    public static AtlasRegion carBmwX6;
    public static AtlasRegion carChevroletCrossfire;
    public static AtlasRegion carCitroenC4;
    public static AtlasRegion carDodgeCharger;
    public static AtlasRegion carFiat500Lounge;
    public static AtlasRegion carHondaCRV;
    public static AtlasRegion carMazda6;
    public static AtlasRegion carMazdaRx8;
    public static AtlasRegion carSeatIbiza;
    public static AtlasRegion carVolkswagenScirocco;

    // Styles
    public static LabelStyle labelStyleLarge;
    public static LabelStyle labelStyleSmall;

    public static ScrollPaneStyle styleScrollPane;

    public static TextButtonStyle styleTextButtonBuy;
    public static TextButtonStyle styleTextButtonPurchased;
    public static TextButtonStyle styleTextButtonSelected;

    public static ButtonStyle styleButtonMusic;

    static TextureAtlas atlas;
    public static AtlasRegion redMarkerBar;
    public static AtlasRegion greenMarkerBar;

    public static Sound soundTurn1;
    public static Sound soundTurn2;
    public static Sound soundCrash;
    public static Music music;

    private static void loadFont() {
        fontGrande = new BitmapFont(Gdx.files.internal("data/font.fnt"),
                atlas.findRegion("font"));

        fontChico = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"),
                atlas.findRegion("fontChico"));
    }

    private static void loadStyles() {
        labelStyleLarge = new LabelStyle(fontGrande, Color.WHITE);
        labelStyleSmall = new LabelStyle(fontChico, Color.WHITE);

        horizontalSeparatorDrawable = new NinePatchDrawable(new NinePatch(
                atlas.findRegion("Shop/separadorHorizontal"), 0, 1, 0, 0));
        verticalSeparatorDrawable = new NinePatchDrawable(new NinePatch(
                atlas.findRegion("Shop/separadorVertical"), 0, 1, 0, 0));

        // Button Buy
        TextureRegionDrawable btBuy = new TextureRegionDrawable(
                atlas.findRegion("Shop/btBuy"));
        styleTextButtonBuy = new TextButtonStyle(btBuy, null, null, fontChico);

        // Button Purchased
        TextureRegionDrawable btPurchased = new TextureRegionDrawable(
                atlas.findRegion("Shop/btPurchased"));
        styleTextButtonPurchased = new TextButtonStyle(btPurchased, null, null,
                fontChico);

        // Button Selected
        TextureRegionDrawable btSelected = new TextureRegionDrawable(
                atlas.findRegion("Shop/btSelected"));
        styleTextButtonSelected = new TextButtonStyle(btSelected, null, null,
                fontChico);

        styleScrollPane = new ScrollPaneStyle(null, null, null, null,
                verticalSeparatorDrawable);

        // Button Music
        TextureRegionDrawable btMusicOn = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btMusica"));
        TextureRegionDrawable btMusicOff = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btSinMusica"));
        styleButtonMusic = new ButtonStyle(btMusicOn, null, btMusicOff);
    }

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));
        loadFont();
        loadStyles();

        titleDrawable = new TextureRegionDrawable(atlas.findRegion("titulo2"));

        blackPixel = new NinePatchDrawable(new NinePatch(
                atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
        scoresBackgroundDrawable = new TextureRegionDrawable(
                atlas.findRegion("fondoPuntuaciones"));

        coin = atlas.findRegion("coin");
        coinFront = atlas.findRegion("coinFrente");

        redMarkerBar = atlas.findRegion("barraMarcadorRojo");
        greenMarkerBar = atlas.findRegion("barraMarcadorVerde");

        street = atlas.findRegion("calle");
        carDiablo = atlas.findRegion("Carros/diablo");
        carBanshee = atlas.findRegion("Carros/banshee");
        carTurismo = atlas.findRegion("Carros/turismo");
        carCamaro = atlas.findRegion("Carros/bullet");
        carTornado = atlas.findRegion("Carros/tornado");

        carAudiS5 = atlas.findRegion("Carros/Audi S5");
        carBmwX6 = atlas.findRegion("Carros/BMW X6");
        carChevroletCrossfire = atlas.findRegion("Carros/Chevrolet Crossfire");
        carCitroenC4 = atlas.findRegion("Carros/Citroen C4");
        carDodgeCharger = atlas.findRegion("Carros/Dodge Charger");
        carFiat500Lounge = atlas.findRegion("Carros/Fiat 500 Lounge");
        carHondaCRV = atlas.findRegion("Carros/Honda CRV");
        carMazda6 = atlas.findRegion("Carros/Mazda 6");
        carMazdaRx8 = atlas.findRegion("Carros/Mazda RX8");
        carSeatIbiza = atlas.findRegion("Carros/Seat Ibiza");
        carVolkswagenScirocco = atlas.findRegion("Carros/Volkswagen Scirocco");

        AtlasRegion newExpl1 = atlas.findRegion("Animaciones/newExplosion1");
        AtlasRegion newExpl2 = atlas.findRegion("Animaciones/newExplosion2");
        AtlasRegion newExpl3 = atlas.findRegion("Animaciones/newExplosion3");
        AtlasRegion newExpl4 = atlas.findRegion("Animaciones/newExplosion4");
        AtlasRegion newExpl5 = atlas.findRegion("Animaciones/newExplosion5");
        AtlasRegion newExpl6 = atlas.findRegion("Animaciones/newExplosion6");
        AtlasRegion newExpl7 = atlas.findRegion("Animaciones/newExplosion7");
        AtlasRegion newExpl8 = atlas.findRegion("Animaciones/newExplosion8");
        AtlasRegion newExpl9 = atlas.findRegion("Animaciones/newExplosion9");
        AtlasRegion newExpl10 = atlas.findRegion("Animaciones/newExplosion10");
        AtlasRegion newExpl11 = atlas.findRegion("Animaciones/newExplosion11");
        AtlasRegion newExpl12 = atlas.findRegion("Animaciones/newExplosion12");
        AtlasRegion newExpl13 = atlas.findRegion("Animaciones/newExplosion13");
        AtlasRegion newExpl14 = atlas.findRegion("Animaciones/newExplosion14");
        AtlasRegion newExpl15 = atlas.findRegion("Animaciones/newExplosion15");
        AtlasRegion newExpl16 = atlas.findRegion("Animaciones/newExplosion16");
        AtlasRegion newExpl17 = atlas.findRegion("Animaciones/newExplosion17");
        AtlasRegion newExpl18 = atlas.findRegion("Animaciones/newExplosion18");
        AtlasRegion newExpl19 = atlas.findRegion("Animaciones/newExplosion19");
        newExplosion = new Animation(0.05f, newExpl1, newExpl2, newExpl3,
                newExpl4, newExpl5, newExpl6, newExpl7, newExpl8, newExpl9,
                newExpl10, newExpl11, newExpl12, newExpl13, newExpl14,
                newExpl15, newExpl16, newExpl17, newExpl18, newExpl19);

        buttonBack = new TextureRegionDrawable(atlas.findRegion("Shop/btAtras2"));
        buttonNoAds = new TextureRegionDrawable(atlas.findRegion("Shop/btNoAds"));
        upgradeOff = new TextureRegionDrawable(
                atlas.findRegion("Shop/upgradeOff"));
        upgradeOn = new TextureRegionDrawable(
                atlas.findRegion("Shop/upgradeOn"));

        buttonFacebook = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btFacebook"));
        buttonTwitter = new TextureRegionDrawable(
                atlas.findRegion("MenuPrincipal/btTwitter"));

        swipeHand = new TextureRegionDrawable(atlas.findRegion("swipeHand"));
        swipeHandDown = new TextureRegionDrawable(
                atlas.findRegion("swipeHandDown"));
        swipeArrows = new TextureRegionDrawable(atlas.findRegion("swipeArrows"));

        soundTurn1 = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/turn1.mp3"));
        soundTurn2 = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/turn2.mp3"));
        soundCrash = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/crash.mp3"));

        music = Gdx.audio.newMusic(Gdx.files
                .internal("data/Sounds/DST-BreakOut.mp3"));
        music.setLooping(true);

        Settings.load();

        if (Settings.isMusicOn)
            music.play();
    }

    public static void playSound(Sound son) {
        if (Settings.isMusicOn) {
            son.play(1);
        }
    }
}
