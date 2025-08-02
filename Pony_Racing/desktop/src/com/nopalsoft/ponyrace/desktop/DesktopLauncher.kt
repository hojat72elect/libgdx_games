package com.nopalsoft.ponyrace.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.handlers.FloatFormatter

val formatter = FloatFormatter { format: String, args: Float -> String.format(format, args) }

fun main() {
    val cfg = LwjglApplicationConfiguration()
    cfg.title = "Pony Racing"
    cfg.width = 800
    cfg.height = 480
    LwjglApplication(PonyRacingGame(formatter), cfg)
}
