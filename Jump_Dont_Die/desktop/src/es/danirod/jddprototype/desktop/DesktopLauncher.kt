package es.danirod.jddprototype.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import es.danirod.jddprototype.game.MainGame

fun main() {
    val config = LwjglApplicationConfiguration()
    config.width = 640
    config.height = 360
    LwjglApplication(MainGame(), config)
}
