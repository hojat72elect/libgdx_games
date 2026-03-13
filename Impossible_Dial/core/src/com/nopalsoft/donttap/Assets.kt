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

    var blackPixel: NinePatchDrawable? = null
    var scoresBackgroundDrawable: NinePatchDrawable? = null

    var yellowTile: AtlasRegion? = null
    var blueTile: AtlasRegion? = null
    var purpleTile: AtlasRegion? = null
    var orangeTile: AtlasRegion? = null
    var whiteTile: AtlasRegion? = null
    var redTile: AtlasRegion? = null
    var wrong: AtlasRegion? = null
    var step1: AtlasRegion? = null
    var title: AtlasRegion? = null

    var labelStyleSmall: LabelStyle? = null
    var labelStyleBlack: LabelStyle? = null

    var textButtonStyleSmall: TextButtonStyle? = null

    var buttonFacebook: TextureRegionDrawable? = null
    var buttonTwitter: TextureRegionDrawable? = null
    var buttonBack: TextureRegionDrawable? = null

    private fun loadStyles(atlas: TextureAtlas) {
        labelStyleSmall = LabelStyle(font, Color.WHITE)
        labelStyleBlack = LabelStyle(font, Color.GRAY)

        scoresBackgroundDrawable = NinePatchDrawable(atlas.createPatch("fondoPuntuaciones"))

        textButtonStyleSmall = TextButtonStyle(scoresBackgroundDrawable, null, null, font)
        textButtonStyleSmall!!.fontColor = Color.WHITE
    }

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        title = atlas.findRegion("titulo")

        font = BitmapFont(Gdx.files.internal("data/font.fnt"), atlas.findRegion("font"))

        blackPixel = NinePatchDrawable(NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0))

        loadStyles(atlas)

        yellowTile = atlas.findRegion("amarillo")
        blueTile = atlas.findRegion("azul")
        redTile = atlas.findRegion("rojo")
        whiteTile = atlas.findRegion("blanco")
        purpleTile = atlas.findRegion("morado")
        orangeTile = atlas.findRegion("naranja")
        wrong = atlas.findRegion("wrong")
        step1 = atlas.findRegion("step")

        buttonTwitter = TextureRegionDrawable(atlas.findRegion("btTwitter"))
        buttonFacebook = TextureRegionDrawable(atlas.findRegion("btFacebook"))
        buttonBack = TextureRegionDrawable(atlas.findRegion("btAtras"))

        loadSounds()

        Settings.load()
    }

    private fun loadSounds() {
        pianoSounds = arrayOfNulls(26)
        val directory = "data/Sounds/piano"
        for (i in pianoSounds!!.indices) {
            val sonido = Gdx.audio.newSound(Gdx.files.internal("$directory$i.mp3"))
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
