package com.nopalsoft.dragracer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object Assets {
    var fontGrande: BitmapFont? = null
    var fontChico: BitmapFont? = null

    var newExplosion: Animation<TextureRegion?>? = null


    var blackPixel: NinePatchDrawable? = null
    var scoresBackgroundDrawable: TextureRegionDrawable? = null

    var horizontalSeparatorDrawable: NinePatchDrawable? = null
    var verticalSeparatorDrawable: NinePatchDrawable? = null

    var street: AtlasRegion? = null
    var coin: AtlasRegion? = null
    var coinFront: AtlasRegion? = null

    var titleDrawable: TextureRegionDrawable? = null

    var buttonBack: TextureRegionDrawable? = null
    var buttonNoAds: TextureRegionDrawable? = null
    var buttonFacebook: TextureRegionDrawable? = null
    var buttonTwitter: TextureRegionDrawable? = null

    var upgradeOn: TextureRegionDrawable? = null
    var upgradeOff: TextureRegionDrawable? = null

    var swipeHand: TextureRegionDrawable? = null
    var swipeHandDown: TextureRegionDrawable? = null
    var swipeArrows: TextureRegionDrawable? = null

    // Cars
    var carDiablo: AtlasRegion? = null
    var carBanshee: AtlasRegion? = null
    var carTurismo: AtlasRegion? = null
    var carCamaro: AtlasRegion? = null
    var carTornado: AtlasRegion? = null
    var carAudiS5: AtlasRegion? = null
    var carBmwX6: AtlasRegion? = null
    var carChevroletCrossfire: AtlasRegion? = null
    var carCitroenC4: AtlasRegion? = null
    var carDodgeCharger: AtlasRegion? = null
    var carFiat500Lounge: AtlasRegion? = null
    var carHondaCRV: AtlasRegion? = null
    var carMazda6: AtlasRegion? = null
    var carMazdaRx8: AtlasRegion? = null
    var carSeatIbiza: AtlasRegion? = null
    var carVolkswagenScirocco: AtlasRegion? = null

    // Styles
    var labelStyleLarge: LabelStyle? = null
    var labelStyleSmall: LabelStyle? = null

    var styleScrollPane: ScrollPaneStyle? = null

    var styleTextButtonBuy: TextButtonStyle? = null
    var styleTextButtonPurchased: TextButtonStyle? = null
    var styleTextButtonSelected: TextButtonStyle? = null

    var styleButtonMusic: ButtonStyle? = null

    var atlas: TextureAtlas? = null
    var redMarkerBar: AtlasRegion? = null
    var greenMarkerBar: AtlasRegion? = null

    var soundTurn1: Sound? = null
    var soundTurn2: Sound? = null
    var soundCrash: Sound? = null


    var music: Music? = null

    private fun loadFont() {
        fontGrande = BitmapFont(
            Gdx.files.internal("data/font.fnt"),
            atlas!!.findRegion("font")
        )

        fontChico = BitmapFont(
            Gdx.files.internal("data/fontChico.fnt"),
            atlas!!.findRegion("fontChico")
        )
    }

    private fun loadStyles() {
        labelStyleLarge = LabelStyle(fontGrande, Color.WHITE)
        labelStyleSmall = LabelStyle(fontChico, Color.WHITE)

        horizontalSeparatorDrawable = NinePatchDrawable(
            NinePatch(
                atlas!!.findRegion("Shop/separadorHorizontal"), 0, 1, 0, 0
            )
        )
        verticalSeparatorDrawable = NinePatchDrawable(
            NinePatch(
                atlas!!.findRegion("Shop/separadorVertical"), 0, 1, 0, 0
            )
        )

        // Button Buy
        val btBuy = TextureRegionDrawable(
            atlas!!.findRegion("Shop/btBuy")
        )
        styleTextButtonBuy = TextButtonStyle(btBuy, null, null, fontChico)

        // Button Purchased
        val btPurchased = TextureRegionDrawable(
            atlas!!.findRegion("Shop/btPurchased")
        )
        styleTextButtonPurchased = TextButtonStyle(
            btPurchased, null, null,
            fontChico
        )

        // Button Selected
        val btSelected = TextureRegionDrawable(
            atlas!!.findRegion("Shop/btSelected")
        )
        styleTextButtonSelected = TextButtonStyle(
            btSelected, null, null,
            fontChico
        )

        styleScrollPane = ScrollPaneStyle(
            null, null, null, null,
            verticalSeparatorDrawable
        )

        // Button Music
        val btMusicOn = TextureRegionDrawable(
            atlas!!.findRegion("MenuPrincipal/btMusica")
        )
        val btMusicOff = TextureRegionDrawable(
            atlas!!.findRegion("MenuPrincipal/btSinMusica")
        )
        styleButtonMusic = ButtonStyle(btMusicOn, null, btMusicOff)
    }

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))
        loadFont()
        loadStyles()

        titleDrawable = TextureRegionDrawable(atlas!!.findRegion("titulo2"))

        blackPixel = NinePatchDrawable(
            NinePatch(
                atlas!!.findRegion("pixelNegro"), 1, 1, 0, 0
            )
        )
        scoresBackgroundDrawable = TextureRegionDrawable(
            atlas!!.findRegion("fondoPuntuaciones")
        )

        coin = atlas!!.findRegion("coin")
        coinFront = atlas!!.findRegion("coinFrente")

        redMarkerBar = atlas!!.findRegion("barraMarcadorRojo")
        greenMarkerBar = atlas!!.findRegion("barraMarcadorVerde")

        street = atlas!!.findRegion("calle")
        carDiablo = atlas!!.findRegion("Carros/diablo")
        carBanshee = atlas!!.findRegion("Carros/banshee")
        carTurismo = atlas!!.findRegion("Carros/turismo")
        carCamaro = atlas!!.findRegion("Carros/bullet")
        carTornado = atlas!!.findRegion("Carros/tornado")

        carAudiS5 = atlas!!.findRegion("Carros/Audi S5")
        carBmwX6 = atlas!!.findRegion("Carros/BMW X6")
        carChevroletCrossfire = atlas!!.findRegion("Carros/Chevrolet Crossfire")
        carCitroenC4 = atlas!!.findRegion("Carros/Citroen C4")
        carDodgeCharger = atlas!!.findRegion("Carros/Dodge Charger")
        carFiat500Lounge = atlas!!.findRegion("Carros/Fiat 500 Lounge")
        carHondaCRV = atlas!!.findRegion("Carros/Honda CRV")
        carMazda6 = atlas!!.findRegion("Carros/Mazda 6")
        carMazdaRx8 = atlas!!.findRegion("Carros/Mazda RX8")
        carSeatIbiza = atlas!!.findRegion("Carros/Seat Ibiza")
        carVolkswagenScirocco = atlas!!.findRegion("Carros/Volkswagen Scirocco")

        val explosionFrame1 = atlas!!.findRegion("Animaciones/newExplosion1")
        val explosionFrame2 = atlas!!.findRegion("Animaciones/newExplosion2")
        val explosionFrame3 = atlas!!.findRegion("Animaciones/newExplosion3")
        val explosionFrame4 = atlas!!.findRegion("Animaciones/newExplosion4")
        val explosionFrame5 = atlas!!.findRegion("Animaciones/newExplosion5")
        val explosionFrame6 = atlas!!.findRegion("Animaciones/newExplosion6")
        val explosionFrame7 = atlas!!.findRegion("Animaciones/newExplosion7")
        val explosionFrame8 = atlas!!.findRegion("Animaciones/newExplosion8")
        val explosionFrame9 = atlas!!.findRegion("Animaciones/newExplosion9")
        val explosionFrame10 = atlas!!.findRegion("Animaciones/newExplosion10")
        val explosionFrame11 = atlas!!.findRegion("Animaciones/newExplosion11")
        val explosionFrame12 = atlas!!.findRegion("Animaciones/newExplosion12")
        val explosionFrame13 = atlas!!.findRegion("Animaciones/newExplosion13")
        val explosionFrame14 = atlas!!.findRegion("Animaciones/newExplosion14")
        val explosionFrame15 = atlas!!.findRegion("Animaciones/newExplosion15")
        val explosionFrame16 = atlas!!.findRegion("Animaciones/newExplosion16")
        val explosionFrame17 = atlas!!.findRegion("Animaciones/newExplosion17")
        val explosionFrame18 = atlas!!.findRegion("Animaciones/newExplosion18")
        val explosionFrame19 = atlas!!.findRegion("Animaciones/newExplosion19")
        newExplosion = Animation<TextureRegion?>(
            0.05f, explosionFrame1, explosionFrame2, explosionFrame3,
            explosionFrame4, explosionFrame5, explosionFrame6, explosionFrame7, explosionFrame8, explosionFrame9,
            explosionFrame10, explosionFrame11, explosionFrame12, explosionFrame13, explosionFrame14,
            explosionFrame15, explosionFrame16, explosionFrame17, explosionFrame18, explosionFrame19
        )

        buttonBack = TextureRegionDrawable(atlas!!.findRegion("Shop/btAtras2"))
        buttonNoAds = TextureRegionDrawable(atlas!!.findRegion("Shop/btNoAds"))
        upgradeOff = TextureRegionDrawable(
            atlas!!.findRegion("Shop/upgradeOff")
        )
        upgradeOn = TextureRegionDrawable(
            atlas!!.findRegion("Shop/upgradeOn")
        )

        buttonFacebook = TextureRegionDrawable(
            atlas!!.findRegion("MenuPrincipal/btFacebook")
        )
        buttonTwitter = TextureRegionDrawable(
            atlas!!.findRegion("MenuPrincipal/btTwitter")
        )

        swipeHand = TextureRegionDrawable(atlas!!.findRegion("swipeHand"))
        swipeHandDown = TextureRegionDrawable(
            atlas!!.findRegion("swipeHandDown")
        )
        swipeArrows = TextureRegionDrawable(atlas!!.findRegion("swipeArrows"))

        soundTurn1 = Gdx.audio.newSound(
            Gdx.files
                .internal("data/Sounds/turn1.mp3")
        )
        soundTurn2 = Gdx.audio.newSound(
            Gdx.files
                .internal("data/Sounds/turn2.mp3")
        )
        soundCrash = Gdx.audio.newSound(
            Gdx.files
                .internal("data/Sounds/crash.mp3")
        )

        music = Gdx.audio.newMusic(
            Gdx.files
                .internal("data/Sounds/DST-BreakOut.mp3")
        )
        music?.setLooping(true)

        Settings.load()

        if (Settings.isMusicOn) music?.play()
    }

    fun playSound(son: Sound) {
        if (Settings.isMusicOn) {
            son.play(1f)
        }
    }
}
