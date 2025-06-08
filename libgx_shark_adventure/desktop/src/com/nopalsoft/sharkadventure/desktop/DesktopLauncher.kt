package com.nopalsoft.sharkadventure.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.sharkadventure.SharkAdventureGame

fun main() {
    val config = LwjglApplicationConfiguration()
    config.width = 800
    config.height = 480
    LwjglApplication(SharkAdventureGame(), config)
}

