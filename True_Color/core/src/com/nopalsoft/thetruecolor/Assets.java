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

    public static I18NBundle languagesBundle;

    public static BitmapFont fontSmall;
    public static BitmapFont fontLarge;


    public static AtlasRegion header;

    public static NinePatchDrawable blackPixelDrawable;

    public static NinePatchDrawable barTimerDrawable;

    public static NinePatchDrawable rankingDialogDrawable;
    public static NinePatchDrawable dialogDrawable;

    public static NinePatchDrawable buttonFacebookTextDrawable;
    public static NinePatchDrawable buttonGoogleTextDrawable;
    public static NinePatchDrawable buttonPlayDrawable;
    public static NinePatchDrawable buttonEnabledDrawable;
    public static NinePatchDrawable buttonDisabledDrawable;

    public static TextureRegionDrawable titleDrawable;

    public static TextureRegionDrawable photoFrameDrawable;

    public static TextureRegionDrawable buttonFacebookDrawable;
    public static TextureRegionDrawable buttonGoogleDrawable;
    public static TextureRegionDrawable buttonAmazonDrawable;

    public static TextureRegionDrawable oneDrawable;
    public static TextureRegionDrawable twoDrawable;
    public static TextureRegionDrawable threeDrawable;

    public static TextureRegionDrawable buttonRateDrawable;
    public static TextureRegionDrawable buttonAchievementDrawable;
    public static TextureRegionDrawable buttonLeaderBoardDrawable;
    public static TextureRegionDrawable buttonHelpDrawable;
    public static TextureRegionDrawable buttonTrueDrawable;
    public static TextureRegionDrawable buttonFalseDrawable;
    public static TextureRegionDrawable buttonBackDrawable;
    public static TextureRegionDrawable buttonTryAgainDrawable;
    public static TextureRegionDrawable buttonShareDrawable;

    public static TextureRegionDrawable playDrawable;

    public static TextureRegionDrawable flagChineseDrawable;
    public static TextureRegionDrawable flagRussianDrawable;
    public static TextureRegionDrawable flagSpanishDrawable;
    public static TextureRegionDrawable flagEnglishDrawable;
    public static TextureRegionDrawable flagFrenchDrawable;
    public static TextureRegionDrawable flagJapaneseDrawable;
    public static TextureRegionDrawable flagPortugueseDrawable;
    public static TextureRegionDrawable flagMoreDrawable;

    public static TextButtonStyle textButtonStyle;

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontSmall = new BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("font32"));
        fontSmall.getData().markupEnabled = true;

        fontLarge = new BitmapFont(Gdx.files.internal("data/font100.fnt"), atlas.findRegion("font100"));

        titleDrawable = new TextureRegionDrawable(atlas.findRegion("titulo"));
        header = atlas.findRegion("header");

        blackPixelDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
        barTimerDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("barTimer"), 4, 4, 5, 4));
        rankingDialogDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("dialogRanking"), 40, 40, 63, 30));
        dialogDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("dialogVentana"), 33, 33, 33, 33));

        buttonPlayDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("btJugar"), 30, 30, 25, 25));
        buttonEnabledDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("btEnabled"), 9, 9, 7, 7));
        buttonDisabledDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("btDisabled"), 9, 9, 7, 7));
        playDrawable = new TextureRegionDrawable(atlas.findRegion("play"));

        buttonFacebookDrawable = new TextureRegionDrawable(atlas.findRegion("btFacebook"));
        buttonFacebookTextDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("btFacebookText"), 55, 20, 0, 0));
        buttonGoogleDrawable = new TextureRegionDrawable(atlas.findRegion("btGoogle"));
        buttonGoogleTextDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("btGoogleText"), 60, 20, 0, 0));
        buttonAmazonDrawable = new TextureRegionDrawable(atlas.findRegion("btAmazon"));
        photoFrameDrawable = new TextureRegionDrawable(atlas.findRegion("photoFrame"));

        oneDrawable = new TextureRegionDrawable(atlas.findRegion("one"));
        twoDrawable = new TextureRegionDrawable(atlas.findRegion("two"));
        threeDrawable = new TextureRegionDrawable(atlas.findRegion("three"));

        buttonRateDrawable = new TextureRegionDrawable(atlas.findRegion("btRate"));
        buttonAchievementDrawable = new TextureRegionDrawable(atlas.findRegion("btAchievement"));
        buttonLeaderBoardDrawable = new TextureRegionDrawable(atlas.findRegion("btLeaderboard"));
        buttonHelpDrawable = new TextureRegionDrawable(atlas.findRegion("btHelp"));
        buttonTrueDrawable = new TextureRegionDrawable(atlas.findRegion("btTrue"));
        buttonFalseDrawable = new TextureRegionDrawable(atlas.findRegion("btFalse"));
        buttonBackDrawable = new TextureRegionDrawable(atlas.findRegion("btBack"));
        buttonTryAgainDrawable = new TextureRegionDrawable(atlas.findRegion("btTryAgain"));
        buttonShareDrawable = new TextureRegionDrawable(atlas.findRegion("btShare"));

        textButtonStyle = new TextButtonStyle(buttonDisabledDrawable, buttonEnabledDrawable, buttonEnabledDrawable, fontSmall);

        flagChineseDrawable = new TextureRegionDrawable(atlas.findRegion("flags/flag_twd"));
        flagEnglishDrawable = new TextureRegionDrawable(atlas.findRegion("flags/flag_gbp"));
        flagSpanishDrawable = new TextureRegionDrawable(atlas.findRegion("flags/flag_esp"));
        flagRussianDrawable = new TextureRegionDrawable(atlas.findRegion("flags/flag_rub"));
        flagFrenchDrawable = new TextureRegionDrawable(atlas.findRegion("flags/flag_frf"));
        flagJapaneseDrawable = new TextureRegionDrawable(atlas.findRegion("flags/flag_jpy"));
        flagPortugueseDrawable = new TextureRegionDrawable(atlas.findRegion("flags/flag_pte"));
        flagMoreDrawable = new TextureRegionDrawable(atlas.findRegion("flags/flag_more"));

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
            case CHINESE:
                locale = new Locale("zh", "TW");
                break;
            case DEFAULT:
            default:
                locale = Locale.getDefault();
                break;
        }

        languagesBundle = I18NBundle.createBundle(Gdx.files.internal("strings/strings"), locale);
    }
}
