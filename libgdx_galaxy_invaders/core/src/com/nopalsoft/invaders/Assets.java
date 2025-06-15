package com.nopalsoft.invaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.invaders.parallax.ParallaxBackground;
import com.nopalsoft.invaders.parallax.ParallaxLayer;

public class Assets {

    public static I18NBundle languagesBundle;
    private static final GlyphLayout glyphLayout = new GlyphLayout();

    public static AtlasRegion backgroundAtlasRegion;
    public static ParallaxBackground backgroundLayer;

    public static AtlasRegion spaceShipLeft;
    public static AtlasRegion spaceShipRight;
    public static AtlasRegion spaceShip;

    // Fonts
    public static BitmapFont font60;// Mainly for the title of the app
    public static BitmapFont font45;
    public static BitmapFont font15;
    public static BitmapFont font10;

    // Menu
    public static AtlasRegion leftMenuEllipse;
    public static NinePatchDrawable buttonSignInUp;
    public static NinePatchDrawable buttonSignInDown;
    public static NinePatchDrawable titleMenuBox;

    // Game
    public static NinePatchDrawable inGameStatusDrawable;
    public static TextureRegionDrawable buttonLeft;
    public static TextureRegionDrawable buttonRight;
    public static TextureRegionDrawable buttonFire;
    public static TextureRegionDrawable buttonFirePressed;
    public static TextureRegionDrawable buttonMissile;
    public static TextureRegionDrawable buttonMissilePressed;

    // Aid
    public static AtlasRegion help1;
    public static AtlasRegion helpClick;

    // Buttons
    public static TextureRegionDrawable buttonSoundOn;
    public static TextureRegionDrawable buttonSoundOff;
    public static TextureRegionDrawable buttonMusicOn;
    public static TextureRegionDrawable buttonMusicOff;

    // Ammunition
    public static AtlasRegion normalBullet;
    public static Animation<TextureRegion> missileAnimation;
    public static Animation<TextureRegion> superBombAnimation;
    public static AtlasRegion bulletLevel1;
    public static AtlasRegion bulletLevel2;
    public static AtlasRegion bulletLevel3;
    public static AtlasRegion bulletLevel4;

    public static AtlasRegion boost1;
    public static AtlasRegion boost2;
    public static AtlasRegion boost3;
    public static AtlasRegion upgLife;

    public static Animation<TextureRegion> explosionAnimation;
    public static Animation<TextureRegion> shieldAnimation;

    public static AtlasRegion normalEnemyBullet;
    public static AtlasRegion alien1;
    public static AtlasRegion alien2;
    public static AtlasRegion alien3;
    public static AtlasRegion alien4;

    // Sounds
    public static Music music;
    public static Sound coinSound;
    public static Sound clickSound;
    public static Sound explosionSound;
    public static Sound missileFiringSound;

    // Styles
    public static TextButtonStyle styleTextButtonMenu;
    public static TextButtonStyle styleTextButtonFacebook;
    public static TextButtonStyle styleTextButtonBack;
    public static TextButtonStyle styleTextButton;

    public static WindowStyle styleDialogPause;

    public static LabelStyle styleLabel;

    public static LabelStyle styleLabelDialog;
    public static SliderStyle styleSlider;

    public static ImageButtonStyle styleImageButtonPause;
    public static ImageButtonStyle styleImageButtonStyleCheckBox;


    static private void loadFont(TextureAtlas atlas) {
        font60 = new BitmapFont(Gdx.files.internal("data/font35.fnt"), atlas.findRegion("font35"), false);
        font45 = new BitmapFont(Gdx.files.internal("data/font35.fnt"), atlas.findRegion("font35"), false);
        font15 = new BitmapFont(Gdx.files.internal("data/font15.fnt"), atlas.findRegion("font15"), false);
        font10 = new BitmapFont(Gdx.files.internal("data/font15.fnt"), atlas.findRegion("font15"), false);

        font60.setColor(Color.GREEN);
        font45.setColor(Color.GREEN);
        font15.setColor(Color.GREEN);
        font10.setColor(Color.GREEN);
    }

    static private void loadSceneStyles(TextureAtlas atlas) {

        // Dialog
        NinePatchDrawable loginDialogDrawable = new NinePatchDrawable(atlas.createPatch("recuadroLogIn"));
        AtlasRegion dialogDim = atlas.findRegion("fondoNegro");
        styleDialogPause = new WindowStyle(font45, Color.GREEN, loginDialogDrawable);
        styleDialogPause.stageBackground = new NinePatchDrawable(new NinePatch(dialogDim));
        styleLabelDialog = new LabelStyle(font15, Color.GREEN);

        NinePatchDrawable defaultRoundDown = new NinePatchDrawable(atlas.createPatch("botonDown"));
        NinePatchDrawable defaultRound = new NinePatchDrawable(atlas.createPatch("boton"));
        styleTextButton = new TextButtonStyle(defaultRound, defaultRoundDown, null, font15);
        styleTextButton.fontColor = Color.GREEN;

        // Menu
        NinePatchDrawable menuButtonDrawable = new NinePatchDrawable(atlas.createPatch("botonMenu"));
        NinePatchDrawable menuButtonPressedDrawable = new NinePatchDrawable(atlas.createPatch("botonMenuPresionado"));
        styleTextButtonMenu = new TextButtonStyle(menuButtonDrawable, menuButtonPressedDrawable, null, font45);
        styleTextButtonMenu.fontColor = Color.GREEN;

        styleLabel = new LabelStyle(font15, Color.GREEN);

        // Slider
        NinePatchDrawable defaultSlider = new NinePatchDrawable(atlas.createPatch("default-slider"));
        TextureRegionDrawable defaultSliderKnob = new TextureRegionDrawable(atlas.findRegion("default-slider-knob"));

        styleSlider = new SliderStyle(defaultSlider, defaultSliderKnob);

        TextureRegionDrawable buttonBackUp = new TextureRegionDrawable(atlas.findRegion("btBack"));
        TextureRegionDrawable buttonBackDown = new TextureRegionDrawable(atlas.findRegion("btBackDown"));

        styleTextButtonBack = new TextButtonStyle(buttonBackUp, buttonBackDown, null, font15);
        styleTextButtonBack.fontColor = Color.GREEN;

        TextureRegionDrawable buttonPauseUp = new TextureRegionDrawable(atlas.findRegion("btPause"));
        TextureRegionDrawable buttonPauseDown = new TextureRegionDrawable(atlas.findRegion("btPauseDown"));
        styleImageButtonPause = new ImageButtonStyle(buttonPauseUp, buttonPauseDown, null, null, null, null);

        NinePatchDrawable buttonFacebook = new NinePatchDrawable(atlas.createPatch("btShareFacebookUp"));
        NinePatchDrawable buttonFacebookPressed = new NinePatchDrawable(atlas.createPatch("btShareFacebookDown"));
        styleTextButtonFacebook = new TextButtonStyle(buttonFacebook, buttonFacebookPressed, null, font10);

        TextureRegionDrawable checked = new TextureRegionDrawable(atlas.findRegion("checkBoxDown"));
        TextureRegionDrawable unchecked = new TextureRegionDrawable(atlas.findRegion("checkBox"));

        styleImageButtonStyleCheckBox = new ImageButtonStyle(unchecked, checked, checked, null, null, null);
    }

    public static void load() {
        languagesBundle = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));


        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        loadFont(textureAtlas);
        loadSceneStyles(textureAtlas);

        // Menu
        leftMenuEllipse = textureAtlas.findRegion("elipseMenuIzq");
        titleMenuBox = new NinePatchDrawable(textureAtlas.createPatch("tituloMenuRecuadro"));

        // Game
        inGameStatusDrawable = new NinePatchDrawable(textureAtlas.createPatch("recuadroInGameStatus"));
        buttonLeft = new TextureRegionDrawable(textureAtlas.findRegion("btLeft"));
        buttonRight = new TextureRegionDrawable(textureAtlas.findRegion("btRight"));
        buttonFire = new TextureRegionDrawable(textureAtlas.findRegion("btFire"));
        buttonFirePressed = new TextureRegionDrawable(textureAtlas.findRegion("btFire"));
        buttonMissile = new TextureRegionDrawable(textureAtlas.findRegion("btMissil"));
        buttonMissilePressed = new TextureRegionDrawable(textureAtlas.findRegion("btMissil"));

        backgroundAtlasRegion = textureAtlas.findRegion("fondo");

        buttonSignInUp = new NinePatchDrawable(new NinePatch(textureAtlas.createPatch("btSignInUp")));
        buttonSignInDown = new NinePatchDrawable(new NinePatch(textureAtlas.createPatch("btSignInDown")));

        // Aid
        help1 = textureAtlas.findRegion("help1");
        helpClick = textureAtlas.findRegion("ayudaClick");

        // Buttons
        buttonMusicOn = new TextureRegionDrawable(textureAtlas.findRegion("btMusica"));
        buttonMusicOff = new TextureRegionDrawable(textureAtlas.findRegion("btSinMusica"));
        buttonSoundOn = new TextureRegionDrawable(textureAtlas.findRegion("btSonido"));
        buttonSoundOff = new TextureRegionDrawable(textureAtlas.findRegion("btSinSonido"));

        // spaceship
        spaceShipRight = textureAtlas.findRegion("naveRight");
        spaceShipLeft = textureAtlas.findRegion("naveLeft");
        spaceShip = textureAtlas.findRegion("nave");

        AtlasRegion shield0 = textureAtlas.findRegion("shield0");
        AtlasRegion shield1 = textureAtlas.findRegion("shield1");
        AtlasRegion shield2 = textureAtlas.findRegion("shield2");
        AtlasRegion shield3 = textureAtlas.findRegion("shield3");
        AtlasRegion shield4 = textureAtlas.findRegion("shield4");
        AtlasRegion shield5 = textureAtlas.findRegion("shield5");
        AtlasRegion shield6 = textureAtlas.findRegion("shield6");
        AtlasRegion shield7 = textureAtlas.findRegion("shield7");
        AtlasRegion shield8 = textureAtlas.findRegion("shield9");
        AtlasRegion shield9 = textureAtlas.findRegion("shield9");
        AtlasRegion shield10 = textureAtlas.findRegion("shield10");
        AtlasRegion shield11 = textureAtlas.findRegion("shield11");
        shieldAnimation = new Animation(.1f, shield0, shield1, shield2, shield3, shield4, shield5, shield6, shield7, shield8, shield9, shield10, shield11);

        // UFO
        alien1 = textureAtlas.findRegion("alien1");
        alien2 = textureAtlas.findRegion("alien2");
        alien3 = textureAtlas.findRegion("alien3");
        alien4 = textureAtlas.findRegion("alien4");

        boost1 = textureAtlas.findRegion("upgLaser");
        boost2 = textureAtlas.findRegion("upgBomb");
        boost3 = textureAtlas.findRegion("upgShield");
        upgLife = textureAtlas.findRegion("upgLife");

        // Ammunition
        normalBullet = textureAtlas.findRegion("balaNormal");
        normalEnemyBullet = textureAtlas.findRegion("balaNormalEnemigo");

        AtlasRegion missile1AtlasRegion = textureAtlas.findRegion("misil1");
        AtlasRegion missile2AtlasRegion = textureAtlas.findRegion("misil2");
        missileAnimation = new Animation(0.2f, missile1AtlasRegion, missile2AtlasRegion);

        AtlasRegion superBomb1AtlasRegion = textureAtlas.findRegion("superRayo1");
        AtlasRegion superBomb2AtlasRegion = textureAtlas.findRegion("superRayo2");
        superBombAnimation = new Animation(0.2f, superBomb1AtlasRegion, superBomb2AtlasRegion);

        bulletLevel1 = textureAtlas.findRegion("disparoA1");
        bulletLevel2 = textureAtlas.findRegion("disparoA2");
        bulletLevel3 = textureAtlas.findRegion("disparoA3");
        bulletLevel4 = textureAtlas.findRegion("disparoA4");

        // explosion Fire
        AtlasRegion explosionFrame1 = textureAtlas.findRegion("newExplosion1");
        AtlasRegion explosionFrame2 = textureAtlas.findRegion("newExplosion2");
        AtlasRegion explosionFrame3 = textureAtlas.findRegion("newExplosion3");
        AtlasRegion explosionFrame4 = textureAtlas.findRegion("newExplosion4");
        AtlasRegion explosionFrame5 = textureAtlas.findRegion("newExplosion5");
        AtlasRegion explosionFrame6 = textureAtlas.findRegion("newExplosion6");
        AtlasRegion explosionFrame7 = textureAtlas.findRegion("newExplosion7");
        AtlasRegion explosionFrame8 = textureAtlas.findRegion("newExplosion8");
        AtlasRegion explosionFrame9 = textureAtlas.findRegion("newExplosion9");
        AtlasRegion explosionFrame10 = textureAtlas.findRegion("newExplosion10");
        AtlasRegion explosionFrame11 = textureAtlas.findRegion("newExplosion11");
        AtlasRegion explosionFrame12 = textureAtlas.findRegion("newExplosion12");
        AtlasRegion explosionFrame13 = textureAtlas.findRegion("newExplosion13");
        AtlasRegion explosionFrame14 = textureAtlas.findRegion("newExplosion14");
        AtlasRegion explosionFrame15 = textureAtlas.findRegion("newExplosion15");
        AtlasRegion explosionFrame16 = textureAtlas.findRegion("newExplosion16");
        AtlasRegion explosionFrame17 = textureAtlas.findRegion("newExplosion17");
        AtlasRegion explosionFrame18 = textureAtlas.findRegion("newExplosion18");
        AtlasRegion explosionFrame19 = textureAtlas.findRegion("newExplosion19");
        explosionAnimation = new Animation(0.05f, explosionFrame1, explosionFrame2, explosionFrame3, explosionFrame4, explosionFrame5, explosionFrame6, explosionFrame7, explosionFrame8, explosionFrame9, explosionFrame10, explosionFrame11, explosionFrame12, explosionFrame13, explosionFrame14, explosionFrame15, explosionFrame16, explosionFrame17, explosionFrame18, explosionFrame19);

        ParallaxLayer parallaxLayer = new ParallaxLayer(backgroundAtlasRegion, new Vector2(0, 50), new Vector2(0, 0));
        ParallaxLayer[] arrayLayers = new ParallaxLayer[]{parallaxLayer};
        backgroundLayer = new ParallaxBackground(arrayLayers, 320, 480, new Vector2(0, 1));

        music = Gdx.audio.newMusic(Gdx.files.internal("data/sonidos/musica.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/coin.ogg"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/click.ogg"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/sound_explode.ogg"));
        missileFiringSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/missilFire3.ogg"));

        Settings.load();
        if (Settings.musicEnabled)
            music.play();
    }

    public static void playSound(Sound sound, float volumen) {
        if (Settings.soundEnabled)
            sound.play(volumen);
    }

    public static void playSound(Sound sound) {
        if (Settings.soundEnabled)
            sound.play(1);
    }

    public static float getTextWidth(BitmapFont font, String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }
}
