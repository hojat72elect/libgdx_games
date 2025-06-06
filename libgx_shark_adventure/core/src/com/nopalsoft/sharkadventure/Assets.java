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

    public static BitmapFont fontGrande;
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

    public static TextureRegionDrawable btDer;
    public static TextureRegionDrawable btDerPress;
    public static TextureRegionDrawable btIzq;
    public static TextureRegionDrawable btIzqPress;
    public static TextureRegionDrawable btUp;
    public static TextureRegionDrawable btUpPress;
    public static TextureRegionDrawable btFire;
    public static TextureRegionDrawable btFirePress;

    public static TextureRegionDrawable btHome;
    public static TextureRegionDrawable btHomePress;
    public static TextureRegionDrawable btPausa;
    public static TextureRegionDrawable btPausaPress;
    public static TextureRegionDrawable btLeaderboard;
    public static TextureRegionDrawable btLeaderboardPress;
    public static TextureRegionDrawable btFacebook;
    public static TextureRegionDrawable btFacebookPress;
    public static TextureRegionDrawable btMusicOn;
    public static TextureRegionDrawable btMusicOff;
    public static TextureRegionDrawable btSoundOn;
    public static TextureRegionDrawable btSoundOff;
    public static TextureRegionDrawable btRefresh;
    public static TextureRegionDrawable btRefreshPress;
    public static TextureRegionDrawable btAchievements;
    public static TextureRegionDrawable btAchievementsPress;
    public static TextureRegionDrawable btTwitter;
    public static TextureRegionDrawable btTwitterPress;

    public static TextureRegionDrawable backgroundProgressBar;
    public static NinePatchDrawable backgroundMenu;
    public static NinePatchDrawable backgroundVentana;
    public static NinePatchDrawable backgroundTitulo;

    public static LabelStyle lblStyle;

    public static Sound sSwim;
    public static Sound sSonar;
    public static Sound sExplosion1;
    public static Sound sExplosion2;
    public static Sound sBlast;

    public static Music musica;

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontGrande = new BitmapFont(Gdx.files.internal("data/FontGrande.fnt"), atlas.findRegion("FontGrande"));

        loadUI();

        sharkDead = atlas.findRegion("tiburonDead");

        AtlasRegion tiburon1 = atlas.findRegion("tiburon1");
        AtlasRegion tiburon2 = atlas.findRegion("tiburon2");
        AtlasRegion tiburon3 = atlas.findRegion("tiburon3");
        AtlasRegion tiburon4 = atlas.findRegion("tiburon4");
        AtlasRegion tiburon5 = atlas.findRegion("tiburon5");
        AtlasRegion tiburon6 = atlas.findRegion("tiburon6");
        AtlasRegion tiburon7 = atlas.findRegion("tiburon7");
        AtlasRegion tiburon8 = atlas.findRegion("tiburon8");

        sharkSwimAnimation = new Animation(.15f, tiburon1, tiburon2, tiburon3, tiburon4, tiburon5, tiburon6, tiburon7, tiburon8);
        sharkDashAnimation = new Animation(.04f, tiburon1, tiburon2, tiburon3, tiburon4, tiburon5, tiburon6, tiburon7, tiburon8);

        AtlasRegion tiburonFire1 = atlas.findRegion("tiburonFire1");
        AtlasRegion tiburonFire2 = atlas.findRegion("tiburonFire2");
        AtlasRegion tiburonFire3 = atlas.findRegion("tiburonFire3");
        AtlasRegion tiburonFire4 = atlas.findRegion("tiburonFire4");
        AtlasRegion tiburonFire5 = atlas.findRegion("tiburonFire5");

        sharkFireAnimation = new Animation(.075f, tiburonFire1, tiburonFire2, tiburonFire3, tiburonFire4, tiburonFire5);

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

        reloadFondo();

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

        sSwim = Gdx.audio.newSound(Gdx.files.internal("sound/swim.mp3"));
        sSonar = Gdx.audio.newSound(Gdx.files.internal("sound/sonar.mp3"));
        sExplosion1 = Gdx.audio.newSound(Gdx.files.internal("sound/explosion1.mp3"));
        sExplosion2 = Gdx.audio.newSound(Gdx.files.internal("sound/explosion2.mp3"));
        sBlast = Gdx.audio.newSound(Gdx.files.internal("sound/blast1.mp3"));

        musica = Gdx.audio.newMusic(Gdx.files.internal("sound/jungleHaze.mp3"));
        musica.setLooping(true);

        if (Settings.isMusicOn)
            musica.play();
    }

    private static void loadUI() {
        titleDrawable = new TextureRegionDrawable(atlas.findRegion("UI/titulo"));
        gameOverDrawable = new TextureRegionDrawable(atlas.findRegion("UI/gameOver2"));

        btDer = new TextureRegionDrawable(atlas.findRegion("UI/btDer"));
        btDerPress = new TextureRegionDrawable(atlas.findRegion("UI/btDerPress"));
        btIzq = new TextureRegionDrawable(atlas.findRegion("UI/btIzq"));
        btIzqPress = new TextureRegionDrawable(atlas.findRegion("UI/btIzqPress"));
        btUp = new TextureRegionDrawable(atlas.findRegion("UI/btUp"));
        btUpPress = new TextureRegionDrawable(atlas.findRegion("UI/btUpPress"));
        btFire = new TextureRegionDrawable(atlas.findRegion("UI/btFire"));
        btFirePress = new TextureRegionDrawable(atlas.findRegion("UI/btFirePress"));

        btRefresh = new TextureRegionDrawable(atlas.findRegion("UI/btRefresh"));
        btRefreshPress = new TextureRegionDrawable(atlas.findRegion("UI/btRefreshPress"));
        btHome = new TextureRegionDrawable(atlas.findRegion("UI/btHome"));
        btHomePress = new TextureRegionDrawable(atlas.findRegion("UI/btHomePress"));
        btPausa = new TextureRegionDrawable(atlas.findRegion("UI/btPausa"));
        btPausaPress = new TextureRegionDrawable(atlas.findRegion("UI/btPausaPress"));
        btLeaderboard = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
        btLeaderboardPress = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"));
        btAchievements = new TextureRegionDrawable(atlas.findRegion("UI/btAchievements"));
        btAchievementsPress = new TextureRegionDrawable(atlas.findRegion("UI/btAchievementsPress"));
        btFacebook = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        btFacebookPress = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        btTwitter = new TextureRegionDrawable(atlas.findRegion("UI/btTwitter"));
        btTwitterPress = new TextureRegionDrawable(atlas.findRegion("UI/btTwitterPress"));
        btSoundOn = new TextureRegionDrawable(atlas.findRegion("UI/btSonido"));
        btSoundOff = new TextureRegionDrawable(atlas.findRegion("UI/btSonidoOff"));
        btMusicOn = new TextureRegionDrawable(atlas.findRegion("UI/btMusic"));
        btMusicOff = new TextureRegionDrawable(atlas.findRegion("UI/btMusicOff"));

        redBar = atlas.findRegion("UI/redBar");
        energyBar = atlas.findRegion("UI/energyBar");

        backgroundProgressBar = new TextureRegionDrawable(atlas.findRegion("UI/backgroundProgressBar"));
        backgroundMenu = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundMenu"), 70, 70, 60, 60));
        backgroundVentana = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundVentana"), 25, 25, 25, 25));
        backgroundTitulo = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/backgroundTitulo"), 30, 30, 0, 0));

        lblStyle = new LabelStyle(Assets.fontGrande, null);
    }

    public static void reloadFondo() {
        ParallaxLayer sueloFrente;
        ParallaxLayer sueloAtras;

        if (MathUtils.randomBoolean()) {
            background = atlas.findRegion("fondo");
            sueloAtras = new ParallaxLayer(atlas.findRegion("sueloAtras"), new Vector2(5, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
            sueloFrente = new ParallaxLayer(atlas.findRegion("suelo"), new Vector2(15, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
        } else {
            background = atlas.findRegion("fondo2");
            sueloAtras = new ParallaxLayer(atlas.findRegion("suelo2Atras"), new Vector2(5, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
            sueloFrente = new ParallaxLayer(atlas.findRegion("suelo2"), new Vector2(15, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
        }
        parallaxBackground = new ParallaxBackground(new ParallaxLayer[]{sueloAtras}, 800, 480, new Vector2(10, 0));
        parallaxForeground = new ParallaxBackground(new ParallaxLayer[]{sueloFrente}, 800, 480, new Vector2(10, 0));
    }

    public static void playExplosionSound() {
        int sound = MathUtils.random(1);
        Sound sonido;
        if (sound == 0)
            sonido = sExplosion1;
        else
            sonido = sExplosion2;
        sonido.play();
    }
}
