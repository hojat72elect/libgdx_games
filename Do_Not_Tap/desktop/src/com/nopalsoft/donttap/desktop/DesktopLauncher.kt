package com.nopalsoft.donttap.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.donttap.DoNotTapGame
import com.nopalsoft.donttap.handlers.FloatFormatter

fun main() {
    val formatter = FloatFormatter { format, number -> String.format(format, number) }
    val config = LwjglApplicationConfiguration()
    config.title = "Do not tap the wrong tile"
    config.width = 480
    config.height = 800
    LwjglApplication(DoNotTapGame(formatter), config)
}
