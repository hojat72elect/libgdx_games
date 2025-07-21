package com.nopalsoft.clumsy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.nopalsoft.clumsy.parallax.ParallaxBackground
import com.nopalsoft.clumsy.parallax.ParallaxLayer

object Assets {
    var LOAD_FLAPPY_PENIS: Boolean = false

    var bird: Animation<TextureRegion?>? = null

    var buttonPlayArcade: AtlasRegion? = null
    var buttonPlayClassic: AtlasRegion? = null
    var buttonLeaderboard: AtlasRegion? = null

    var buttonFacebook: AtlasRegion? = null
    var buttonTwitter: AtlasRegion? = null
    var rainbowLight: AtlasRegion? = null

    var whiteDrawable: NinePatchDrawable? = null
    var blackDrawable: NinePatchDrawable? = null

    var buttonRate: AtlasRegion? = null
    var buttonNoAds: AtlasRegion? = null
    var buttonAchievements: AtlasRegion? = null
    var buttonRestorePurchases: AtlasRegion? = null

    var meteor1: AtlasRegion? = null
    var meteor2: AtlasRegion? = null
    var meteor3: AtlasRegion? = null
    var meteor4: AtlasRegion? = null
    var meteor5: AtlasRegion? = null
    var meteor6: AtlasRegion? = null

    var num0Large: AtlasRegion? = null
    var num1Large: AtlasRegion? = null
    var num2Large: AtlasRegion? = null
    var num3Large: AtlasRegion? = null
    var num4Large: AtlasRegion? = null
    var num5Large: AtlasRegion? = null
    var num6Large: AtlasRegion? = null
    var num7Large: AtlasRegion? = null
    var num8Large: AtlasRegion? = null
    var num9Large: AtlasRegion? = null

    var num0Small: AtlasRegion? = null
    var num1Small: AtlasRegion? = null
    var num2Small: AtlasRegion? = null
    var num3Small: AtlasRegion? = null
    var num4Small: AtlasRegion? = null
    var num5Small: AtlasRegion? = null
    var num6Small: AtlasRegion? = null
    var num7Small: AtlasRegion? = null
    var num8Small: AtlasRegion? = null
    var num9Small: AtlasRegion? = null

    var appTitle: AtlasRegion? = null
    var upperTube: AtlasRegion? = null
    var lowerTube: AtlasRegion? = null

    var parallaxBackground: ParallaxBackground? = null

    var background0: AtlasRegion? = null
    var background1: AtlasRegion? = null
    var background2: AtlasRegion? = null
    var background3: AtlasRegion? = null

    var medalsBackground: AtlasRegion? = null

    var med1: AtlasRegion? = null
    var med2: AtlasRegion? = null
    var med3: AtlasRegion? = null
    var med4: AtlasRegion? = null

    var getReady: AtlasRegion? = null
    var gameover: AtlasRegion? = null
    var tapCat: AtlasRegion? = null

    var die: Sound? = null
    var hit: Sound? = null
    var point: Sound? = null
    var wing: Sound? = null
    var swooshing: Sound? = null

    @JvmStatic
    fun load() {
        var path = "data"
        if (LOAD_FLAPPY_PENIS) {
            path = "data_flappy_penis"
        }

        val atlas = TextureAtlas(Gdx.files.internal("$path/atlasMap.txt"))

        val b1 = atlas.findRegion("nave1")
        val b2 = atlas.findRegion("nave2")
        val b3 = atlas.findRegion("nave3")
        val b4 = atlas.findRegion("nave4")
        bird = Animation<TextureRegion?>(.1f, b1, b2, b3, b4)

        rainbowLight = atlas.findRegion("luzA")

        appTitle = atlas.findRegion("titulo")

        upperTube = atlas.findRegion("tubosEspacioArriba")
        lowerTube = atlas.findRegion("tubosEspacio")

        gameover = atlas.findRegion("gameover")
        getReady = atlas.findRegion("getready")
        tapCat = atlas.findRegion("tap")

        buttonPlayArcade = atlas.findRegion("btArcade")
        buttonPlayClassic = atlas.findRegion("btClassic")
        buttonLeaderboard = atlas.findRegion("btLeaderboard")
        buttonRate = atlas.findRegion("btRate")
        buttonNoAds = atlas.findRegion("btNoAds")
        buttonAchievements = atlas.findRegion("btAchievements")
        buttonRestorePurchases = atlas.findRegion("btRestore")

        if (buttonRestorePurchases == null) buttonRestorePurchases = atlas.findRegion("btNoAds")

        buttonFacebook = atlas.findRegion("btFacebook")
        buttonTwitter = atlas.findRegion("btTwitter")

        background1 = atlas.findRegion("fondo1")
        background2 = atlas.findRegion("fondo1")
        background3 = atlas.findRegion("fondo1")

        num0Large = atlas.findRegion("num0")
        num1Large = atlas.findRegion("num1")
        num2Large = atlas.findRegion("num2")
        num3Large = atlas.findRegion("num3")
        num4Large = atlas.findRegion("num4")
        num5Large = atlas.findRegion("num5")
        num6Large = atlas.findRegion("num6")
        num7Large = atlas.findRegion("num7")
        num8Large = atlas.findRegion("num8")
        num9Large = atlas.findRegion("num9")

        num0Small = atlas.findRegion("0")
        num1Small = atlas.findRegion("1")
        num2Small = atlas.findRegion("2")
        num3Small = atlas.findRegion("3")
        num4Small = atlas.findRegion("4")
        num5Small = atlas.findRegion("5")
        num6Small = atlas.findRegion("6")
        num7Small = atlas.findRegion("7")
        num8Small = atlas.findRegion("8")
        num9Small = atlas.findRegion("9")

        meteor1 = atlas.findRegion("meteoro1")
        meteor2 = atlas.findRegion("meteoro2")
        meteor3 = atlas.findRegion("meteoro3")
        meteor4 = atlas.findRegion("meteoro4")
        meteor5 = atlas.findRegion("meteoro5")
        meteor6 = atlas.findRegion("meteoro6")

        whiteDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("luz"), 1, 1, 0, 0))
        blackDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("oscuridad"), 1, 1, 0, 0))

        medalsBackground = atlas.findRegion("medallsFondo")
        med1 = atlas.findRegion("monedaOro")
        med2 = atlas.findRegion("monedaPlata")
        med3 = atlas.findRegion("monedaBronce")
        med4 = atlas.findRegion("monedaAluminio")

        val floor = ParallaxLayer(
            atlas.findRegion("floor"),
            Vector2(24f, 0f), Vector2(0f, 0f), Vector2(-1f, 700f),
            336f, 140f
        )
        val parallaxLayers: Array<ParallaxLayer> = arrayOf(floor)

        parallaxBackground = ParallaxBackground(parallaxLayers, 480f, 800f, Vector2(10f, 0f))

        die = Gdx.audio.newSound(Gdx.files.internal("$path/sonidos/sfx_die.mp3"))
        hit = Gdx.audio.newSound(Gdx.files.internal("$path/sonidos/sfx_hit.mp3"))
        point = Gdx.audio.newSound(Gdx.files.internal("$path/sonidos/sfx_point.mp3"))
        swooshing = Gdx.audio.newSound(Gdx.files.internal("$path/sonidos/sfx_swooshing.mp3"))
        wing = Gdx.audio.newSound(Gdx.files.internal("$path/sonidos/sfx_wing.mp3"))

        Settings.load()
    }

    fun playSound(sound: Sound) {
        sound.play(1f)
    }
}