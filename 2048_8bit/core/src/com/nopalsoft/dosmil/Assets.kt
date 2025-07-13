package com.nopalsoft.dosmil

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
import com.badlogic.gdx.utils.I18NBundle

object Assets {
    @JvmField
    var languagesBundle: I18NBundle? = null

    var fontSmall: BitmapFont? = null
    var fontLarge: BitmapFont? = null

    @JvmField
    var backgroundAtlasRegion: AtlasRegion? = null

    @JvmField
    var backgroundBoardAtlasRegion: AtlasRegion? = null

    @JvmField
    var puzzleSolvedAtlasRegion: AtlasRegion? = null

    @JvmField
    var titleAtlasRegion: AtlasRegion? = null

    @JvmField
    var blackPixel: NinePatchDrawable? = null

    @JvmField
    var scoresBackgroundAtlasRegion: AtlasRegion? = null

    @JvmField
    var buttonBack: TextureRegionDrawable? = null

    @JvmField
    var buttonFacebook: TextureRegionDrawable? = null

    @JvmField
    var buttonTwitter: TextureRegionDrawable? = null

    @JvmField
    var emptyPieceAtlasRegion: AtlasRegion? = null

    @JvmField
    var piece2AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece4AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece8AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece16AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece32AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece64AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece128AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece256AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece512AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece1024AtlasRegion: AtlasRegion? = null

    @JvmField
    var piece2048AtlasRegion: AtlasRegion? = null

    @JvmField
    var labelStyleSmall: LabelStyle? = null

    @JvmField
    var labelStyleLarge: LabelStyle? = null

    @JvmField
    var buttonStyleMusic: ButtonStyle? = null

    @JvmField
    var buttonStylePause: ButtonStyle? = null

    @JvmField
    var buttonStyleSound: ButtonStyle? = null

    var atlas: TextureAtlas? = null

    private var music2: Music? = null

    var move1: Sound? = null
    var move2: Sound? = null

    fun loadFont() {
        fontSmall = BitmapFont(
            Gdx.files.internal("data/font25.fnt"),
            atlas!!.findRegion("font25")
        )

        fontLarge = BitmapFont(
            Gdx.files.internal("data/font45.fnt"),
            atlas!!.findRegion("font45")
        )
    }

    private fun loadStyles() {
        labelStyleSmall = LabelStyle(fontSmall, Color.WHITE)
        labelStyleLarge = LabelStyle(fontLarge, Color.WHITE)

        val buttonMusicOn = TextureRegionDrawable(atlas!!.findRegion("btMusica"))
        val buttonMusicOff = TextureRegionDrawable(atlas!!.findRegion("btSinMusica"))
        buttonStyleMusic = ButtonStyle(buttonMusicOn, null, buttonMusicOff)

        val buttonSoundOn = TextureRegionDrawable(atlas!!.findRegion("btSonido"))
        val buttonSoundOff = TextureRegionDrawable(atlas!!.findRegion("btSinSonido"))
        buttonStyleSound = ButtonStyle(buttonSoundOn, null, buttonSoundOff)

        val buttonPauseUp = TextureRegionDrawable(atlas!!.findRegion("btPause"))
        val buttonPauseDown = TextureRegionDrawable(atlas!!.findRegion("btPauseDown"))
        buttonStylePause = ButtonStyle(buttonPauseUp, buttonPauseDown, null)
    }

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        loadFont()
        loadStyles()

        if (MathUtils.randomBoolean()) backgroundAtlasRegion = atlas!!.findRegion("fondo")
        else backgroundAtlasRegion = atlas!!.findRegion("fondo2")
        backgroundBoardAtlasRegion = atlas!!.findRegion("fondoPuntuaciones")

        titleAtlasRegion = atlas!!.findRegion("titulo")

        blackPixel = NinePatchDrawable(NinePatch(atlas!!.findRegion("pixelNegro"), 1, 1, 0, 0))
        scoresBackgroundAtlasRegion = atlas!!.findRegion("fondoPuntuaciones")

        puzzleSolvedAtlasRegion = atlas!!.findRegion("puzzleSolved")

        emptyPieceAtlasRegion = atlas!!.findRegion("piezaVacia")

        piece2AtlasRegion = atlas!!.findRegion("pieza2")
        piece4AtlasRegion = atlas!!.findRegion("pieza4")
        piece8AtlasRegion = atlas!!.findRegion("pieza8")
        piece16AtlasRegion = atlas!!.findRegion("pieza16")
        piece32AtlasRegion = atlas!!.findRegion("pieza32")
        piece64AtlasRegion = atlas!!.findRegion("pieza64")
        piece128AtlasRegion = atlas!!.findRegion("pieza128")
        piece256AtlasRegion = atlas!!.findRegion("pieza256")
        piece512AtlasRegion = atlas!!.findRegion("pieza512")
        piece1024AtlasRegion = atlas!!.findRegion("pieza1024")
        piece2048AtlasRegion = atlas!!.findRegion("pieza2048")

        buttonBack = TextureRegionDrawable(atlas!!.findRegion("btAtras2"))
        buttonFacebook = TextureRegionDrawable(atlas!!.findRegion("btFacebook"))
        buttonTwitter = TextureRegionDrawable(atlas!!.findRegion("btTwitter"))

        move1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move1.mp3"))
        move2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move2.mp3"))

        music2 = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music2.mp3"))

        Settings.load()

        music2?.volume = .1f

        playMusic()

        languagesBundle = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))
    }

    @JvmStatic
    fun playMusic() {
        if (Settings.isMusicOn) music2!!.play()
    }

    @JvmStatic
    fun pauseMusic() {
        music2!!.stop()
    }

    @JvmStatic
    fun playSoundMove() {
        if (Settings.isSoundOn) {
            if (MathUtils.randomBoolean()) move1!!.play(.3f)
            else move2!!.play(.3f)
        }
    }
}
