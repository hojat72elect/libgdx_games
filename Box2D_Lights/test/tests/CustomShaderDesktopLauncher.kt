package tests

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration


fun main() {
    val config = LwjglApplicationConfiguration()
    config.title = "box2d lights test"
    config.width = 800
    config.height = 480
    config.samples = 4
    config.depth = 0
    config.vSyncEnabled = true

    config.fullscreen = false
    LwjglApplication(Box2dLightCustomShaderTest(), config)
}

