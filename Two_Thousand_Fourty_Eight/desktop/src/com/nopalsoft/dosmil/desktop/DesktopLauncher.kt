package com.nopalsoft.dosmil.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.dosmil.MainGame


fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle("2048 8Bit")
    config.setWindowedMode(480, 800)

    Lwjgl3Application(MainGame(), config)
}

