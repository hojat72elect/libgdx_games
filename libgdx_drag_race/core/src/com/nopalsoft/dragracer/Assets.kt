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
    lateinit var fontLarge: BitmapFont
    lateinit var fontSmall: BitmapFont

    lateinit var newExplosion: Animation<TextureRegion>

    lateinit var pixelBlack: NinePatchDrawable
    lateinit var scoresBackground: TextureRegionDrawable

    lateinit var horizontalSeparator: NinePatchDrawable
    lateinit var verticalSeparator: NinePatchDrawable

    lateinit var street: AtlasRegion
    lateinit var coin: AtlasRegion
    lateinit var coinFront: AtlasRegion

    lateinit var title: TextureRegionDrawable
    lateinit var buttonBack: TextureRegionDrawable
    lateinit var buttonNoAds: TextureRegionDrawable
    lateinit var buttonFacebook: TextureRegionDrawable
    lateinit var buttonTwitter: TextureRegionDrawable
    lateinit var upgradeOn: TextureRegionDrawable
    lateinit var upgradeOff: TextureRegionDrawable
    lateinit var swipeHand: TextureRegionDrawable
    lateinit var swipeHandDown: TextureRegionDrawable
    lateinit var swipeArrows: TextureRegionDrawable

    // All cars we have in this game.
    lateinit var carDevil: AtlasRegion
    lateinit var carBanshee: AtlasRegion
    lateinit var carTourism: AtlasRegion
    lateinit var carBullet: AtlasRegion
    lateinit var carTornado: AtlasRegion
    lateinit var carAudiS5: AtlasRegion
    lateinit var carBmwX6: AtlasRegion
    lateinit var carChevroletCrossfire: AtlasRegion
    lateinit var carCitroenC4: AtlasRegion
    lateinit var carDodgeCharger: AtlasRegion
    lateinit var carFiat500Lounge: AtlasRegion
    lateinit var carHondaCRV: AtlasRegion
    lateinit var carMazda6: AtlasRegion
    lateinit var carMazdaRX8: AtlasRegion
    lateinit var carSeatIbiza: AtlasRegion
    lateinit var carVolkswagenScirocco: AtlasRegion

    // All kinds of styles used in this game
    lateinit var labelStyleLarge: LabelStyle
    lateinit var labelStyleSmall: LabelStyle
    lateinit var styleScrollPane: ScrollPaneStyle
    lateinit var styleTextButtonBuy: TextButtonStyle
    lateinit var styleTextButtonPurchased: TextButtonStyle
    lateinit var styleTextButtonSelected: TextButtonStyle
    lateinit var styleButtonMusic: ButtonStyle

    lateinit var barMarkedRed: AtlasRegion
    lateinit var barMarkedGreen: AtlasRegion

    // Sounds and music
    lateinit var soundTurn1: Sound
    lateinit var soundTurn2: Sound
    lateinit var soundCrash: Sound
    lateinit var music: Music

    private lateinit var atlas: TextureAtlas

    private fun loadFonts() {
        fontLarge = BitmapFont(Gdx.files.internal("data/font.fnt"), atlas.findRegion("font"))
        fontSmall = BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"))
    }

    private fun loadStyles() {
        labelStyleLarge = LabelStyle(fontLarge, Color.WHITE)
        labelStyleSmall = LabelStyle(fontSmall, Color.WHITE)

        horizontalSeparator = NinePatchDrawable(
            NinePatch(
                atlas.findRegion("Shop/separadorHorizontal"),
                0,
                1,
                0,
                0
            )
        )
        verticalSeparator = NinePatchDrawable(
            NinePatch(
                atlas.findRegion("Shop/separadorVertical"),
                0,
                1,
                0,
                0
            )
        )

        // Button Buy
        val buttonBuy = TextureRegionDrawable(atlas.findRegion("Shop/btBuy"))
        styleTextButtonBuy = TextButtonStyle(buttonBuy, null, null, fontSmall)

        // Button Purchase
        val buttonPurchase = TextureRegionDrawable(atlas.findRegion("Shop/btPurchased"))
        styleTextButtonPurchased = TextButtonStyle(buttonPurchase, null, null, fontSmall)

        // Button Selected
        val buttonSelected = TextureRegionDrawable(atlas.findRegion("Shop/btSelected"))
        styleTextButtonSelected = TextButtonStyle(buttonSelected, null, null, fontSmall)

        styleScrollPane = ScrollPaneStyle(null, null, null, null, verticalSeparator)

        // Buttons for turning music on or off
        val buttonMusicOn = TextureRegionDrawable(atlas.findRegion("MenuPrincipal/btMusica"))
        val buttonMusicOff = TextureRegionDrawable(atlas.findRegion("MenuPrincipal/btSinMusica"))
        styleButtonMusic = ButtonStyle(buttonMusicOn, null, buttonMusicOff)
    }

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))
        loadFonts()
        loadStyles()
        title = TextureRegionDrawable(atlas.findRegion("titulo2"))
        pixelBlack = NinePatchDrawable(NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0))
        scoresBackground = TextureRegionDrawable(atlas.findRegion("fondoPuntuaciones"))
        coin = atlas.findRegion("coin")
        coinFront = atlas.findRegion("coinFrente")
        barMarkedRed = atlas.findRegion("barraMarcadorRojo")
        barMarkedGreen = atlas.findRegion("barraMarcadorVerde")
        street = atlas.findRegion("calle")
        carDevil = atlas.findRegion("Carros/diablo")
        carBanshee = atlas.findRegion("Carros/banshee")
        carTourism = atlas.findRegion("Carros/turismo")
        carBullet = atlas.findRegion("Carros/bullet")
        carTornado = atlas.findRegion("Carros/tornado")
        carAudiS5 = atlas.findRegion("Carros/Audi S5")
        carBmwX6 = atlas.findRegion("Carros/BMW X6")
        carChevroletCrossfire = atlas.findRegion("Carros/Chevrolet Crossfire")
        carCitroenC4 = atlas.findRegion("Carros/Citroen C4")
        carDodgeCharger = atlas.findRegion("Carros/Dodge Charger")
        carFiat500Lounge = atlas.findRegion("Carros/Fiat 500 Lounge")
        carHondaCRV = atlas.findRegion("Carros/Honda CRV")
        carMazda6 = atlas.findRegion("Carros/Mazda 6")
        carMazdaRX8 = atlas.findRegion("Carros/Mazda RX8")
        carSeatIbiza = atlas.findRegion("Carros/Seat Ibiza")
        carVolkswagenScirocco = atlas.findRegion("Carros/Volkswagen Scirocco")

        // The explosion animation
        val newExplosion1 = atlas.findRegion("Animaciones/newExplosion1")
        val newExplosion2 = atlas.findRegion("Animaciones/newExplosion2")
        val newExplosion3 = atlas.findRegion("Animaciones/newExplosion3")
        val newExplosion4 = atlas.findRegion("Animaciones/newExplosion4")
        val newExplosion5 = atlas.findRegion("Animaciones/newExplosion5")
        val newExplosion6 = atlas.findRegion("Animaciones/newExplosion6")
        val newExplosion7 = atlas.findRegion("Animaciones/newExplosion7")
        val newExplosion8 = atlas.findRegion("Animaciones/newExplosion8")
        val newExplosion9 = atlas.findRegion("Animaciones/newExplosion9")
        val newExplosion10 = atlas.findRegion("Animaciones/newExplosion10")
        val newExplosion11 = atlas.findRegion("Animaciones/newExplosion11")
        val newExplosion12 = atlas.findRegion("Animaciones/newExplosion12")
        val newExplosion13 = atlas.findRegion("Animaciones/newExplosion13")
        val newExplosion14 = atlas.findRegion("Animaciones/newExplosion14")
        val newExplosion15 = atlas.findRegion("Animaciones/newExplosion15")
        val newExplosion16 = atlas.findRegion("Animaciones/newExplosion16")
        val newExplosion17 = atlas.findRegion("Animaciones/newExplosion17")
        val newExplosion18 = atlas.findRegion("Animaciones/newExplosion18")
        val newExplosion19 = atlas.findRegion("Animaciones/newExplosion19")

        newExplosion = Animation(
            0.05f, newExplosion1, newExplosion2, newExplosion3,
            newExplosion4, newExplosion5, newExplosion6, newExplosion7, newExplosion8, newExplosion9,
            newExplosion10, newExplosion11, newExplosion12, newExplosion13, newExplosion14,
            newExplosion15, newExplosion16, newExplosion17, newExplosion18, newExplosion19
        )

        // All the buttons
        buttonBack = TextureRegionDrawable(atlas.findRegion("Shop/btAtras2"))
        buttonNoAds = TextureRegionDrawable(atlas.findRegion("Shop/btNoAds"))
        upgradeOff = TextureRegionDrawable(atlas.findRegion("Shop/upgradeOff"))
        upgradeOn = TextureRegionDrawable(atlas.findRegion("Shop/upgradeOn"))
        buttonFacebook = TextureRegionDrawable(atlas.findRegion("MenuPrincipal/btFacebook"))
        buttonTwitter = TextureRegionDrawable(atlas.findRegion("MenuPrincipal/btTwitter"))

        // swipe drawables (we use them as a HUD)
        swipeHand = TextureRegionDrawable(atlas.findRegion("swipeHand"))
        swipeHandDown = TextureRegionDrawable(atlas.findRegion("swipeHandDown"))
        swipeArrows = TextureRegionDrawable(atlas.findRegion("swipeArrows"))

        // Sounds and musics
        soundTurn1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/turn1.mp3"))
        soundTurn2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/turn2.mp3"))
        soundCrash = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/crash.mp3"))
        music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/DST-BreakOut.mp3"))
        music.isLooping = true

        Settings.load()

        if (Settings.isMusicOn) music.play()
    }


    fun playSound(sound: Sound) {
        if (Settings.isMusicOn) {
            sound.play(1f)
        }
    }
}