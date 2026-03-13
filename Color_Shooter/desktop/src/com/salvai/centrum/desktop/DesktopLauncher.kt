package com.salvai.centrum.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.salvai.centrum.CentrumGameClass

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    Lwjgl3Application(CentrumGameClass(), config)
}

