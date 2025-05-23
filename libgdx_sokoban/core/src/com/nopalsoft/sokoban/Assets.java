package com.nopalsoft.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.sokoban.parallax.ParallaxBackground;
import com.nopalsoft.sokoban.parallax.ParallaxLayer;

public class Assets {

    public static BitmapFont font;
    public static BitmapFont fontRed;

    public static ParallaxBackground background;
    public static NinePatchDrawable blackPixelDrawable;

    public static TiledMap map;

    public static TextureRegionDrawable backgroundMoves;
    public static TextureRegionDrawable backgroundTime;

    public static TextureRegionDrawable buttonRightDrawable;
    public static TextureRegionDrawable buttonRightPressedDrawable;
    public static TextureRegionDrawable buttonLeftDrawable;
    public static TextureRegionDrawable buttonLeftPressedDrawable;
    public static TextureRegionDrawable buttonUpDrawable;
    public static TextureRegionDrawable buttonUpPressedDrawable;
    public static TextureRegionDrawable buttonDownDrawable;
    public static TextureRegionDrawable buttonDownPressedDrawable;
    public static TextureRegionDrawable buttonRefreshDrawable;
    public static TextureRegionDrawable buttonRefreshPressedDrawable;
    public static TextureRegionDrawable buttonPauseDrawable;
    public static TextureRegionDrawable buttonPausePressedDrawable;
    public static TextureRegionDrawable buttonLeaderboardDrawable;
    public static TextureRegionDrawable buttonLeaderboardPressedDrawable;
    public static TextureRegionDrawable buttonAchievementDrawable;
    public static TextureRegionDrawable buttonAchievementPressedDrawable;
    public static TextureRegionDrawable buttonFacebookDrawable;
    public static TextureRegionDrawable buttonFacebookPressedDrawable;
    public static TextureRegionDrawable buttonSettingsDrawable;
    public static TextureRegionDrawable buttonSettingsPressedDrawable;
    public static TextureRegionDrawable buttonMoreDrawable;
    public static TextureRegionDrawable buttonMorePressedDrawable;
    public static TextureRegionDrawable buttonCloseDrawable;
    public static TextureRegionDrawable buttonClosePressedDrawable;
    public static TextureRegionDrawable homeButtonDrawable;
    public static TextureRegionDrawable homeButtonPressedDrawable;
    public static TextureRegionDrawable buttonOffDrawable;
    public static TextureRegionDrawable buttonOnDrawable;
    public static TextureRegionDrawable buttonPlayDrawable;
    public static TextureRegionDrawable buttonPlayPressedDrawable;

    public static TextureRegionDrawable levelOffDrawable;
    public static TextureRegionDrawable levelStarDrawable;

    public static TextureRegionDrawable clockDrawable;

    public static AtlasRegion beigeBox;
    public static AtlasRegion darkBeigeBox;
    public static AtlasRegion blackBox;
    public static AtlasRegion darkBlackBox;
    public static AtlasRegion blueBox;
    public static AtlasRegion darkBlueBox;
    public static AtlasRegion brownBox;
    public static AtlasRegion darkBrownBox;
    public static AtlasRegion grayBox;
    public static AtlasRegion darkGrayBox;
    public static AtlasRegion purpleBox;
    public static AtlasRegion darkPurpleBox;
    public static AtlasRegion redBox;
    public static AtlasRegion darkRedBox;
    public static AtlasRegion yellowBox;
    public static AtlasRegion darkYellowBox;

    public static AtlasRegion beigeTargetPlatform;
    public static AtlasRegion blackTargetPlatform;
    public static AtlasRegion blueTargetPlatform;
    public static AtlasRegion brownTargetPlatform;
    public static AtlasRegion grayTargetPlatform;
    public static AtlasRegion purpleTargetPlatform;
    public static AtlasRegion redTargetPlatform;
    public static AtlasRegion yellowTargetPlatform;

    public static Animation<TextureRegion> playerUpAnimation;
    public static Animation<TextureRegion> playerDownAnimation;
    public static Animation<TextureRegion> playerLeftAnimation;
    public static Animation<TextureRegion> playerRightAnimation;
    public static AtlasRegion playerStand;

    static TextureAtlas atlas;

    public static TextureRegionDrawable windowBackground;

    public static TextButtonStyle styleTextButtonLevel;
    public static TextButtonStyle styleTextButtonLevelLocked;

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        font = new BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("UI/font32"));
        fontRed = new BitmapFont(Gdx.files.internal("data/font32Red.fnt"), atlas.findRegion("UI/font32Red"));

        loadUI();

        blackPixelDrawable = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));

        windowBackground = new TextureRegionDrawable(atlas.findRegion("UI/backgroundVentana"));

        beigeBox = atlas.findRegion("cajaBeige");
        darkBeigeBox = atlas.findRegion("cajaDarkBeige");
        blackBox = atlas.findRegion("cajaBlack");
        darkBlackBox = atlas.findRegion("cajaDarkBlack");
        blueBox = atlas.findRegion("cajaBlue");
        darkBlueBox = atlas.findRegion("cajaDarkBlue");
        brownBox = atlas.findRegion("cajaBrown");
        darkBrownBox = atlas.findRegion("cajaDarkBrown");
        grayBox = atlas.findRegion("cajaGray");
        darkGrayBox = atlas.findRegion("cajaDarkGray");
        purpleBox = atlas.findRegion("cajaPurple");
        darkPurpleBox = atlas.findRegion("cajaDarkPurple");
        redBox = atlas.findRegion("cajaRed");
        darkRedBox = atlas.findRegion("cajaDarkRed");
        yellowBox = atlas.findRegion("cajaYellow");
        darkYellowBox = atlas.findRegion("cajaDarkYellow");

        beigeTargetPlatform = atlas.findRegion("endPointBeige");
        blackTargetPlatform = atlas.findRegion("endPointBlack");
        blueTargetPlatform = atlas.findRegion("endPointBlue");
        brownTargetPlatform = atlas.findRegion("endPointBrown");
        grayTargetPlatform = atlas.findRegion("endPointGray");
        purpleTargetPlatform = atlas.findRegion("endPointPurple");
        redTargetPlatform = atlas.findRegion("endPointRed");
        yellowTargetPlatform = atlas.findRegion("endPointYellow");

        playerStand = atlas.findRegion("Character4");

        AtlasRegion up1 = atlas.findRegion("Character7");
        AtlasRegion up2 = atlas.findRegion("Character8");
        AtlasRegion up3 = atlas.findRegion("Character9");
        playerUpAnimation = new Animation<>(.09f, up2, up3, up1);

        AtlasRegion down1 = atlas.findRegion("Character4");
        AtlasRegion down2 = atlas.findRegion("Character5");
        AtlasRegion down3 = atlas.findRegion("Character6");
        playerDownAnimation = new Animation<>(.09f, down2, down3, down1);

        AtlasRegion right1 = atlas.findRegion("Character2");
        AtlasRegion right2 = atlas.findRegion("Character3");
        playerRightAnimation = new Animation<>(.09f, right1, right2, right1);

        AtlasRegion left1 = atlas.findRegion("Character1");
        AtlasRegion left2 = atlas.findRegion("Character10");
        playerLeftAnimation = new Animation<>(.09f, left1, left2, left1);

        AtlasRegion regioFondoFlip = atlas.findRegion("backgroundFlip");
        regioFondoFlip.flip(true, false);
        ParallaxLayer fondo = new ParallaxLayer(atlas.findRegion("background"), new Vector2(1, 0), new Vector2(0, 0), new Vector2(798, 480), 800, 480);
        ParallaxLayer fondoFlip = new ParallaxLayer(regioFondoFlip, new Vector2(1, 0), new Vector2(799, 0), new Vector2(798, 480), 800, 480);
        background = new ParallaxBackground(new ParallaxLayer[]{fondo, fondoFlip}, 800, 480, new Vector2(20, 0));
    }

    private static void loadUI() {
        buttonRightDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btDer"));
        buttonRightPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btDerPress"));
        buttonLeftDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btIzq"));
        buttonLeftPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btIzqPress"));
        buttonUpDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btUp"));
        buttonUpPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btUpPress"));
        buttonDownDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btDown"));
        buttonDownPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btDownPress"));
        buttonRefreshDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btRefresh"));
        buttonRefreshPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btRefreshPress"));
        buttonPauseDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btPausa"));
        buttonPausePressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btPausaPress"));
        buttonLeaderboardDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
        buttonLeaderboardPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"));
        buttonAchievementDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btAchievement"));
        buttonAchievementPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btAchievementPress"));
        buttonFacebookDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        buttonFacebookPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        buttonSettingsDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btSettings"));
        buttonSettingsPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btSettingsPress"));
        buttonMoreDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btMas"));
        buttonMorePressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btMasPress"));
        buttonCloseDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btClose"));
        buttonClosePressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btClosePress"));
        homeButtonDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btHome"));
        homeButtonPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btHomePress"));
        buttonOffDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btOff"));
        buttonOnDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btOn"));

        buttonPlayDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btPlay"));
        buttonPlayPressedDrawable = new TextureRegionDrawable(atlas.findRegion("UI/btPlayPress"));
        clockDrawable = new TextureRegionDrawable(atlas.findRegion("UI/clock"));

        levelOffDrawable = new TextureRegionDrawable(atlas.findRegion("UI/levelOff"));
        levelStarDrawable = new TextureRegionDrawable(atlas.findRegion("UI/levelStar"));

        // Button level
        TextureRegionDrawable btLevel = new TextureRegionDrawable(atlas.findRegion("UI/btLevel"));
        styleTextButtonLevel = new TextButtonStyle(btLevel, null, null, font);

        // Button level
        TextureRegionDrawable btLevelLocked = new TextureRegionDrawable(atlas.findRegion("UI/btLevelLocked"));
        styleTextButtonLevelLocked = new TextButtonStyle(btLevelLocked, null, null, font);

        backgroundMoves = new TextureRegionDrawable(atlas.findRegion("UI/backgroundMoves"));
        backgroundTime = new TextureRegionDrawable(atlas.findRegion("UI/backgroundTime"));
    }

    public static void loadTiledMap(int numMap) {
        if (map != null) {
            map.dispose();
            map = null;
        }
        if (Settings.isTest) {
            map = new TmxMapLoader().load("data/mapsTest/map" + numMap + ".tmx");
        } else {
            map = new AtlasTmxMapLoader().load("data/maps/map" + numMap + ".tmx");
        }
    }
}
