package com.nopalsoft.dosmil.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.dosmil.MainGame

fun main(args: Array<String>) {

    val cfg = LwjglApplicationConfiguration()
    cfg.title = "2048 8Bit"
    cfg.width = 480
    cfg.height = 800

    LwjglApplication(MainGame(), cfg)

}