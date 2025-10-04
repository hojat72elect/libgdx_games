package com.bitfire.uracer.resources

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.LongMap
import com.bitfire.utils.Hash.apHash

/**
 * Lazy factory for BitmapFont objects.
 *
 * Used font resources number count is usually fairy low, so let's assume this and implement a "lazy loading" strategy: cache-hits
 * should be early maximized (100%) just at the first couple of frames of the real game.
 */
object BitmapFontFactory {

    private val fontCache = LongMap<BitmapFont>()

    /**
     * Returns an instance of a BitmapFont, performing a font reset to the initial state (when reused), if specified.
     */
    @JvmOverloads
    @JvmStatic
    fun get(face: FontFace, reset: Boolean = true): BitmapFont {
        val name = face.fontName
        val hash = apHash(name)
        var f: BitmapFont? = fontCache.get(hash)

        if (f != null) {
            if (reset) setupFont(f)
            return f
        }
        f = BitmapFont(Gdx.files.internal("data/font/$name.fnt"), Art.fontAtlas.findRegion(name), true)
        setupFont(f)
        fontCache.put(hash, f)
        return f
    }

    private fun setupFont(font: BitmapFont) {
        font.setUseIntegerPositions(false)
    }

    @JvmStatic
    fun dispose() {
        for (f in fontCache.values()) f.dispose()

        fontCache.clear()
    }

    /**
     * The fontName reflects the filename.
     */
    enum class FontFace(val fontName: String) {
        AdobeSourceSans("adobe-source-sans"),
        CurseGreen("curse-g"),
        CurseGreenBig("curse-g-big"),
        CurseRed("curse-r"),
        CurseRedBig("curse-r-big"),
        CurseRedYellow("curse-y-r"),
        CurseRedYellowBig("curse-y-r-big"),
        CurseWhiteBig("curse-big-white"),
        Molengo("molengo"),
        Roboto("roboto"),
        CurseRedYellowNew("curse-new"),
        Lcd("lcd"),
        LcdWhite("lcd-white"),
        Arcade("arcade"),
        Karmatic("karmatic"),
        DottyShadow("dotty-shadow"),
        AuXDotBitCXtra("AuX DotBitC Xtra");
    }
}
