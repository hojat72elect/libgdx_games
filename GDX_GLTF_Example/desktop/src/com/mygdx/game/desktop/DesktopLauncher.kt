package com.mygdx.game.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.mygdx.game.MyGdxGame

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(1280, 720)
    config.setBackBufferConfig(8, 8, 8, 8, 24, 0, 8)
    Lwjgl3Application(MyGdxGame(), config)
}