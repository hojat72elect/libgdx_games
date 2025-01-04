package dev.lonami.klooni.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import dev.lonami.klooni.Klooni

fun main(args: Array<String>) {
    with(LwjglApplicationConfiguration()) {
        title = "Klooni 1010!"
        width = Klooni.GAME_WIDTH
        height = Klooni.GAME_HEIGHT
        addIcon("ic_launcher/icon128.png", Files.FileType.Internal)
        addIcon("ic_launcher/icon32.png", Files.FileType.Internal)
        addIcon("ic_launcher/icon16.png", Files.FileType.Internal)
        LwjglApplication(Klooni(null), this)
    }
}