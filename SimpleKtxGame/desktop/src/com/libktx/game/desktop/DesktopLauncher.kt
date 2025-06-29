package com.libktx.game.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.libktx.game.Game


fun main() {
    val config = LwjglApplicationConfiguration().apply {
        title = "Drop"
        width = 800
        height = 480
    }
    LwjglApplication(Game(), config).logLevel = Application.LOG_DEBUG
}

