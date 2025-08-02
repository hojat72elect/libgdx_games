package com.nopalsoft.fifteen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
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
    var fondo: AtlasRegion? = null
    var fondoTablero: AtlasRegion? = null
    var puzzleSolved: AtlasRegion? = null
    var titulo: AtlasRegion? = null
    var pixelNegro: NinePatchDrawable? = null
    var fondoPuntuaciones: AtlasRegion? = null
    var btAtras: TextureRegionDrawable? = null
    var btFacebook: TextureRegionDrawable? = null
    var btTwitter: TextureRegionDrawable? = null
    var piezaVacia: AtlasRegion? = null
    var pieza1: AtlasRegion? = null
    var pieza2: AtlasRegion? = null
    var pieza3: AtlasRegion? = null
    var pieza4: AtlasRegion? = null
    var pieza5: AtlasRegion? = null
    var pieza6: AtlasRegion? = null
    var pieza7: AtlasRegion? = null
    var pieza8: AtlasRegion? = null
    var pieza9: AtlasRegion? = null
    var pieza10: AtlasRegion? = null
    var pieza11: AtlasRegion? = null
    var pieza12: AtlasRegion? = null
    var pieza13: AtlasRegion? = null
    var pieza14: AtlasRegion? = null
    var pieza15: AtlasRegion? = null
    var labelStyleChico: LabelStyle? = null
    var labelStyleGrande: LabelStyle? = null
    var styleButtonMusica: ButtonStyle? = null
    var styleButtonPause: ButtonStyle? = null
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

        fondo = if (MathUtils.randomBoolean()) atlas!!.findRegion("fondo")
        else atlas!!.findRegion("fondo2")
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

        music1!!.setOnCompletionListener { if (Settings.isMusicOn) music2!!.play() }

        music2!!.setOnCompletionListener { if (Settings.isMusicOn) music1!!.play() }

        playMusic()
    }


    fun playMusic() {
        if (Settings.isMusicOn) {
            music1!!.stop()
            music2!!.stop()
            if (MathUtils.randomBoolean()) music1!!.play()
            else music2!!.play()
        }
    }


    fun pauseMusic() {
        music1!!.stop()
        music2!!.stop()
    }


    fun playSoundMove() {
        if (Settings.isSoundOn) {
            if (MathUtils.randomBoolean()) move1!!.play(1f)
            else move2!!.play(1f)
        }
    }
}
