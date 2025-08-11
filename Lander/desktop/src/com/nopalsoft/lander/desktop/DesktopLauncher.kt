package com.nopalsoft.lander.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.lander.MainLander

fun main() {
    val config = LwjglApplicationConfiguration()
    config.title = "Lander"
    config.width = 480
    config.height = 800
    LwjglApplication(MainLander(), config)
}

