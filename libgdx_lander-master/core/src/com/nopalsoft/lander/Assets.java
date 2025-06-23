package com.nopalsoft.lander;

import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

	public static boolean isDebug = false;

	public static TextureRegionDrawable titulo;

	public static AtlasRegion nave;
	public static Animation<TextureRegion> naveFly;
	public static Animation<TextureRegion> explosion;

	public static Animation<TextureRegion> laser;
	public static Animation<TextureRegion> laserVertical;

	public static AtlasRegion fondo;

	public static AtlasRegion candado;
	public static AtlasRegion gas;
	public static AtlasRegion star;
	public static AtlasRegion starOff;
	public static AtlasRegion bomba;

	public static AtlasRegion upgradeOn;
	public static AtlasRegion upgradeOff;

	public static TextureRegionDrawable marcoStats;
	public static AtlasRegion barraMarcadorRojo;
	public static AtlasRegion barraMarcadorVerde;

	public static BitmapFont font;
	public static LinkedHashMap<Integer, String> mundos;

	public static TiledMap map;

	public static TextButtonStyle styleTextButtonMenu;
	public static TextButtonStyle styleTextButtonLevels;

	public static LabelStyle styleLabelMediana;

	public static ImageButtonStyle styleImageButtonPause;
	public static ImageButtonStyle styleImageButtonUpgradePlus;

	public static WindowStyle styleDialogGameOver;

	static private void loadSceneStyles(TextureAtlas atlas) {

		styleLabelMediana = new LabelStyle(font, Color.BLACK);

		// Botones del menu
		TextureRegionDrawable botonMenu = new TextureRegionDrawable(atlas.findRegion("btMenu"));
		TextureRegionDrawable botonMenuDown = new TextureRegionDrawable(atlas.findRegion("btMenuDown"));
		styleTextButtonMenu = new TextButtonStyle(botonMenu, botonMenuDown, null, font);
		styleTextButtonMenu.fontColor = Color.GREEN;

		// Boton + de la ventana upgrades
		TextureRegionDrawable btUp = new TextureRegionDrawable(atlas.findRegion("btUpgrade"));
		TextureRegionDrawable btUpDown = new TextureRegionDrawable(atlas.findRegion("btUpgradeDown"));
		styleImageButtonUpgradePlus = new ImageButtonStyle(btUp, btUpDown, null, null, null, null);

		// Dialogo GameOVer, Paused, Tienda
		NinePatchDrawable recuadroGame = new NinePatchDrawable(atlas.createPatch("recuadroGameOver"));
		AtlasRegion dialogDim = atlas.findRegion("dim");
		styleDialogGameOver = new WindowStyle(font, Color.GREEN, recuadroGame);
		styleDialogGameOver.stageBackground = new NinePatchDrawable(new NinePatch(dialogDim));

		// Botton Pausa
		TextureRegionDrawable btPause = new TextureRegionDrawable(atlas.findRegion("btPause"));
		TextureRegionDrawable btPauseDown = new TextureRegionDrawable(atlas.findRegion("btPauseDown"));
		styleImageButtonPause = new ImageButtonStyle(btPause, btPauseDown, null, null, null, null);

		// Botones levels
		NinePatchDrawable btLevels = new NinePatchDrawable(atlas.createPatch("btnLevels"));
		NinePatchDrawable btLevelsDown = new NinePatchDrawable(atlas.createPatch("btLevelsDown"));
		styleTextButtonLevels = new TextButtonStyle(btLevels, btLevelsDown, null, font);
		styleTextButtonLevels.fontColor = Color.GREEN;

	}

	public static void cargar() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

		font = new BitmapFont();
		loadSceneStyles(atlas);

		titulo = new TextureRegionDrawable(atlas.findRegion("titulo"));

		nave = atlas.findRegion("nave");

		AtlasRegion an1 = atlas.findRegion("nave1");
		AtlasRegion an2 = atlas.findRegion("nave2");
		AtlasRegion an3 = atlas.findRegion("nave3");
		AtlasRegion an4 = atlas.findRegion("nave4");
		AtlasRegion an5 = atlas.findRegion("nave5");
		naveFly = new Animation(.15f, an1, an2, an3, an4, an5);

		barraMarcadorRojo = atlas.findRegion("barraMarcadorRojo");
		barraMarcadorVerde = atlas.findRegion("barraMarcadorVerde");
		marcoStats = new TextureRegionDrawable(atlas.findRegion("marcador"));

		fondo = atlas.findRegion("fondo");

		AtlasRegion newExpl1 = atlas.findRegion("newExplosion1");
		AtlasRegion newExpl2 = atlas.findRegion("newExplosion2");
		AtlasRegion newExpl3 = atlas.findRegion("newExplosion3");
		AtlasRegion newExpl4 = atlas.findRegion("newExplosion4");
		AtlasRegion newExpl5 = atlas.findRegion("newExplosion5");
		AtlasRegion newExpl6 = atlas.findRegion("newExplosion6");
		AtlasRegion newExpl7 = atlas.findRegion("newExplosion7");
		AtlasRegion newExpl8 = atlas.findRegion("newExplosion8");
		AtlasRegion newExpl9 = atlas.findRegion("newExplosion9");
		AtlasRegion newExpl10 = atlas.findRegion("newExplosion10");
		AtlasRegion newExpl11 = atlas.findRegion("newExplosion11");
		AtlasRegion newExpl12 = atlas.findRegion("newExplosion12");
		AtlasRegion newExpl13 = atlas.findRegion("newExplosion13");
		AtlasRegion newExpl14 = atlas.findRegion("newExplosion14");
		AtlasRegion newExpl15 = atlas.findRegion("newExplosion15");
		AtlasRegion newExpl16 = atlas.findRegion("newExplosion16");
		AtlasRegion newExpl17 = atlas.findRegion("newExplosion17");
		AtlasRegion newExpl18 = atlas.findRegion("newExplosion18");
		AtlasRegion newExpl19 = atlas.findRegion("newExplosion19");
		explosion = new Animation(0.05f, newExpl1, newExpl2, newExpl3, newExpl4, newExpl5, newExpl6, newExpl7, newExpl8, newExpl9, newExpl10, newExpl11, newExpl12, newExpl13, newExpl14, newExpl15, newExpl16, newExpl17, newExpl18, newExpl19);

		AtlasRegion laser0 = atlas.findRegion("rayo0");
		AtlasRegion laser1 = atlas.findRegion("rayo1");
		AtlasRegion laser2 = atlas.findRegion("rayo2");
		AtlasRegion laser3 = atlas.findRegion("rayo3");
		laser = new Animation(0.1f, laser0, laser1, laser2, laser3);

		laser0 = atlas.findRegion("rayo0Vertical");
		laser1 = atlas.findRegion("rayo1Vertical");
		laser2 = atlas.findRegion("rayo2Vertical");
		laser3 = atlas.findRegion("rayo3Vertical");
		laserVertical = new Animation(0.1f, laser0, laser1, laser2, laser3);

		gas = atlas.findRegion("gas");
		star = atlas.findRegion("star");
		starOff = atlas.findRegion("starOff");
		bomba = atlas.findRegion("bomba");
		candado = atlas.findRegion("candado");

		upgradeOn = atlas.findRegion("upgradeOn");
		upgradeOff = atlas.findRegion("upgradeOff");

		mundos = new LinkedHashMap<Integer, String>();
		mundos.put(0, "data/mundos/mundo00.tmx");
		mundos.put(1, "data/mundos/mundo01.tmx");
		mundos.put(2, "data/mundos/mundo02.tmx");
		mundos.put(3, "data/mundos/mundo03.tmx");
		mundos.put(4, "data/mundos/mundo04.tmx");
		mundos.put(5, "data/mundos/mundo05.tmx");
		mundos.put(6, "data/mundos/mundo06.tmx");
		mundos.put(7, "data/mundos/mundo07.tmx");
		mundos.put(8, "data/mundos/mundo08.tmx");
		mundos.put(9, "data/mundos/mundo09.tmx");
		mundos.put(10, "data/mundos/mundo10.tmx");
		mundos.put(11, "data/mundos/mundo11.tmx");
		// mundos.put(12, "data/mundos/mundo12.tmx");

		Settings.load(500);

	}

	public static void cargarMapa(int numeroMundo) {
		if (map != null) {
			map.dispose();
			map = null;
		}
		if (isDebug)
			map = new TmxMapLoader().load(mundos.get(numeroMundo));
		else
			// map=new AtlasTmxMapLoader().load(mundos[numeroMundo - 1]);
			map = new TmxMapLoader().load(mundos.get(numeroMundo));
	}
}
