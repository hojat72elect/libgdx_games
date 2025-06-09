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
    var titleDrawable: TextureRegionDrawable? = null
    var gameOverDrawable: TextureRegionDrawable? = null

    var sharkSwimAnimation: Animation<TextureRegion?>? = null
    var sharkDashAnimation: Animation<TextureRegion?>? = null
    var sharkFireAnimation: Animation<TextureRegion?>? = null
    var sharkDead: AtlasRegion? = null
    var turboTail: AtlasRegion? = null

    var greenBarrel: AtlasRegion? = null
    var blackBarrel: AtlasRegion? = null
    var redBarrel: AtlasRegion? = null
    var yellowBarrel: AtlasRegion? = null

    var explosionAnimation: Animation<TextureRegion?>? = null

    var redSubmarine: AtlasRegion? = null
    var yellowSubmarine: AtlasRegion? = null

    var grayMine: AtlasRegion? = null
    var rustyMine: AtlasRegion? = null
    var chain: AtlasRegion? = null
    var blast: AtlasRegion? = null
    var torpedo: AtlasRegion? = null
    var heart: AtlasRegion? = null
    var meat: AtlasRegion? = null

    var blastHit: Animation<TextureRegion?>? = null

    var redBar: AtlasRegion? = null
    var energyBar: AtlasRegion? = null

    var background: AtlasRegion? = null
    var parallaxBackground: ParallaxBackground? = null
    var parallaxForeground: ParallaxBackground? = null

    var atlas: TextureAtlas? = null

    var bubbleParticleEffect: ParticleEffect? = null
    var sharkBubbleParticleEffect: ParticleEffect? = null
    var torpedoBubbleRightSideParticleEffect: ParticleEffect? = null
    var torpedoBubbleLeftSideParticleEffect: ParticleEffect? = null
    var fishParticleEffect: ParticleEffect? = null
    var mediumFishParticleEffect: ParticleEffect? = null

    var buttonRight: TextureRegionDrawable? = null
    var buttonRightPressed: TextureRegionDrawable? = null
    var buttonLeft: TextureRegionDrawable? = null
    var buttonLeftPressed: TextureRegionDrawable? = null
    var buttonUp: TextureRegionDrawable? = null
    var buttonUpPressed: TextureRegionDrawable? = null
    var buttonFire: TextureRegionDrawable? = null
    var buttonFirePressed: TextureRegionDrawable? = null

    var buttonHome: TextureRegionDrawable? = null
    var buttonHomePressed: TextureRegionDrawable? = null
    var buttonPause: TextureRegionDrawable? = null
    var buttonPausePressed: TextureRegionDrawable? = null
    var buttonLeaderboard: TextureRegionDrawable? = null
    var buttonLeaderboardPressed: TextureRegionDrawable? = null
    var buttonFacebook: TextureRegionDrawable? = null
    var buttonFacebookPressed: TextureRegionDrawable? = null
    var buttonMusicOn: TextureRegionDrawable? = null
    var buttonMusicOff: TextureRegionDrawable? = null
    var buttonSoundOn: TextureRegionDrawable? = null
    var buttonSoundOff: TextureRegionDrawable? = null
    var buttonRefresh: TextureRegionDrawable? = null
    var buttonRefreshPressed: TextureRegionDrawable? = null
    var buttonAchievements: TextureRegionDrawable? = null
    var buttonAchievementsPressed: TextureRegionDrawable? = null
    var buttonTwitter: TextureRegionDrawable? = null
    var buttonTwitterPressed: TextureRegionDrawable? = null

    var backgroundProgressBar: TextureRegionDrawable? = null
    var menuBackgroundDrawable: NinePatchDrawable? = null
    var windowBackgroundDrawable: NinePatchDrawable? = null
    var titleBackgroundDrawable: NinePatchDrawable? = null

    var lblStyle: LabelStyle? = null

    var swimSound: Sound? = null
    var sonarSound: Sound? = null
    var explosionSound1: Sound? = null
    var explosionSound2: Sound? = null
    var blastSound: Sound? = null

    var music: Music? = null

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontLarge = BitmapFont(Gdx.files.internal("data/FontGrande.fnt"), atlas!!.findRegion("FontGrande"))

        loadUI()

        sharkDead = atlas!!.findRegion("tiburonDead")

        val sharkFrame1 = atlas!!.findRegion("tiburon1")
        val sharkFrame2 = atlas!!.findRegion("tiburon2")
        val sharkFrame3 = atlas!!.findRegion("tiburon3")
        val sharkFrame4 = atlas!!.findRegion("tiburon4")
        val sharkFrame5 = atlas!!.findRegion("tiburon5")
        val sharkFrame6 = atlas!!.findRegion("tiburon6")
        val sharkFrame7 = atlas!!.findRegion("tiburon7")
        val sharkFrame8 = atlas!!.findRegion("tiburon8")

        sharkSwimAnimation = Animation<TextureRegion?>(.15f, sharkFrame1, sharkFrame2, sharkFrame3, sharkFrame4, sharkFrame5, sharkFrame6, sharkFrame7, sharkFrame8)
        sharkDashAnimation = Animation<TextureRegion?>(.04f, sharkFrame1, sharkFrame2, sharkFrame3, sharkFrame4, sharkFrame5, sharkFrame6, sharkFrame7, sharkFrame8)

        val sharkFireFrame1 = atlas!!.findRegion("tiburonFire1")
        val sharkFireFrame2 = atlas!!.findRegion("tiburonFire2")
        val sharkFireFrame3 = atlas!!.findRegion("tiburonFire3")
        val sharkFireFrame4 = atlas!!.findRegion("tiburonFire4")
        val sharkFireFrame5 = atlas!!.findRegion("tiburonFire5")

        sharkFireAnimation = Animation<TextureRegion?>(.075f, sharkFireFrame1, sharkFireFrame2, sharkFireFrame3, sharkFireFrame4, sharkFireFrame5)

        turboTail = atlas!!.findRegion("turbo")

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

        blastHit = Animation<TextureRegion?>(.05f, blastHit1, blastHit2, blastHit3, blastHit4, blastHit5, blastHit6)

        yellowSubmarine = atlas!!.findRegion("submarinoAmarillo")
        redSubmarine = atlas!!.findRegion("submarinoRojo")

        grayMine = atlas!!.findRegion("minaGris")
        rustyMine = atlas!!.findRegion("minaOxido")
        chain = atlas!!.findRegion("chain")
        blast = atlas!!.findRegion("blast")
        torpedo = atlas!!.findRegion("torpedo")
        heart = atlas!!.findRegion("corazon")
        meat = atlas!!.findRegion("carne")

        reloadBackground()

        bubbleParticleEffect = ParticleEffect()
        bubbleParticleEffect!!.load(Gdx.files.internal("particulas/burbujas"), atlas)

        sharkBubbleParticleEffect = ParticleEffect()
        sharkBubbleParticleEffect!!.load(Gdx.files.internal("particulas/burbujasTiburon"), atlas)

        torpedoBubbleRightSideParticleEffect = ParticleEffect()
        torpedoBubbleRightSideParticleEffect!!.load(Gdx.files.internal("particulas/burbujasTorpedoRightSide"), atlas)

        torpedoBubbleLeftSideParticleEffect = ParticleEffect()
        torpedoBubbleLeftSideParticleEffect!!.load(Gdx.files.internal("particulas/burbujasTorpedoLeftSide"), atlas)

        fishParticleEffect = ParticleEffect()
        fishParticleEffect!!.load(Gdx.files.internal("particulas/peces"), atlas)

        mediumFishParticleEffect = ParticleEffect()
        mediumFishParticleEffect!!.load(Gdx.files.internal("particulas/pecesMediano"), atlas)

        swimSound = Gdx.audio.newSound(Gdx.files.internal("sound/swim.mp3"))
        sonarSound = Gdx.audio.newSound(Gdx.files.internal("sound/sonar.mp3"))
        explosionSound1 = Gdx.audio.newSound(Gdx.files.internal("sound/explosion1.mp3"))
        explosionSound2 = Gdx.audio.newSound(Gdx.files.internal("sound/explosion2.mp3"))
        blastSound = Gdx.audio.newSound(Gdx.files.internal("sound/blast1.mp3"))

        music = Gdx.audio.newMusic(Gdx.files.internal("sound/jungleHaze.mp3"))
        music!!.isLooping = true

        if (Settings.isMusicOn) music!!.play()
    }

    private fun loadUI() {
        titleDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/titulo"))
        gameOverDrawable = TextureRegionDrawable(atlas!!.findRegion("UI/gameOver2"))

        buttonRight = TextureRegionDrawable(atlas!!.findRegion("UI/btDer"))
        buttonRightPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btDerPress"))
        buttonLeft = TextureRegionDrawable(atlas!!.findRegion("UI/btIzq"))
        buttonLeftPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btIzqPress"))
        buttonUp = TextureRegionDrawable(atlas!!.findRegion("UI/btUp"))
        buttonUpPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btUpPress"))
        buttonFire = TextureRegionDrawable(atlas!!.findRegion("UI/btFire"))
        buttonFirePressed = TextureRegionDrawable(atlas!!.findRegion("UI/btFirePress"))

        buttonRefresh = TextureRegionDrawable(atlas!!.findRegion("UI/btRefresh"))
        buttonRefreshPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btRefreshPress"))
        buttonHome = TextureRegionDrawable(atlas!!.findRegion("UI/btHome"))
        buttonHomePressed = TextureRegionDrawable(atlas!!.findRegion("UI/btHomePress"))
        buttonPause = TextureRegionDrawable(atlas!!.findRegion("UI/btPausa"))
        buttonPausePressed = TextureRegionDrawable(atlas!!.findRegion("UI/btPausaPress"))
        buttonLeaderboard = TextureRegionDrawable(atlas!!.findRegion("UI/btLeaderboard"))
        buttonLeaderboardPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btLeaderboardPress"))
        buttonAchievements = TextureRegionDrawable(atlas!!.findRegion("UI/btAchievements"))
        buttonAchievementsPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btAchievementsPress"))
        buttonFacebook = TextureRegionDrawable(atlas!!.findRegion("UI/btFacebook"))
        buttonFacebookPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btFacebookPress"))
        buttonTwitter = TextureRegionDrawable(atlas!!.findRegion("UI/btTwitter"))
        buttonTwitterPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btTwitterPress"))
        buttonSoundOn = TextureRegionDrawable(atlas!!.findRegion("UI/btSonido"))
        buttonSoundOff = TextureRegionDrawable(atlas!!.findRegion("UI/btSonidoOff"))
        buttonMusicOn = TextureRegionDrawable(atlas!!.findRegion("UI/btMusic"))
        buttonMusicOff = TextureRegionDrawable(atlas!!.findRegion("UI/btMusicOff"))

        redBar = atlas!!.findRegion("UI/redBar")
        energyBar = atlas!!.findRegion("UI/energyBar")

        backgroundProgressBar = TextureRegionDrawable(atlas!!.findRegion("UI/backgroundProgressBar"))
        menuBackgroundDrawable = NinePatchDrawable(NinePatch(atlas!!.findRegion("UI/backgroundMenu"), 70, 70, 60, 60))
        windowBackgroundDrawable = NinePatchDrawable(NinePatch(atlas!!.findRegion("UI/backgroundVentana"), 25, 25, 25, 25))
        titleBackgroundDrawable = NinePatchDrawable(NinePatch(atlas!!.findRegion("UI/backgroundTitulo"), 30, 30, 0, 0))

        lblStyle = LabelStyle(fontLarge, null)
    }

    fun reloadBackground() {
        val frontLayer: ParallaxLayer?
        val backgroundLayer: ParallaxLayer?

        if (MathUtils.randomBoolean()) {
            background = atlas!!.findRegion("fondo")
            backgroundLayer = ParallaxLayer(atlas!!.findRegion("sueloAtras"), Vector2(5f, 0f), Vector2(0f, -50f), Vector2(-1f, 480f), 1024f, 121f)
            frontLayer = ParallaxLayer(atlas!!.findRegion("suelo"), Vector2(15f, 0f), Vector2(0f, -50f), Vector2(-1f, 480f), 1024f, 121f)
        } else {
            background = atlas!!.findRegion("fondo2")
            backgroundLayer = ParallaxLayer(atlas!!.findRegion("suelo2Atras"), Vector2(5f, 0f), Vector2(0f, -50f), Vector2(-1f, 480f), 1024f, 121f)
            frontLayer = ParallaxLayer(atlas!!.findRegion("suelo2"), Vector2(15f, 0f), Vector2(0f, -50f), Vector2(-1f, 480f), 1024f, 121f)
        }
        parallaxBackground = ParallaxBackground(arrayOf<ParallaxLayer>(backgroundLayer), 800f, 480f, Vector2(10f, 0f))
        parallaxForeground = ParallaxBackground(arrayOf<ParallaxLayer>(frontLayer), 800f, 480f, Vector2(10f, 0f))
    }

    fun playExplosionSound() {
        val sound = MathUtils.random(1)
        val explosionSoundToBePlayed: Sound = if (sound == 0) explosionSound1!!
        else explosionSound2!!
        explosionSoundToBePlayed.play()
    }
}
