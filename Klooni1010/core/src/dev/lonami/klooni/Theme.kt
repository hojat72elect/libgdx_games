package dev.lonami.klooni

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.JsonReader
import dev.lonami.klooni.SkinLoader.loadPng
import kotlin.math.sqrt

// Represents a Theme for the current game.
// These are loaded from external files, so more
// can be easily added
class Theme private constructor() {
    // Save the button styles so the changes here get reflected
    private val buttonStyles: Array<ImageButtonStyle?>

    @JvmField
    var background: Color? = null

    @JvmField
    var foreground: Color? = null

    @JvmField
    var currentScore: Color? = null

    @JvmField
    var highScore: Color? = null

    @JvmField
    var bonus: Color? = null

    @JvmField
    var bandColor: Color? = null

    @JvmField
    var textColor: Color? = null

    @JvmField
    var cellTexture: Texture? = null

    @JvmField
    var displayName: String? = null

    @JvmField
    var name: String? = null

    @JvmField
    var price = 0

    private var emptyCell: Color? = null

    private lateinit var cells: Array<Color?>

    init {
        buttonStyles = arrayOfNulls<ImageButtonStyle>(4)
    }

    // Updates the theme with all the values from the specified file or name
    fun update(name: String?): Theme {
        return update(Gdx.files.internal("themes/" + name + ".theme"))
    }

    private fun update(handle: FileHandle): Theme {
        if (skin == null) {
            throw NullPointerException("A Theme.skin must be set before updating any Theme instance")
        }

        val json = JsonReader().parse(handle.readString())

        name = handle.nameWithoutExtension()
        displayName = json.getString("name")
        price = json.getInt("price")

        val colors = json.get("colors")
        // Java won't allow unsigned integers, we need to use Long
        background = Color(colors.getString("background").toLong(16).toInt())
        foreground = Color(colors.getString("foreground").toLong(16).toInt())

        val buttonColors = colors.get("buttons")
        val buttons = arrayOfNulls<Color>(buttonColors.size)
        for (i in buttons.indices) {
            buttons[i] = Color(buttonColors.getString(i).toLong(16).toInt())
            if (buttonStyles[i] == null) {
                buttonStyles[i] = ImageButtonStyle()
            }
            // Update the style. Since every button uses an instance from this
            // array, the changes will appear on screen automatically.
            buttonStyles[i]!!.up = skin!!.newDrawable("button_up", buttons[i])
            buttonStyles[i]!!.down = skin!!.newDrawable("button_down", buttons[i])
        }

        currentScore = Color(colors.getString("current_score").toLong(16).toInt())
        highScore = Color(colors.getString("high_score").toLong(16).toInt())
        bonus = Color(colors.getString("bonus").toLong(16).toInt())
        bandColor = Color(colors.getString("band").toLong(16).toInt())
        textColor = Color(colors.getString("text").toLong(16).toInt())

        emptyCell = Color(colors.getString("empty_cell").toLong(16).toInt())

        val cellColors = colors.get("cells")
        cells = arrayOfNulls<Color>(cellColors.size)
        for (i in cells.indices) {
            cells[i] = Color(cellColors.getString(i).toLong(16).toInt())
        }

        val cellTextureFile = json.getString("cell_texture")
        cellTexture = loadPng("cells/" + cellTextureFile)

        return this
    }

    fun getStyle(button: Int): ImageButtonStyle? {
        return buttonStyles[button]
    }

    fun getCellColor(colorIndex: Int): Color? {
        return if (colorIndex < 0) emptyCell else cells[colorIndex]
    }

    fun glClearBackground() {
        Gdx.gl.glClearColor(background!!.r, background!!.g, background!!.b, background!!.a)
    }

    fun updateStyle(style: ImageButtonStyle, styleIndex: Int) {
        style.imageUp = buttonStyles[styleIndex]!!.imageUp
        style.imageDown = buttonStyles[styleIndex]!!.imageDown
    }

    fun dispose() {
        cellTexture!!.dispose()
    }

    companion object {
        // Used to determine the best foreground color (black or white) given a background color
        // Formula took from http://alienryderflex.com/hsp.html
        // Not used yet, but may be useful
        private const val BRIGHTNESS_CUTOFF = 0.5

        @JvmField
        var skin: Skin? = null

        @JvmStatic
        fun exists(name: String?): Boolean {
            return Gdx.files.internal("themes/" + name + ".theme").exists()
        }


        // Gets all the available themes on the available on the internal game storage
        @JvmStatic
        fun getThemes(): com.badlogic.gdx.utils.Array<Theme?> {
            val themes: Array<String?> = Gdx.files.internal("themes/theme.list").readString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val result = com.badlogic.gdx.utils.Array<Theme?>(themes.size)
            for (i in themes.indices) {
                val file = Gdx.files.internal("themes/" + themes[i] + ".theme")
                if (file.exists()) result.add(fromFile(file))
                else {
                    Gdx.app.log(
                        "Theme/Info", "Non-existing theme '" + themes[i] +
                                "' found on theme.list (line " + (i + 1) + ")"
                    )
                }
            }

            return result
        }

        @JvmStatic
        fun getTheme(name: String?): Theme {
            return Theme().update(name)
        }

        private fun fromFile(handle: FileHandle): Theme {
            return Theme().update(handle)
        }

        @JvmStatic
        fun shouldUseWhite(color: Color): Boolean {
            val brightness = sqrt(
                color.r * color.r * .299 + color.g * color.g * .587 + color.b * color.b * .114
            )

            return brightness < BRIGHTNESS_CUTOFF
        }

        // A 1x1 blank pixel map to be tinted and used in multiple places
        @JvmStatic
        fun getBlankTexture(): Texture {
            val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
            pixmap.setColor(Color.WHITE)
            pixmap.fill()
            val result = Texture(pixmap)
            pixmap.dispose()
            return result
        }
    }
}
