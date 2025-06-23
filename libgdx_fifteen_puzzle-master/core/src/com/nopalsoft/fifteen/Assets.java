package com.nopalsoft.fifteen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

	public static BitmapFont fontChico;
	public static BitmapFont fontGrande;

	public static AtlasRegion fondo;
	public static AtlasRegion fondoTablero;
	public static AtlasRegion puzzleSolved;

	public static AtlasRegion titulo;

	public static NinePatchDrawable pixelNegro;
	public static AtlasRegion fondoPuntuaciones;

	public static TextureRegionDrawable btAtras;
	public static TextureRegionDrawable btFacebook;
	public static TextureRegionDrawable btTwitter;

	public static AtlasRegion piezaVacia;
	public static AtlasRegion pieza1;
	public static AtlasRegion pieza2;
	public static AtlasRegion pieza3;
	public static AtlasRegion pieza4;
	public static AtlasRegion pieza5;
	public static AtlasRegion pieza6;
	public static AtlasRegion pieza7;
	public static AtlasRegion pieza8;
	public static AtlasRegion pieza9;
	public static AtlasRegion pieza10;
	public static AtlasRegion pieza11;
	public static AtlasRegion pieza12;
	public static AtlasRegion pieza13;
	public static AtlasRegion pieza14;
	public static AtlasRegion pieza15;

	public static LabelStyle labelStyleChico;
	public static LabelStyle labelStyleGrande;
	public static ButtonStyle styleButtonMusica;
	public static ButtonStyle styleButtonPause;
	public static ButtonStyle styleButtonSonido;

	static TextureAtlas atlas;

	private static Music music1;
	private static Music music2;

	static Sound move1;
	static Sound move2;

	public static void loadFont() {
		fontChico = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"));

		fontGrande = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"));
	}

	private static void cargarEstilos() {
		labelStyleChico = new LabelStyle(fontChico, Color.WHITE);
		labelStyleGrande = new LabelStyle(fontGrande, Color.WHITE);

		/* Button Musica */
		TextureRegionDrawable btMusicOn = new TextureRegionDrawable(atlas.findRegion("btMusica"));
		TextureRegionDrawable btMusicOff = new TextureRegionDrawable(atlas.findRegion("btSinMusica"));
		styleButtonMusica = new ButtonStyle(btMusicOn, null, btMusicOff);

		/* Boton Sonido */
		TextureRegionDrawable botonSonidoOn = new TextureRegionDrawable(atlas.findRegion("btSonido"));
		TextureRegionDrawable botonSonidoOff = new TextureRegionDrawable(atlas.findRegion("btSinSonido"));
		styleButtonSonido = new ButtonStyle(botonSonidoOn, null, botonSonidoOff);

		/* ImageButton Pause */
		TextureRegionDrawable btPauseUp = new TextureRegionDrawable(atlas.findRegion("btPause"));
		TextureRegionDrawable btPauseDown = new TextureRegionDrawable(atlas.findRegion("btPauseDown"));
		styleButtonPause = new ButtonStyle(btPauseUp, btPauseDown, null);

	}

	public static void load() {
		atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

		loadFont();
		cargarEstilos();

		if (MathUtils.randomBoolean())
			fondo = atlas.findRegion("fondo");
		else
			fondo = atlas.findRegion("fondo2");
		fondoTablero = atlas.findRegion("fondoPuntuaciones");

		titulo = atlas.findRegion("titulo");

		pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
		fondoPuntuaciones = atlas.findRegion("fondoPuntuaciones");

		puzzleSolved = atlas.findRegion("puzzleSolved");

		piezaVacia = atlas.findRegion("piezaVacia");
		pieza1 = atlas.findRegion("pieza1");
		pieza2 = atlas.findRegion("pieza2");
		pieza3 = atlas.findRegion("pieza3");
		pieza4 = atlas.findRegion("pieza4");
		pieza5 = atlas.findRegion("pieza5");
		pieza6 = atlas.findRegion("pieza6");
		pieza7 = atlas.findRegion("pieza7");
		pieza8 = atlas.findRegion("pieza8");
		pieza9 = atlas.findRegion("pieza9");
		pieza10 = atlas.findRegion("pieza10");
		pieza11 = atlas.findRegion("pieza11");
		pieza12 = atlas.findRegion("pieza12");
		pieza13 = atlas.findRegion("pieza13");
		pieza14 = atlas.findRegion("pieza14");
		pieza15 = atlas.findRegion("pieza15");

		btAtras = new TextureRegionDrawable(atlas.findRegion("btAtras2"));
		btFacebook = new TextureRegionDrawable(atlas.findRegion("btFacebook"));
		btTwitter = new TextureRegionDrawable(atlas.findRegion("btTwitter"));

		move1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move1.mp3"));
		move2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move2.mp3"));

		music1 = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music1.mp3"));
		music2 = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music2.mp3"));

		Settings.load();

		music1.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(Music music) {
				if (Settings.isMusicOn)
					music2.play();

			}
		});

		music2.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(Music music) {

				if (Settings.isMusicOn)
					music1.play();

			}
		});

		playMusic();
	}

	public static void playMusic() {
		if (Settings.isMusicOn) {
			music1.stop();
			music2.stop();
			if (MathUtils.randomBoolean())
				music1.play();
			else
				music2.play();

		}
	}

	public static void pauseMusic() {
		music1.stop();
		music2.stop();
	}

	public static void playSoundMove() {
		if (Settings.isSoundOn) {
			if (MathUtils.randomBoolean())
				move1.play(1);
			else
				move2.play(1);
		}
	}
}
