package com.nopalsoft.flappy.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.flappy.MainFlappyBird

fun main() {
    val config = LwjglApplicationConfiguration()
    config.title = "Flappy Bird"
    config.width = 480
    config.height = 800
    LwjglApplication(MainFlappyBird(), config)
}
