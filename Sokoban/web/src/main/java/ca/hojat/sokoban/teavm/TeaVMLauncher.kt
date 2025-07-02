package ca.hojat.sokoban.teavm

import ca.hojat.sokoban.SokobanGame
import com.github.xpenatan.gdx.backends.teavm.TeaApplication
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration

/**
 * Launches the TeaVM/HTML application.
 */
fun main() {
    val config = TeaApplicationConfiguration("canvas")

    // Fill up the whole canvas.
    config.width = -1
    config.height = -1
    TeaApplication(SokobanGame(), config)
}

