package com.gamestudio24.martianrun.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.gamestudio24.martianrun.MartianRun
import com.gamestudio24.martianrun.utils.Constants


fun main() {
    val config = LwjglApplicationConfiguration()
    config.width = Constants.APP_WIDTH
    config.height = Constants.APP_HEIGHT
    LwjglApplication(MartianRun(), config)
}

