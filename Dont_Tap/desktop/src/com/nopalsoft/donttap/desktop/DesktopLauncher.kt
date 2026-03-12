package com.nopalsoft.donttap.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.donttap.DoNotTapGame

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(800, 480)
    config.setTitle("Ninja Runner")
    val game = DoNotTapGame(null)
    Lwjgl3Application(game, config)
}

