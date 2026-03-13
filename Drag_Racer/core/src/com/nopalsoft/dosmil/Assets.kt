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
    var languagesBundle: I18NBundle? = null

    private var fontSmall: BitmapFont? = null
    private var fontLarge: BitmapFont? = null

    var backgroundAtlasRegion: AtlasRegion? = null

    var backgroundBoardAtlasRegion: AtlasRegion? = null

    var puzzleSolvedAtlasRegion: AtlasRegion? = null

    var titleAtlasRegion: AtlasRegion? = null

    var blackPixel: NinePatchDrawable? = null

    var scoresBackgroundAtlasRegion: AtlasRegion? = null

    var buttonBack: TextureRegionDrawable? = null

    var buttonFacebook: TextureRegionDrawable? = null

    var buttonTwitter: TextureRegionDrawable? = null

    var emptyPieceAtlasRegion: AtlasRegion? = null

    var piece2AtlasRegion: AtlasRegion? = null

    var piece4AtlasRegion: AtlasRegion? = null

    var piece8AtlasRegion: AtlasRegion? = null

    var piece16AtlasRegion: AtlasRegion? = null

    var piece32AtlasRegion: AtlasRegion? = null

    var piece64AtlasRegion: AtlasRegion? = null

    var piece128AtlasRegion: AtlasRegion? = null

    var piece256AtlasRegion: AtlasRegion? = null

    var piece512AtlasRegion: AtlasRegion? = null

    var piece1024AtlasRegion: AtlasRegion? = null

    var piece2048AtlasRegion: AtlasRegion? = null

    var labelStyleSmall: LabelStyle? = null

    var labelStyleLarge: LabelStyle? = null

    var buttonStyleMusic: ButtonStyle? = null

    var buttonStylePause: ButtonStyle? = null

    var buttonStyleSound: ButtonStyle? = null

    private var atlas: TextureAtlas? = null

    private var music2: Music? = null

    private var move1: Sound? = null
    private var move2: Sound? = null

    private fun loadFont() {
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

        backgroundAtlasRegion = if (MathUtils.randomBoolean()) atlas!!.findRegion("fondo")
        else atlas!!.findRegion("fondo2")
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

        music2?.setVolume(.1F)

        playMusic()

        languagesBundle = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))
    }

    fun playMusic() {
        if (Settings.isMusicOn) music2!!.play()
    }

    fun pauseMusic() {
        music2!!.stop()
    }

    fun playSoundMove() {
        if (Settings.isSoundOn) {
            if (MathUtils.randomBoolean()) move1!!.play(.3f)
            else move2!!.play(.3f)
        }
    }
}
