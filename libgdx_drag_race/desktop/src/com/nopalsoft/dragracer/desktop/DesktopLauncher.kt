package com.nopalsoft.dragracer.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.nopalsoft.dragracer.MainStreet


/**
 * warning: Don't remove args from this function.
 */
fun main(args: Array<String>) {

    val config = LwjglApplicationConfiguration()
    with(config) {
        title = "StreetSwipinRace"
        width = 480
        height = 800
    }

    LwjglApplication(MainStreet(), config)
}


