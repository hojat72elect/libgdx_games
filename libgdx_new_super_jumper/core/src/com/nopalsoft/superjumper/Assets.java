package com.nopalsoft.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

	public static BitmapFont fontChico;
	public static BitmapFont fontGrande;

	public static AtlasRegion fondo;
	public static TextureRegionDrawable titulo;

	/**
	 * Personaje
	 */
	public static AtlasRegion personajeJump;
	public static AtlasRegion personajeStand;
	public static Animation<TextureRegion> personajeWalk;

	public static AtlasRegion coin;
	public static AtlasRegion gun;
	public static AtlasRegion bullet;
	public static AtlasRegion spring;
	public static AtlasRegion bubbleSmall;
	public static AtlasRegion jetpackSmall;
	public static AtlasRegion bubble;
	public static AtlasRegion jetpack;
	public static Animation<TextureRegion> jetpackFire;

	public static Animation<TextureRegion> enemigo;

	public static AtlasRegion nubeHappy;
	public static AtlasRegion nubeAngry;

	public static Animation<TextureRegion> rayo;
	public static AtlasRegion nubeViento;
	/**
	 * Plataformas
	 */

	public static AtlasRegion plataformaBeige;
	public static AtlasRegion plataformaBeigeLight;
	public static AtlasRegion plataformaBeigeBroken;
	public static AtlasRegion plataformaBeigeLeft;
	public static AtlasRegion plataformaBeigeRight;

	public static AtlasRegion plataformaBlue;
	public static AtlasRegion plataformaBlueLight;
	public static AtlasRegion plataformaBlueBroken;
	public static AtlasRegion plataformaBlueLeft;
	public static AtlasRegion plataformaBlueRight;

	public static AtlasRegion plataformaGray;
	public static AtlasRegion plataformaGrayLight;
	public static AtlasRegion plataformaGrayBroken;
	public static AtlasRegion plataformaGrayLeft;
	public static AtlasRegion plataformaGrayRight;
	public static AtlasRegion plataformaGreen;
	public static AtlasRegion plataformaGreenLight;
	public static AtlasRegion plataformaGreenBroken;
	public static AtlasRegion plataformaGreenLeft;
	public static AtlasRegion plataformaGreenRight;

	public static AtlasRegion plataformaMulticolor;
	public static AtlasRegion plataformaMulticolorLight;
	public static AtlasRegion plataformaMulticolorBroken;
	public static AtlasRegion plataformaMulticolorLeft;
	public static AtlasRegion plataformaMulticolorRight;

	public static AtlasRegion plataformaPink;
	public static AtlasRegion plataformaPinkLight;
	public static AtlasRegion plataformaPinkBroken;
	public static AtlasRegion plataformaPinkLeft;
	public static AtlasRegion plataformaPinkRight;

	public static TextureRegionDrawable btPause;

	public static LabelStyle labelStyleChico;
	public static LabelStyle labelStyleGrande;
	public static TextButtonStyle textButtonStyleGrande;

	public static NinePatchDrawable pixelNegro;

	public static void loadStyles(TextureAtlas atlas) {
		// Label Style
		labelStyleChico = new LabelStyle(fontChico, Color.WHITE);
		labelStyleGrande = new LabelStyle(fontGrande, Color.WHITE);

		TextureRegionDrawable button = new TextureRegionDrawable(atlas.findRegion("button"));
		textButtonStyleGrande = new TextButtonStyle(button, button, null, fontGrande);

		pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
	}

	public static void load() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

		// fontChico = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"));
		fontChico = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"));
		fontGrande = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"));

		loadStyles(atlas);

		btPause = new TextureRegionDrawable(atlas.findRegion("btPause"));

		fondo = atlas.findRegion("Background");
		titulo = new TextureRegionDrawable(atlas.findRegion("titulo"));

		/**
		 * Personaje
		 */

		personajeJump = atlas.findRegion("personajeJump");
		personajeStand = atlas.findRegion("personajeStand");

		AtlasRegion walk1 = atlas.findRegion("personajeWalk1");
		AtlasRegion walk2 = atlas.findRegion("personajeWalk2");
		personajeWalk = new Animation(.5f, walk1, walk2);

		coin = atlas.findRegion("Coin");
		gun = atlas.findRegion("Pistol");
		bullet = atlas.findRegion("Bullet");
		spring = atlas.findRegion("Spring");
		bubbleSmall = atlas.findRegion("Bubble_Small");
		jetpackSmall = atlas.findRegion("Jetpack_Small");
		bubble = atlas.findRegion("Bubble_Big");
		jetpack = atlas.findRegion("Jetpack_Big");

		AtlasRegion jetpackFire1 = atlas.findRegion("JetFire1");
		AtlasRegion jetpackFire2 = atlas.findRegion("JetFire2");
		jetpackFire = new Animation(.085f, jetpackFire1, jetpackFire2);

		AtlasRegion enemigo1 = atlas.findRegion("HearthEnemy1");
		AtlasRegion enemigo2 = atlas.findRegion("HearthEnemy2");
		enemigo = new Animation(.2f, enemigo1, enemigo2);

		nubeHappy = atlas.findRegion("HappyCloud");
		nubeAngry = atlas.findRegion("AngryCloud");
		nubeViento = atlas.findRegion("CloudWind");

		AtlasRegion lightning1 = atlas.findRegion("Lightning1");
		AtlasRegion lightning2 = atlas.findRegion("Lightning2");
		rayo = new Animation(.08f, lightning1, lightning2);

		/**
		 * Plataformas
		 */

		plataformaBeige = atlas.findRegion("LandPiece_DarkBeige");
		plataformaBeigeLight = atlas.findRegion("LandPiece_LightBeige");
		plataformaBeigeBroken = atlas.findRegion("BrokenLandPiece_Beige");
		plataformaBeigeLeft = atlas.findRegion("HalfLandPiece_Left_Beige");
		plataformaBeigeRight = atlas.findRegion("HalfLandPiece_Right_Beige");

		plataformaBlue = atlas.findRegion("LandPiece_DarkBlue");
		plataformaBlueLight = atlas.findRegion("LandPiece_LightBlue");
		plataformaBlueBroken = atlas.findRegion("BrokenLandPiece_Blue");
		plataformaBlueLeft = atlas.findRegion("HalfLandPiece_Left_Blue");
		plataformaBlueRight = atlas.findRegion("HalfLandPiece_Right_Blue");

		plataformaGray = atlas.findRegion("LandPiece_DarkGray");
		plataformaGrayLight = atlas.findRegion("LandPiece_LightGray");
		plataformaGrayBroken = atlas.findRegion("BrokenLandPiece_Gray");
		plataformaGrayLeft = atlas.findRegion("HalfLandPiece_Left_Gray");
		plataformaGrayRight = atlas.findRegion("HalfLandPiece_Right_Gray");

		plataformaGreen = atlas.findRegion("LandPiece_DarkGreen");
		plataformaGreenLight = atlas.findRegion("LandPiece_LightGreen");
		plataformaGreenBroken = atlas.findRegion("BrokenLandPiece_Green");
		plataformaGreenLeft = atlas.findRegion("HalfLandPiece_Left_Green");
		plataformaGreenRight = atlas.findRegion("HalfLandPiece_Right_Green");

		plataformaMulticolor = atlas.findRegion("LandPiece_DarkMulticolored");
		plataformaMulticolorLight = atlas.findRegion("LandPiece_LightMulticolored");
		plataformaMulticolorBroken = atlas.findRegion("BrokenLandPiece_Multicolored");
		plataformaMulticolorLeft = atlas.findRegion("HalfLandPiece_Left_Multicolored");
		plataformaMulticolorRight = atlas.findRegion("HalfLandPiece_Right_Multicolored");

		plataformaPink = atlas.findRegion("LandPiece_DarkPink");
		plataformaPinkLight = atlas.findRegion("LandPiece_LightPink");
		plataformaPinkBroken = atlas.findRegion("BrokenLandPiece_Pink");
		plataformaPinkLeft = atlas.findRegion("HalfLandPiece_Left_Pink");
		plataformaPinkRight = atlas.findRegion("HalfLandPiece_Right_Pink");

	}
}
