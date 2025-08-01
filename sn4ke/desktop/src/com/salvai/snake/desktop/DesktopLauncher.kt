package com.salvai.snake.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.salvai.snake.SnakeIt

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    Lwjgl3Application(SnakeIt(), config)
}
