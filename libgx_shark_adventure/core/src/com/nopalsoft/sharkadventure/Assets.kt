package com.nopalsoft.sharkadventure

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.sharkadventure.parallax.ParallaxBackground
import com.nopalsoft.sharkadventure.parallax.ParallaxLayer

object Assets {
    var fontLarge: BitmapFont? = null
    var drawableTitle: TextureRegionDrawable? = null
    var drawableGameOver: TextureRegionDrawable? = null

    var animationSharkSwim: Animation<TextureRegion?>? = null
    var animationSharkMove: Animation<TextureRegion?>? = null
    var animationSharkFire: Animation<TextureRegion?>? = null
    var SharkDead: AtlasRegion? = null
    var TurboTail: AtlasRegion? = null
    var greenBarrel: AtlasRegion? = null
    var blackBarrel: AtlasRegion? = null
    var redBarrel: AtlasRegion? = null
    var yellowBarrel: AtlasRegion? = null

    var explosionAnimation: Animation<TextureRegion?>? = null

    var redSubmarine: AtlasRegion? = null
    var yellowSubmarine: AtlasRegion? = null
    var grayMine: AtlasRegion? = null
    var oxideMine: AtlasRegion? = null
    var chain: AtlasRegion? = null
    var blast: AtlasRegion? = null
    var torpedo: AtlasRegion? = null
    var heart: AtlasRegion? = null
    var meat: AtlasRegion? = null

    var animationBlastHit: Animation<TextureRegion?>? = null

    var redBar: AtlasRegion? = null
    var energyBar: AtlasRegion? = null
    var background: AtlasRegion? = null

    var parallaxBack: ParallaxBackground? = null
    var parallaxFront: ParallaxBackground? = null

    var particleEffectBubble: ParticleEffect? = null
    var particleEffectSharkBubble: ParticleEffect? = null
    var particleEffectTorpedoBubbleRightSide: ParticleEffect? = null
    var particleEffectTorpedoBubbleLeftSide: ParticleEffect? = null
    var particleEffectFish: ParticleEffect? = null
    var particleEffectMediumFish: ParticleEffect? = null

    var buttonRight: TextureRegionDrawable? = null
    var buttonRightPress: TextureRegionDrawable? = null
    var buttonLeft: TextureRegionDrawable? = null
    var buttonLeftPress: TextureRegionDrawable? = null
    var buttonUp: TextureRegionDrawable? = null
    var buttonUpPress: TextureRegionDrawable? = null
    var buttonFire: TextureRegionDrawable? = null
    var buttonFirePress: TextureRegionDrawable? = null
    var buttonHome: TextureRegionDrawable? = null
    var buttonHomePress: TextureRegionDrawable? = null
    var buttonPause: TextureRegionDrawable? = null
    var buttonPausePress: TextureRegionDrawable? = null
    var buttonLeaderboard: TextureRegionDrawable? = null
    var buttonLeaderboardPress: TextureRegionDrawable? = null
    var buttonFacebook: TextureRegionDrawable? = null
    var buttonFacebookPress: TextureRegionDrawable? = null
    var buttonMusicOn: TextureRegionDrawable? = null
    var buttonMusicOff: TextureRegionDrawable? = null
    var buttonSoundOn: TextureRegionDrawable? = null
    var buttonSoundOff: TextureRegionDrawable? = null
    var buttonRefresh: TextureRegionDrawable? = null
    var buttonRefreshPress: TextureRegionDrawable? = null
    var buttonAchievements: TextureRegionDrawable? = null
    var buttonAchievementsPress: TextureRegionDrawable? = null
    var buttonTwitter: TextureRegionDrawable? = null
    var buttonTwitterPress: TextureRegionDrawable? = null
    var backgroundProgressBar: TextureRegionDrawable? = null


    var backgroundMenu: NinePatchDrawable? = null
    var backgroundWindow: NinePatchDrawable? = null
    var backgroundTitle: NinePatchDrawable? = null
    var labelStyle: LabelStyle? = null
    var soundSwim: Sound? = null
    var soundSonar: Sound? = null
    var soundExplosion1: Sound? = null
    var soundExplosion2: Sound? = null
    var soundBlast: Sound? = null
    var music: Music? = null
    var atlas: TextureAtlas? = null

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontLarge = BitmapFont(Gdx.files.internal("data/FontGrande.fnt"), atlas!!.findRegion("FontGrande"))

        loadUI()

        SharkDead = atlas!!.findRegion("tiburonDead")

        val tiburon1 = atlas!!.findRegion("tiburon1")
        val tiburon2 = atlas!!.findRegion("tiburon2")
        val tiburon3 = atlas!!.findRegion("tiburon3")
        val tiburon4 = atlas!!.findRegion("tiburon4")
        val tiburon5 = atlas!!.findRegion("tiburon5")
        val tiburon6 = atlas!!.findRegion("tiburon6")
        val tiburon7 = atlas!!.findRegion("tiburon7")
        val tiburon8 = atlas!!.findRegion("tiburon8")

        animationSharkSwim = Animation<TextureRegion?>(.15f, tiburon1, tiburon2, tiburon3, tiburon4, tiburon5, tiburon6, tiburon7, tiburon8)
        animationSharkMove = Animation<TextureRegion?>(.04f, tiburon1, tiburon2, tiburon3, tiburon4, tiburon5, tiburon6, tiburon7, tiburon8)

        val tiburonFire1 = atlas!!.findRegion("tiburonFire1")
        val tiburonFire2 = atlas!!.findRegion("tiburonFire2")
        val tiburonFire3 = atlas!!.findRegion("tiburonFire3")
        val tiburonFire4 = atlas!!.findRegion("tiburonFire4")
        val tiburonFire5 = atlas!!.findRegion("tiburonFire5")

        animationSharkFire = Animation<TextureRegion?>(.075f, tiburonFire1, tiburonFire2, tiburonFire3, tiburonFire4, tiburonFire5)

        TurboTail = atlas!!.findRegion("turbo")

        greenBarrel = atlas!!.findRegion("barrilVerde")
        blackBarrel = atlas!!.findRegion("barrilNegro")
        redBarrel = atlas!!.findRegion("barrilRojo")
        yellowBarrel = atlas!!.findRegion("barrilAmarillo")

        val explosion1 = atlas!!.findRegion("explosion1")
        val explosion2 = atlas!!.findRegion("explosion2")
        val explosion3 = atlas!!.findRegion("explosion3")
        val explosion4 = atlas!!.findRegion("explosion4")
        val explosion5 = atlas!!.findRegion("explosion5")
        val explosion6 = atlas!!.findRegion("explosion6")
        val explosion7 = atlas!!.findRegion("explosion7")
        val explosion8 = atlas!!.findRegion("explosion8")

        explosionAnimation = Animation<TextureRegion?>(.1f, explosion1, explosion2, explosion3, explosion4, explosion5, explosion6, explosion7, explosion8)

        val blastHit1 = atlas!!.findRegion("blastHit1")
        val blastHit2 = atlas!!.findRegion("blastHit2")
        val blastHit3 = atlas!!.findRegion("blastHit3")
        val blastHit4 = atlas!!.findRegion("blastHit4")
        val blastHit5 = atlas!!.findRegion("blastHit5")
        val blastHit6 = atlas!!.findRegion("blastHit6")

        animationBlastHit = Animation<TextureRegion?>(.05f, blastHit1, blastHit2, blastHit3, blastHit4, blastHit5, blastHit6)

        yellowSubmarine = atlas!!.findRegion("submarinoAmarillo")
        redSubmarine = atlas!!.findRegion("submarinoRojo")

        grayMine = atlas!!.findRegion("minaGris")
        oxideMine = atlas!!.findRegion("minaOxido")
        chain = atlas!!.findRegion("chain")
        blast = atlas!!.findRegion("blast")
        torpedo = atlas!!.findRegion("torpedo")
        heart = atlas!!.findRegion("corazon")
        meat = atlas!!.findRegion("carne")

        reloadBackground()

        particleEffectBubble = ParticleEffect()
        particleEffectBubble!!.load(Gdx.files.internal("particulas/burbujas"), atlas)

        particleEffectSharkBubble = ParticleEffect()
        particleEffectSharkBubble!!.load(Gdx.files.internal("particulas/burbujasTiburon"), atlas)

        particleEffectTorpedoBubbleRightSide = ParticleEffect()
        particleEffectTorpedoBubbleRightSide!!.load(Gdx.files.internal("particulas/burbujasTorpedoRightSide"), atlas)

        particleEffectTorpedoBubbleLeftSide = ParticleEffect()
        particleEffectTorpedoBubbleLeftSide!!.load(Gdx.files.internal("particulas/burbujasTorpedoLeftSide"), atlas)

        particleEffectFish = ParticleEffect()
        particleEffectFish!!.load(Gdx.files.internal("particulas/peces"), atlas)

        particleEffectMediumFish = ParticleEffect()
        particleEffectMediumFish!!.load(Gdx.files.internal("particulas/pecesMediano"), atlas)

        soundSwim = Gdx.audio.newSound(Gdx.files.internal("sound/swim.mp3"))
        soundSonar = Gdx.audio.newSound(Gdx.files.internal("sound/sonar.mp3"))
        soundExplosion1 = Gdx.audio.newSound(Gdx.files.internal("sound/explosion1.mp3"))
        soundExplosion2 = Gdx.audio.newSound(Gdx.files.internal("sound/explosion2.mp3"))
        soundBlast = Gdx.audio.newSound(Gdx.files.internal("sound/blast1.mp3"))

        music = Gdx.audio.newMusic(Gdx.files.internal("sound/jungleHaze.mp3"))
        music?.isLooping = true

        if (Settings.isMusicOn) music?.play()
    }

    private fun loadUI() {
        drawableTitle = TextureRegionDrawable(atlas!!.findRegion("UI/titulo"))
        drawableGameOver = TextureRegionDrawable(atlas!!.findRegion("UI/gameOver2"))

        buttonRight = TextureRegionDrawable(atlas!!.findRegion("UI/btDer"))
        buttonRightPress = TextureRegionDrawable(atlas!!.findRegion("UI/btDerPress"))
        buttonLeft = TextureRegionDrawable(atlas!!.findRegion("UI/btIzq"))
        buttonLeftPress = TextureRegionDrawable(atlas!!.findRegion("UI/btIzqPress"))
        buttonUp = TextureRegionDrawable(atlas!!.findRegion("UI/btUp"))
        buttonUpPress = TextureRegionDrawable(atlas!!.findRegion("UI/btUpPress"))
        buttonFire = TextureRegionDrawable(atlas!!.findRegion("UI/btFire"))
        buttonFirePress = TextureRegionDrawable(atlas!!.findRegion("UI/btFirePress"))

        buttonRefresh = TextureRegionDrawable(atlas!!.findRegion("UI/btRefresh"))
        buttonRefreshPress = TextureRegionDrawable(atlas!!.findRegion("UI/btRefreshPress"))
        buttonHome = TextureRegionDrawable(atlas!!.findRegion("UI/btHome"))
        buttonHomePress = TextureRegionDrawable(atlas!!.findRegion("UI/btHomePress"))
        buttonPause = TextureRegionDrawable(atlas!!.findRegion("UI/btPausa"))
        buttonPausePress = TextureRegionDrawable(atlas!!.findRegion("UI/btPausaPress"))
        buttonLeaderboard = TextureRegionDrawable(atlas!!.findRegion("UI/btLeaderboard"))
        buttonLeaderboardPress = TextureRegionDrawable(atlas!!.findRegion("UI/btLeaderboardPress"))
        buttonAchievements = TextureRegionDrawable(atlas!!.findRegion("UI/btAchievements"))
        buttonAchievementsPress = TextureRegionDrawable(atlas!!.findRegion("UI/btAchievementsPress"))
        buttonFacebook = TextureRegionDrawable(atlas!!.findRegion("UI/btFacebook"))
        buttonFacebookPress = TextureRegionDrawable(atlas!!.findRegion("UI/btFacebookPress"))
        buttonTwitter = TextureRegionDrawable(atlas!!.findRegion("UI/btTwitter"))
        buttonTwitterPress = TextureRegionDrawable(atlas!!.findRegion("UI/btTwitterPress"))
        buttonSoundOn = TextureRegionDrawable(atlas!!.findRegion("UI/btSonido"))
        buttonSoundOff = TextureRegionDrawable(atlas!!.findRegion("UI/btSonidoOff"))
        buttonMusicOn = TextureRegionDrawable(atlas!!.findRegion("UI/btMusic"))
        buttonMusicOff = TextureRegionDrawable(atlas!!.findRegion("UI/btMusicOff"))

        redBar = atlas!!.findRegion("UI/redBar")
        energyBar = atlas!!.findRegion("UI/energyBar")

        backgroundProgressBar = TextureRegionDrawable(atlas!!.findRegion("UI/backgroundProgressBar"))
        backgroundMenu = NinePatchDrawable(NinePatch(atlas!!.findRegion("UI/backgroundMenu"), 70, 70, 60, 60))
        backgroundWindow = NinePatchDrawable(NinePatch(atlas!!.findRegion("UI/backgroundVentana"), 25, 25, 25, 25))
        backgroundTitle = NinePatchDrawable(NinePatch(atlas!!.findRegion("UI/backgroundTitulo"), 30, 30, 0, 0))

        labelStyle = LabelStyle(fontLarge, null)
    }

    fun reloadBackground() {
        val frontLayer: ParallaxLayer
        val backLayer: ParallaxLayer

        if (MathUtils.randomBoolean()) {
            background = atlas!!.findRegion("fondo")
            backLayer = ParallaxLayer(atlas!!.findRegion("sueloAtras"), Vector2(5f, 0f), Vector2(0f, -50f), Vector2(-1f, 480f), 1024f, 121f)
            frontLayer = ParallaxLayer(atlas!!.findRegion("suelo"), Vector2(15f, 0f), Vector2(0f, -50f), Vector2(-1f, 480f), 1024f, 121f)
        } else {
            background = atlas!!.findRegion("fondo2")
            backLayer = ParallaxLayer(atlas!!.findRegion("suelo2Atras"), Vector2(5f, 0f), Vector2(0f, -50f), Vector2(-1f, 480f), 1024f, 121f)
            frontLayer = ParallaxLayer(atlas!!.findRegion("suelo2"), Vector2(15f, 0f), Vector2(0f, -50f), Vector2(-1f, 480f), 1024f, 121f)
        }
        parallaxBack = ParallaxBackground(arrayOf(backLayer), 800f, 480f, Vector2(10f, 0f))
        parallaxFront = ParallaxBackground(arrayOf(frontLayer), 800f, 480f, Vector2(10f, 0f))
    }

    fun playExplosionSound() {
        val sound = MathUtils.random(1)
        val soundToBePlayed = if (sound == 0) soundExplosion1
        else soundExplosion2
        soundToBePlayed!!.play()
    }
}
