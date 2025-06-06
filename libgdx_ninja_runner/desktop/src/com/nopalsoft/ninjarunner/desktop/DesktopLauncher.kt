package com.nopalsoft.ninjarunner.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.ninjarunner.NinjaRunnerGame

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(800, 480)
    config.setTitle("Ninja Runner")
    val game = NinjaRunnerGame()
    Lwjgl3Application(game, config)
}

