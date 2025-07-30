package com.nopalsoft.thetruecolor.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.thetruecolor.TrueColorGame

fun main() {
    val config = LwjglApplicationConfiguration()
    config.width = 480
    config.height = 800
    LwjglApplication(TrueColorGame(), config)
}