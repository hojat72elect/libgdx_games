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
    var languages: I18NBundle? = null
    var fontSmall: BitmapFont? = null
    var fontLarge: BitmapFont? = null
    var background: AtlasRegion? = null
    var backgroundBoard: AtlasRegion? = null
    var puzzleSolved: AtlasRegion? = null
    var title: AtlasRegion? = null
    var pixelBlack: NinePatchDrawable? = null
    var scoresBackground: AtlasRegion? = null
    var buttonBack: TextureRegionDrawable? = null
    var buttonFacebook: TextureRegionDrawable? = null
    var buttonTwitter: TextureRegionDrawable? = null
    var emptyTile: AtlasRegion? = null
    var tile2: AtlasRegion? = null
    var tile4: AtlasRegion? = null
    var tile8: AtlasRegion? = null
    var tile16: AtlasRegion? = null
    var tile32: AtlasRegion? = null
    var tile64: AtlasRegion? = null
    var tile128: AtlasRegion? = null
    var tile256: AtlasRegion? = null
    var tile512: AtlasRegion? = null
    var tile1024: AtlasRegion? = null
    var tile2048: AtlasRegion? = null
    var labelStyleSmall: LabelStyle? = null
    var labelStyleLarge: LabelStyle? = null
    var buttonStyleMusic: ButtonStyle? = null
    var buttonStylePause: ButtonStyle? = null
    var buttonStyleSound: ButtonStyle? = null
    var atlas: TextureAtlas? = null
    var move1: Sound? = null
    var move2: Sound? = null
    private var music2: Music? = null

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

        // Button Music
        val buttonMusicOn = TextureRegionDrawable(
            atlas!!.findRegion("btMusica")
        )
        val buttonMusicOff = TextureRegionDrawable(
            atlas!!.findRegion("btSinMusica")
        )
        buttonStyleMusic = ButtonStyle(buttonMusicOn, null, buttonMusicOff)


        // Sound Button
        val buttonSoundOn = TextureRegionDrawable(
            atlas!!.findRegion("btSonido")
        )
        val buttonSoundOff = TextureRegionDrawable(
            atlas!!.findRegion("btSinSonido")
        )
        buttonStyleSound = ButtonStyle(buttonSoundOn, null, buttonSoundOff)

        // ImageButton Pause
        val buttonPauseUp = TextureRegionDrawable(
            atlas!!.findRegion("btPause")
        )
        val buttonPauseDown = TextureRegionDrawable(
            atlas!!.findRegion("btPauseDown")
        )
        buttonStylePause = ButtonStyle(buttonPauseUp, buttonPauseDown, null)
    }

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        loadFont()
        loadStyles()

        if (MathUtils.randomBoolean()) background = atlas!!.findRegion("fondo")
        else background = atlas!!.findRegion("fondo2")
        backgroundBoard = atlas!!.findRegion("fondoPuntuaciones")

        title = atlas!!.findRegion("titulo")

        pixelBlack = NinePatchDrawable(
            NinePatch(
                atlas!!.findRegion("pixelNegro"), 1, 1, 0, 0
            )
        )
        scoresBackground = atlas!!.findRegion("fondoPuntuaciones")

        puzzleSolved = atlas!!.findRegion("puzzleSolved")

        emptyTile = atlas!!.findRegion("piezaVacia")

        tile2 = atlas!!.findRegion("pieza2")
        tile4 = atlas!!.findRegion("pieza4")
        tile8 = atlas!!.findRegion("pieza8")
        tile16 = atlas!!.findRegion("pieza16")
        tile32 = atlas!!.findRegion("pieza32")
        tile64 = atlas!!.findRegion("pieza64")
        tile128 = atlas!!.findRegion("pieza128")
        tile256 = atlas!!.findRegion("pieza256")
        tile512 = atlas!!.findRegion("pieza512")
        tile1024 = atlas!!.findRegion("pieza1024")
        tile2048 = atlas!!.findRegion("pieza2048")

        buttonBack = TextureRegionDrawable(atlas!!.findRegion("btAtras2"))
        buttonFacebook = TextureRegionDrawable(atlas!!.findRegion("btFacebook"))
        buttonTwitter = TextureRegionDrawable(atlas!!.findRegion("btTwitter"))

        move1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move1.mp3"))
        move2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move2.mp3"))

        music2 = Gdx.audio.newMusic(
            Gdx.files
                .internal("data/Sounds/music2.mp3")
        )

        Settings.load()

        music2?.volume = .1f

        playMusic()

        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))
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
