package com.nopalsoft.thetruecolor.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.thetruecolor.MainStreet

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle("StreetSwipinRace")
    config.setWindowedMode(480, 800)
    Lwjgl3Application(MainStreet(), config)
}

