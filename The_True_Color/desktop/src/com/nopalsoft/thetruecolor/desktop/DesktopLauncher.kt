package com.nopalsoft.thetruecolor.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.thetruecolor.TrueColorGame

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(480, 800)
    Lwjgl3Application(TrueColorGame(), config)
}

