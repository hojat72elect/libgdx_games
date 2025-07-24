package dev.lonami.klooni

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin

object SkinLoader {
    private val multipliers = floatArrayOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f, 4.0f)
    private val ids = arrayOf(
        "play", "play_saved", "star", "stopwatch", "palette", "home", "replay",
        "share", "sound_on", "sound_off", "snap_on", "snap_off", "issues", "credits",
        "web", "back", "ok", "cancel", "power_off", "effects"
    )

    private val bestMultiplier: Float

    // FIXME this static code is exposed to a race condition and will fail if called class gets loaded before execution of Klooni.create
    init {
        val desired = (Gdx.graphics.height / Klooni.GAME_HEIGHT).toFloat()
        // Use the height to determine the best match
        // We cannot use a size which is over the device height,
        // so use the closest smaller one
        var i = multipliers.size - 1
        while (i > 0) {
            if (multipliers[i] < desired) break
            --i
        }

        // Now that we have the right multiplier, load the skin
        Gdx.app.log("SkinLoader", "Using assets multiplier x" + multipliers[i])
        bestMultiplier = multipliers[i]
    }

    @JvmStatic
    fun loadSkin(): Skin {
        var folder = "ui/x$bestMultiplier/"

        // Base skin
        val skin = Skin(Gdx.files.internal("skin/uiskin.json"))

        // Nine patches
        val border = (28 * bestMultiplier).toInt()
        skin.add(
            "button_up", NinePatch(
                Texture(
                    Gdx.files.internal(folder + "button_up.png")
                ), border, border, border, border
            )
        )

        skin.add(
            "button_down", NinePatch(
                Texture(
                    Gdx.files.internal(folder + "button_down.png")
                ), border, border, border, border
            )
        )

        for (id in ids) {
            skin.add(id + "_texture", Texture(Gdx.files.internal("$folder$id.png")))
        }

        folder = "font/x$bestMultiplier/"
        skin.add("font", BitmapFont(Gdx.files.internal(folder + "geosans-light64.fnt")))
        skin.add("font_small", BitmapFont(Gdx.files.internal(folder + "geosans-light32.fnt")))
        skin.add("font_bonus", BitmapFont(Gdx.files.internal(folder + "the-next-font.fnt")))

        return skin
    }

    @JvmStatic
    fun loadPng(name: String): Texture {
        val filename = "ui/x$bestMultiplier/$name"
        return Texture(Gdx.files.internal(filename))
    }
}
