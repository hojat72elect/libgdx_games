package com.nopalsoft.sharkadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.sharkadventure.parallax.ParallaxBackground;
import com.nopalsoft.sharkadventure.parallax.ParallaxLayer;

public class Assets {

    public static BitmapFont fontLarge;
    public static TextureRegionDrawable titleDrawable;
    public static TextureRegionDrawable gameOverDrawable;

    public static Animation<TextureRegion> sharkSwimAnimation;
    public static Animation<TextureRegion> sharkDashAnimation;
    public static Animation<TextureRegion> sharkFireAnimation;
    public static AtlasRegion sharkDead;
    public static AtlasRegion turboTail;

    public static AtlasRegion greenBarrel;
    public static AtlasRegion blackBarrel;
    public static AtlasRegion redBarrel;
    public static AtlasRegion yellowBarrel;

    public static Animation<TextureRegion> explosionAnimation;

    public static AtlasRegion redSubmarine;
    public static AtlasRegion yellowSubmarine;

    public static AtlasRegion grayMine;
    public static AtlasRegion rustyMine;
    public static AtlasRegion chain;
    public static AtlasRegion blast;
    public static AtlasRegion torpedo;
    public static AtlasRegion heart;
    public static AtlasRegion meat;

    public static Animation<TextureRegion> blastHit;

    public static AtlasRegion redBar;
    public static AtlasRegion energyBar;

    public static AtlasRegion background;
    public static ParallaxBackground parallaxBackground;
    public static ParallaxBackground parallaxForeground;

    static TextureAtlas atlas;

    public static ParticleEffect bubbleParticleEffect;
    public static ParticleEffect sharkBubbleParticleEffect;
    public static ParticleEffect torpedoBubbleRightSideParticleEffect;
    public static ParticleEffect torpedoBubbleLeftSideParticleEffect;
    public static ParticleEffect fishParticleEffect;
    public static ParticleEffect mediumFishParticleEffect;

    public static TextureRegionDrawable buttonRight;
    public static TextureRegionDrawable buttonRightPressed;
    public static TextureRegionDrawable buttonLeft;
    public static TextureRegionDrawable buttonLeftPressed;
    public static TextureRegionDrawable buttonUp;
    public static TextureRegionDrawable buttonUpPressed;
    public static TextureRegionDrawable buttonFire;
    public static TextureRegionDrawable buttonFirePressed;

    public static TextureRegionDrawable buttonHome;
    public static TextureRegionDrawable buttonHomePressed;
    public static TextureRegionDrawable buttonPause;
    public static TextureRegionDrawable buttonPausePressed;
    public static TextureRegionDrawable buttonLeaderboard;
    public static TextureRegionDrawable buttonLeaderboardPressed;
    public static TextureRegionDrawable buttonFacebook;
    public static TextureRegionDrawable buttonFacebookPressed;
    public static TextureRegionDrawable buttonMusicOn;
    public static TextureRegionDrawable buttonMusicOff;
    public static TextureRegionDrawable buttonSoundOn;
    public static TextureRegionDrawable buttonSoundOff;
    public static TextureRegionDrawable buttonRefresh;
    public static TextureRegionDrawable buttonRefreshPressed;
    public static TextureRegionDrawable buttonAchievements;
    public static TextureRegionDrawable buttonAchievementsPressed;
    public static TextureRegionDrawable buttonTwitter;
    public static TextureRegionDrawable buttonTwitterPressed;

    public static TextureRegionDrawable backgroundProgressBar;
    public static NinePatchDrawable menuBackgroundDrawable;
    public static NinePatchDrawable windowBackgroundDrawable;
    public static NinePatchDrawable titleBackgroundDrawable;

    public static LabelStyle lblStyle;

    public static Sound swimSound;
    public static Sound sonarSound;
    public static Sound explosionSound1;
    public static Sound explosionSound2;
    public static Sound blastSound;

    public static Music music;

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontLarge = new BitmapFont(Gdx.files.internal("data/FontGrande.fnt"), atlas.findRegion("FontGrande"));

        loadUI();

        sharkDead = atlas.findRegion("tiburonDead");

        AtlasRegion sharkFrame1 = atlas.findRegion("tiburon1");
        AtlasRegion sharkFrame2 = atlas.findRegion("tiburon2");
        AtlasRegion sharkFrame3 = atlas.findRegion("tiburon3");
        AtlasRegion sharkFrame4 = atlas.findRegion("tiburon4");
        AtlasRegion sharkFrame5 = atlas.findRegion("tiburon5");
        AtlasRegion sharkFrame6 = atlas.findRegion("tiburon6");
        AtlasRegion sharkFrame7 = atlas.findRegion("tiburon7");
        AtlasRegion sharkFrame8 = atlas.findRegion("tiburon8");

        sharkSwimAnimation = new Animation(.15f, sharkFrame1, sharkFrame2, sharkFrame3, sharkFrame4, sharkFrame5, sharkFrame6, sharkFrame7, sharkFrame8);
        sharkDashAnimation = new Animation(.04f, sharkFrame1, sharkFrame2, sharkFrame3, sharkFrame4, sharkFrame5, sharkFrame6, sharkFrame7, sharkFrame8);

        AtlasRegion sharkFireFrame1 = atlas.findRegion("tiburonFire1");
        AtlasRegion sharkFireFrame2 = atlas.findRegion("tiburonFire2");
        AtlasRegion sharkFireFrame3 = atlas.findRegion("tiburonFire3");
        AtlasRegion sharkFireFrame4 = atlas.findRegion("tiburonFire4");
        AtlasRegion sharkFireFrame5 = atlas.findRegion("tiburonFire5");

        sharkFireAnimation = new Animation(.075f, sharkFireFrame1, sharkFireFrame2, sharkFireFrame3, sharkFireFrame4, sharkFireFrame5);

        turboTail = atlas.findRegion("turbo");

        greenBarrel = atlas.findRegion("barrilVerde");
        blackBarrel = atlas.findRegion("barrilNegro");
        redBarrel = atlas.findRegion("barrilRojo");
        yellowBarrel = atlas.findRegion("barrilAmarillo");

        AtlasRegion explosion1 = atlas.findRegion("explosion1");
        AtlasRegion explosion2 = atlas.findRegion("explosion2");
        AtlasRegion explosion3 = atlas.findRegion("explosion3");
        AtlasRegion explosion4 = atlas.findRegion("explosion4");
        AtlasRegion explosion5 = atlas.findRegion("explosion5");
        AtlasRegion explosion6 = atlas.findRegion("explosion6");
        AtlasRegion explosion7 = atlas.findRegion("explosion7");
        AtlasRegion explosion8 = atlas.findRegion("explosion8");

        explosionAnimation = new Animation(.1f, explosion1, explosion2, explosion3, explosion4, explosion5, explosion6, explosion7, explosion8);

        AtlasRegion blastHit1 = atlas.findRegion("blastHit1");
        AtlasRegion blastHit2 = atlas.findRegion("blastHit2");
        AtlasRegion blastHit3 = atlas.findRegion("blastHit3");
        AtlasRegion blastHit4 = atlas.findRegion("blastHit4");
        AtlasRegion blastHit5 = atlas.findRegion("blastHit5");
        AtlasRegion blastHit6 = atlas.findRegion("blastHit6");

        blastHit = new Animation(.05f, blastHit1, blastHit2, blastHit3, blastHit4, blastHit5, blastHit6);

        yellowSubmarine = atlas.findRegion("submarinoAmarillo");
        redSubmarine = atlas.findRegion("submarinoRojo");

        grayMine = atlas.findRegion("minaGris");
        rustyMine = atlas.findRegion("minaOxido");
        chain = atlas.findRegion("chain");
        blast = atlas.findRegion("blast");
        torpedo = atlas.findRegion("torpedo");
        heart = atlas.findRegion("corazon");
        meat = atlas.findRegion("carne");

        reloadBackground();

        bubbleParticleEffect = new ParticleEffect();
        bubbleParticleEffect.load(Gdx.files.internal("particulas/burbujas"), atlas);

        sharkBubbleParticleEffect = new ParticleEffect();
        sharkBubbleParticleEffect.load(Gdx.files.internal("particulas/burbujasTiburon"), atlas);

        torpedoBubbleRightSideParticleEffect = new ParticleEffect();
        torpedoBubbleRightSideParticleEffect.load(Gdx.files.internal("particulas/burbujasTorpedoRightSide"), atlas);

        torpedoBubbleLeftSideParticleEffect = new ParticleEffect();
        torpedoBubbleLeftSideParticleEffect.load(Gdx.files.internal("particulas/burbujasTorpedoLeftSide"), atlas);

        fishParticleEffect = new ParticleEffect();
        fishParticleEffect.load(Gdx.files.internal("particulas/peces"), atlas);

        mediumFishParticleEffect = new ParticleEffect();
        mediumFishParticleEffect.load(Gdx.files.internal("particulas/pecesMediano"), atlas);

        swimSound = Gdx.audio.newSound(Gdx.files.internal("sound/swim.mp3"));
        sonarSound = Gdx.audio.newSound(Gdx.files.internal("sound/sonar.mp3"));
        explosionSound1 = Gdx.audio.newSound(Gdx.files.internal("sound/explosion1.mp3"));
        explosionSound2 = Gdx.audio.newSound(Gdx.files.internal("sound/explosion2.mp3"));
        blastSound = Gdx.audio.newSound(Gdx.files.internal("sound/blast1.mp3"));

        music = Gdx.audio.newMusic(Gdx.files.internal("sound/jungleHaze.mp3"));
        music.setLooping(true);

        if (Settings.isMusicOn)
            music.play();
    }

    private static void loadUI() {
        titleDrawable = new TextureRegionDrawable(atlas.findRegion("UI/titulo"));
        gameOverDrawable = new TextureRegionDrawable(atlas.findRegion("UI/gameOver2"));

        buttonRight = new TextureRegionDrawable(atlas.findRegion("UI/btDer"));
        buttonRightPressed = new TextureRegionDrawable(atlas.findRegion("UI/btDerPress"));
        buttonLeft = new TextureRegionDrawable(atlas.findRegion("UI/btIzq"));
        buttonLeftPressed = new TextureRegionDrawable(atlas.findRegion("UI/btIzqPress"));
        buttonUp = new TextureRegionDrawable(atlas.findRegion("UI/btUp"));
        buttonUpPressed = new TextureRegionDrawable(atlas.findRegion("UI/btUpPress"));
        buttonFire = new TextureRegionDrawable(atlas.findRegion("UI/btFire"));
        buttonFirePressed = new TextureRegionDrawable(atlas.findRegion("UI/btFirePress"));

        buttonRefresh = new TextureRegionDrawable(atlas.findRegion("UI/btRefresh"));
        buttonRefreshPressed = new TextureRegionDrawable(atlas.findRegion("UI/btRefreshPress"));
        buttonHome = new TextureRegionDrawable(atlas.findRegion("UI/btHome"));
        buttonHomePressed = new TextureRegionDrawable(atlas.findRegion("UI/btHomePress"));
        buttonPause = new TextureRegionDrawable(atlas.findRegion("UI/btPausa"));
        buttonPausePressed = new TextureRegionDrawable(atlas.findRegion("UI/btPausaPress"));
        buttonLeaderboard = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
        buttonLeaderboardPressed = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"));
        buttonAchievements = new TextureRegionDrawable(atlas.findRegion("UI/btAchievements"));
        buttonAchievementsPressed = new TextureRegionDrawable(atlas.findRegion("UI/btAchievementsPress"));
        buttonFacebook = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        buttonFacebookPressed = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        buttonTwitter = new TextureRegionDrawable(atlas.findRegion("UI/btTwitter"));
        buttonTwitterPressed = new TextureRegionDrawable(atlas.findRegion("UI/btTwitterPress"));
        buttonSoundOn = new TextureRegionDrawable(atlas.findRegion("UI/btSonido"));
        buttonSoundOff = new TextureRegionDrawable(atlas.findRegion("UI/btSonidoOff"));
        buttonMusicOn = new TextureRegionDrawable(atlas.findRegion("UI/btMusic"));
        buttonMusicOff = new TextureRegionDrawable(atlas.findRegion("UI/btMusicOff"));

        redBar = atlas.findRegion("UI/redBar");
        energyBar = atlas.findRegion("UI/energyBar");

        backgroundProgressBar = new TextureRegionDrawable(atlas.findRegion("UI/backgroundProgressBar"));
        menuBackgroundDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundMenu"), 70, 70, 60, 60));
        windowBackgroundDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundVentana"), 25, 25, 25, 25));
        titleBackgroundDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundTitulo"), 30, 30, 0, 0));

        lblStyle = new LabelStyle(Assets.fontLarge, null);
    }

    public static void reloadBackground() {
        ParallaxLayer frontLayer;
        ParallaxLayer backgroundLayer;

        if (MathUtils.randomBoolean()) {
            background = atlas.findRegion("fondo");
            backgroundLayer = new ParallaxLayer(atlas.findRegion("sueloAtras"), new Vector2(5, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
            frontLayer = new ParallaxLayer(atlas.findRegion("suelo"), new Vector2(15, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
        } else {
            background = atlas.findRegion("fondo2");
            backgroundLayer = new ParallaxLayer(atlas.findRegion("suelo2Atras"), new Vector2(5, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
            frontLayer = new ParallaxLayer(atlas.findRegion("suelo2"), new Vector2(15, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
        }
        parallaxBackground = new ParallaxBackground(new ParallaxLayer[]{backgroundLayer}, 800, 480, new Vector2(10, 0));
        parallaxForeground = new ParallaxBackground(new ParallaxLayer[]{frontLayer}, 800, 480, new Vector2(10, 0));
    }

    public static void playExplosionSound() {
        int sound = MathUtils.random(1);
        Sound explosionSoundToBePlayed;
        if (sound == 0)
            explosionSoundToBePlayed = explosionSound1;
        else
            explosionSoundToBePlayed = explosionSound2;
        explosionSoundToBePlayed.play();
    }
}
