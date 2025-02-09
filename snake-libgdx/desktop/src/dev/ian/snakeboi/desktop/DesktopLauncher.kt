package dev.ian.snakeboi.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import dev.ian.snakeboi.SnakeBoi

fun main() {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(SnakeBoi(), config)
}