package com.nopalsoft.invaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.invaders.parallax.ParallaxBackground
import com.nopalsoft.invaders.parallax.ParallaxLayer

object Assets {
    @JvmField
    var languagesBundle: I18NBundle? = null
    private val glyphLayout = GlyphLayout()

    var backgroundAtlasRegion: AtlasRegion? = null

    @JvmField
    var backgroundLayer: ParallaxBackground? = null

    @JvmField
    var spaceShipLeft: AtlasRegion? = null

    @JvmField
    var spaceShipRight: AtlasRegion? = null

    @JvmField
    var spaceShip: AtlasRegion? = null

    // Fonts
    @JvmField
    var font60: BitmapFont? = null // Mainly for the title of the app

    @JvmField
    var font45: BitmapFont? = null

    @JvmField
    var font15: BitmapFont? = null

    @JvmField
    var font10: BitmapFont? = null

    // Menu
    @JvmField
    var leftMenuEllipse: AtlasRegion? = null

    @JvmField
    var buttonSignInUp: NinePatchDrawable? = null

    @JvmField
    var buttonSignInDown: NinePatchDrawable? = null

    @JvmField
    var titleMenuBox: NinePatchDrawable? = null

    // Game
    @JvmField
    var inGameStatusDrawable: NinePatchDrawable? = null

    @JvmField
    var buttonLeft: TextureRegionDrawable? = null

    @JvmField
    var buttonRight: TextureRegionDrawable? = null

    @JvmField
    var buttonFire: TextureRegionDrawable? = null

    @JvmField
    var buttonFirePressed: TextureRegionDrawable? = null

    @JvmField
    var buttonMissile: TextureRegionDrawable? = null

    @JvmField
    var buttonMissilePressed: TextureRegionDrawable? = null

    // Aid
    @JvmField
    var help1: AtlasRegion? = null

    @JvmField
    var helpClick: AtlasRegion? = null

    // Buttons
    @JvmField
    var buttonSoundOn: TextureRegionDrawable? = null

    @JvmField
    var buttonSoundOff: TextureRegionDrawable? = null

    @JvmField
    var buttonMusicOn: TextureRegionDrawable? = null

    @JvmField
    var buttonMusicOff: TextureRegionDrawable? = null

    // Ammunition
    @JvmField
    var normalBullet: AtlasRegion? = null

    @JvmField
    var missileAnimation: Animation<TextureRegion?>? = null
    var superBombAnimation: Animation<TextureRegion?>? = null

    @JvmField
    var bulletLevel1: AtlasRegion? = null

    @JvmField
    var bulletLevel2: AtlasRegion? = null

    @JvmField
    var bulletLevel3: AtlasRegion? = null

    @JvmField
    var bulletLevel4: AtlasRegion? = null

    @JvmField
    var boost1: AtlasRegion? = null

    @JvmField
    var boost2: AtlasRegion? = null

    @JvmField
    var boost3: AtlasRegion? = null

    @JvmField
    var upgLife: AtlasRegion? = null

    @JvmField
    var explosionAnimation: Animation<TextureRegion?>? = null

    @JvmField
    var shieldAnimation: Animation<TextureRegion?>? = null

    @JvmField
    var normalEnemyBullet: AtlasRegion? = null

    @JvmField
    var alien1: AtlasRegion? = null

    @JvmField
    var alien2: AtlasRegion? = null

    @JvmField
    var alien3: AtlasRegion? = null

    @JvmField
    var alien4: AtlasRegion? = null

    // Sounds
    @JvmField
    var music: Music? = null

    @JvmField
    var coinSound: Sound? = null

    @JvmField
    var clickSound: Sound? = null

    @JvmField
    var explosionSound: Sound? = null

    @JvmField
    var missileFiringSound: Sound? = null

    // Styles
    @JvmField
    var styleTextButtonMenu: TextButtonStyle? = null

    @JvmField
    var styleTextButtonFacebook: TextButtonStyle? = null

    @JvmField
    var styleTextButtonBack: TextButtonStyle? = null

    @JvmField
    var styleTextButton: TextButtonStyle? = null

    @JvmField
    var styleDialogPause: WindowStyle? = null

    @JvmField
    var styleLabel: LabelStyle? = null

    @JvmField
    var styleLabelDialog: LabelStyle? = null

    @JvmField
    var styleSlider: SliderStyle? = null

    @JvmField
    var styleImageButtonPause: ImageButtonStyle? = null

    @JvmField
    var styleImageButtonStyleCheckBox: ImageButtonStyle? = null


    private fun loadFont(atlas: TextureAtlas) {
        font60 = BitmapFont(Gdx.files.internal("data/font35.fnt"), atlas.findRegion("font35"), false)
        font45 = BitmapFont(Gdx.files.internal("data/font35.fnt"), atlas.findRegion("font35"), false)
        font15 = BitmapFont(Gdx.files.internal("data/font15.fnt"), atlas.findRegion("font15"), false)
        font10 = BitmapFont(Gdx.files.internal("data/font15.fnt"), atlas.findRegion("font15"), false)

        font60!!.color = Color.GREEN
        font45!!.color = Color.GREEN
        font15!!.color = Color.GREEN
        font10!!.color = Color.GREEN
    }

    private fun loadSceneStyles(atlas: TextureAtlas) {
        // Dialog

        val loginDialogDrawable = NinePatchDrawable(atlas.createPatch("recuadroLogIn"))
        val dialogDim = atlas.findRegion("fondoNegro")
        styleDialogPause = WindowStyle(font45, Color.GREEN, loginDialogDrawable)
        styleDialogPause!!.stageBackground = NinePatchDrawable(NinePatch(dialogDim))
        styleLabelDialog = LabelStyle(font15, Color.GREEN)

        val defaultRoundDown = NinePatchDrawable(atlas.createPatch("botonDown"))
        val defaultRound = NinePatchDrawable(atlas.createPatch("boton"))
        styleTextButton = TextButtonStyle(defaultRound, defaultRoundDown, null, font15)
        styleTextButton!!.fontColor = Color.GREEN

        // Menu
        val menuButtonDrawable = NinePatchDrawable(atlas.createPatch("botonMenu"))
        val menuButtonPressedDrawable = NinePatchDrawable(atlas.createPatch("botonMenuPresionado"))
        styleTextButtonMenu = TextButtonStyle(menuButtonDrawable, menuButtonPressedDrawable, null, font45)
        styleTextButtonMenu!!.fontColor = Color.GREEN

        styleLabel = LabelStyle(font15, Color.GREEN)

        // Slider
        val defaultSlider = NinePatchDrawable(atlas.createPatch("default-slider"))
        val defaultSliderKnob = TextureRegionDrawable(atlas.findRegion("default-slider-knob"))

        styleSlider = SliderStyle(defaultSlider, defaultSliderKnob)

        val buttonBackUp = TextureRegionDrawable(atlas.findRegion("btBack"))
        val buttonBackDown = TextureRegionDrawable(atlas.findRegion("btBackDown"))

        styleTextButtonBack = TextButtonStyle(buttonBackUp, buttonBackDown, null, font15)
        styleTextButtonBack!!.fontColor = Color.GREEN

        val buttonPauseUp = TextureRegionDrawable(atlas.findRegion("btPause"))
        val buttonPauseDown = TextureRegionDrawable(atlas.findRegion("btPauseDown"))
        styleImageButtonPause = ImageButtonStyle(buttonPauseUp, buttonPauseDown, null, null, null, null)

        val buttonFacebook = NinePatchDrawable(atlas.createPatch("btShareFacebookUp"))
        val buttonFacebookPressed = NinePatchDrawable(atlas.createPatch("btShareFacebookDown"))
        styleTextButtonFacebook = TextButtonStyle(buttonFacebook, buttonFacebookPressed, null, font10)

        val checked = TextureRegionDrawable(atlas.findRegion("checkBoxDown"))
        val unchecked = TextureRegionDrawable(atlas.findRegion("checkBox"))

        styleImageButtonStyleCheckBox = ImageButtonStyle(unchecked, checked, checked, null, null, null)
    }

    fun load() {
        languagesBundle = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))


        val textureAtlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        loadFont(textureAtlas)
        loadSceneStyles(textureAtlas)

        // Menu
        leftMenuEllipse = textureAtlas.findRegion("elipseMenuIzq")
        titleMenuBox = NinePatchDrawable(textureAtlas.createPatch("tituloMenuRecuadro"))

        // Game
        inGameStatusDrawable = NinePatchDrawable(textureAtlas.createPatch("recuadroInGameStatus"))
        buttonLeft = TextureRegionDrawable(textureAtlas.findRegion("btLeft"))
        buttonRight = TextureRegionDrawable(textureAtlas.findRegion("btRight"))
        buttonFire = TextureRegionDrawable(textureAtlas.findRegion("btFire"))
        buttonFirePressed = TextureRegionDrawable(textureAtlas.findRegion("btFire"))
        buttonMissile = TextureRegionDrawable(textureAtlas.findRegion("btMissil"))
        buttonMissilePressed = TextureRegionDrawable(textureAtlas.findRegion("btMissil"))

        backgroundAtlasRegion = textureAtlas.findRegion("fondo")

        buttonSignInUp = NinePatchDrawable(NinePatch(textureAtlas.createPatch("btSignInUp")))
        buttonSignInDown = NinePatchDrawable(NinePatch(textureAtlas.createPatch("btSignInDown")))

        // Aid
        help1 = textureAtlas.findRegion("help1")
        helpClick = textureAtlas.findRegion("ayudaClick")

        // Buttons
        buttonMusicOn = TextureRegionDrawable(textureAtlas.findRegion("btMusica"))
        buttonMusicOff = TextureRegionDrawable(textureAtlas.findRegion("btSinMusica"))
        buttonSoundOn = TextureRegionDrawable(textureAtlas.findRegion("btSonido"))
        buttonSoundOff = TextureRegionDrawable(textureAtlas.findRegion("btSinSonido"))

        // spaceship
        spaceShipRight = textureAtlas.findRegion("naveRight")
        spaceShipLeft = textureAtlas.findRegion("naveLeft")
        spaceShip = textureAtlas.findRegion("nave")

        val shield0 = textureAtlas.findRegion("shield0")
        val shield1 = textureAtlas.findRegion("shield1")
        val shield2 = textureAtlas.findRegion("shield2")
        val shield3 = textureAtlas.findRegion("shield3")
        val shield4 = textureAtlas.findRegion("shield4")
        val shield5 = textureAtlas.findRegion("shield5")
        val shield6 = textureAtlas.findRegion("shield6")
        val shield7 = textureAtlas.findRegion("shield7")
        val shield8 = textureAtlas.findRegion("shield9")
        val shield9 = textureAtlas.findRegion("shield9")
        val shield10 = textureAtlas.findRegion("shield10")
        val shield11 = textureAtlas.findRegion("shield11")
        shieldAnimation = Animation<TextureRegion?>(.1f, shield0, shield1, shield2, shield3, shield4, shield5, shield6, shield7, shield8, shield9, shield10, shield11)

        // UFO
        alien1 = textureAtlas.findRegion("alien1")
        alien2 = textureAtlas.findRegion("alien2")
        alien3 = textureAtlas.findRegion("alien3")
        alien4 = textureAtlas.findRegion("alien4")

        boost1 = textureAtlas.findRegion("upgLaser")
        boost2 = textureAtlas.findRegion("upgBomb")
        boost3 = textureAtlas.findRegion("upgShield")
        upgLife = textureAtlas.findRegion("upgLife")

        // Ammunition
        normalBullet = textureAtlas.findRegion("balaNormal")
        normalEnemyBullet = textureAtlas.findRegion("balaNormalEnemigo")

        val missile1AtlasRegion = textureAtlas.findRegion("misil1")
        val missile2AtlasRegion = textureAtlas.findRegion("misil2")
        missileAnimation = Animation<TextureRegion?>(0.2f, missile1AtlasRegion, missile2AtlasRegion)

        val superBomb1AtlasRegion = textureAtlas.findRegion("superRayo1")
        val superBomb2AtlasRegion = textureAtlas.findRegion("superRayo2")
        superBombAnimation = Animation<TextureRegion?>(0.2f, superBomb1AtlasRegion, superBomb2AtlasRegion)

        bulletLevel1 = textureAtlas.findRegion("disparoA1")
        bulletLevel2 = textureAtlas.findRegion("disparoA2")
        bulletLevel3 = textureAtlas.findRegion("disparoA3")
        bulletLevel4 = textureAtlas.findRegion("disparoA4")

        // explosion Fire
        val explosionFrame1 = textureAtlas.findRegion("newExplosion1")
        val explosionFrame2 = textureAtlas.findRegion("newExplosion2")
        val explosionFrame3 = textureAtlas.findRegion("newExplosion3")
        val explosionFrame4 = textureAtlas.findRegion("newExplosion4")
        val explosionFrame5 = textureAtlas.findRegion("newExplosion5")
        val explosionFrame6 = textureAtlas.findRegion("newExplosion6")
        val explosionFrame7 = textureAtlas.findRegion("newExplosion7")
        val explosionFrame8 = textureAtlas.findRegion("newExplosion8")
        val explosionFrame9 = textureAtlas.findRegion("newExplosion9")
        val explosionFrame10 = textureAtlas.findRegion("newExplosion10")
        val explosionFrame11 = textureAtlas.findRegion("newExplosion11")
        val explosionFrame12 = textureAtlas.findRegion("newExplosion12")
        val explosionFrame13 = textureAtlas.findRegion("newExplosion13")
        val explosionFrame14 = textureAtlas.findRegion("newExplosion14")
        val explosionFrame15 = textureAtlas.findRegion("newExplosion15")
        val explosionFrame16 = textureAtlas.findRegion("newExplosion16")
        val explosionFrame17 = textureAtlas.findRegion("newExplosion17")
        val explosionFrame18 = textureAtlas.findRegion("newExplosion18")
        val explosionFrame19 = textureAtlas.findRegion("newExplosion19")
        explosionAnimation = Animation<TextureRegion?>(
            0.05f,
            explosionFrame1,
            explosionFrame2,
            explosionFrame3,
            explosionFrame4,
            explosionFrame5,
            explosionFrame6,
            explosionFrame7,
            explosionFrame8,
            explosionFrame9,
            explosionFrame10,
            explosionFrame11,
            explosionFrame12,
            explosionFrame13,
            explosionFrame14,
            explosionFrame15,
            explosionFrame16,
            explosionFrame17,
            explosionFrame18,
            explosionFrame19
        )

        val parallaxLayer = ParallaxLayer(backgroundAtlasRegion, Vector2(0f, 50f), Vector2(0f, 0f))
        val arrayLayers = arrayOf(parallaxLayer)
        backgroundLayer = ParallaxBackground(arrayLayers, 320f, 480f, Vector2(0f, 1f))

        music = Gdx.audio.newMusic(Gdx.files.internal("data/sonidos/musica.mp3"))
        music?.isLooping = true
        music?.volume = 0.1f
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/coin.ogg"))
        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/click.ogg"))
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/sound_explode.ogg"))
        missileFiringSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/missilFire3.ogg"))

        Settings.load()
        if (Settings.musicEnabled) music?.play()
    }

    @JvmStatic
    fun playSound(sound: Sound, volumen: Float) {
        if (Settings.soundEnabled) sound.play(volumen)
    }

    @JvmStatic
    fun playSound(sound: Sound) {
        if (Settings.soundEnabled) sound.play(1f)
    }

    @JvmStatic
    fun getTextWidth(font: BitmapFont?, text: String?): Float {
        glyphLayout.setText(font, text)
        return glyphLayout.width
    }
}
