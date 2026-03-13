package com.nopalsoft.impossibledial.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.impossibledial.MainGame

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(480, 800)
    Lwjgl3Application(MainGame(), config)
}

