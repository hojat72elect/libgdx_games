package com.nopalsoft.donttap.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.donttap.DoNotTapGame
import com.nopalsoft.donttap.handlers.FloatFormatter

fun main() {
    val formatter = FloatFormatter { format, number -> String.format(format, number) }
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle("Do not tap the wrong tile")
    config.setWindowedMode(480, 800)
    Lwjgl3Application(DoNotTapGame(formatter), config)
}

