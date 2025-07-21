package com.nopalsoft.clumsy.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.clumsy.ClumsyUfoGame

object DesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = "Clumsy Ufo"
        config.width = 480
        config.height = 800

        LwjglApplication(ClumsyUfoGame(), config)
    }
}
