
package com.nopalsoft.sharkadventure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.sharkadventure.parallax.ParallaxBackground;
import com.nopalsoft.sharkadventure.parallax.ParallaxLayer;

public class Assets {

	public static BitmapFont fontGrande;
	public static TextureRegionDrawable titulo;
	public static TextureRegionDrawable gameOver;

	public static Animation<TextureRegion> tiburonSwim;
	public static Animation<TextureRegion> tiburonFast;
	public static Animation<TextureRegion> tiburonFire;
	public static AtlasRegion tiburonDead;
	public static AtlasRegion turboTail;

	public static AtlasRegion barrilVerde;
	public static AtlasRegion barrilNegro;
	public static AtlasRegion barrilRojo;
	public static AtlasRegion barrilAmarillo;

	public static Animation<TextureRegion> explosion;

	public static AtlasRegion submarinoRojo;
	public static AtlasRegion submarinoAmarillo;

	public static AtlasRegion minaGris;
	public static AtlasRegion minaOxido;
	public static AtlasRegion chain;
	public static AtlasRegion blast;
	public static AtlasRegion torpedo;
	public static AtlasRegion corazon;
	public static AtlasRegion carne;

	public static Animation<TextureRegion> blastHit;

	public static AtlasRegion redBar;
	public static AtlasRegion energyBar;

	public static AtlasRegion fondo;
	public static ParallaxBackground parallaxAtras;
	public static ParallaxBackground parallaxFrente;

	static TextureAtlas atlas;

	public static ParticleEffect burbujaEffect;
	public static ParticleEffect burbujaTiburon;
	public static ParticleEffect burbujaTorpedoRightSide;
	public static ParticleEffect burbujaTorpedoLeftSide;
	public static ParticleEffect peces;
	public static ParticleEffect pecesMediano;

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

		tiburonDead = atlas.findRegion("tiburonDead");

		AtlasRegion tiburon1 = atlas.findRegion("tiburon1");
		AtlasRegion tiburon2 = atlas.findRegion("tiburon2");
		AtlasRegion tiburon3 = atlas.findRegion("tiburon3");
		AtlasRegion tiburon4 = atlas.findRegion("tiburon4");
		AtlasRegion tiburon5 = atlas.findRegion("tiburon5");
		AtlasRegion tiburon6 = atlas.findRegion("tiburon6");
		AtlasRegion tiburon7 = atlas.findRegion("tiburon7");
		AtlasRegion tiburon8 = atlas.findRegion("tiburon8");

		tiburonSwim = new Animation(.15f, tiburon1, tiburon2, tiburon3, tiburon4, tiburon5, tiburon6, tiburon7, tiburon8);
		tiburonFast = new Animation(.04f, tiburon1, tiburon2, tiburon3, tiburon4, tiburon5, tiburon6, tiburon7, tiburon8);

		AtlasRegion tiburonFire1 = atlas.findRegion("tiburonFire1");
		AtlasRegion tiburonFire2 = atlas.findRegion("tiburonFire2");
		AtlasRegion tiburonFire3 = atlas.findRegion("tiburonFire3");
		AtlasRegion tiburonFire4 = atlas.findRegion("tiburonFire4");
		AtlasRegion tiburonFire5 = atlas.findRegion("tiburonFire5");

		tiburonFire = new Animation(.075f, tiburonFire1, tiburonFire2, tiburonFire3, tiburonFire4, tiburonFire5);

		turboTail = atlas.findRegion("turbo");

		barrilVerde = atlas.findRegion("barrilVerde");
		barrilNegro = atlas.findRegion("barrilNegro");
		barrilRojo = atlas.findRegion("barrilRojo");
		barrilAmarillo = atlas.findRegion("barrilAmarillo");

		AtlasRegion explosion1 = atlas.findRegion("explosion1");
		AtlasRegion explosion2 = atlas.findRegion("explosion2");
		AtlasRegion explosion3 = atlas.findRegion("explosion3");
		AtlasRegion explosion4 = atlas.findRegion("explosion4");
		AtlasRegion explosion5 = atlas.findRegion("explosion5");
		AtlasRegion explosion6 = atlas.findRegion("explosion6");
		AtlasRegion explosion7 = atlas.findRegion("explosion7");
		AtlasRegion explosion8 = atlas.findRegion("explosion8");

		explosion = new Animation(.1f, explosion1, explosion2, explosion3, explosion4, explosion5, explosion6, explosion7, explosion8);

		AtlasRegion blastHit1 = atlas.findRegion("blastHit1");
		AtlasRegion blastHit2 = atlas.findRegion("blastHit2");
		AtlasRegion blastHit3 = atlas.findRegion("blastHit3");
		AtlasRegion blastHit4 = atlas.findRegion("blastHit4");
		AtlasRegion blastHit5 = atlas.findRegion("blastHit5");
		AtlasRegion blastHit6 = atlas.findRegion("blastHit6");

		blastHit = new Animation(.05f, blastHit1, blastHit2, blastHit3, blastHit4, blastHit5, blastHit6);

		submarinoAmarillo = atlas.findRegion("submarinoAmarillo");
		submarinoRojo = atlas.findRegion("submarinoRojo");

		minaGris = atlas.findRegion("minaGris");
		minaOxido = atlas.findRegion("minaOxido");
		chain = atlas.findRegion("chain");
		blast = atlas.findRegion("blast");
		torpedo = atlas.findRegion("torpedo");
		corazon = atlas.findRegion("corazon");
		carne = atlas.findRegion("carne");

		reloadFondo();

		burbujaEffect = new ParticleEffect();
		burbujaEffect.load(Gdx.files.internal("particulas/burbujas"), atlas);

		burbujaTiburon = new ParticleEffect();
		burbujaTiburon.load(Gdx.files.internal("particulas/burbujasTiburon"), atlas);

		burbujaTorpedoRightSide = new ParticleEffect();
		burbujaTorpedoRightSide.load(Gdx.files.internal("particulas/burbujasTorpedoRightSide"), atlas);

		burbujaTorpedoLeftSide = new ParticleEffect();
		burbujaTorpedoLeftSide.load(Gdx.files.internal("particulas/burbujasTorpedoLeftSide"), atlas);

		peces = new ParticleEffect();
		peces.load(Gdx.files.internal("particulas/peces"), atlas);

		pecesMediano = new ParticleEffect();
		pecesMediano.load(Gdx.files.internal("particulas/pecesMediano"), atlas);

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
		titulo = new TextureRegionDrawable(atlas.findRegion("UI/titulo"));
		gameOver = new TextureRegionDrawable(atlas.findRegion("UI/gameOver2"));

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
			fondo = atlas.findRegion("fondo");
			sueloAtras = new ParallaxLayer(atlas.findRegion("sueloAtras"), new Vector2(5, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
			sueloFrente = new ParallaxLayer(atlas.findRegion("suelo"), new Vector2(15, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);

		}
		else {
			fondo = atlas.findRegion("fondo2");
			sueloAtras = new ParallaxLayer(atlas.findRegion("suelo2Atras"), new Vector2(5, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);
			sueloFrente = new ParallaxLayer(atlas.findRegion("suelo2"), new Vector2(15, 0), new Vector2(0, -50), new Vector2(-1, 480), 1024, 121);

		}
		parallaxAtras = new ParallaxBackground(new ParallaxLayer[] { sueloAtras }, 800, 480, new Vector2(10, 0));
		parallaxFrente = new ParallaxBackground(new ParallaxLayer[] { sueloFrente }, 800, 480, new Vector2(10, 0));
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
