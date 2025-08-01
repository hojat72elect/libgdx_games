package com.nopalsoft.impossibledial.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.impossibledial.MainGame

fun main() {
    val config = LwjglApplicationConfiguration()
    config.height = 800
    config.width = 480
    LwjglApplication(MainGame(), config)
}
