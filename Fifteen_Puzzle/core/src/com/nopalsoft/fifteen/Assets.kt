package com.nopalsoft.fifteen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Music.OnCompletionListener
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object Assets {
    var fontChico: BitmapFont? = null
    var fontGrande: BitmapFont? = null

    @JvmField
    var fondo: AtlasRegion? = null

    @JvmField
    var fondoTablero: AtlasRegion? = null

    @JvmField
    var puzzleSolved: AtlasRegion? = null

    @JvmField
    var titulo: AtlasRegion? = null

    @JvmField
    var pixelNegro: NinePatchDrawable? = null

    @JvmField
    var fondoPuntuaciones: AtlasRegion? = null

    @JvmField
    var btAtras: TextureRegionDrawable? = null

    @JvmField
    var btFacebook: TextureRegionDrawable? = null

    @JvmField
    var btTwitter: TextureRegionDrawable? = null

    @JvmField
    var piezaVacia: AtlasRegion? = null

    @JvmField
    var pieza1: AtlasRegion? = null

    @JvmField
    var pieza2: AtlasRegion? = null

    @JvmField
    var pieza3: AtlasRegion? = null

    @JvmField
    var pieza4: AtlasRegion? = null

    @JvmField
    var pieza5: AtlasRegion? = null

    @JvmField
    var pieza6: AtlasRegion? = null

    @JvmField
    var pieza7: AtlasRegion? = null

    @JvmField
    var pieza8: AtlasRegion? = null

    @JvmField
    var pieza9: AtlasRegion? = null

    @JvmField
    var pieza10: AtlasRegion? = null

    @JvmField
    var pieza11: AtlasRegion? = null

    @JvmField
    var pieza12: AtlasRegion? = null

    @JvmField
    var pieza13: AtlasRegion? = null

    @JvmField
    var pieza14: AtlasRegion? = null

    @JvmField
    var pieza15: AtlasRegion? = null

    @JvmField
    var labelStyleChico: LabelStyle? = null

    @JvmField
    var labelStyleGrande: LabelStyle? = null

    @JvmField
    var styleButtonMusica: ButtonStyle? = null

    @JvmField
    var styleButtonPause: ButtonStyle? = null

    @JvmField
    var styleButtonSonido: ButtonStyle? = null

    var atlas: TextureAtlas? = null
    var move1: Sound? = null
    var move2: Sound? = null
    private var music1: Music? = null
    private var music2: Music? = null

    fun loadFont() {
        fontChico = BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas!!.findRegion("fontChico"))

        fontGrande = BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas!!.findRegion("fontGrande"))
    }

    private fun cargarEstilos() {
        labelStyleChico = LabelStyle(fontChico, Color.WHITE)
        labelStyleGrande = LabelStyle(fontGrande, Color.WHITE)

        /* Button Musica */
        val btMusicOn = TextureRegionDrawable(atlas!!.findRegion("btMusica"))
        val btMusicOff = TextureRegionDrawable(atlas!!.findRegion("btSinMusica"))
        styleButtonMusica = ButtonStyle(btMusicOn, null, btMusicOff)

        /* Boton Sonido */
        val botonSonidoOn = TextureRegionDrawable(atlas!!.findRegion("btSonido"))
        val botonSonidoOff = TextureRegionDrawable(atlas!!.findRegion("btSinSonido"))
        styleButtonSonido = ButtonStyle(botonSonidoOn, null, botonSonidoOff)

        /* ImageButton Pause */
        val btPauseUp = TextureRegionDrawable(atlas!!.findRegion("btPause"))
        val btPauseDown = TextureRegionDrawable(atlas!!.findRegion("btPauseDown"))
        styleButtonPause = ButtonStyle(btPauseUp, btPauseDown, null)
    }

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        loadFont()
        cargarEstilos()

        if (MathUtils.randomBoolean()) fondo = atlas!!.findRegion("fondo")
        else fondo = atlas!!.findRegion("fondo2")
        fondoTablero = atlas!!.findRegion("fondoPuntuaciones")

        titulo = atlas!!.findRegion("titulo")

        pixelNegro = NinePatchDrawable(NinePatch(atlas!!.findRegion("pixelNegro"), 1, 1, 0, 0))
        fondoPuntuaciones = atlas!!.findRegion("fondoPuntuaciones")

        puzzleSolved = atlas!!.findRegion("puzzleSolved")

        piezaVacia = atlas!!.findRegion("piezaVacia")
        pieza1 = atlas!!.findRegion("pieza1")
        pieza2 = atlas!!.findRegion("pieza2")
        pieza3 = atlas!!.findRegion("pieza3")
        pieza4 = atlas!!.findRegion("pieza4")
        pieza5 = atlas!!.findRegion("pieza5")
        pieza6 = atlas!!.findRegion("pieza6")
        pieza7 = atlas!!.findRegion("pieza7")
        pieza8 = atlas!!.findRegion("pieza8")
        pieza9 = atlas!!.findRegion("pieza9")
        pieza10 = atlas!!.findRegion("pieza10")
        pieza11 = atlas!!.findRegion("pieza11")
        pieza12 = atlas!!.findRegion("pieza12")
        pieza13 = atlas!!.findRegion("pieza13")
        pieza14 = atlas!!.findRegion("pieza14")
        pieza15 = atlas!!.findRegion("pieza15")

        btAtras = TextureRegionDrawable(atlas!!.findRegion("btAtras2"))
        btFacebook = TextureRegionDrawable(atlas!!.findRegion("btFacebook"))
        btTwitter = TextureRegionDrawable(atlas!!.findRegion("btTwitter"))

        move1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move1.mp3"))
        move2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move2.mp3"))

        music1 = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music1.mp3"))
        music2 = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music2.mp3"))

        Settings.load()

        music1!!.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(music: Music?) {
                if (Settings.isMusicOn) music2!!.play()
            }
        })

        music2!!.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(music: Music?) {
                if (Settings.isMusicOn) music1!!.play()
            }
        })

        playMusic()
    }

    @JvmStatic
    fun playMusic() {
        if (Settings.isMusicOn) {
            music1!!.stop()
            music2!!.stop()
            if (MathUtils.randomBoolean()) music1!!.play()
            else music2!!.play()
        }
    }

    @JvmStatic
    fun pauseMusic() {
        music1!!.stop()
        music2!!.stop()
    }

    @JvmStatic
    fun playSoundMove() {
        if (Settings.isSoundOn) {
            if (MathUtils.randomBoolean()) move1!!.play(1f)
            else move2!!.play(1f)
        }
    }
}
