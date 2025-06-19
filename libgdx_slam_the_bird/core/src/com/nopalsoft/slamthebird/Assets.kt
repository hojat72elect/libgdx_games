package com.nopalsoft.slamthebird

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
import com.nopalsoft.slamthebird.shop.PlayerSkinsSubMenu

object Assets {
    private var textureAtlas: TextureAtlas? = null

    @JvmField
    var title: AtlasRegion? = null

    @JvmField
    var tapToPlay: AtlasRegion? = null

    @JvmField
    var bestScore: AtlasRegion? = null

    @JvmField
    var score: AtlasRegion? = null
    var combo: AtlasRegion? = null

    @JvmField
    var coinsEarned: AtlasRegion? = null
    var shop: AtlasRegion? = null

    var horizontalSeparator: NinePatchDrawable? = null
    var verticalSeparator: NinePatchDrawable? = null

    @JvmField
    var background: AtlasRegion? = null

    @JvmField
    var gameOverBackground: AtlasRegion? = null

    @JvmField
    var player: AtlasRegion? = null

    var defaultPlayerSkin: AtlasRegion? = null
    var redPlayerSkin: AtlasRegion? = null
    var bluePlayerSkin: AtlasRegion? = null

    @JvmField
    var playerJumpAnimation: Animation<TextureRegion>? = null

    @JvmField
    var playerSlamAnimation: Animation<TextureRegion>? = null

    @JvmField
    var playerHitAnimation: Animation<TextureRegion>? = null

    @JvmField
    var slamAnimation: Animation<TextureRegion>? = null

    @JvmField
    var blackPixel: NinePatchDrawable? = null

    @JvmField
    var platform: AtlasRegion? = null

    @JvmField
    var platformFireAnimation: Animation<TextureRegion>? = null

    @JvmField
    var platformBreakAnimation: Animation<TextureRegion>? = null

    @JvmField
    var buttonAchievements: TextureRegionDrawable? = null

    @JvmField
    var buttonLeaderboard: TextureRegionDrawable? = null

    @JvmField
    var buttonMore: TextureRegionDrawable? = null

    @JvmField
    var buttonRate: TextureRegionDrawable? = null

    @JvmField
    var buttonShop: TextureRegionDrawable? = null

    @JvmField
    var buttonFacebook: TextureRegionDrawable? = null

    @JvmField
    var buttonTwitter: TextureRegionDrawable? = null
    var buttonBack: TextureRegionDrawable? = null
    var buttonNoAds: TextureRegionDrawable? = null

    @JvmField
    var buttonStyleMusic: ButtonStyle? = null

    @JvmField
    var buttonStyleSound: ButtonStyle? = null

    var upgradeOn: TextureRegionDrawable? = null
    var upgradeOff: TextureRegionDrawable? = null

    @JvmField
    var scoresBackground: TextureRegionDrawable? = null

    @JvmField
    var flapSpawnRegion: AtlasRegion? = null

    @JvmField
    var flapBlueRegion: AtlasRegion? = null

    @JvmField
    var blueWingsFlapAnimation: Animation<TextureRegion>? = null

    @JvmField
    var redWingsFlapAnimation: Animation<TextureRegion>? = null

    @JvmField
    var evolvingFlapAnimation: Animation<TextureRegion?>? = null

    @JvmField
    var coinAnimation: Animation<TextureRegion>? = null

    @JvmField
    var coinsRegion: AtlasRegion? = null
    private var transparentPixelRegion: AtlasRegion? = null

    @JvmField
    var invincibilityBoost: AtlasRegion? = null

    @JvmField
    var coinRainBoost: AtlasRegion? = null

    @JvmField
    var freezeBoost: AtlasRegion? = null

    @JvmField
    var superJumpBoost: AtlasRegion? = null
    var boosts: AtlasRegion? = null

    @JvmField
    var invincibilityBoostEndAnimation: Animation<TextureRegion?>? = null

    @JvmField
    var superJumpBoostEndAnimation: Animation<TextureRegion?>? = null

    @JvmField
    var largeNum0: AtlasRegion? = null

    @JvmField
    var largeNum1: AtlasRegion? = null

    @JvmField
    var largeNum2: AtlasRegion? = null

    @JvmField
    var largeNum3: AtlasRegion? = null

    @JvmField
    var largeNum4: AtlasRegion? = null

    @JvmField
    var largeNum5: AtlasRegion? = null

    @JvmField
    var largeNum6: AtlasRegion? = null

    @JvmField
    var largeNum7: AtlasRegion? = null

    @JvmField
    var largeNum8: AtlasRegion? = null

    @JvmField
    var largeNum9: AtlasRegion? = null

    @JvmField
    var smallNum0: AtlasRegion? = null

    @JvmField
    var smallNum1: AtlasRegion? = null

    @JvmField
    var smallNum2: AtlasRegion? = null

    @JvmField
    var smallNum3: AtlasRegion? = null

    @JvmField
    var smallNum4: AtlasRegion? = null

    @JvmField
    var smallNum5: AtlasRegion? = null

    @JvmField
    var smallNum6: AtlasRegion? = null

    @JvmField
    var smallNum7: AtlasRegion? = null

    @JvmField
    var smallNum8: AtlasRegion? = null

    @JvmField
    var smallNum9: AtlasRegion? = null

    private var font: BitmapFont? = null

    var styleTextButtonBuy: TextButtonStyle? = null
    var styleTextButtonPurchased: TextButtonStyle? = null
    var styleTextButtonSelected: TextButtonStyle? = null

    var styleScrollPane: ScrollPaneStyle? = null
    var smallLabelStyle: LabelStyle? = null

    var soundJump: Sound? = null

    @JvmField
    var soundCoin: Sound? = null

    @JvmField
    var soundBoost: Sound? = null

    private var music: Music? = null

    private fun loadStyles() {
        font = BitmapFont()
        font!!.data.setScale(1.15f)

        horizontalSeparator = NinePatchDrawable(NinePatch(textureAtlas!!.findRegion("Shop/separadorHorizontal"), 0, 1, 0, 0))
        verticalSeparator = NinePatchDrawable(NinePatch(textureAtlas!!.findRegion("Shop/separadorVertical"), 0, 1, 0, 0))

        smallLabelStyle = LabelStyle(font, Color.WHITE)


        val buttonBuy = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/btBuy"))
        styleTextButtonBuy = TextButtonStyle(buttonBuy, null, null, font)


        val buttonPurchase = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/btPurchased"))
        styleTextButtonPurchased = TextButtonStyle(buttonPurchase, null, null, font)


        val buttonSelected = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/btSelected"))
        styleTextButtonSelected = TextButtonStyle(buttonSelected, null, null, font)


        val buttonMusicOn = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btMusica"))
        val buttonMusicOff = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btSinMusica"))
        buttonStyleMusic = ButtonStyle(buttonMusicOn, null, buttonMusicOff)


        val buttonSoundOn = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btSonido"))
        val buttonSoundOff = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btSinSonido"))
        buttonStyleSound = ButtonStyle(buttonSoundOn, null, buttonSoundOff)

        styleScrollPane = ScrollPaneStyle(null, null, null, null, verticalSeparator)
    }

    fun drawPlayer() {
        var selectedPlayer = "AndroidBot"

        if (Settings.selectedSkin == PlayerSkinsSubMenu.SKIN_RED_ANDROID) {
            selectedPlayer = "AndroidBotRojo"
        } else if (Settings.selectedSkin == PlayerSkinsSubMenu.SKIN_BLUE_ANDROID) {
            selectedPlayer = "AndroidBotAzul"
        }

        player = textureAtlas!!.findRegion(
            ("Personajes/" + selectedPlayer
                    + "/personajeFall")
        )

        var per1 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeSlam1")
        var per2 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeSlam2")
        var per3 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeSlam3")
        val per4 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeSlam4")
        playerSlamAnimation = Animation(.05f, per1, per2, per3, per4)

        per1 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeJump1")
        per2 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeJump1")
        per3 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeJump1")
        playerJumpAnimation = Animation(.1f, per1, per2, per3)

        per1 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeHit")
        per2 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeHit")
        per3 = textureAtlas!!.findRegion("Personajes/$selectedPlayer/personajeHit")
        playerHitAnimation = Animation(.1f, per1, per2, per3)

        // These are the ones that appear in the store;
        defaultPlayerSkin = textureAtlas!!.findRegion("Personajes/AndroidBot/personajeFall")
        redPlayerSkin = textureAtlas!!.findRegion("Personajes/AndroidBotRojo/personajeFall")
        bluePlayerSkin = textureAtlas!!.findRegion("Personajes/AndroidBotAzul/personajeFall")
    }

    fun load() {
        textureAtlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        loadStyles()

        title = textureAtlas!!.findRegion("MenuPrincipal/titulo")
        tapToPlay = textureAtlas!!.findRegion("MenuPrincipal/tapToPlay")
        bestScore = textureAtlas!!.findRegion("MenuPrincipal/bestScore")
        score = textureAtlas!!.findRegion("MenuPrincipal/score")
        combo = textureAtlas!!.findRegion("MenuPrincipal/combo")
        coinsEarned = textureAtlas!!.findRegion("MenuPrincipal/coinsEarned")

        background = textureAtlas!!.findRegion("fondo")
        scoresBackground = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/fondoPuntuaciones"))
        gameOverBackground = textureAtlas!!.findRegion("fondoGameover")

        transparentPixelRegion = textureAtlas!!.findRegion("pixelTransparente")

        shop = textureAtlas!!.findRegion("Shop/Shop")

        buttonBack = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/btAtras"))
        buttonNoAds = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/btNoAds"))

        upgradeOff = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/upgradeOff"))
        upgradeOn = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/upgradeOn"))

        blackPixel = NinePatchDrawable(NinePatch(textureAtlas!!.findRegion("MenuPrincipal/pixelNegro"), 1, 1, 0, 0))

        val per1 = textureAtlas!!.findRegion("moneda1")
        val per2 = textureAtlas!!.findRegion("moneda2")
        val per3 = textureAtlas!!.findRegion("moneda3")
        coinsRegion = per1
        coinAnimation = Animation(.15f, per1, per2, per3, per2)

        flapBlueRegion = textureAtlas!!.findRegion("InGame/flapAzul")
        flapSpawnRegion = textureAtlas!!.findRegion("InGame/flapSpawn")

        var flap1 = textureAtlas!!.findRegion("InGame/flapAzulAlas1")
        var flap2 = textureAtlas!!.findRegion("InGame/flapAzulAlas2")
        var flap3 = textureAtlas!!.findRegion("InGame/flapAzulAlas3")
        blueWingsFlapAnimation = Animation(.15f, flap1, flap2, flap3, flap2)

        flap1 = textureAtlas!!.findRegion("InGame/flapRojoAlas1")
        flap2 = textureAtlas!!.findRegion("InGame/flapRojoAlas2")
        flap3 = textureAtlas!!.findRegion("InGame/flapRojoAlas3")
        redWingsFlapAnimation = Animation(.15f, flap1, flap2, flap3, flap2)
        evolvingFlapAnimation = Animation(.075f, flapBlueRegion, flap1, flapBlueRegion, flap2, flapBlueRegion, flap3)

        flap1 = textureAtlas!!.findRegion("InGame/plataformFire1")
        flap2 = textureAtlas!!.findRegion("InGame/plataformFire2")
        flap3 = textureAtlas!!.findRegion("InGame/plataformFire3")
        platformFireAnimation = Animation(.15f, flap1, flap2, flap3, flap2)

        flap1 = textureAtlas!!.findRegion("InGame/plataforma2")
        flap2 = textureAtlas!!.findRegion("InGame/plataforma3")
        flap3 = textureAtlas!!.findRegion("InGame/plataforma4")
        platformBreakAnimation = Animation(.1f, flap1, flap2, flap3)
        platform = textureAtlas!!.findRegion("InGame/plataforma1")

        flap1 = textureAtlas!!.findRegion("InGame/slam1")
        flap2 = textureAtlas!!.findRegion("InGame/slam2")
        flap3 = textureAtlas!!.findRegion("InGame/slam3")
        slamAnimation = Animation(.1f, flap1, flap2, flap3)

        invincibilityBoost = textureAtlas!!.findRegion("InGame/boostInvencible")
        coinRainBoost = textureAtlas!!.findRegion("InGame/boostCoinRain")
        freezeBoost = textureAtlas!!.findRegion("InGame/boostIce")
        superJumpBoost = textureAtlas!!.findRegion("InGame/boostSuperSalto")
        boosts = textureAtlas!!.findRegion("InGame/boosts")

        invincibilityBoostEndAnimation = Animation(.15f, invincibilityBoost, transparentPixelRegion)
        superJumpBoostEndAnimation = Animation(.15f, superJumpBoost, transparentPixelRegion)

        buttonAchievements = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btAchievements"))
        buttonLeaderboard = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btLeaderboard"))
        buttonMore = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btMore"))
        buttonRate = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btRate"))
        buttonShop = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btShop"))
        buttonFacebook = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btFacebook"))
        buttonTwitter = TextureRegionDrawable(textureAtlas!!.findRegion("MenuPrincipal/btTwitter"))

        largeNum0 = textureAtlas!!.findRegion("Numeros/num0")
        largeNum1 = textureAtlas!!.findRegion("Numeros/num1")
        largeNum2 = textureAtlas!!.findRegion("Numeros/num2")
        largeNum3 = textureAtlas!!.findRegion("Numeros/num3")
        largeNum4 = textureAtlas!!.findRegion("Numeros/num4")
        largeNum5 = textureAtlas!!.findRegion("Numeros/num5")
        largeNum6 = textureAtlas!!.findRegion("Numeros/num6")
        largeNum7 = textureAtlas!!.findRegion("Numeros/num7")
        largeNum8 = textureAtlas!!.findRegion("Numeros/num8")
        largeNum9 = textureAtlas!!.findRegion("Numeros/num9")

        smallNum0 = textureAtlas!!.findRegion("Numeros/0")
        smallNum1 = textureAtlas!!.findRegion("Numeros/1")
        smallNum2 = textureAtlas!!.findRegion("Numeros/2")
        smallNum3 = textureAtlas!!.findRegion("Numeros/3")
        smallNum4 = textureAtlas!!.findRegion("Numeros/4")
        smallNum5 = textureAtlas!!.findRegion("Numeros/5")
        smallNum6 = textureAtlas!!.findRegion("Numeros/6")
        smallNum7 = textureAtlas!!.findRegion("Numeros/7")
        smallNum8 = textureAtlas!!.findRegion("Numeros/8")
        smallNum9 = textureAtlas!!.findRegion("Numeros/9")

        Settings.load()

        // Must be called after loading settings
        drawPlayer()

        soundCoin = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/pickCoin.mp3"))
        soundJump = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/salto.mp3"))
        soundBoost = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/pickBoost.mp3"))

        music = Gdx.audio.newMusic(
            Gdx.files
                .internal("data/Sounds/musica.mp3")
        )

        music?.isLooping = true

        if (Settings.isMusicOn) playMusic()
    }

    @JvmStatic
    fun playSound(sound: Sound) {
        if (Settings.isSoundOn) sound.play(1f)
    }

    @JvmStatic
    fun playMusic() {
        music!!.play()
    }

    @JvmStatic
    fun pauseMusic() {
        music!!.pause()
    }
}
