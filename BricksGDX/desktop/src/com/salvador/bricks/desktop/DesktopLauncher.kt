package com.salvador.bricks.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.salvador.bricks.BrickBreaker

fun main() {
    val gameConfig = LwjglApplicationConfiguration()
    gameConfig.title = "Arkanoid"
    gameConfig.height = 800
    gameConfig.width = 450
    LwjglApplication(BrickBreaker(), gameConfig)
}