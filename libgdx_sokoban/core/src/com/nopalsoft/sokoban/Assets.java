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

    public static BitmapFont fontDefault;
    public static BitmapFont fontRed;

    public static ParallaxBackground background;
    public static NinePatchDrawable pixelBlack;

    public static TiledMap map;

    public static TextureRegionDrawable backgroundMoves;
    public static TextureRegionDrawable backgroundTime;

    public static TextureRegionDrawable btDer;
    public static TextureRegionDrawable btDerPress;
    public static TextureRegionDrawable btIzq;
    public static TextureRegionDrawable btIzqPress;
    public static TextureRegionDrawable btUp;
    public static TextureRegionDrawable btUpPress;
    public static TextureRegionDrawable btDown;
    public static TextureRegionDrawable btDownPress;
    public static TextureRegionDrawable btRefresh;
    public static TextureRegionDrawable btRefreshPress;
    public static TextureRegionDrawable btPausa;
    public static TextureRegionDrawable btPausaPress;
    public static TextureRegionDrawable btLeaderboard;
    public static TextureRegionDrawable btLeaderboardPress;
    public static TextureRegionDrawable btAchievement;
    public static TextureRegionDrawable btAchievementPress;
    public static TextureRegionDrawable btFacebook;
    public static TextureRegionDrawable btFacebookPress;
    public static TextureRegionDrawable btSettings;
    public static TextureRegionDrawable btSettingsPress;
    public static TextureRegionDrawable btMas;
    public static TextureRegionDrawable btMasPress;
    public static TextureRegionDrawable btClose;
    public static TextureRegionDrawable btClosePress;
    public static TextureRegionDrawable btHome;
    public static TextureRegionDrawable btHomePress;
    public static TextureRegionDrawable btOff;
    public static TextureRegionDrawable btOn;
    public static TextureRegionDrawable btPlay;
    public static TextureRegionDrawable btPlayPress;

    public static TextureRegionDrawable levelOff;
    public static TextureRegionDrawable levelStar;

    public static TextureRegionDrawable clock;

    public static AtlasRegion boxBeige;
    public static AtlasRegion boxDarkBeige;
    public static AtlasRegion boxBlack;
    public static AtlasRegion boxDarkBlack;
    public static AtlasRegion boxBlue;
    public static AtlasRegion boxDarkBlue;
    public static AtlasRegion boxBrown;
    public static AtlasRegion boxDarkBrown;
    public static AtlasRegion boxGray;
    public static AtlasRegion boxDarkGray;
    public static AtlasRegion boxPurple;
    public static AtlasRegion boxDarkPurple;
    public static AtlasRegion boxRed;
    public static AtlasRegion boxDarkRed;
    public static AtlasRegion boxYellow;
    public static AtlasRegion boxDarkYellow;

    public static AtlasRegion endPointBeige;
    public static AtlasRegion endPointBlack;
    public static AtlasRegion endPointBlue;
    public static AtlasRegion endPointBrown;
    public static AtlasRegion endPointGray;
    public static AtlasRegion endPointPurple;
    public static AtlasRegion endPointRed;
    public static AtlasRegion endPointYellow;

    public static Animation<TextureRegion> animationMoveUp;
    public static Animation<TextureRegion> animationMoveDown;
    public static Animation<TextureRegion> animationMoveLeft;
    public static Animation<TextureRegion> animationMoveRight;
    public static AtlasRegion playerStand;
    public static TextureRegionDrawable windowBackground;
    public static TextButtonStyle styleTextButtonLevel;
    public static TextButtonStyle styleTextButtonLevelLocked;
    static TextureAtlas atlas;

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontDefault = new BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("UI/font32"));
        fontRed = new BitmapFont(Gdx.files.internal("data/font32Red.fnt"), atlas.findRegion("UI/font32Red"));

        loadUI();

        pixelBlack = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));

        windowBackground = new TextureRegionDrawable(atlas.findRegion("UI/backgroundVentana"));

        boxBeige = atlas.findRegion("cajaBeige");
        boxDarkBeige = atlas.findRegion("cajaDarkBeige");
        boxBlack = atlas.findRegion("cajaBlack");
        boxDarkBlack = atlas.findRegion("cajaDarkBlack");
        boxBlue = atlas.findRegion("cajaBlue");
        boxDarkBlue = atlas.findRegion("cajaDarkBlue");
        boxBrown = atlas.findRegion("cajaBrown");
        boxDarkBrown = atlas.findRegion("cajaDarkBrown");
        boxGray = atlas.findRegion("cajaGray");
        boxDarkGray = atlas.findRegion("cajaDarkGray");
        boxPurple = atlas.findRegion("cajaPurple");
        boxDarkPurple = atlas.findRegion("cajaDarkPurple");
        boxRed = atlas.findRegion("cajaRed");
        boxDarkRed = atlas.findRegion("cajaDarkRed");
        boxYellow = atlas.findRegion("cajaYellow");
        boxDarkYellow = atlas.findRegion("cajaDarkYellow");

        endPointBeige = atlas.findRegion("endPointBeige");
        endPointBlack = atlas.findRegion("endPointBlack");
        endPointBlue = atlas.findRegion("endPointBlue");
        endPointBrown = atlas.findRegion("endPointBrown");
        endPointGray = atlas.findRegion("endPointGray");
        endPointPurple = atlas.findRegion("endPointPurple");
        endPointRed = atlas.findRegion("endPointRed");
        endPointYellow = atlas.findRegion("endPointYellow");

        playerStand = atlas.findRegion("Character4");

        AtlasRegion up1 = atlas.findRegion("Character7");
        AtlasRegion up2 = atlas.findRegion("Character8");
        AtlasRegion up3 = atlas.findRegion("Character9");
        animationMoveUp = new Animation<>(.09f, up2, up3, up1);

        AtlasRegion down1 = atlas.findRegion("Character4");
        AtlasRegion down2 = atlas.findRegion("Character5");
        AtlasRegion down3 = atlas.findRegion("Character6");
        animationMoveDown = new Animation<>(.09f, down2, down3, down1);

        AtlasRegion right1 = atlas.findRegion("Character2");
        AtlasRegion right2 = atlas.findRegion("Character3");
        animationMoveRight = new Animation<>(.09f, right1, right2, right1);

        AtlasRegion left1 = atlas.findRegion("Character1");
        AtlasRegion left2 = atlas.findRegion("Character10");
        animationMoveLeft = new Animation<>(.09f, left1, left2, left1);

        AtlasRegion regioFondoFlip = atlas.findRegion("backgroundFlip");
        regioFondoFlip.flip(true, false);
        ParallaxLayer backgroundParallaxLayer = new ParallaxLayer(atlas.findRegion("background"), new Vector2(1, 0), new Vector2(0, 0), new Vector2(798, 480), 800, 480);
        ParallaxLayer flipBackgroundParallaxLayer = new ParallaxLayer(regioFondoFlip, new Vector2(1, 0), new Vector2(799, 0), new Vector2(798, 480), 800, 480);
        background = new ParallaxBackground(new ParallaxLayer[]{backgroundParallaxLayer, flipBackgroundParallaxLayer}, 800, 480, new Vector2(20, 0));

    }

    private static void loadUI() {
        btDer = new TextureRegionDrawable(atlas.findRegion("UI/btDer"));
        btDerPress = new TextureRegionDrawable(atlas.findRegion("UI/btDerPress"));
        btIzq = new TextureRegionDrawable(atlas.findRegion("UI/btIzq"));
        btIzqPress = new TextureRegionDrawable(atlas.findRegion("UI/btIzqPress"));
        btUp = new TextureRegionDrawable(atlas.findRegion("UI/btUp"));
        btUpPress = new TextureRegionDrawable(atlas.findRegion("UI/btUpPress"));
        btDown = new TextureRegionDrawable(atlas.findRegion("UI/btDown"));
        btDownPress = new TextureRegionDrawable(atlas.findRegion("UI/btDownPress"));
        btRefresh = new TextureRegionDrawable(atlas.findRegion("UI/btRefresh"));
        btRefreshPress = new TextureRegionDrawable(atlas.findRegion("UI/btRefreshPress"));
        btPausa = new TextureRegionDrawable(atlas.findRegion("UI/btPausa"));
        btPausaPress = new TextureRegionDrawable(atlas.findRegion("UI/btPausaPress"));
        btLeaderboard = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
        btLeaderboardPress = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"));
        btAchievement = new TextureRegionDrawable(atlas.findRegion("UI/btAchievement"));
        btAchievementPress = new TextureRegionDrawable(atlas.findRegion("UI/btAchievementPress"));
        btFacebook = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        btFacebookPress = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        btSettings = new TextureRegionDrawable(atlas.findRegion("UI/btSettings"));
        btSettingsPress = new TextureRegionDrawable(atlas.findRegion("UI/btSettingsPress"));
        btMas = new TextureRegionDrawable(atlas.findRegion("UI/btMas"));
        btMasPress = new TextureRegionDrawable(atlas.findRegion("UI/btMasPress"));
        btClose = new TextureRegionDrawable(atlas.findRegion("UI/btClose"));
        btClosePress = new TextureRegionDrawable(atlas.findRegion("UI/btClosePress"));
        btHome = new TextureRegionDrawable(atlas.findRegion("UI/btHome"));
        btHomePress = new TextureRegionDrawable(atlas.findRegion("UI/btHomePress"));
        btOff = new TextureRegionDrawable(atlas.findRegion("UI/btOff"));
        btOn = new TextureRegionDrawable(atlas.findRegion("UI/btOn"));

        btPlay = new TextureRegionDrawable(atlas.findRegion("UI/btPlay"));
        btPlayPress = new TextureRegionDrawable(atlas.findRegion("UI/btPlayPress"));
        clock = new TextureRegionDrawable(atlas.findRegion("UI/clock"));

        levelOff = new TextureRegionDrawable(atlas.findRegion("UI/levelOff"));
        levelStar = new TextureRegionDrawable(atlas.findRegion("UI/levelStar"));

        /* Button level */
        TextureRegionDrawable btLevel = new TextureRegionDrawable(atlas.findRegion("UI/btLevel"));
        styleTextButtonLevel = new TextButtonStyle(btLevel, null, null, fontDefault);

        /* Button level */
        TextureRegionDrawable btLevelLocked = new TextureRegionDrawable(atlas.findRegion("UI/btLevelLocked"));
        styleTextButtonLevelLocked = new TextButtonStyle(btLevelLocked, null, null, fontDefault);

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
