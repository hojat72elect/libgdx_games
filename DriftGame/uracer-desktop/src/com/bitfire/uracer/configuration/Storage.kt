package com.bitfire.uracer.configuration

import com.badlogic.gdx.Gdx

/**
 * Represents storage path specifiers, such as levels and replays data store,
 */
object Storage {

    const val PREFERENCES = "uracer-preferences.cfg"

    // local to installation folder
    const val BOOT_CONFIG_FILE = "uracer-boot.cfg"
    const val LEVELS = "data/levels/"
    const val UI = "data/ui/"
    const val AUDIO = "data/audio/"

    // externals
    private const val CONFIG_ROOT = "/.config/uracer/"
    private const val DATA_ROOT = "/.local/share/uracer/"

    const val REPLAYS_ROOT = "${DATA_ROOT}replays/"

    @JvmStatic
    fun initialize() {
        Gdx.files.external(CONFIG_ROOT).mkdirs()
        Gdx.files.external(DATA_ROOT).mkdirs()
        Gdx.files.external(REPLAYS_ROOT).mkdirs()

        Gdx.app.log("Storage", "Config root at " + Gdx.files.external(CONFIG_ROOT))
        Gdx.app.log("Storage", "Data root at " + Gdx.files.external(DATA_ROOT))
        Gdx.app.log("Storage", "Replays root at " + Gdx.files.external(REPLAYS_ROOT))
    }
}
