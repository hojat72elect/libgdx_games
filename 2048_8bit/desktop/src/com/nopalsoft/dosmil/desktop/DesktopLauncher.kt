package com.nopalsoft.dosmil.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.dosmil.MainGame

fun main() {
    val config = LwjglApplicationConfiguration()
    config.title = "2048 8Bit"
    config.width = 480
    config.height = 800

    LwjglApplication(MainGame(), config)
}

