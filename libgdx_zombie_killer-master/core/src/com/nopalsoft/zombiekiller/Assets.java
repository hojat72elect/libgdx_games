package com.nopalsoft.zombiekiller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class Assets {

	public static BitmapFont fontChico;
	public static BitmapFont fontGrande;

	public static AtlasRegion backBackground;
	public static AtlasRegion background;
	public static AtlasRegion moon;
	public static TextureRegionDrawable zombieKillerTitulo;
	public static TextureRegionDrawable skullBarBackground;

	public static TiledMap map;

	public static AtlasRegion weapon;
	public static AtlasRegion itemCollection;

	public static AtlasRegion itemGem;
	public static AtlasRegion itemHeart;
	public static AtlasRegion itemMeat;
	public static AtlasRegion itemSkull;
	public static AtlasRegion itemShield;
	public static AtlasRegion itemStar;
	public static AtlasRegion crate;
	public static AtlasRegion saw;

	public static AtlasRegion redBar;
	public static AtlasRegion whiteBar;

	/**
	 * Hero
	 */
	public static AnimationSprite heroForceClimb;
	public static AnimationSprite heroForceDie;
	public static Sprite heroForceHurt;
	public static Sprite heroForceIdle;
	public static AnimationSprite heroForceShoot;
	public static AnimationSprite heroForceWalk;

	public static AnimationSprite heroRamboClimb;
	public static AnimationSprite heroRamboDie;
	public static Sprite heroRamboHurt;
	public static Sprite heroRamboIdle;
	public static AnimationSprite heroRamboShoot;
	public static AnimationSprite heroRamboWalk;

	public static AnimationSprite heroSoldierClimb;
	public static AnimationSprite heroSoldierDie;
	public static Sprite heroSoldierHurt;
	public static Sprite heroSoldierIdle;
	public static AnimationSprite heroSoldierShoot;
	public static AnimationSprite heroSoldierWalk;

	public static AnimationSprite heroSwatClimb;
	public static AnimationSprite heroSwatDie;
	public static Sprite heroSwatHurt;
	public static Sprite heroSwatIdle;
	public static AnimationSprite heroSwatShoot;
	public static AnimationSprite heroSwatWalk;

	public static AnimationSprite heroVaderClimb;
	public static AnimationSprite heroVaderDie;
	public static Sprite heroVaderHurt;
	public static Sprite heroVaderIdle;
	public static AnimationSprite heroVaderShoot;
	public static AnimationSprite heroVaderWalk;

	/**
	 * Bullet
	 */
	public static AnimationSprite bullet1;
	public static AnimationSprite bullet2;
	public static AnimationSprite bullet3;
	public static AnimationSprite bullet4;
	public static AnimationSprite bullet5;
	public static AnimationSprite muzzle;

	/**
	 * Zombies
	 */
	public static AnimationSprite zombieKidWalk;
	public static AnimationSprite zombieKidIdle;
	public static AnimationSprite zombieKidRise;
	public static AnimationSprite zombieKidDie;
	public static Sprite zombieKidHurt;

	public static AnimationSprite zombiePanWalk;
	public static AnimationSprite zombiePanIdle;
	public static AnimationSprite zombiePanRise;
	public static AnimationSprite zombiePanDie;
	public static Sprite zombiePanHurt;

	public static AnimationSprite zombieCuasyWalk;
	public static AnimationSprite zombieCuasyIdle;
	public static AnimationSprite zombieCuasyRise;
	public static AnimationSprite zombieCuasyDie;
	public static Sprite zombieCuasyHurt;

	public static AnimationSprite zombieFrankWalk;
	public static AnimationSprite zombieFrankIdle;
	public static AnimationSprite zombieFrankRise;
	public static AnimationSprite zombieFrankDie;
	public static Sprite zombieFrankHurt;

	public static AnimationSprite zombieMummyWalk;
	public static AnimationSprite zombieMummyIdle;
	public static AnimationSprite zombieMummyRise;
	public static AnimationSprite zombieMummyDie;
	public static Sprite zombieMummyHurt;

	public static TextureRegionDrawable containerButtons;
	public static TextureRegionDrawable btPlay;
	public static TextureRegionDrawable btLeaderboard;
	public static TextureRegionDrawable btAchievement;
	public static TextureRegionDrawable btSettings;
	public static TextureRegionDrawable btHelp;
	public static TextureRegionDrawable btFacebook;
	public static TextureRegionDrawable btTwitter;
	public static TextureRegionDrawable btShop;
	public static TextureRegionDrawable btClose;
	public static TextureRegionDrawable btMenu;
	public static TextureRegionDrawable btTryAgain;
	public static TextureRegionDrawable btPause;
	public static TextureRegionDrawable btPlayer;
	public static TextureRegionDrawable btFire;

	public static TextureRegionDrawable btUp;
	public static TextureRegionDrawable btUpPress;
	public static TextureRegionDrawable btRight;
	public static TextureRegionDrawable btRightPress;
	public static TextureRegionDrawable btLeft;
	public static TextureRegionDrawable btLeftPress;
	public static TextureRegionDrawable btDown;
	public static TextureRegionDrawable btDownPress;

	public static TextureRegionDrawable btGems;
	public static TextureRegionDrawable btMore;
	public static TextureRegionDrawable upgradeOff;
	public static TextureRegionDrawable backgroundProgressBar;

	public static NinePatchDrawable storeTableBackground;
	public static NinePatchDrawable helpDialog;
	public static NinePatchDrawable helpDialogInverted;

	public static TextureRegionDrawable backgroundBigWindow;
	public static TextureRegionDrawable backgroundSmallWindow;

	public static TouchpadStyle touchPadStyle;
	public static TextButtonStyle styleTextButtonBuy;
	public static TextButtonStyle styleTextButtontLevel;
	public static TextButtonStyle styleTextButtontLevelLocked;
	public static TextButtonStyle styleTextButtonPurchased;
	public static ScrollPaneStyle styleScrollPane;

	public static ButtonStyle styleButtonMusic;
	public static ButtonStyle styleButtonSound;

	public static LabelStyle labelStyleChico;
	public static LabelStyle labelStyleGrande;
	public static LabelStyle labelStyleHelpDialog;
	public static SliderStyle sliderStyle;

	public static NinePatchDrawable pixelNegro;

	public static Sound shoot1;
	public static Sound zombiePan;
	public static Sound zombieKid;
	public static Sound zombieCuasy;
	public static Sound zombieMummy;
	public static Sound zombieFrank;

	public static Sound hurt1;
	public static Sound hurt2;
	public static Sound hurt3;
	public static Sound gem;
	public static Sound skull;
	public static Sound jump;
	public static Sound shield;
	public static Sound hearth;

	public static void loadStyles(TextureAtlas atlas) {
		// Label Style
		labelStyleChico = new LabelStyle(fontChico, Color.WHITE);
		labelStyleGrande = new LabelStyle(fontGrande, Color.WHITE);
		labelStyleHelpDialog = new LabelStyle(fontChico, Color.BLACK);

		// Touch Pad
		TextureRegionDrawable pad = new TextureRegionDrawable(atlas.findRegion("pad"));
		TextureRegionDrawable knob = new TextureRegionDrawable(atlas.findRegion("knob"));
		touchPadStyle = new TouchpadStyle(pad, knob);

		/* Button Buy */
		TextureRegionDrawable btBuy = new TextureRegionDrawable(atlas.findRegion("UI/btBuy"));
		styleTextButtonBuy = new TextButtonStyle(btBuy, null, null, fontChico);

		/* Button Purchased */
		TextureRegionDrawable btPurchased = new TextureRegionDrawable(atlas.findRegion("UI/btPurchased"));
		styleTextButtonPurchased = new TextButtonStyle(btPurchased, null, null, fontChico);
		/* Button level */
		TextureRegionDrawable btLevel = new TextureRegionDrawable(atlas.findRegion("UI/btLevel"));
		styleTextButtontLevel = new TextButtonStyle(btLevel, null, null, fontGrande);

		/* Button level locked */
		TextureRegionDrawable btLevelLocked = new TextureRegionDrawable(atlas.findRegion("UI/btLevelLocked"));
		styleTextButtontLevelLocked = new TextButtonStyle(btLevelLocked, null, null, fontGrande);

		/* Button Musica */
		TextureRegionDrawable btMusicOn = new TextureRegionDrawable(atlas.findRegion("UI/btMusic"));
		TextureRegionDrawable btMusicOff = new TextureRegionDrawable(atlas.findRegion("UI/btMusicOff"));
		styleButtonMusic = new ButtonStyle(btMusicOn, null, btMusicOff);

		/* Boton Sonido */
		TextureRegionDrawable botonSonidoOn = new TextureRegionDrawable(atlas.findRegion("UI/btSound"));
		TextureRegionDrawable botonSonidoOff = new TextureRegionDrawable(atlas.findRegion("UI/btSoundOff"));
		styleButtonSound = new ButtonStyle(botonSonidoOn, null, botonSonidoOff);

		// Scrollpane
		TextureRegionDrawable separadorVertical = new TextureRegionDrawable(atlas.findRegion("UI/separadorVertical"));
		TextureRegionDrawable knobScroll = new TextureRegionDrawable(atlas.findRegion("knob"));
		styleScrollPane = new ScrollPaneStyle(null, null, null, separadorVertical, knobScroll);

		TextureRegionDrawable separadorHorizontal = new TextureRegionDrawable(atlas.findRegion("UI/separadorHorizontal"));
		sliderStyle = new SliderStyle(separadorHorizontal, knobScroll);

		pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0));
	}

	public static void load() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

		fontChico = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"));
		fontGrande = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"));

		loadStyles(atlas);

		background = atlas.findRegion("background");
		moon = atlas.findRegion("moon");

		backBackground = atlas.findRegion("backBackground");
		backgroundBigWindow = new TextureRegionDrawable(atlas.findRegion("UI/ventanaGrande"));
		backgroundSmallWindow = new TextureRegionDrawable(atlas.findRegion("UI/ventanaChica"));

		zombieKillerTitulo = new TextureRegionDrawable(atlas.findRegion("UI/titulo"));
		skullBarBackground = new TextureRegionDrawable(atlas.findRegion("skullBar"));
		storeTableBackground = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/storeTableBackground"), 50, 40, 50, 40));
		helpDialog = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/helpDialog"), 56, 17, 15, 50));
		helpDialogInverted = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/helpDialogInverted"), 56, 17, 50, 15));

		containerButtons = new TextureRegionDrawable(atlas.findRegion("UI/containerButtons"));
		btPlay = new TextureRegionDrawable(atlas.findRegion("UI/btPlay"));
		btLeaderboard = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
		btAchievement = new TextureRegionDrawable(atlas.findRegion("UI/btAchievement"));
		btSettings = new TextureRegionDrawable(atlas.findRegion("UI/btSettings"));

		btHelp = new TextureRegionDrawable(atlas.findRegion("UI/btHelp"));
		btFacebook = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
		btTwitter = new TextureRegionDrawable(atlas.findRegion("UI/btTwitter"));
		btShop = new TextureRegionDrawable(atlas.findRegion("UI/btShop"));
		btClose = new TextureRegionDrawable(atlas.findRegion("UI/btClose"));
		btMenu = new TextureRegionDrawable(atlas.findRegion("UI/btMenu"));
		btTryAgain = new TextureRegionDrawable(atlas.findRegion("UI/btTryAgain"));
		btPause = new TextureRegionDrawable(atlas.findRegion("btPause"));
		btPlayer = new TextureRegionDrawable(atlas.findRegion("UI/btPlayer"));
		btGems = new TextureRegionDrawable(atlas.findRegion("UI/btGems"));
		btFire = new TextureRegionDrawable(atlas.findRegion("btFire"));
		btMore = new TextureRegionDrawable(atlas.findRegion("UI/btMore"));
		upgradeOff = new TextureRegionDrawable(atlas.findRegion("UI/upgradeOff"));
		btUp = new TextureRegionDrawable(atlas.findRegion("UI/btUp"));
		btUpPress = new TextureRegionDrawable(atlas.findRegion("UI/btUpPress"));
		btRight = new TextureRegionDrawable(atlas.findRegion("UI/btRight"));
		btRightPress = new TextureRegionDrawable(atlas.findRegion("UI/btRightPress"));
		btLeft = new TextureRegionDrawable(atlas.findRegion("UI/btLeft"));
		btLeftPress = new TextureRegionDrawable(atlas.findRegion("UI/btLeftPress"));
		btDown = new TextureRegionDrawable(atlas.findRegion("UI/btDown"));
		btDownPress = new TextureRegionDrawable(atlas.findRegion("UI/btDownPress"));

		weapon = atlas.findRegion("UI/weapon");
		itemCollection = atlas.findRegion("UI/itemCollection");
		backgroundProgressBar = new TextureRegionDrawable(atlas.findRegion("backgroundProgressBar"));

		/**
		 * Bullets
		 */
		bullet1 = loadAnimationBullet(atlas, "Bullet/bullet1");
		bullet2 = loadAnimationBullet(atlas, "Bullet/bullet2");
		bullet3 = loadAnimationBullet(atlas, "Bullet/bullet3");
		bullet4 = loadAnimationBullet(atlas, "Bullet/bullet4");
		bullet5 = loadAnimationBullet(atlas, "Bullet/bullet5");
		muzzle = loadAnimationMuzzle(atlas, "Bullet/");
		/**
		 * Items
		 */
		itemGem = atlas.findRegion("gem");
		itemHeart = atlas.findRegion("heart");
		itemMeat = atlas.findRegion("meat");
		itemSkull = atlas.findRegion("skull");
		itemShield = atlas.findRegion("shield");
		itemStar = atlas.findRegion("star");

		crate = atlas.findRegion("crate");
		saw = atlas.findRegion("saw");

		redBar = atlas.findRegion("redBar");
		whiteBar = atlas.findRegion("whiteBar");

		/**
		 * HeroForce
		 */
		heroForceClimb = loadAnimationClimb(atlas, "HeroForce/");
		heroForceDie = loadAnimationDie(atlas, "HeroForce/");
		heroForceHurt = atlas.createSprite("HeroForce/hurt");
		heroForceIdle = atlas.createSprite("HeroForce/idle");
		heroForceShoot = loadAnimationShoot(atlas, "HeroForce/");
		heroForceWalk = loadAnimationWalk(atlas, "HeroForce/");

		/**
		 * HeroRambo
		 */
		heroRamboClimb = loadAnimationClimb(atlas, "HeroRambo/");
		heroRamboDie = loadAnimationDie(atlas, "HeroRambo/");
		heroRamboHurt = atlas.createSprite("HeroRambo/hurt");
		heroRamboIdle = atlas.createSprite("HeroRambo/idle");
		heroRamboShoot = loadAnimationShoot(atlas, "HeroRambo/");
		heroRamboWalk = loadAnimationWalk(atlas, "HeroRambo/");

		/**
		 * HeroSoldier
		 */
		heroSoldierClimb = loadAnimationClimb(atlas, "HeroSoldier/");
		heroSoldierDie = loadAnimationDie(atlas, "HeroSoldier/");
		heroSoldierHurt = atlas.createSprite("HeroSoldier/hurt");
		heroSoldierIdle = atlas.createSprite("HeroSoldier/idle");
		heroSoldierShoot = loadAnimationShoot(atlas, "HeroSoldier/");
		heroSoldierWalk = loadAnimationWalk(atlas, "HeroSoldier/");

		/**
		 * HeroSwat
		 */
		heroSwatClimb = loadAnimationClimb(atlas, "HeroSwat/");
		heroSwatDie = loadAnimationDie(atlas, "HeroSwat/");
		heroSwatHurt = atlas.createSprite("HeroSwat/hurt");
		heroSwatIdle = atlas.createSprite("HeroSwat/idle");
		heroSwatShoot = loadAnimationShoot(atlas, "HeroSwat/");
		heroSwatWalk = loadAnimationWalk(atlas, "HeroSwat/");

		/**
		 * HeroVader
		 */
		heroVaderClimb = loadAnimationClimb(atlas, "HeroVader/");
		heroVaderDie = loadAnimationDie(atlas, "HeroVader/");
		heroVaderHurt = atlas.createSprite("HeroVader/hurt");
		heroVaderIdle = atlas.createSprite("HeroVader/idle");
		heroVaderShoot = loadAnimationShoot(atlas, "HeroVader/");
		heroVaderWalk = loadAnimationWalk(atlas, "HeroVader/");

		/**
		 * Zombie kid
		 */
		zombieKidWalk = loadAnimationWalk(atlas, "ZombieKid/");
		zombieKidIdle = loadAnimationIdle(atlas, "ZombieKid/");
		zombieKidRise = loadAnimationRise(atlas, "ZombieKid/");
		zombieKidDie = loadAnimationDie(atlas, "ZombieKid/");
		zombieKidHurt = atlas.createSprite("ZombieKid/die1");

		/**
		 * Zombie pan
		 */
		zombiePanWalk = loadAnimationWalk(atlas, "ZombiePan/");
		zombiePanIdle = loadAnimationIdle(atlas, "ZombiePan/");
		zombiePanRise = loadAnimationRise(atlas, "ZombiePan/");
		zombiePanDie = loadAnimationDie(atlas, "ZombiePan/");
		zombiePanHurt = atlas.createSprite("ZombiePan/die1");

		/**
		 * Zombie Cuasy
		 */
		zombieCuasyWalk = loadAnimationWalk(atlas, "ZombieCuasy/");
		zombieCuasyIdle = loadAnimationIdle(atlas, "ZombieCuasy/");
		zombieCuasyRise = loadAnimationRise(atlas, "ZombieCuasy/");
		zombieCuasyDie = loadAnimationDie(atlas, "ZombieCuasy/");
		zombieCuasyHurt = atlas.createSprite("ZombieCuasy/die1");

		/**
		 * Zombie Frank
		 */
		zombieFrankWalk = loadAnimationWalk(atlas, "ZombieFrank/");
		zombieFrankIdle = loadAnimationIdle(atlas, "ZombieFrank/");
		zombieFrankRise = loadAnimationRise(atlas, "ZombieFrank/");
		zombieFrankDie = loadAnimationDie(atlas, "ZombieFrank/");
		zombieFrankHurt = atlas.createSprite("ZombieFrank/die1");

		/**
		 * Zombie mummy
		 */
		zombieMummyWalk = loadAnimationWalk(atlas, "ZombieMummy/");
		zombieMummyIdle = loadAnimationIdle(atlas, "ZombieMummy/");
		zombieMummyRise = loadAnimationRise(atlas, "ZombieMummy/");
		zombieMummyDie = loadAnimationDie(atlas, "ZombieMummy/");
		zombieMummyHurt = atlas.createSprite("ZombieMummy/die1");

		Settings.load();

		shoot1 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/shoot2.mp3"));
		zombiePan = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombiePan.mp3"));
		zombieKid = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombieKid.mp3"));
		zombieCuasy = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombieCuasy.mp3"));
		zombieMummy = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombieMummy.mp3"));
		zombieFrank = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombieFrank.mp3"));

		hurt1 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hurt.mp3"));
		hurt2 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hurt2.mp3"));
		hurt3 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hurt3.mp3"));

		gem = Gdx.audio.newSound(Gdx.files.internal("data/sounds/gem.mp3"));
		skull = Gdx.audio.newSound(Gdx.files.internal("data/sounds/skull.mp3"));
		jump = Gdx.audio.newSound(Gdx.files.internal("data/sounds/jump.mp3"));
		shield = Gdx.audio.newSound(Gdx.files.internal("data/sounds/pick.mp3"));
		hearth = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hearth.mp3"));

		// 2

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

	private static AnimationSprite loadAnimationIdle(TextureAtlas atlas, String ruta) {
		Array<Sprite> arrSprites = new Array<Sprite>();

		int i = 1;
		Sprite obj = null;
		do {
			obj = atlas.createSprite(ruta + "idle" + i);
			i++;
			if (obj != null)
				arrSprites.add(obj);
		} while (obj != null);

		float time = .05f * arrSprites.size;
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

		float time = .01875f * arrSprites.size;
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

	private static AnimationSprite loadAnimationClimb(TextureAtlas atlas, String ruta) {
		Array<Sprite> arrSprites = new Array<Sprite>();

		int i = 1;
		Sprite obj = null;
		do {
			obj = atlas.createSprite(ruta + "climb" + i);
			i++;
			if (obj != null)
				arrSprites.add(obj);
		} while (obj != null);

		float time = .03f * arrSprites.size;
		return new AnimationSprite(time, arrSprites);
	}

	private static AnimationSprite loadAnimationShoot(TextureAtlas atlas, String ruta) {
		Array<Sprite> arrSprites = new Array<Sprite>();

		int i = 1;
		Sprite obj = null;
		do {
			obj = atlas.createSprite(ruta + "shoot" + i);
			i++;
			if (obj != null)
				arrSprites.add(obj);
		} while (obj != null);

		float time = .0095f * arrSprites.size;
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

	public static void loadTiledMap(int numMap) {
		Gdx.app.log("Inside TEst", "TEST");
		Gdx.app.debug("Inside TEst", "TEST");
		Gdx.app.log("Inside NORMAL", "NORMAL");
		Gdx.app.debug("Inside NORMAL", "NORMAL");
		if (map != null) {
			map.dispose();
			map = null;
		}

		if (Settings.isTest) {
			Gdx.app.log("Inside TEst", "TEST");
			Gdx.app.debug("Inside TEst", "TEST");
			map = new TmxMapLoader().load("data/mapsTest/map" + numMap + ".tmx");
		}
		else {
			Gdx.app.log("Inside NORMAL", "NORMAL");
			Gdx.app.debug("Inside NORMAL", "NORMAL");
			map = new AtlasTmxMapLoader().load("data/maps/map" + numMap + ".tmx");
		}
	}

	public static void playSound(Sound sonido, float volume) {
		if (Settings.isSoundOn) {
			sonido.play(volume);
		}
	}

}
