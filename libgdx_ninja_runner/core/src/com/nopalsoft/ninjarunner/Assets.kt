package com.nopalsoft.ninjarunner

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.ninjarunner.parallax.ParallaxBackground
import com.nopalsoft.ninjarunner.parallax.ParallaxLayer

object Assets {
    @JvmField
    var smallFont: BitmapFont? = null
    var largeFont: BitmapFont? = null

    // All the animations that are related to the girl character
    @JvmField
    var girlRunAnimation: AnimationSprite? = null

    @JvmField
    var girlDashAnimation: AnimationSprite? = null

    @JvmField
    var girlIdleAnimation: AnimationSprite? = null

    @JvmField
    var girlDeathAnimation: AnimationSprite? = null

    @JvmField
    var girlJumpAnimation: AnimationSprite? = null

    @JvmField
    var girlHurtAnimation: AnimationSprite? = null

    @JvmField
    var girlDizzyAnimation: AnimationSprite? = null

    @JvmField
    var girlSlideAnimation: AnimationSprite? = null

    // All the animations that are related to the ninja character
    @JvmField
    var ninjaRunAnimation: AnimationSprite? = null

    @JvmField
    var ninjaDashAnimation: AnimationSprite? = null

    @JvmField
    var ninjaIdleAnimation: AnimationSprite? = null

    @JvmField
    var ninjaDeathAnimation: AnimationSprite? = null

    @JvmField
    var ninjaJumpAnimation: AnimationSprite? = null

    @JvmField
    var ninjaHurtAnimation: AnimationSprite? = null

    @JvmField
    var ninjaDizzyAnimation: AnimationSprite? = null

    @JvmField
    var ninjaSlideAnimation: AnimationSprite? = null

    @JvmField
    var mascotBirdFlyAnimation: AnimationSprite? = null

    @JvmField
    var mascotBirdDashAnimation: AnimationSprite? = null

    @JvmField
    var mascotBombFlyAnimation: AnimationSprite? = null

    @JvmField
    var coinAnimation: AnimationSprite? = null

    @JvmField
    var pickUpAnimation: AnimationSprite? = null

    @JvmField
    var candyExplosionRed: AnimationSprite? = null

    @JvmField
    var magnet: Sprite? = null

    @JvmField
    var energy: Sprite? = null

    @JvmField
    var hearth: Sprite? = null

    @JvmField
    var jellyRed: Sprite? = null

    @JvmField
    var beanRed: Sprite? = null

    @JvmField
    var candyCorn: Sprite? = null

    @JvmField
    var platform: Sprite? = null

    @JvmField
    var wall: Sprite? = null

    // Obstacles
    @JvmField
    var boxes4Sprite: Sprite? = null

    @JvmField
    var boxes7Sprite: Sprite? = null

    @JvmField
    var missileAnimation: AnimationSprite? = null

    @JvmField
    var explosionAnimation: AnimationSprite? = null

    @JvmField
    var cloudsParallaxBackground: ParallaxBackground? = null

    @JvmField
    var music1: Music? = null

    @JvmField
    var blackPixelDrawable: NinePatchDrawable? = null

    @JvmField
    var jumpSound: Sound? = null

    @JvmField
    var coinSound: Sound? = null

    @JvmField
    var candySound: Sound? = null

    var boxesEffectPool: ParticleEffectPool? = null

    // UI STUFF
    @JvmField
    var titleDrawable: TextureRegionDrawable? = null

    @JvmField
    var backgroundMenu: NinePatchDrawable? = null

    @JvmField
    var backgroundShop: NinePatchDrawable? = null

    @JvmField
    var backgroundTitleShop: NinePatchDrawable? = null

    @JvmField
    var backgroundItemShop: NinePatchDrawable? = null

    @JvmField
    var backgroundUpgradeBar: NinePatchDrawable? = null

    @JvmField
    var buttonShop: TextureRegionDrawable? = null

    @JvmField
    var buttonShopPress: TextureRegionDrawable? = null

    @JvmField
    var buttonLeaderboard: TextureRegionDrawable? = null

    @JvmField
    var buttonLeaderBoardPress: TextureRegionDrawable? = null

    @JvmField
    var buttonAchievement: TextureRegionDrawable? = null

    @JvmField
    var buttonAchievementPress: TextureRegionDrawable? = null

    @JvmField
    var buttonSettings: TextureRegionDrawable? = null

    @JvmField
    var buttonSettingsPress: TextureRegionDrawable? = null

    @JvmField
    var buttonRate: TextureRegionDrawable? = null

    @JvmField
    var buttonRatePress: TextureRegionDrawable? = null

    @JvmField
    var buttonShare: TextureRegionDrawable? = null

    @JvmField
    var buttonSharePress: TextureRegionDrawable? = null
    var buttonUpgrade: TextureRegionDrawable? = null
    var buttonUpgradePress: TextureRegionDrawable? = null

    @JvmField
    var buttonFacebook: TextureRegionDrawable? = null

    @JvmField
    var buttonFacebookPress: TextureRegionDrawable? = null

    @JvmField
    var photoFrame: TextureRegionDrawable? = null

    @JvmField
    var photoNA: TextureRegionDrawable? = null

    @JvmField
    var imageGoogle: TextureRegionDrawable? = null

    @JvmField
    var imageAmazon: TextureRegionDrawable? = null

    @JvmField
    var imageFacebook: TextureRegionDrawable? = null

    @JvmField
    var labelStyleSmall: LabelStyle? = null

    @JvmField
    var labelStyleLarge: LabelStyle? = null

    @JvmField
    var styleTextButtonPurchased: TextButtonStyle? = null

    @JvmField
    var styleTextButtonBuy: TextButtonStyle? = null

    @JvmField
    var styleButtonUpgrade: ButtonStyle? = null

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        largeFont = BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("Font/fontGrande"))
        smallFont = BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("Font/fontChico"))
        smallFont!!.setUseIntegerPositions(false)


        loadUI(atlas)
        loadShanti(atlas)
        loadNinja(atlas)

        var fly1 = atlas.createSprite("Mascota1/fly1")
        var fly2 = atlas.createSprite("Mascota1/fly2")
        var fly3 = atlas.createSprite("Mascota1/fly3")
        var fly4 = atlas.createSprite("Mascota1/fly4")
        var fly5 = atlas.createSprite("Mascota1/fly5")
        var fly6 = atlas.createSprite("Mascota1/fly6")
        var fly7 = atlas.createSprite("Mascota1/fly7")
        var fly8 = atlas.createSprite("Mascota1/fly8")
        mascotBirdFlyAnimation = AnimationSprite(.075f, fly1, fly2, fly3, fly4, fly5, fly6, fly7, fly8)

        fly1 = atlas.createSprite("Mascota2/fly1")
        fly2 = atlas.createSprite("Mascota2/fly2")
        fly3 = atlas.createSprite("Mascota2/fly3")
        fly4 = atlas.createSprite("Mascota2/fly4")
        fly5 = atlas.createSprite("Mascota2/fly5")
        fly6 = atlas.createSprite("Mascota2/fly6")
        fly7 = atlas.createSprite("Mascota2/fly7")
        fly8 = atlas.createSprite("Mascota2/fly8")
        mascotBombFlyAnimation = AnimationSprite(.075f, fly1, fly2, fly3, fly4, fly5, fly6, fly7, fly8)

        val dash1 = atlas.createSprite("Mascota1/dash1")
        val dash2 = atlas.createSprite("Mascota1/dash2")
        val dash3 = atlas.createSprite("Mascota1/dash3")
        val dash4 = atlas.createSprite("Mascota1/dash4")
        mascotBirdDashAnimation = AnimationSprite(.04f, dash1, dash2, dash3, dash4)

        val coin1 = atlas.createSprite("moneda1")
        val coin2 = atlas.createSprite("moneda2")
        val coin3 = atlas.createSprite("moneda3")
        val coin4 = atlas.createSprite("moneda4")
        val coin5 = atlas.createSprite("moneda5")
        val coin6 = atlas.createSprite("moneda6")
        val coin7 = atlas.createSprite("moneda7")
        val coin8 = atlas.createSprite("moneda8")
        coinAnimation = AnimationSprite(.075f, coin1, coin2, coin3, coin4, coin5, coin6, coin7, coin8)

        val pick1 = atlas.createSprite("pick1")
        val pick2 = atlas.createSprite("pick2")
        val pick3 = atlas.createSprite("pick3")
        pickUpAnimation = AnimationSprite(.1f, pick1, pick2, pick3)

        val missile1 = atlas.createSprite("misil1")
        val missile2 = atlas.createSprite("misil2")
        val missile3 = atlas.createSprite("misil3")
        val missile4 = atlas.createSprite("misil4")
        missileAnimation = AnimationSprite(.05f, missile1, missile2, missile3, missile4)

        var explosion1 = atlas.createSprite("explosion1")
        var explosion2 = atlas.createSprite("explosion2")
        var explosion3 = atlas.createSprite("explosion3")
        var explosion4 = atlas.createSprite("explosion4")
        var explosion5 = atlas.createSprite("explosion5")
        explosionAnimation = AnimationSprite(.05f, explosion1, explosion2, explosion3, explosion4, explosion5)

        platform = atlas.createSprite("plataforma")
        wall = atlas.createSprite("pared")
        boxes4Sprite = atlas.createSprite("cajas4")
        boxes7Sprite = atlas.createSprite("cajas7")
        magnet = atlas.createSprite("magnet")
        energy = atlas.createSprite("energy")
        hearth = atlas.createSprite("hearth")

        // Candies
        explosion1 = atlas.createSprite("Candy/explosionred01")
        explosion2 = atlas.createSprite("Candy/explosionred02")
        explosion3 = atlas.createSprite("Candy/explosionred03")
        explosion4 = atlas.createSprite("Candy/explosionred04")
        explosion5 = atlas.createSprite("Candy/explosionred05")
        candyExplosionRed = AnimationSprite(.05f, explosion1, explosion2, explosion3, explosion4, explosion5)

        jellyRed = atlas.createSprite("Candy/jelly_red")
        beanRed = atlas.createSprite("Candy/bean_red")
        candyCorn = atlas.createSprite("Candy/candycorn")

        // Particles
        val boxesParticleEffect = ParticleEffect()
        boxesParticleEffect.load(Gdx.files.internal("data/Particulas/Cajas"), atlas)
        boxesEffectPool = ParticleEffectPool(boxesParticleEffect, 1, 10)

        // The smaller the number, the further back
        val sun = ParallaxLayer(
            atlas.findRegion("Background/sol"), Vector2(.5f, 0f), Vector2(600f, 300f), Vector2(800f, 800f),
            170f, 170f
        )

        val clouds1 = ParallaxLayer(
            atlas.findRegion("Background/nubesLayer1"), Vector2(1f, 0f), Vector2(0f, 50f), Vector2(
                690f,
                500f
            ), 557f, 159f
        )
        val clouds2 = ParallaxLayer(
            atlas.findRegion("Background/nubesLayer2"), Vector2(3f, 0f), Vector2(0f, 50f), Vector2(
                -1f,
                500f
            ), 1250f, 198f
        )
        val clouds3 = ParallaxLayer(
            atlas.findRegion("Background/nubesLayer3"), Vector2(5f, 0f), Vector2(0f, 50f), Vector2(
                -1f,
                100f
            ), 1250f, 397f
        )

        val trees1 = ParallaxLayer(
            atlas.findRegion("Background/arbolesLayer1"), Vector2(7f, 0f), Vector2(0f, 50f), Vector2(
                -1f, 500f
            ), 1048f, 159f
        )
        val trees2 = ParallaxLayer(
            atlas.findRegion("Background/arbolesLayer2"), Vector2(8f, 0f), Vector2(0f, 50f), Vector2(
                1008f, 500f
            ), 554f, 242f
        )

        val blueGround = ParallaxLayer(
            atlas.findRegion("Background/sueloAzul"), Vector2(0f, 0f), Vector2(0f, -1f), Vector2(
                -1f,
                500f
            ), 800f, 50f
        )

        cloudsParallaxBackground = ParallaxBackground(
            arrayOf<ParallaxLayer>(sun, clouds1, clouds2, clouds3, trees1, trees2, blueGround), 800f, 480f,
            Vector2(20f, 0f)
        )

        jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/salto.mp3"))
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/pickCoin.mp3"))
        candySound = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/popBubble.mp3"))

        music1 = Gdx.audio.newMusic(Gdx.files.internal("data/Sonidos/Happy.mp3"))
        music1!!.isLooping = true
    }

    private fun loadShanti(atlas: TextureAtlas) {
        val dash1 = atlas.createSprite("dash1")
        val dash2 = atlas.createSprite("dash2")
        val dash3 = atlas.createSprite("dash3")
        girlDashAnimation = AnimationSprite(.085f, dash1, dash2, dash3)

        val idle1 = atlas.createSprite("idle1")
        val idle2 = atlas.createSprite("idle2")
        val idle3 = atlas.createSprite("idle3")
        val idle4 = atlas.createSprite("idle4")
        girlIdleAnimation = AnimationSprite(.25f, idle1, idle2, idle3, idle4)

        val dead1 = atlas.createSprite("dead1")
        val dead2 = atlas.createSprite("dead2")
        val dead3 = atlas.createSprite("dead3")
        val dead4 = atlas.createSprite("dead4")
        val dead5 = atlas.createSprite("dead5")
        girlDeathAnimation = AnimationSprite(.085f, dead1, dead2, dead3, dead4, dead5)

        val hurt1 = atlas.createSprite("hurt1")
        val hurt2 = atlas.createSprite("hurt2")
        girlHurtAnimation = AnimationSprite(.085f, hurt1, hurt2)

        val dizzy1 = atlas.createSprite("dizzy1")
        val dizzy2 = atlas.createSprite("dizzy2")
        val dizzy3 = atlas.createSprite("dizzy3")
        girlDizzyAnimation = AnimationSprite(.18f, dizzy1, dizzy2, dizzy3)

        val jump1 = atlas.createSprite("jump1")
        val jump2 = atlas.createSprite("jump2")
        val jump3 = atlas.createSprite("jump3")
        val jump4 = atlas.createSprite("jump4")
        val jump5 = atlas.createSprite("jump5")
        val jump6 = atlas.createSprite("jump6")
        girlJumpAnimation = AnimationSprite(.18f, jump1, jump2, jump3, jump4, jump5, jump6)

        val run1 = atlas.createSprite("run1")
        val run2 = atlas.createSprite("run2")
        val run3 = atlas.createSprite("run3")
        val run4 = atlas.createSprite("run4")
        val run5 = atlas.createSprite("run5")
        val run6 = atlas.createSprite("run6")
        girlRunAnimation = AnimationSprite(.1f, run1, run2, run3, run4, run5, run6)

        val slide1 = atlas.createSprite("slide1")
        val slide2 = atlas.createSprite("slide2")
        val slide3 = atlas.createSprite("slide3")
        girlSlideAnimation = AnimationSprite(.1f, slide1, slide2, slide3)
    }

    private fun loadNinja(atlas: TextureAtlas) {
        val run1 = atlas.createSprite("Ninja/run1")
        val run2 = atlas.createSprite("Ninja/run2")
        val run3 = atlas.createSprite("Ninja/run3")
        val run4 = atlas.createSprite("Ninja/run4")
        val run5 = atlas.createSprite("Ninja/run5")
        val run6 = atlas.createSprite("Ninja/run6")
        ninjaRunAnimation = AnimationSprite(.1f, run1, run2, run3, run4, run5, run6)
        ninjaDashAnimation = AnimationSprite(.05f, run1, run2, run3, run4, run5, run6)

        val jump1 = atlas.createSprite("Ninja/jump1")
        val jump2 = atlas.createSprite("Ninja/jump2")
        val jump3 = atlas.createSprite("Ninja/jump3")
        val jump4 = atlas.createSprite("Ninja/jump4")
        val jump5 = atlas.createSprite("Ninja/jump5")
        val jump6 = atlas.createSprite("Ninja/jump6")
        val jump7 = atlas.createSprite("Ninja/jump7")
        val jump8 = atlas.createSprite("Ninja/jump8")
        ninjaJumpAnimation = AnimationSprite(.075f, jump1, jump2, jump3, jump4, jump5, jump6, jump7, jump8)

        val slide1 = atlas.createSprite("Ninja/slide1")
        val slide2 = atlas.createSprite("Ninja/slide2")
        val slide3 = atlas.createSprite("Ninja/slide3")
        ninjaSlideAnimation = AnimationSprite(.1f, slide1, slide2, slide3)

        val idle1 = atlas.createSprite("Ninja/idle1")
        val idle2 = atlas.createSprite("Ninja/idle2")
        val idle3 = atlas.createSprite("Ninja/idle3")
        val idle4 = atlas.createSprite("Ninja/idle4")
        ninjaIdleAnimation = AnimationSprite(.25f, idle1, idle2, idle3, idle4)

        val dead1 = atlas.createSprite("Ninja/dead1")
        val dead2 = atlas.createSprite("Ninja/dead2")
        val dead3 = atlas.createSprite("Ninja/dead3")
        val dead4 = atlas.createSprite("Ninja/dead4")
        val dead5 = atlas.createSprite("Ninja/dead5")
        ninjaDeathAnimation = AnimationSprite(.085f, dead1, dead2, dead3, dead4, dead5)

        val hurt1 = atlas.createSprite("Ninja/hurt1")
        val hurt2 = atlas.createSprite("Ninja/hurt2")
        ninjaHurtAnimation = AnimationSprite(.085f, hurt1, hurt2)

        val dizzy1 = atlas.createSprite("Ninja/dizzy1")
        val dizzy2 = atlas.createSprite("Ninja/dizzy2")
        val dizzy3 = atlas.createSprite("Ninja/dizzy3")
        ninjaDizzyAnimation = AnimationSprite(.18f, dizzy1, dizzy2, dizzy3)
    }

    private fun loadUI(atlas: TextureAtlas) {
        titleDrawable = TextureRegionDrawable(atlas.findRegion("UI/titulo"))

        blackPixelDrawable = NinePatchDrawable(NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0))

        backgroundMenu = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundMenu"), 40, 40, 40, 40))
        backgroundShop = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundShop"), 140, 40, 40, 40))
        backgroundTitleShop = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundTitleShop"), 40, 40, 40, 30))
        backgroundItemShop = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundItemShop"), 50, 50, 25, 15))
        backgroundUpgradeBar = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundUpgradeBar"), 15, 15, 9, 10))

        buttonShop = TextureRegionDrawable(atlas.findRegion("UI/btShop"))
        buttonShopPress = TextureRegionDrawable(atlas.findRegion("UI/btShopPress"))
        buttonLeaderboard = TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"))
        buttonLeaderBoardPress = TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"))
        buttonAchievement = TextureRegionDrawable(atlas.findRegion("UI/btAchievement"))
        buttonAchievementPress = TextureRegionDrawable(atlas.findRegion("UI/btAchievementPress"))
        buttonSettings = TextureRegionDrawable(atlas.findRegion("UI/btSettings"))
        buttonSettingsPress = TextureRegionDrawable(atlas.findRegion("UI/btSettingsPress"))
        buttonRate = TextureRegionDrawable(atlas.findRegion("UI/btFacebook"))
        buttonRatePress = TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"))
        buttonFacebook = TextureRegionDrawable(atlas.findRegion("UI/btFacebook"))
        buttonFacebookPress = TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"))
        buttonShare = TextureRegionDrawable(atlas.findRegion("UI/btShare"))
        buttonSharePress = TextureRegionDrawable(atlas.findRegion("UI/btSharePress"))
        buttonUpgrade = TextureRegionDrawable(atlas.findRegion("UI/btUpgrade"))
        buttonUpgradePress = TextureRegionDrawable(atlas.findRegion("UI/btUpgradePress"))
        photoFrame = TextureRegionDrawable(atlas.findRegion("UI/photoFrame"))
        photoNA = TextureRegionDrawable(atlas.findRegion("UI/fotoNA"))

        imageAmazon = TextureRegionDrawable(atlas.findRegion("UI/imAmazon"))
        imageGoogle = TextureRegionDrawable(atlas.findRegion("UI/imGoogle"))
        imageFacebook = TextureRegionDrawable(atlas.findRegion("UI/imFacebook"))

        labelStyleSmall = LabelStyle(smallFont, Color.WHITE)
        labelStyleLarge = LabelStyle(largeFont, Color.WHITE)

        val txtButton = TextureRegionDrawable(atlas.findRegion("UI/txtButton"))
        val txtButtonDisabled = TextureRegionDrawable(atlas.findRegion("UI/txtButtonDisabled"))
        val txtButtonPress = TextureRegionDrawable(atlas.findRegion("UI/txtButtonPress"))

        styleTextButtonPurchased = TextButtonStyle(txtButton, txtButtonPress, null, smallFont)
        styleTextButtonBuy = TextButtonStyle(txtButtonDisabled, txtButtonPress, null, smallFont)
        styleButtonUpgrade = ButtonStyle(buttonUpgrade, buttonUpgradePress, null)
    }

    @JvmStatic
    fun playSound(sound: Sound, volume: Int) {
        if (Settings.isSoundEnabled) {
            sound.play(volume.toFloat())
        }
    }
}
