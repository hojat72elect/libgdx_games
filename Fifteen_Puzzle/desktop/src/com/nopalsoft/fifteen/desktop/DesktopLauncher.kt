package com.nopalsoft.fifteen.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.fifteen.MainFifteen

fun main() {
    val cfg = LwjglApplicationConfiguration()
    cfg.title = "FifteenPuzzle"
    cfg.width = 480
    cfg.height = 800

    LwjglApplication(
        MainFifteen(), cfg
    )
}