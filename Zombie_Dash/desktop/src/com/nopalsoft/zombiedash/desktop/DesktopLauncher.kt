package com.nopalsoft.zombiedash.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.zombiedash.MainZombieDash

fun main() {
    val config = LwjglApplicationConfiguration()
    config.width = 800
    config.height = 480
    LwjglApplication(MainZombieDash(), config)
}
