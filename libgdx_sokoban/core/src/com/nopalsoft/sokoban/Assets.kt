package com.nopalsoft.sokoban

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.sokoban.parallax.ParallaxBackground
import com.nopalsoft.sokoban.parallax.ParallaxLayer

object Assets {
    lateinit var fontDefault: BitmapFont
    lateinit var fontRed: BitmapFont
    lateinit var background: ParallaxBackground
    lateinit var pixelBlack: NinePatchDrawable
     var map: TiledMap? =null
    lateinit var backgroundMoves: TextureRegionDrawable
    lateinit var backgroundTime: TextureRegionDrawable
    lateinit var buttonDer: TextureRegionDrawable
    lateinit var buttonDerPress: TextureRegionDrawable
    lateinit var buttonIzq: TextureRegionDrawable
    lateinit var btIzqPress: TextureRegionDrawable
    lateinit var btUp: TextureRegionDrawable
    lateinit var btUpPress: TextureRegionDrawable
    lateinit var btDown: TextureRegionDrawable
    lateinit var btDownPress: TextureRegionDrawable
    lateinit var btRefresh: TextureRegionDrawable
    lateinit var btRefreshPress: TextureRegionDrawable

    lateinit var buttonPause: TextureRegionDrawable
    lateinit var buttonPausePress: TextureRegionDrawable
    private lateinit var btLeaderboard: TextureRegionDrawable
    private lateinit var btLeaderboardPress: TextureRegionDrawable
    lateinit var btAchievement: TextureRegionDrawable
    lateinit var btAchievementPress: TextureRegionDrawable
    lateinit var btFacebook: TextureRegionDrawable
    lateinit var btFacebookPress: TextureRegionDrawable
    lateinit var btSettings: TextureRegionDrawable
    lateinit var btSettingsPress: TextureRegionDrawable
    lateinit var btMas: TextureRegionDrawable
    lateinit var btMasPress: TextureRegionDrawable
    lateinit var btClose: TextureRegionDrawable
    lateinit var buttonClosePress: TextureRegionDrawable
    lateinit var btHome: TextureRegionDrawable
    lateinit var btHomePress: TextureRegionDrawable
    lateinit var btOff: TextureRegionDrawable
    lateinit var btOn: TextureRegionDrawable
    lateinit var btPlay: TextureRegionDrawable
    lateinit var btPlayPress: TextureRegionDrawable

    lateinit var levelOff: TextureRegionDrawable
    lateinit var levelStar: TextureRegionDrawable
    lateinit var clock: TextureRegionDrawable
    lateinit var boxBeige: AtlasRegion
    lateinit var boxDarkBeige: AtlasRegion
    lateinit var boxBlack: AtlasRegion
    lateinit var boxDarkBlack: AtlasRegion
    lateinit var boxBlue: AtlasRegion
    lateinit var boxDarkBlue: AtlasRegion
    lateinit var boxBrown: AtlasRegion
    lateinit var boxDarkBrown: AtlasRegion
    lateinit var boxGray: AtlasRegion
    lateinit var boxDarkGray: AtlasRegion
    lateinit var boxPurple: AtlasRegion
    lateinit var boxDarkPurple: AtlasRegion
    lateinit var boxRed: AtlasRegion
    lateinit var boxDarkRed: AtlasRegion
    lateinit var boxYellow: AtlasRegion
    lateinit var boxDarkYellow: AtlasRegion
    lateinit var endPointBeige: AtlasRegion
    lateinit var endPointBlack: AtlasRegion
    lateinit var endPointBlue: AtlasRegion
    lateinit var endPointBrown: AtlasRegion
    lateinit var endPointGray: AtlasRegion
    lateinit var endPointPurple: AtlasRegion
    lateinit var endPointRed: AtlasRegion
    lateinit var endPointYellow: AtlasRegion
    lateinit var animationMoveUp: Animation<TextureRegion>
    lateinit var animationMoveDown: Animation<TextureRegion>
    lateinit var animationMoveLeft: Animation<TextureRegion>
    lateinit var animationMoveRight: Animation<TextureRegion>
    lateinit var playerStand: AtlasRegion
    lateinit var windowBackground: TextureRegionDrawable
    lateinit var styleTextButtonLevel: TextButtonStyle
    lateinit var styleTextButtonLevelLocked: TextButtonStyle
    private lateinit var atlas: TextureAtlas

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontDefault = BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("UI/font32"))
        fontRed = BitmapFont(Gdx.files.internal("data/font32Red.fnt"), atlas.findRegion("UI/font32Red"))

        loadUI()

        pixelBlack = NinePatchDrawable(NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0))

        windowBackground = TextureRegionDrawable(atlas.findRegion("UI/backgroundVentana"))

        boxBeige = atlas.findRegion("cajaBeige")
        boxDarkBeige = atlas.findRegion("cajaDarkBeige")
        boxBlack = atlas.findRegion("cajaBlack")
        boxDarkBlack = atlas.findRegion("cajaDarkBlack")
        boxBlue = atlas.findRegion("cajaBlue")
        boxDarkBlue = atlas.findRegion("cajaDarkBlue")
        boxBrown = atlas.findRegion("cajaBrown")
        boxDarkBrown = atlas.findRegion("cajaDarkBrown")
        boxGray = atlas.findRegion("cajaGray")
        boxDarkGray = atlas.findRegion("cajaDarkGray")
        boxPurple = atlas.findRegion("cajaPurple")
        boxDarkPurple = atlas.findRegion("cajaDarkPurple")
        boxRed = atlas.findRegion("cajaRed")
        boxDarkRed = atlas.findRegion("cajaDarkRed")
        boxYellow = atlas.findRegion("cajaYellow")
        boxDarkYellow = atlas.findRegion("cajaDarkYellow")

        endPointBeige = atlas.findRegion("endPointBeige")
        endPointBlack = atlas.findRegion("endPointBlack")
        endPointBlue = atlas.findRegion("endPointBlue")
        endPointBrown = atlas.findRegion("endPointBrown")
        endPointGray = atlas.findRegion("endPointGray")
        endPointPurple = atlas.findRegion("endPointPurple")
        endPointRed = atlas.findRegion("endPointRed")
        endPointYellow = atlas.findRegion("endPointYellow")

        playerStand = atlas.findRegion("Character4")

        val up1 = atlas.findRegion("Character7")
        val up2 = atlas.findRegion("Character8")
        val up3 = atlas.findRegion("Character9")
        animationMoveUp = Animation(.09f, up2, up3, up1)

        val down1 = atlas.findRegion("Character4")
        val down2 = atlas.findRegion("Character5")
        val down3 = atlas.findRegion("Character6")
        animationMoveDown = Animation(.09f, down2, down3, down1)

        val right1 = atlas.findRegion("Character2")
        val right2 = atlas.findRegion("Character3")
        animationMoveRight = Animation(.09f, right1, right2, right1)

        val left1 = atlas.findRegion("Character1")
        val left2 = atlas.findRegion("Character10")
        animationMoveLeft = Animation(.09f, left1, left2, left1)

        val regioFondoFlip = atlas.findRegion("backgroundFlip")
        regioFondoFlip.flip(true, false)
        val backgroundParallaxLayer = ParallaxLayer(atlas.findRegion("background"), Vector2(1f, 0f), Vector2(0f, 0f), Vector2(798f, 480f), 800f, 480f)
        val flipBackgroundParallaxLayer = ParallaxLayer(regioFondoFlip, Vector2(1f, 0f), Vector2(799f, 0f), Vector2(798f, 480f), 800f, 480f)
        background = ParallaxBackground(arrayOf(backgroundParallaxLayer, flipBackgroundParallaxLayer), 800f, 480f, Vector2(20f, 0f))
    }

    private fun loadUI() {
        buttonDer = TextureRegionDrawable(atlas.findRegion("UI/btDer"))
        buttonDerPress = TextureRegionDrawable(atlas.findRegion("UI/btDerPress"))
        buttonIzq = TextureRegionDrawable(atlas.findRegion("UI/btIzq"))
        btIzqPress = TextureRegionDrawable(atlas.findRegion("UI/btIzqPress"))
        btUp = TextureRegionDrawable(atlas.findRegion("UI/btUp"))
        btUpPress = TextureRegionDrawable(atlas.findRegion("UI/btUpPress"))
        btDown = TextureRegionDrawable(atlas.findRegion("UI/btDown"))
        btDownPress = TextureRegionDrawable(atlas.findRegion("UI/btDownPress"))
        btRefresh = TextureRegionDrawable(atlas.findRegion("UI/btRefresh"))
        btRefreshPress = TextureRegionDrawable(atlas.findRegion("UI/btRefreshPress"))
        buttonPause = TextureRegionDrawable(atlas.findRegion("UI/btPausa"))
        buttonPausePress = TextureRegionDrawable(atlas.findRegion("UI/btPausaPress"))
        btLeaderboard = TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"))
        btLeaderboardPress = TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"))
        btAchievement = TextureRegionDrawable(atlas.findRegion("UI/btAchievement"))
        btAchievementPress = TextureRegionDrawable(atlas.findRegion("UI/btAchievementPress"))
        btFacebook = TextureRegionDrawable(atlas.findRegion("UI/btFacebook"))
        btFacebookPress = TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"))
        btSettings = TextureRegionDrawable(atlas.findRegion("UI/btSettings"))
        btSettingsPress = TextureRegionDrawable(atlas.findRegion("UI/btSettingsPress"))
        btMas = TextureRegionDrawable(atlas.findRegion("UI/btMas"))
        btMasPress = TextureRegionDrawable(atlas.findRegion("UI/btMasPress"))
        btClose = TextureRegionDrawable(atlas.findRegion("UI/btClose"))
        buttonClosePress = TextureRegionDrawable(atlas.findRegion("UI/btClosePress"))
        btHome = TextureRegionDrawable(atlas.findRegion("UI/btHome"))
        btHomePress = TextureRegionDrawable(atlas.findRegion("UI/btHomePress"))
        btOff = TextureRegionDrawable(atlas.findRegion("UI/btOff"))
        btOn = TextureRegionDrawable(atlas.findRegion("UI/btOn"))

        btPlay = TextureRegionDrawable(atlas.findRegion("UI/btPlay"))
        btPlayPress = TextureRegionDrawable(atlas.findRegion("UI/btPlayPress"))
        clock = TextureRegionDrawable(atlas.findRegion("UI/clock"))

        levelOff = TextureRegionDrawable(atlas.findRegion("UI/levelOff"))
        levelStar = TextureRegionDrawable(atlas.findRegion("UI/levelStar"))

        /* Button level */
        val btLevel = TextureRegionDrawable(atlas.findRegion("UI/btLevel"))
        styleTextButtonLevel = TextButtonStyle(btLevel, null, null, fontDefault)

        /* Button level */
        val btLevelLocked = TextureRegionDrawable(atlas.findRegion("UI/btLevelLocked"))
        styleTextButtonLevelLocked = TextButtonStyle(btLevelLocked, null, null, fontDefault)

        backgroundMoves = TextureRegionDrawable(atlas.findRegion("UI/backgroundMoves"))
        backgroundTime = TextureRegionDrawable(atlas.findRegion("UI/backgroundTime"))
    }

    fun loadTiledMap(numMap: Int) {
        if (map != null) {
            map?.dispose()
            map = null
        }

        map = TmxMapLoader().load("data/mapsTest/map$numMap.tmx")
    }
}
