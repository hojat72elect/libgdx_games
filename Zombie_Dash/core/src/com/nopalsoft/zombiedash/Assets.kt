package com.nopalsoft.zombiedash

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.nopalsoft.zombiedash.Settings.isSoundOn
import com.nopalsoft.zombiedash.parallax.ParallaxBackground
import com.nopalsoft.zombiedash.parallax.ParallaxLayer

object Assets {
    var fontChico: BitmapFont? = null
    var fontGrande: BitmapFont? = null

    var zombieDashTitulo: TextureRegionDrawable? = null

    var weapon: AtlasRegion? = null
    var itemJump: AtlasRegion? = null

    var itemGem: AtlasRegion? = null
    var itemHeart: AtlasRegion? = null
    var itemShield: AtlasRegion? = null
    var itemSkull: AtlasRegion? = null
    var crate: AtlasRegion? = null
    var saw: AtlasRegion? = null
    var sawDialog: AtlasRegion? = null
    var spike: AtlasRegion? = null
    var weaponSmall: AtlasRegion? = null

    var redBar: AtlasRegion? = null
    var whiteBar: AtlasRegion? = null

    var upgradeOff: TextureRegionDrawable? = null

    /**
     * Heros
     */
    var heroSwatRun: AnimationSprite? = null
    var heroSwatDie: AnimationSprite? = null
    var heroSwatJump: AnimationSprite? = null
    var heroSwatHurt: Sprite? = null

    var heroForceRun: AnimationSprite? = null
    var heroForceDie: AnimationSprite? = null
    var heroForceJump: AnimationSprite? = null
    var heroForceHurt: Sprite? = null

    var heroRamboRun: AnimationSprite? = null
    var heroRamboDie: AnimationSprite? = null
    var heroRamboJump: AnimationSprite? = null
    var heroRamboHurt: Sprite? = null

    var heroSoldierRun: AnimationSprite? = null
    var heroSoldierDie: AnimationSprite? = null
    var heroSoldierJump: AnimationSprite? = null
    var heroSoldierHurt: Sprite? = null

    var heroVaderRun: AnimationSprite? = null
    var heroVaderDie: AnimationSprite? = null
    var heroVaderJump: AnimationSprite? = null
    var heroVaderHurt: Sprite? = null

    /**
     * Zombies
     */
    var zombieKidWalk: AnimationSprite? = null
    var zombieKidRise: AnimationSprite? = null
    var zombieKidDie: AnimationSprite? = null
    var zombieKidHurt: Sprite? = null

    var zombiePanWalk: AnimationSprite? = null
    var zombiePanRise: AnimationSprite? = null
    var zombiePanDie: AnimationSprite? = null
    var zombiePanHurt: Sprite? = null

    var zombieCuasyWalk: AnimationSprite? = null
    var zombieCuasyRise: AnimationSprite? = null
    var zombieCuasyDie: AnimationSprite? = null
    var zombieCuasyHurt: Sprite? = null

    var zombieFrankWalk: AnimationSprite? = null
    var zombieFrankRise: AnimationSprite? = null
    var zombieFrankDie: AnimationSprite? = null
    var zombieFrankHurt: Sprite? = null

    var zombieMummyWalk: AnimationSprite? = null
    var zombieMummyRise: AnimationSprite? = null
    var zombieMummyDie: AnimationSprite? = null
    var zombieMummyHurt: Sprite? = null

    /**
     * Bullet
     */
    var bullet1: AnimationSprite? = null
    var bullet2: AnimationSprite? = null
    var bullet3: AnimationSprite? = null
    var bullet4: AnimationSprite? = null
    var bullet5: AnimationSprite? = null
    var muzzle: AnimationSprite? = null

    var containerButtons: TextureRegionDrawable? = null
    var btPlay: TextureRegionDrawable? = null
    var btLeaderboard: TextureRegionDrawable? = null
    var btAchievement: TextureRegionDrawable? = null
    var btSettings: TextureRegionDrawable? = null
    var btFacebook: TextureRegionDrawable? = null

    var btTwitter: TextureRegionDrawable? = null
    var btPause: TextureRegionDrawable? = null
    var btFire: TextureRegionDrawable? = null
    var btJump: TextureRegionDrawable? = null
    var btClose: TextureRegionDrawable? = null
    var btMenu: TextureRegionDrawable? = null
    var btTryAgain: TextureRegionDrawable? = null
    var btShop: TextureRegionDrawable? = null
    var btPlayer: TextureRegionDrawable? = null
    var btGems: TextureRegionDrawable? = null
    var btMore: TextureRegionDrawable? = null

    var pisoRojo: AtlasRegion? = null
    var pisoVerde: AtlasRegion? = null

    var parallaxBackground: ParallaxBackground? = null

    var backgroundProgressBar: TextureRegionDrawable? = null
    var backgroundBigWindow: TextureRegionDrawable? = null
    var backgroundSmallWindow: TextureRegionDrawable? = null
    var storeTableBackground: NinePatchDrawable? = null

    var helpDialog: NinePatchDrawable? = null
    var helpDialogInverted: NinePatchDrawable? = null

    var labelStyleChico: LabelStyle? = null
    var labelStyleGrande: LabelStyle? = null
    var labelStyleHelpDialog: LabelStyle? = null
    var sliderStyle: SliderStyle? = null

    var styleButtonMusic: ButtonStyle? = null
    var styleButtonSound: ButtonStyle? = null

    var styleTextButtonBuy: TextButtonStyle? = null
    var styleTextButtonShare: TextButtonStyle? = null
    var styleTextButtonPurchased: TextButtonStyle? = null
    var styleTextButtonFacebook: TextButtonStyle? = null
    var styleScrollPane: ScrollPaneStyle? = null

    var pixelNegro: NinePatchDrawable? = null

    var shoot1: Sound? = null
    var noBullet: Sound? = null
    var zombiePan: Sound? = null
    var zombieKid: Sound? = null
    var zombieCuasy: Sound? = null
    var zombieMummy: Sound? = null
    var zombieFrank: Sound? = null

    var hurt1: Sound? = null
    var hurt2: Sound? = null
    var hurt3: Sound? = null
    var gem: Sound? = null
    var jump: Sound? = null
    var shield: Sound? = null
    var hearth: Sound? = null

    fun loadStyles(atlas: TextureAtlas) {
        // Label Style
        labelStyleChico = LabelStyle(fontChico, Color.WHITE)
        labelStyleGrande = LabelStyle(fontGrande, Color.WHITE)
        labelStyleHelpDialog = LabelStyle(fontChico, Color.BLACK)

        /* Button Buy */
        val btBuy = TextureRegionDrawable(atlas.findRegion("UI/btBuy"))
        styleTextButtonBuy = TextButtonStyle(btBuy, null, null, fontChico)

        /* Button Purchased */
        val btPurchased = TextureRegionDrawable(atlas.findRegion("UI/btPurchased"))
        styleTextButtonPurchased = TextButtonStyle(btPurchased, null, null, fontChico)

        /* Button Share */
        styleTextButtonShare = TextButtonStyle(btBuy, null, null, fontChico)

        /* Button Musica */
        val btMusicOn = TextureRegionDrawable(atlas.findRegion("UI/btMusic"))
        val btMusicOff = TextureRegionDrawable(atlas.findRegion("UI/btMusicOff"))
        styleButtonMusic = ButtonStyle(btMusicOn, null, btMusicOff)

        /* Button FacebooLogin */
        val btFacebookLogin = NinePatchDrawable(NinePatch(atlas.findRegion("UI/btFacebookLogin"), 50, 26, 5, 5))
        styleTextButtonFacebook = TextButtonStyle(btFacebookLogin, null, null, fontChico)

        /* Boton Sonido */
        val botonSonidoOn = TextureRegionDrawable(atlas.findRegion("UI/btSound"))
        val botonSonidoOff = TextureRegionDrawable(atlas.findRegion("UI/btSoundOff"))
        styleButtonSound = ButtonStyle(botonSonidoOn, null, botonSonidoOff)

        // Scrollpane
        val separadorVertical = TextureRegionDrawable(atlas.findRegion("UI/separadorVertical"))
        val knobScroll = TextureRegionDrawable(atlas.findRegion("knob"))
        styleScrollPane = ScrollPaneStyle(null, null, null, separadorVertical, knobScroll)

        // sliderStyle
        val separadorHorizontal = TextureRegionDrawable(atlas.findRegion("UI/separadorHorizontal"))
        sliderStyle = SliderStyle(separadorHorizontal, knobScroll)
    }

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontChico = BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"))
        fontGrande = BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"))

        loadStyles(atlas)

        zombieDashTitulo = TextureRegionDrawable(atlas.findRegion("UI/titulo"))

        backgroundProgressBar = TextureRegionDrawable(atlas.findRegion("backgroundProgressBar"))
        backgroundBigWindow = TextureRegionDrawable(atlas.findRegion("UI/ventanaGrande"))
        backgroundSmallWindow = TextureRegionDrawable(atlas.findRegion("UI/ventanaChica"))
        storeTableBackground = NinePatchDrawable(NinePatch(atlas.findRegion("UI/storeTableBackground"), 50, 40, 50, 40))

        helpDialog = NinePatchDrawable(NinePatch(atlas.findRegion("UI/helpDialog"), 56, 17, 15, 50))
        helpDialogInverted = NinePatchDrawable(NinePatch(atlas.findRegion("UI/helpDialogInverted"), 56, 17, 50, 15))

        pixelNegro = NinePatchDrawable(NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0))

        pisoRojo = atlas.findRegion("Plataformas/pisoRojo")
        pisoVerde = atlas.findRegion("Plataformas/pisoVerde")

        containerButtons = TextureRegionDrawable(atlas.findRegion("UI/containerButtons"))
        btPlay = TextureRegionDrawable(atlas.findRegion("UI/btPlay"))
        btLeaderboard = TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"))
        btAchievement = TextureRegionDrawable(atlas.findRegion("UI/btAchievement"))
        btSettings = TextureRegionDrawable(atlas.findRegion("UI/btSettings"))
        btFacebook = TextureRegionDrawable(atlas.findRegion("UI/btFacebook"))
        btTwitter = TextureRegionDrawable(atlas.findRegion("UI/btTwitter"))

        btPause = TextureRegionDrawable(atlas.findRegion("btPause"))
        btFire = TextureRegionDrawable(atlas.findRegion("btFire"))
        btJump = TextureRegionDrawable(atlas.findRegion("btUp"))
        btClose = TextureRegionDrawable(atlas.findRegion("UI/btClose"))
        btMenu = TextureRegionDrawable(atlas.findRegion("UI/btMenu"))
        btTryAgain = TextureRegionDrawable(atlas.findRegion("UI/btTryAgain"))
        btShop = TextureRegionDrawable(atlas.findRegion("UI/btShop"))
        btPlayer = TextureRegionDrawable(atlas.findRegion("UI/btPlayer"))
        btGems = TextureRegionDrawable(atlas.findRegion("UI/btGems"))
        btMore = TextureRegionDrawable(atlas.findRegion("UI/btMore"))
        upgradeOff = TextureRegionDrawable(atlas.findRegion("UI/upgradeOff"))
        weapon = atlas.findRegion("UI/weapon")

        /*
         * Items
         */
        itemGem = atlas.findRegion("gem")
        itemHeart = atlas.findRegion("heart")
        itemShield = atlas.findRegion("shield")
        itemSkull = atlas.findRegion("skull")
        itemJump = atlas.findRegion("jump")

        crate = atlas.findRegion("crate")
        saw = atlas.findRegion("saw")
        sawDialog = atlas.findRegion("sawDialog")
        spike = atlas.findRegion("spike")
        weaponSmall = atlas.findRegion("weaponSmall")

        redBar = atlas.findRegion("redBar")
        whiteBar = atlas.findRegion("whiteBar")

        /*
         * Hero Swat
         */
        heroSwatRun = loadAnimationRun(atlas, "HeroSwat/")
        heroSwatJump = loadAnimationJump(atlas, "HeroSwat/")
        heroSwatDie = loadAnimationDie(atlas, "HeroSwat/")
        heroSwatHurt = atlas.createSprite("HeroSwat/hurt")

        /*
         * Hero Force
         */
        heroForceRun = loadAnimationRun(atlas, "HeroForce/")
        heroForceJump = loadAnimationJump(atlas, "HeroForce/")
        heroForceDie = loadAnimationDie(atlas, "HeroForce/")
        heroForceHurt = atlas.createSprite("HeroForce/hurt")

        /*
         * Hero Rambo
         */
        heroRamboRun = loadAnimationRun(atlas, "HeroRambo/")
        heroRamboJump = loadAnimationJump(atlas, "HeroRambo/")
        heroRamboDie = loadAnimationDie(atlas, "HeroRambo/")
        heroRamboHurt = atlas.createSprite("HeroRambo/hurt")

        /*
         * Hero Soldier
         */
        heroSoldierRun = loadAnimationRun(atlas, "HeroSoldier/")
        heroSoldierJump = loadAnimationJump(atlas, "HeroSoldier/")
        heroSoldierDie = loadAnimationDie(atlas, "HeroSoldier/")
        heroSoldierHurt = atlas.createSprite("HeroSoldier/hurt")

        /*
         * Hero Vader
         */
        heroVaderRun = loadAnimationRun(atlas, "HeroVader/")
        heroVaderJump = loadAnimationJump(atlas, "HeroVader/")
        heroVaderDie = loadAnimationDie(atlas, "HeroVader/")
        heroVaderHurt = atlas.createSprite("HeroVader/hurt")

        /*
         * Zombie kid
         */
        zombieKidWalk = loadAnimationWalk(atlas, "ZombieKid/")
        zombieKidRise = loadAnimationRise(atlas, "ZombieKid/")
        zombieKidDie = loadAnimationDie(atlas, "ZombieKid/")
        zombieKidHurt = atlas.createSprite("ZombieKid/die1")

        /*
         * Zombie pan
         */
        zombiePanWalk = loadAnimationWalk(atlas, "ZombiePan/")
        zombiePanRise = loadAnimationRise(atlas, "ZombiePan/")
        zombiePanDie = loadAnimationDie(atlas, "ZombiePan/")
        zombiePanHurt = atlas.createSprite("ZombiePan/die1")

        /*
         * Zombie Cuasy
         */
        zombieCuasyWalk = loadAnimationWalk(atlas, "ZombieCuasy/")
        zombieCuasyRise = loadAnimationRise(atlas, "ZombieCuasy/")
        zombieCuasyDie = loadAnimationDie(atlas, "ZombieCuasy/")
        zombieCuasyHurt = atlas.createSprite("ZombieCuasy/die1")

        /*
         * Zombie Frank
         */
        zombieFrankWalk = loadAnimationWalk(atlas, "ZombieFrank/")
        zombieFrankRise = loadAnimationRise(atlas, "ZombieFrank/")
        zombieFrankDie = loadAnimationDie(atlas, "ZombieFrank/")
        zombieFrankHurt = atlas.createSprite("ZombieFrank/die1")

        /*
         * Zombie mummy
         */
        zombieMummyWalk = loadAnimationWalk(atlas, "ZombieMummy/")
        zombieMummyRise = loadAnimationRise(atlas, "ZombieMummy/")
        zombieMummyDie = loadAnimationDie(atlas, "ZombieMummy/")
        zombieMummyHurt = atlas.createSprite("ZombieMummy/die1")

        /*
         * Bullets
         */
        bullet1 = loadAnimationBullet(atlas, "Bullet/bullet1")
        bullet2 = loadAnimationBullet(atlas, "Bullet/bullet2")
        bullet3 = loadAnimationBullet(atlas, "Bullet/bullet3")
        bullet4 = loadAnimationBullet(atlas, "Bullet/bullet4")
        bullet5 = loadAnimationBullet(atlas, "Bullet/bullet5")
        muzzle = loadAnimationMuzzle(atlas)

        // Parallax stuff
        val layerFondo = ParallaxLayer(atlas.findRegion("background"), Vector2(1.5f, 0f), Vector2(798f, 0f))
        val fondoFlipped = atlas.findRegion("backgroundFlipped")
        fondoFlipped.flip(true, false)
        val layerFondo2 = ParallaxLayer(fondoFlipped, Vector2(1.5f, 0f), Vector2(799f, 0f), Vector2(798f, 0f))
        val layerMoon = ParallaxLayer(atlas.findRegion("moon"), Vector2(2f, 0f), Vector2(799f, 260f), Vector2(2500f, 300f))

        val `as`: kotlin.Array<ParallaxLayer?> = arrayOf(layerFondo, layerFondo2, layerMoon)
        parallaxBackground = ParallaxBackground(`as`, 800f, 480f, Vector2(10f, 0f))

        Settings.load()

        shoot1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/shoot2.mp3"))
        noBullet = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/noBullet.mp3"))
        zombiePan = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombiePan.mp3"))
        zombieKid = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieKid.mp3"))
        zombieCuasy = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieCuasy.mp3"))
        zombieMummy = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieMummy.mp3"))
        zombieFrank = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieFrank.mp3"))

        hurt1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt.mp3"))
        hurt2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt2.mp3"))
        hurt3 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt3.mp3"))

        gem = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/gem.mp3"))
        jump = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/jump.mp3"))
        shield = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/pick.mp3"))
        hearth = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hearth.mp3"))
    }

    private fun loadAnimationRun(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "run" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .004f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationDie(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "die" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .03f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationJump(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "jump" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .035f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationWalk(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "walk" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .009f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationRise(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "rise" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .00575f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationBullet(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .03f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationMuzzle(atlas: TextureAtlas): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite("Bullet/muzzle$i")
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .009f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    fun playSound(sonido: Sound, volume: Float) {
        if (isSoundOn) {
            sonido.play(volume)
        }
    }
}
