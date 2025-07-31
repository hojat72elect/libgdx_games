package com.nopalsoft.zombiekiller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array

object Assets {
    var fontChico: BitmapFont? = null
    var fontGrande: BitmapFont? = null

    var backBackground: AtlasRegion? = null
    var background: AtlasRegion? = null
    var moon: AtlasRegion? = null
    var zombieKillerTitulo: TextureRegionDrawable? = null
    var skullBarBackground: TextureRegionDrawable? = null

    var map: TiledMap? = null

    var weapon: AtlasRegion? = null
    var itemCollection: AtlasRegion? = null

    var itemGem: AtlasRegion? = null
    var itemHeart: AtlasRegion? = null
    var itemMeat: AtlasRegion? = null
    var itemSkull: AtlasRegion? = null
    var itemShield: AtlasRegion? = null
    var itemStar: AtlasRegion? = null
    var crate: AtlasRegion? = null
    var saw: AtlasRegion? = null

    var redBar: AtlasRegion? = null
    var whiteBar: AtlasRegion? = null

    /**
     * Hero
     */
    var heroForceClimb: AnimationSprite? = null
    var heroForceDie: AnimationSprite? = null
    var heroForceHurt: Sprite? = null
    var heroForceIdle: Sprite? = null
    var heroForceShoot: AnimationSprite? = null
    var heroForceWalk: AnimationSprite? = null

    var heroRamboClimb: AnimationSprite? = null
    var heroRamboDie: AnimationSprite? = null
    var heroRamboHurt: Sprite? = null
    var heroRamboIdle: Sprite? = null
    var heroRamboShoot: AnimationSprite? = null
    var heroRamboWalk: AnimationSprite? = null

    var heroSoldierClimb: AnimationSprite? = null
    var heroSoldierDie: AnimationSprite? = null
    var heroSoldierHurt: Sprite? = null
    var heroSoldierIdle: Sprite? = null
    var heroSoldierShoot: AnimationSprite? = null
    var heroSoldierWalk: AnimationSprite? = null

    var heroSwatClimb: AnimationSprite? = null
    var heroSwatDie: AnimationSprite? = null
    var heroSwatHurt: Sprite? = null
    var heroSwatIdle: Sprite? = null
    var heroSwatShoot: AnimationSprite? = null
    var heroSwatWalk: AnimationSprite? = null

    var heroVaderClimb: AnimationSprite? = null
    var heroVaderDie: AnimationSprite? = null
    var heroVaderHurt: Sprite? = null
    var heroVaderIdle: Sprite? = null
    var heroVaderShoot: AnimationSprite? = null
    var heroVaderWalk: AnimationSprite? = null

    /**
     * Bullet
     */
    var bullet1: AnimationSprite? = null
    var bullet2: AnimationSprite? = null
    var bullet3: AnimationSprite? = null
    var bullet4: AnimationSprite? = null
    var bullet5: AnimationSprite? = null
    var muzzle: AnimationSprite? = null

    /**
     * Zombies
     */
    var zombieKidWalk: AnimationSprite? = null
    var zombieKidIdle: AnimationSprite? = null
    var zombieKidRise: AnimationSprite? = null
    var zombieKidDie: AnimationSprite? = null
    var zombieKidHurt: Sprite? = null

    var zombiePanWalk: AnimationSprite? = null
    var zombiePanIdle: AnimationSprite? = null
    var zombiePanRise: AnimationSprite? = null
    var zombiePanDie: AnimationSprite? = null
    var zombiePanHurt: Sprite? = null

    var zombieCuasyWalk: AnimationSprite? = null
    var zombieCuasyIdle: AnimationSprite? = null
    var zombieCuasyRise: AnimationSprite? = null
    var zombieCuasyDie: AnimationSprite? = null
    var zombieCuasyHurt: Sprite? = null

    var zombieFrankWalk: AnimationSprite? = null
    var zombieFrankIdle: AnimationSprite? = null
    var zombieFrankRise: AnimationSprite? = null
    var zombieFrankDie: AnimationSprite? = null
    var zombieFrankHurt: Sprite? = null

    var zombieMummyWalk: AnimationSprite? = null
    var zombieMummyIdle: AnimationSprite? = null
    var zombieMummyRise: AnimationSprite? = null
    var zombieMummyDie: AnimationSprite? = null
    var zombieMummyHurt: Sprite? = null

    var containerButtons: TextureRegionDrawable? = null
    var btPlay: TextureRegionDrawable? = null
    var btLeaderboard: TextureRegionDrawable? = null
    var btAchievement: TextureRegionDrawable? = null
    var btSettings: TextureRegionDrawable? = null
    var btHelp: TextureRegionDrawable? = null
    var btFacebook: TextureRegionDrawable? = null
    var btTwitter: TextureRegionDrawable? = null
    var btShop: TextureRegionDrawable? = null
    var btClose: TextureRegionDrawable? = null
    var btMenu: TextureRegionDrawable? = null
    var btTryAgain: TextureRegionDrawable? = null
    var btPause: TextureRegionDrawable? = null
    var btPlayer: TextureRegionDrawable? = null
    var btFire: TextureRegionDrawable? = null

    var btUp: TextureRegionDrawable? = null
    var btUpPress: TextureRegionDrawable? = null
    var btRight: TextureRegionDrawable? = null
    var btRightPress: TextureRegionDrawable? = null
    var btLeft: TextureRegionDrawable? = null
    var btLeftPress: TextureRegionDrawable? = null
    var btDown: TextureRegionDrawable? = null
    var btDownPress: TextureRegionDrawable? = null

    var btGems: TextureRegionDrawable? = null
    var btMore: TextureRegionDrawable? = null
    var upgradeOff: TextureRegionDrawable? = null
    var backgroundProgressBar: TextureRegionDrawable? = null

    var storeTableBackground: NinePatchDrawable? = null
    var helpDialog: NinePatchDrawable? = null
    var helpDialogInverted: NinePatchDrawable? = null

    var backgroundBigWindow: TextureRegionDrawable? = null
    var backgroundSmallWindow: TextureRegionDrawable? = null

    var touchPadStyle: TouchpadStyle? = null
    var styleTextButtonBuy: TextButtonStyle? = null
    var styleTextButtontLevel: TextButtonStyle? = null
    var styleTextButtontLevelLocked: TextButtonStyle? = null
    var styleTextButtonPurchased: TextButtonStyle? = null
    var styleScrollPane: ScrollPaneStyle? = null

    var styleButtonMusic: ButtonStyle? = null
    var styleButtonSound: ButtonStyle? = null

    var labelStyleChico: LabelStyle? = null
    var labelStyleGrande: LabelStyle? = null
    var labelStyleHelpDialog: LabelStyle? = null
    var sliderStyle: SliderStyle? = null

    var pixelNegro: NinePatchDrawable? = null

    var shoot1: Sound? = null
    var zombiePan: Sound? = null
    var zombieKid: Sound? = null
    var zombieCuasy: Sound? = null
    var zombieMummy: Sound? = null
    var zombieFrank: Sound? = null

    var hurt1: Sound? = null
    var hurt2: Sound? = null
    var hurt3: Sound? = null
    var gem: Sound? = null
    var skull: Sound? = null
    var jump: Sound? = null
    var shield: Sound? = null
    var hearth: Sound? = null

    fun loadStyles(atlas: TextureAtlas) {
        // Label Style
        labelStyleChico = LabelStyle(fontChico, Color.WHITE)
        labelStyleGrande = LabelStyle(fontGrande, Color.WHITE)
        labelStyleHelpDialog = LabelStyle(fontChico, Color.BLACK)

        // Touch Pad
        val pad = TextureRegionDrawable(atlas.findRegion("pad"))
        val knob = TextureRegionDrawable(atlas.findRegion("knob"))
        touchPadStyle = TouchpadStyle(pad, knob)

        /* Button Buy */
        val btBuy = TextureRegionDrawable(atlas.findRegion("UI/btBuy"))
        styleTextButtonBuy = TextButtonStyle(btBuy, null, null, fontChico)

        /* Button Purchased */
        val btPurchased = TextureRegionDrawable(atlas.findRegion("UI/btPurchased"))
        styleTextButtonPurchased = TextButtonStyle(btPurchased, null, null, fontChico)
        /* Button level */
        val btLevel = TextureRegionDrawable(atlas.findRegion("UI/btLevel"))
        styleTextButtontLevel = TextButtonStyle(btLevel, null, null, fontGrande)

        /* Button level locked */
        val btLevelLocked = TextureRegionDrawable(atlas.findRegion("UI/btLevelLocked"))
        styleTextButtontLevelLocked = TextButtonStyle(btLevelLocked, null, null, fontGrande)

        /* Button Musica */
        val btMusicOn = TextureRegionDrawable(atlas.findRegion("UI/btMusic"))
        val btMusicOff = TextureRegionDrawable(atlas.findRegion("UI/btMusicOff"))
        styleButtonMusic = ButtonStyle(btMusicOn, null, btMusicOff)

        /* Boton Sonido */
        val botonSonidoOn = TextureRegionDrawable(atlas.findRegion("UI/btSound"))
        val botonSonidoOff = TextureRegionDrawable(atlas.findRegion("UI/btSoundOff"))
        styleButtonSound = ButtonStyle(botonSonidoOn, null, botonSonidoOff)

        // Scrollpane
        val separadorVertical = TextureRegionDrawable(atlas.findRegion("UI/separadorVertical"))
        val knobScroll = TextureRegionDrawable(atlas.findRegion("knob"))
        styleScrollPane = ScrollPaneStyle(null, null, null, separadorVertical, knobScroll)

        val separadorHorizontal = TextureRegionDrawable(atlas.findRegion("UI/separadorHorizontal"))
        sliderStyle = SliderStyle(separadorHorizontal, knobScroll)

        pixelNegro = NinePatchDrawable(NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0))
    }

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontChico = BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"))
        fontGrande = BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"))

        loadStyles(atlas)

        background = atlas.findRegion("background")
        moon = atlas.findRegion("moon")

        backBackground = atlas.findRegion("backBackground")
        backgroundBigWindow = TextureRegionDrawable(atlas.findRegion("UI/ventanaGrande"))
        backgroundSmallWindow = TextureRegionDrawable(atlas.findRegion("UI/ventanaChica"))

        zombieKillerTitulo = TextureRegionDrawable(atlas.findRegion("UI/titulo"))
        skullBarBackground = TextureRegionDrawable(atlas.findRegion("skullBar"))
        storeTableBackground = NinePatchDrawable(NinePatch(atlas.findRegion("UI/storeTableBackground"), 50, 40, 50, 40))
        helpDialog = NinePatchDrawable(NinePatch(atlas.findRegion("UI/helpDialog"), 56, 17, 15, 50))
        helpDialogInverted = NinePatchDrawable(NinePatch(atlas.findRegion("UI/helpDialogInverted"), 56, 17, 50, 15))

        containerButtons = TextureRegionDrawable(atlas.findRegion("UI/containerButtons"))
        btPlay = TextureRegionDrawable(atlas.findRegion("UI/btPlay"))
        btLeaderboard = TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"))
        btAchievement = TextureRegionDrawable(atlas.findRegion("UI/btAchievement"))
        btSettings = TextureRegionDrawable(atlas.findRegion("UI/btSettings"))

        btHelp = TextureRegionDrawable(atlas.findRegion("UI/btHelp"))
        btFacebook = TextureRegionDrawable(atlas.findRegion("UI/btFacebook"))
        btTwitter = TextureRegionDrawable(atlas.findRegion("UI/btTwitter"))
        btShop = TextureRegionDrawable(atlas.findRegion("UI/btShop"))
        btClose = TextureRegionDrawable(atlas.findRegion("UI/btClose"))
        btMenu = TextureRegionDrawable(atlas.findRegion("UI/btMenu"))
        btTryAgain = TextureRegionDrawable(atlas.findRegion("UI/btTryAgain"))
        btPause = TextureRegionDrawable(atlas.findRegion("btPause"))
        btPlayer = TextureRegionDrawable(atlas.findRegion("UI/btPlayer"))
        btGems = TextureRegionDrawable(atlas.findRegion("UI/btGems"))
        btFire = TextureRegionDrawable(atlas.findRegion("btFire"))
        btMore = TextureRegionDrawable(atlas.findRegion("UI/btMore"))
        upgradeOff = TextureRegionDrawable(atlas.findRegion("UI/upgradeOff"))
        btUp = TextureRegionDrawable(atlas.findRegion("UI/btUp"))
        btUpPress = TextureRegionDrawable(atlas.findRegion("UI/btUpPress"))
        btRight = TextureRegionDrawable(atlas.findRegion("UI/btRight"))
        btRightPress = TextureRegionDrawable(atlas.findRegion("UI/btRightPress"))
        btLeft = TextureRegionDrawable(atlas.findRegion("UI/btLeft"))
        btLeftPress = TextureRegionDrawable(atlas.findRegion("UI/btLeftPress"))
        btDown = TextureRegionDrawable(atlas.findRegion("UI/btDown"))
        btDownPress = TextureRegionDrawable(atlas.findRegion("UI/btDownPress"))

        weapon = atlas.findRegion("UI/weapon")
        itemCollection = atlas.findRegion("UI/itemCollection")
        backgroundProgressBar = TextureRegionDrawable(atlas.findRegion("backgroundProgressBar"))

        /*
         * Bullets
         */
        bullet1 = loadAnimationBullet(atlas, "Bullet/bullet1")
        bullet2 = loadAnimationBullet(atlas, "Bullet/bullet2")
        bullet3 = loadAnimationBullet(atlas, "Bullet/bullet3")
        bullet4 = loadAnimationBullet(atlas, "Bullet/bullet4")
        bullet5 = loadAnimationBullet(atlas, "Bullet/bullet5")
        muzzle = loadAnimationMuzzle(atlas)

        /*
         * Items
         */
        itemGem = atlas.findRegion("gem")
        itemHeart = atlas.findRegion("heart")
        itemMeat = atlas.findRegion("meat")
        itemSkull = atlas.findRegion("skull")
        itemShield = atlas.findRegion("shield")
        itemStar = atlas.findRegion("star")

        crate = atlas.findRegion("crate")
        saw = atlas.findRegion("saw")

        redBar = atlas.findRegion("redBar")
        whiteBar = atlas.findRegion("whiteBar")

        /*
         * HeroForce
         */
        heroForceClimb = loadAnimationClimb(atlas, "HeroForce/")
        heroForceDie = loadAnimationDie(atlas, "HeroForce/")
        heroForceHurt = atlas.createSprite("HeroForce/hurt")
        heroForceIdle = atlas.createSprite("HeroForce/idle")
        heroForceShoot = loadAnimationShoot(atlas, "HeroForce/")
        heroForceWalk = loadAnimationWalk(atlas, "HeroForce/")

        /*
         * HeroRambo
         */
        heroRamboClimb = loadAnimationClimb(atlas, "HeroRambo/")
        heroRamboDie = loadAnimationDie(atlas, "HeroRambo/")
        heroRamboHurt = atlas.createSprite("HeroRambo/hurt")
        heroRamboIdle = atlas.createSprite("HeroRambo/idle")
        heroRamboShoot = loadAnimationShoot(atlas, "HeroRambo/")
        heroRamboWalk = loadAnimationWalk(atlas, "HeroRambo/")

        /*
         * HeroSoldier
         */
        heroSoldierClimb = loadAnimationClimb(atlas, "HeroSoldier/")
        heroSoldierDie = loadAnimationDie(atlas, "HeroSoldier/")
        heroSoldierHurt = atlas.createSprite("HeroSoldier/hurt")
        heroSoldierIdle = atlas.createSprite("HeroSoldier/idle")
        heroSoldierShoot = loadAnimationShoot(atlas, "HeroSoldier/")
        heroSoldierWalk = loadAnimationWalk(atlas, "HeroSoldier/")

        /*
         * HeroSwat
         */
        heroSwatClimb = loadAnimationClimb(atlas, "HeroSwat/")
        heroSwatDie = loadAnimationDie(atlas, "HeroSwat/")
        heroSwatHurt = atlas.createSprite("HeroSwat/hurt")
        heroSwatIdle = atlas.createSprite("HeroSwat/idle")
        heroSwatShoot = loadAnimationShoot(atlas, "HeroSwat/")
        heroSwatWalk = loadAnimationWalk(atlas, "HeroSwat/")

        /*
         * HeroVader
         */
        heroVaderClimb = loadAnimationClimb(atlas, "HeroVader/")
        heroVaderDie = loadAnimationDie(atlas, "HeroVader/")
        heroVaderHurt = atlas.createSprite("HeroVader/hurt")
        heroVaderIdle = atlas.createSprite("HeroVader/idle")
        heroVaderShoot = loadAnimationShoot(atlas, "HeroVader/")
        heroVaderWalk = loadAnimationWalk(atlas, "HeroVader/")

        /*
         * Zombie kid
         */
        zombieKidWalk = loadAnimationWalk(atlas, "ZombieKid/")
        zombieKidIdle = loadAnimationIdle(atlas, "ZombieKid/")
        zombieKidRise = loadAnimationRise(atlas, "ZombieKid/")
        zombieKidDie = loadAnimationDie(atlas, "ZombieKid/")
        zombieKidHurt = atlas.createSprite("ZombieKid/die1")

        /*
         * Zombie pan
         */
        zombiePanWalk = loadAnimationWalk(atlas, "ZombiePan/")
        zombiePanIdle = loadAnimationIdle(atlas, "ZombiePan/")
        zombiePanRise = loadAnimationRise(atlas, "ZombiePan/")
        zombiePanDie = loadAnimationDie(atlas, "ZombiePan/")
        zombiePanHurt = atlas.createSprite("ZombiePan/die1")

        /*
         * Zombie Cuasy
         */
        zombieCuasyWalk = loadAnimationWalk(atlas, "ZombieCuasy/")
        zombieCuasyIdle = loadAnimationIdle(atlas, "ZombieCuasy/")
        zombieCuasyRise = loadAnimationRise(atlas, "ZombieCuasy/")
        zombieCuasyDie = loadAnimationDie(atlas, "ZombieCuasy/")
        zombieCuasyHurt = atlas.createSprite("ZombieCuasy/die1")

        /*
         * Zombie Frank
         */
        zombieFrankWalk = loadAnimationWalk(atlas, "ZombieFrank/")
        zombieFrankIdle = loadAnimationIdle(atlas, "ZombieFrank/")
        zombieFrankRise = loadAnimationRise(atlas, "ZombieFrank/")
        zombieFrankDie = loadAnimationDie(atlas, "ZombieFrank/")
        zombieFrankHurt = atlas.createSprite("ZombieFrank/die1")

        /*
         * Zombie mummy
         */
        zombieMummyWalk = loadAnimationWalk(atlas, "ZombieMummy/")
        zombieMummyIdle = loadAnimationIdle(atlas, "ZombieMummy/")
        zombieMummyRise = loadAnimationRise(atlas, "ZombieMummy/")
        zombieMummyDie = loadAnimationDie(atlas, "ZombieMummy/")
        zombieMummyHurt = atlas.createSprite("ZombieMummy/die1")

        Settings.load()

        shoot1 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/shoot2.mp3"))
        zombiePan = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombiePan.mp3"))
        zombieKid = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombieKid.mp3"))
        zombieCuasy = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombieCuasy.mp3"))
        zombieMummy = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombieMummy.mp3"))
        zombieFrank = Gdx.audio.newSound(Gdx.files.internal("data/sounds/zombieFrank.mp3"))

        hurt1 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hurt.mp3"))
        hurt2 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hurt2.mp3"))
        hurt3 = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hurt3.mp3"))

        gem = Gdx.audio.newSound(Gdx.files.internal("data/sounds/gem.mp3"))
        skull = Gdx.audio.newSound(Gdx.files.internal("data/sounds/skull.mp3"))
        jump = Gdx.audio.newSound(Gdx.files.internal("data/sounds/jump.mp3"))
        shield = Gdx.audio.newSound(Gdx.files.internal("data/sounds/pick.mp3"))
        hearth = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hearth.mp3"))
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

    private fun loadAnimationIdle(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "idle" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .05f * arrSprites.size
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

        val time = .01875f * arrSprites.size
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

    private fun loadAnimationClimb(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "climb" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .03f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationShoot(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "shoot" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .0095f * arrSprites.size
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

    fun loadTiledMap(numMap: Int) {
        Gdx.app.log("Inside TEst", "TEST")
        Gdx.app.debug("Inside TEst", "TEST")
        Gdx.app.log("Inside NORMAL", "NORMAL")
        Gdx.app.debug("Inside NORMAL", "NORMAL")
        if (map != null) {
            map!!.dispose()
            map = null
        }

        if (Settings.IS_TEST) {
            Gdx.app.log("Inside TEst", "TEST")
            Gdx.app.debug("Inside TEst", "TEST")
            map = TmxMapLoader().load("data/mapsTest/map$numMap.tmx")
        } else {
            Gdx.app.log("Inside NORMAL", "NORMAL")
            Gdx.app.debug("Inside NORMAL", "NORMAL")
            map = AtlasTmxMapLoader().load("data/maps/map$numMap.tmx")
        }
    }

    fun playSound(sonido: Sound, volume: Float) {
        if (Settings.isSoundOn) {
            sonido.play(volume)
        }
    }
}
