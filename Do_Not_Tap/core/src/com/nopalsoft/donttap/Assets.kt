package com.nopalsoft.donttap

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object Assets {
    var font: BitmapFont? = null
    var pianoSounds: Array<Sound?>? = null

    @JvmField
    var pixelNegro: NinePatchDrawable? = null

    @JvmField
    var fondoPuntuaciones: NinePatchDrawable? = null

    var tileAmarillo: AtlasRegion? = null
    var tileAzul: AtlasRegion? = null
    var tileMorado: AtlasRegion? = null
    var tileNaranja: AtlasRegion? = null
    var tileBlanco: AtlasRegion? = null
    var tileRojo: AtlasRegion? = null
    var wrong: AtlasRegion? = null
    var step1: AtlasRegion? = null

    var titulo: AtlasRegion? = null

    @JvmField
    var labelStyleChico: LabelStyle? = null

    @JvmField
    var labelStyleBlack: LabelStyle? = null

    @JvmField
    var textButtonStyleChico: TextButtonStyle? = null

    @JvmField
    var btFacebook: TextureRegionDrawable? = null

    @JvmField
    var btTwitter: TextureRegionDrawable? = null
    var btAtras: TextureRegionDrawable? = null

    private fun cargarEstilos(atlas: TextureAtlas) {
        labelStyleChico = LabelStyle(font, Color.WHITE)
        labelStyleBlack = LabelStyle(font, Color.GRAY)

        fondoPuntuaciones = NinePatchDrawable(atlas.createPatch("fondoPuntuaciones"))

        textButtonStyleChico = TextButtonStyle(fondoPuntuaciones, null, null, font)
        textButtonStyleChico!!.fontColor = Color.WHITE
    }

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        titulo = atlas.findRegion("titulo")

        font = BitmapFont(Gdx.files.internal("data/font.fnt"), atlas.findRegion("font"))

        pixelNegro = NinePatchDrawable(NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0))

        cargarEstilos(atlas)

        tileAmarillo = atlas.findRegion("amarillo")
        tileAzul = atlas.findRegion("azul")
        tileRojo = atlas.findRegion("rojo")
        tileBlanco = atlas.findRegion("blanco")
        tileMorado = atlas.findRegion("morado")
        tileNaranja = atlas.findRegion("naranja")
        wrong = atlas.findRegion("wrong")
        step1 = atlas.findRegion("step")

        btTwitter = TextureRegionDrawable(atlas.findRegion("btTwitter"))
        btFacebook = TextureRegionDrawable(atlas.findRegion("btFacebook"))
        btAtras = TextureRegionDrawable(atlas.findRegion("btAtras"))

        loadSounds()

        Settings.load()
    }

    private fun loadSounds() {
        pianoSounds = arrayOfNulls<Sound>(26)
        val ruta = "data/Sounds/piano"
        for (i in pianoSounds!!.indices) {
            val sonido = Gdx.audio.newSound(Gdx.files.internal(ruta + i + ".mp3"))
            pianoSounds?.set(i, sonido)
        }

        soundTap1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/tap1.mp3"))
        soundTap2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/tap2.mp3"))
        soundWrong = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/wrong.mp3"))
    }


    var soundTap1: Sound? = null
    var soundTap2: Sound? = null
    var soundWrong: Sound? = null

    fun playTapSound() {
        if (!Settings.isSoundEnabled) return
        soundTap2!!.play()
    }
}
