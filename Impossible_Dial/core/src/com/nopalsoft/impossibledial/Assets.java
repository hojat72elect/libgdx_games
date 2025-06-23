package com.nopalsoft.impossibledial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

public class Assets {

    public static I18NBundle idiomas;

    public static BitmapFont fontChico;

    public static TextureRegionDrawable titulo;
    public static AtlasRegion header;


    public static NinePatchDrawable pixelNegro;
    public static NinePatchDrawable dialogRanking;
    public static NinePatchDrawable dialogVentana;

    public static TextureRegionDrawable btFacebook;

    public static TextureRegionDrawable one;
    public static TextureRegionDrawable two;
    public static TextureRegionDrawable three;

    public static TextureRegionDrawable arrowGreen;
    public static TextureRegionDrawable arrowRed;
    public static TextureRegionDrawable arrowBlue;
    public static TextureRegionDrawable arrowYellow;
    public static TextureRegionDrawable arrowBlack;
    public static TextureRegionDrawable arrowBrown;
    public static TextureRegionDrawable arrowPurple;
    public static TextureRegionDrawable arrowCyan;
    public static TextureRegionDrawable circle;
    public static TextureRegionDrawable circleHard;


    public static TextureRegionDrawable btRate;
    public static TextureRegionDrawable btAchievement;
    public static TextureRegionDrawable btLeaderboard;
    public static TextureRegionDrawable btTrue;
    public static TextureRegionDrawable btFalse;
    public static TextureRegionDrawable btBack;
    public static TextureRegionDrawable btTryAgain;
    public static TextureRegionDrawable btShare;

    public static NinePatchDrawable btJugar;
    public static NinePatchDrawable btEnabled;
    public static NinePatchDrawable btDisabled;
    public static TextureRegionDrawable play;


    public static TextButtonStyle txtButtonStyle;

    private static TextureAtlas atlas;

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontChico = new BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("font32"));
        fontChico.getData().markupEnabled = true;

        titulo = new TextureRegionDrawable(atlas.findRegion("titulo"));
        header = atlas.findRegion("header");

        pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
        dialogRanking = new NinePatchDrawable(new NinePatch(atlas.findRegion("dialogRanking"), 40, 40, 63, 30));
        dialogVentana = new NinePatchDrawable(new NinePatch(atlas.findRegion("dialogVentana"), 33, 33, 33, 33));

        btJugar = new NinePatchDrawable(new NinePatch(atlas.findRegion("btJugar"), 30, 30, 25, 25));
        btEnabled = new NinePatchDrawable(new NinePatch(atlas.findRegion("btEnabled"), 9, 9, 7, 7));
        btDisabled = new NinePatchDrawable(new NinePatch(atlas.findRegion("btDisabled"), 9, 9, 7, 7));
        play = new TextureRegionDrawable(atlas.findRegion("play"));

        btFacebook = new TextureRegionDrawable(atlas.findRegion("btFacebook"));


        one = new TextureRegionDrawable(atlas.findRegion("one"));
        two = new TextureRegionDrawable(atlas.findRegion("two"));
        three = new TextureRegionDrawable(atlas.findRegion("three"));

        arrowBlue = new TextureRegionDrawable(atlas.findRegion("arrowBlue"));
        arrowGreen = new TextureRegionDrawable(atlas.findRegion("arrowGreen"));
        arrowRed = new TextureRegionDrawable(atlas.findRegion("arrowRed"));
        arrowYellow = new TextureRegionDrawable(atlas.findRegion("arrowYellow"));
        arrowBlack = new TextureRegionDrawable(atlas.findRegion("arrowNegro"));
        arrowBrown = new TextureRegionDrawable(atlas.findRegion("arrowBrown"));
        arrowPurple = new TextureRegionDrawable(atlas.findRegion("arrowMorado"));
        arrowCyan = new TextureRegionDrawable(atlas.findRegion("arrowCyan"));
        circle = new TextureRegionDrawable(atlas.findRegion("circulo"));
        circleHard = new TextureRegionDrawable(atlas.findRegion("circuloHard"));


        btRate = new TextureRegionDrawable(atlas.findRegion("btRate"));
        btAchievement = new TextureRegionDrawable(atlas.findRegion("btAchievement"));
        btLeaderboard = new TextureRegionDrawable(atlas.findRegion("btLeaderboard"));
        btTrue = new TextureRegionDrawable(atlas.findRegion("btTrue"));
        btFalse = new TextureRegionDrawable(atlas.findRegion("btFalse"));
        btBack = new TextureRegionDrawable(atlas.findRegion("btBack"));
        btTryAgain = new TextureRegionDrawable(atlas.findRegion("btTryAgain"));
        btShare = new TextureRegionDrawable(atlas.findRegion("btShare"));

        txtButtonStyle = new TextButtonStyle(btDisabled, btEnabled, btEnabled, fontChico);


        idiomas = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));

    }


}
