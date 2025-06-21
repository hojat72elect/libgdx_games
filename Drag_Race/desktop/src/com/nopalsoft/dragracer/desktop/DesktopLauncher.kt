package com.nopalsoft.dragracer.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.dragracer.MainStreet

fun main() {
    val gameConfig = LwjglApplicationConfiguration()
    gameConfig.title = "StreetSwipinRace"
    gameConfig.width = 480
    gameConfig.height = 800

    LwjglApplication(MainStreet(), gameConfig)
}
