package ca.hojat.sokoban

import ca.hojat.sokoban.parallax.ParallaxBackground
import ca.hojat.sokoban.parallax.ParallaxLayer
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object Assets {
    var font: BitmapFont? = null
    var fontRed: BitmapFont? = null
    var background: ParallaxBackground? = null
    var blackPixelDrawable: NinePatchDrawable? = null
    var map: TiledMap? = null
    var backgroundMoves: TextureRegionDrawable? = null
    var backgroundTime: TextureRegionDrawable? = null
    var buttonRightDrawable: TextureRegionDrawable? = null
    var buttonRightPressedDrawable: TextureRegionDrawable? = null
    var buttonLeftDrawable: TextureRegionDrawable? = null
    var buttonLeftPressedDrawable: TextureRegionDrawable? = null
    var buttonUpDrawable: TextureRegionDrawable? = null
    var buttonUpPressedDrawable: TextureRegionDrawable? = null
    var buttonDownDrawable: TextureRegionDrawable? = null
    var buttonDownPressedDrawable: TextureRegionDrawable? = null
    var buttonRefreshDrawable: TextureRegionDrawable? = null
    var buttonRefreshPressedDrawable: TextureRegionDrawable? = null
    var buttonPauseDrawable: TextureRegionDrawable? = null
    var buttonPausePressedDrawable: TextureRegionDrawable? = null
    var buttonLeaderboardDrawable: TextureRegionDrawable? = null
    var buttonLeaderboardPressedDrawable: TextureRegionDrawable? = null
    var buttonAchievementDrawable: TextureRegionDrawable? = null
    var buttonAchievementPressedDrawable: TextureRegionDrawable? = null
    var buttonFacebookDrawable: TextureRegionDrawable? = null
    var buttonFacebookPressedDrawable: TextureRegionDrawable? = null
    var buttonSettingsDrawable: TextureRegionDrawable? = null
    var buttonSettingsPressedDrawable: TextureRegionDrawable? = null
    var buttonMoreDrawable: TextureRegionDrawable? = null
    var buttonMorePressedDrawable: TextureRegionDrawable? = null
    var buttonCloseDrawable: TextureRegionDrawable? = null
    var buttonClosePressedDrawable: TextureRegionDrawable? = null
    var homeButtonDrawable: TextureRegionDrawable? = null
    var homeButtonPressedDrawable: TextureRegionDrawable? = null
    var buttonOffDrawable: TextureRegionDrawable? = null
    var buttonOnDrawable: TextureRegionDrawable? = null
    var buttonPlayDrawable: TextureRegionDrawable? = null
    var buttonPlayPressedDrawable: TextureRegionDrawable? = null
    var levelOffDrawable: TextureRegionDrawable? = null
    var levelStarDrawable: TextureRegionDrawable? = null
    var clockDrawable: TextureRegionDrawable? = null
    var beigeBox: AtlasRegion? = null
    var darkBeigeBox: AtlasRegion? = null
    var blackBox: AtlasRegion? = null
    var darkBlackBox: AtlasRegion? = null
    var blueBox: AtlasRegion? = null
    var darkBlueBox: AtlasRegion? = null
    var brownBox: AtlasRegion? = null
    var darkBrownBox: AtlasRegion? = null
    var grayBox: AtlasRegion? = null
    var darkGrayBox: AtlasRegion? = null
    var purpleBox: AtlasRegion? = null
    var darkPurpleBox: AtlasRegion? = null
    var redBox: AtlasRegion? = null
    var darkRedBox: AtlasRegion? = null
    var yellowBox: AtlasRegion? = null
    var darkYellowBox: AtlasRegion? = null

    var beigeTargetPlatform: AtlasRegion? = null
    var blackTargetPlatform: AtlasRegion? = null
    var blueTargetPlatform: AtlasRegion? = null
    var brownTargetPlatform: AtlasRegion? = null
    var grayTargetPlatform: AtlasRegion? = null
    var purpleTargetPlatform: AtlasRegion? = null
    var redTargetPlatform: AtlasRegion? = null
    var yellowTargetPlatform: AtlasRegion? = null

    var playerUpAnimation: Animation<TextureRegion>? = null
    var playerDownAnimation: Animation<TextureRegion>? = null
    var playerLeftAnimation: Animation<TextureRegion>? = null
    var playerRightAnimation: Animation<TextureRegion>? = null


    var playerStand: AtlasRegion? = null

    private var atlas: TextureAtlas? = null


    var windowBackground: TextureRegionDrawable? = null


    var styleTextButtonLevel: TextButtonStyle? = null


    var styleTextButtonLevelLocked: TextButtonStyle? = null

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        font = BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas!!.findRegion("UI/font32"))
        fontRed = BitmapFont(Gdx.files.internal("data/font32Red.fnt"), atlas!!.findRegion("UI/font32Red"))

        loadUI()

        blackPixelDrawable = NinePatchDrawable(NinePatch(atlas!!.findRegion("pixelNegro"), 1, 1, 0, 0))

        windowBackground = TextureRegionDrawable(atlas!!.findRegion("UI/backgroundVentana"))

        beigeBox = atlas!!.findRegion("cajaBeige")
        darkBeigeBox = atlas!!.findRegion("cajaDarkBeige")
        blackBox = atlas!!.findRegion("cajaBlack")
        darkBlackBox = atlas!!.findRegion("cajaDarkBlack")
        blueBox = atlas!!.findRegion("cajaBlue")
        darkBlueBox = atlas!!.findRegion("cajaDarkBlue")
        brownBox = atlas!!.findRegion("cajaBrown")
        darkBrownBox = atlas!!.findRegion("cajaDarkBrown")
        grayBox = atlas!!.findRegion("cajaGray")
        darkGrayBox = atlas!!.findRegion("cajaDarkGray")
        purpleBox = atlas!!.findRegion("cajaPurple")
        darkPurpleBox = atlas!!.findRegion("cajaDarkPurple")
        redBox = atlas!!.findRegion("cajaRed")
        darkRedBox = atlas!!.findRegion("cajaDarkRed")
        yellowBox = atlas!!.findRegion("cajaYellow")
        darkYellowBox = atlas!!.findRegion("cajaDarkYellow")

        beigeTargetPlatform = atlas!!.findRegion("endPointBeige")
        blackTargetPlatform = atlas!!.findRegion("endPointBlack")
        blueTargetPlatform = atlas!!.findRegion("endPointBlue")
        brownTargetPlatform = atlas!!.findRegion("endPointBrown")
        grayTargetPlatform = atlas!!.findRegion("endPointGray")
        purpleTargetPlatform = atlas!!.findRegion("endPointPurple")
        redTargetPlatform = atlas!!.findRegion("endPointRed")
        yellowTargetPlatform = atlas!!.findRegion("endPointYellow")

        playerStand = atlas!!.findRegion("Character4")

        val up1 = atlas!!.findRegion("Character7")
        val up2 = atlas!!.findRegion("Character8")
        val up3 = atlas!!.findRegion("Character9")
        playerUpAnimation = Animation(.09f, up2, up3, up1)

        val down1 = atlas!!.findRegion("Character4")
        val down2 = atlas!!.findRegion("Character5")
        val down3 = atlas!!.findRegion("Character6")
        playerDownAnimation = Animation(.09f, down2, down3, down1)

        val right1 = atlas!!.findRegion("Character2")
        val right2 = atlas!!.findRegion("Character3")
        playerRightAnimation = Animation(.09f, right1, right2, right1)

        val left1 = atlas!!.findRegion("Character1")
        val left2 = atlas!!.findRegion("Character10")
        playerLeftAnimation = Animation(.09f, left1, left2, left1)

        val regioFondoFlip = atlas!!.findRegion("backgroundFlip")
        regioFondoFlip.flip(true, false)
        val fondo = ParallaxLayer(atlas!!.findRegion("background"), Vector2(1f, 0f), Vector2(0f, 0f), Vector2(798f, 480f), 800f, 480f)
        val fondoFlip = ParallaxLayer(regioFondoFlip, Vector2(1f, 0f), Vector2(799f, 0f), Vector2(798f, 480f), 800f, 480f)
        background = ParallaxBackground(arrayOf(fondo, fondoFlip), 800f, 480f, Vector2(20f, 0f))
    }

    private fun loadUI() {
        buttonRightDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btDer"))
        buttonRightPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btDerPress"))
        buttonLeftDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btIzq"))
        buttonLeftPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btIzqPress"))
        buttonUpDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btUp"))
        buttonUpPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btUpPress"))
        buttonDownDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btDown"))
        buttonDownPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btDownPress"))
        buttonRefreshDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btRefresh"))
        buttonRefreshPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btRefreshPress"))
        buttonPauseDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btPausa"))
        buttonPausePressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btPausaPress"))
        buttonLeaderboardDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btLeaderboard"))
        buttonLeaderboardPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btLeaderboardPress"))
        buttonAchievementDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btAchievement"))
        buttonAchievementPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btAchievementPress"))
        buttonFacebookDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btFacebook"))
        buttonFacebookPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btFacebookPress"))
        buttonSettingsDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btSettings"))
        buttonSettingsPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btSettingsPress"))
        buttonMoreDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btMas"))
        buttonMorePressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btMasPress"))
        buttonCloseDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btClose"))
        buttonClosePressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btClosePress"))
        homeButtonDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btHome"))
        homeButtonPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btHomePress"))
        buttonOffDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btOff"))
        buttonOnDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btOn"))

        buttonPlayDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btPlay"))
        buttonPlayPressedDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/btPlayPress"))
        clockDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/clock"))

        levelOffDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/levelOff"))
        levelStarDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/levelStar"))

        // Button level
        val btLevel = TextureRegionDrawable(atlas!!.findRegion("UI/btLevel"))
        styleTextButtonLevel = TextButtonStyle(btLevel, null, null, font)

        // Button level
        val btLevelLocked = TextureRegionDrawable(atlas!!.findRegion("UI/btLevelLocked"))
        styleTextButtonLevelLocked = TextButtonStyle(btLevelLocked, null, null, font)

        backgroundMoves = TextureRegionDrawable(atlas!!.findRegion("UI/backgroundMoves"))
        backgroundTime = TextureRegionDrawable(atlas!!.findRegion("UI/backgroundTime"))
    }

    fun loadTiledMap(numMap: Int) {
        if (map != null) {
            map!!.dispose()
            map = null
        }
        map = if (Settings.isTest) {
            TmxMapLoader().load("data/mapsTest/map$numMap.tmx")
        } else {
            AtlasTmxMapLoader().load("data/maps/map$numMap.tmx")
        }
    }
}
