package com.nopalsoft.thetruecolor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class Assets {

    public static I18NBundle idiomas;

    public static BitmapFont fontChico;
    public static BitmapFont fontExtraGrande;

    public static TextureRegionDrawable titulo;
    public static AtlasRegion header;

    public static NinePatchDrawable pixelNegro;
    public static NinePatchDrawable barTimer;
    public static NinePatchDrawable dialogRanking;
    public static NinePatchDrawable dialogVentana;

    public static TextureRegionDrawable photoFrame;
    public static TextureRegionDrawable btFacebook;
    public static NinePatchDrawable btFacebookText;
    public static TextureRegionDrawable btGoogle;
    public static NinePatchDrawable btGoogleText;
    public static TextureRegionDrawable btAmazon;

    public static TextureRegionDrawable one;
    public static TextureRegionDrawable two;
    public static TextureRegionDrawable three;

    public static TextureRegionDrawable btRate;
    public static TextureRegionDrawable btAchievement;
    public static TextureRegionDrawable btLeaderboard;
    public static TextureRegionDrawable btHelp;
    public static TextureRegionDrawable btTrue;
    public static TextureRegionDrawable btFalse;
    public static TextureRegionDrawable btBack;
    public static TextureRegionDrawable btTryAgain;
    public static TextureRegionDrawable btShare;

    public static NinePatchDrawable btJugar;
    public static NinePatchDrawable btEnabled;
    public static NinePatchDrawable btDisabled;
    public static TextureRegionDrawable play;

    public static TextureRegionDrawable flagChinese_TW;
    public static TextureRegionDrawable flagRussian;
    public static TextureRegionDrawable flagSpanish;
    public static TextureRegionDrawable flagEnglish;
    public static TextureRegionDrawable flagFench;
    public static TextureRegionDrawable flagJapanese;
    public static TextureRegionDrawable flagPortugese;
    public static TextureRegionDrawable flagMore;

    public static TextButtonStyle txtButtonStyle;

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontChico = new BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("font32"));
        fontChico.getData().markupEnabled = true;

        fontExtraGrande = new BitmapFont(Gdx.files.internal("data/font100.fnt"), atlas.findRegion("font100"));

        titulo = new TextureRegionDrawable(atlas.findRegion("titulo"));
        header = atlas.findRegion("header");

        pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
        barTimer = new NinePatchDrawable(new NinePatch(atlas.findRegion("barTimer"), 4, 4, 5, 4));
        dialogRanking = new NinePatchDrawable(new NinePatch(atlas.findRegion("dialogRanking"), 40, 40, 63, 30));
        dialogVentana = new NinePatchDrawable(new NinePatch(atlas.findRegion("dialogVentana"), 33, 33, 33, 33));

        btJugar = new NinePatchDrawable(new NinePatch(atlas.findRegion("btJugar"), 30, 30, 25, 25));
        btEnabled = new NinePatchDrawable(new NinePatch(atlas.findRegion("btEnabled"), 9, 9, 7, 7));
        btDisabled = new NinePatchDrawable(new NinePatch(atlas.findRegion("btDisabled"), 9, 9, 7, 7));
        play = new TextureRegionDrawable(atlas.findRegion("play"));

        btFacebook = new TextureRegionDrawable(atlas.findRegion("btFacebook"));
        btFacebookText = new NinePatchDrawable(new NinePatch(atlas.findRegion("btFacebookText"), 55, 20, 0, 0));
        btGoogle = new TextureRegionDrawable(atlas.findRegion("btGoogle"));
        btGoogleText = new NinePatchDrawable(new NinePatch(atlas.findRegion("btGoogleText"), 60, 20, 0, 0));
        btAmazon = new TextureRegionDrawable(atlas.findRegion("btAmazon"));
        photoFrame = new TextureRegionDrawable(atlas.findRegion("photoFrame"));

        one = new TextureRegionDrawable(atlas.findRegion("one"));
        two = new TextureRegionDrawable(atlas.findRegion("two"));
        three = new TextureRegionDrawable(atlas.findRegion("three"));

        btRate = new TextureRegionDrawable(atlas.findRegion("btRate"));
        btAchievement = new TextureRegionDrawable(atlas.findRegion("btAchievement"));
        btLeaderboard = new TextureRegionDrawable(atlas.findRegion("btLeaderboard"));
        btHelp = new TextureRegionDrawable(atlas.findRegion("btHelp"));
        btTrue = new TextureRegionDrawable(atlas.findRegion("btTrue"));
        btFalse = new TextureRegionDrawable(atlas.findRegion("btFalse"));
        btBack = new TextureRegionDrawable(atlas.findRegion("btBack"));
        btTryAgain = new TextureRegionDrawable(atlas.findRegion("btTryAgain"));
        btShare = new TextureRegionDrawable(atlas.findRegion("btShare"));

        txtButtonStyle = new TextButtonStyle(btDisabled, btEnabled, btEnabled, fontChico);

        flagChinese_TW = new TextureRegionDrawable(atlas.findRegion("flags/flag_twd"));
        flagEnglish = new TextureRegionDrawable(atlas.findRegion("flags/flag_gbp"));
        flagSpanish = new TextureRegionDrawable(atlas.findRegion("flags/flag_esp"));
        flagRussian = new TextureRegionDrawable(atlas.findRegion("flags/flag_rub"));
        flagFench = new TextureRegionDrawable(atlas.findRegion("flags/flag_frf"));
        flagJapanese = new TextureRegionDrawable(atlas.findRegion("flags/flag_jpy"));
        flagPortugese = new TextureRegionDrawable(atlas.findRegion("flags/flag_pte"));
        flagMore = new TextureRegionDrawable(atlas.findRegion("flags/flag_more"));

        loadAssetsWithSettings();
    }

    public static void loadAssetsWithSettings() {
        Locale locale;
        switch (com.nopalsoft.thetruecolor.Settings.selectedLanguage) {
            case ENGLISH:
                locale = Locale.ROOT;
                break;
            case SPANISH:
                locale = new Locale("es");
                break;
            case RUSSIAN:
                locale = new Locale("ru");
                break;
            case FRENCH:
                locale = new Locale("fr");
                break;
            case JAPANESE:
                locale = new Locale("ja");
                break;
            case PORTUGUESE:
                locale = new Locale("pt");
                break;
            case CHINESE_TAIWAN:
                locale = new Locale("zh", "TW");
                break;
            case DEFAULT:
            default:
                locale = Locale.getDefault();
                break;
        }

        idiomas = I18NBundle.createBundle(Gdx.files.internal("strings/strings"), locale);
    }
}
