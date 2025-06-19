package com.nopalsoft.slamthebird.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.slamthebird.SlamTheBirdGame

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(480, 800)
    config.setTitle("Slam the bird")
    Lwjgl3Application(SlamTheBirdGame(), config)
}

