package com.nopalsoft.sokoban.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.sokoban.MainSokoban

fun main(args: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle("Sokoban")
    config.setWindowedMode(800, 480)
    Lwjgl3Application(MainSokoban(), config)
}