package com.nopalsoft.zombiewars.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.zombiewars.MainZombieWars


fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.width = 800
    config.height = 480
    LwjglApplication(MainZombieWars(), config)
}
