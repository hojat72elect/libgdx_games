package com.nopalsoft.flappy.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.flappy.MainFlappyBird

fun main() {
    val config = LwjglApplicationConfiguration()
    with(config) {
        title = "Flappy Bird"
        width = 480
        height = 800
    }
    LwjglApplication(MainFlappyBird(), config)
}