package com.bitfire.uracer

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio
import com.bitfire.uracer.configuration.BootConfig
import com.bitfire.uracer.configuration.BootConfig.BootConfigFlag
import com.bitfire.uracer.configuration.Config
import com.bitfire.uracer.utils.CommandLine
import org.lwjgl.opengl.Display
import java.util.Calendar

private fun createLwjglConfig(boot: BootConfig): LwjglApplicationConfiguration {

    val config = LwjglApplicationConfiguration()
    config.addIcon("data/base/icon.png", FileType.Internal)
    config.title = "${URacer.Name} (${URacer.versionInfo})"
    config.resizable = false
    config.samples = 0
    config.audioDeviceSimultaneousSources = 32

    if (Config.Debug.PauseDisabled) {
        config.backgroundFPS = 0
        config.foregroundFPS = 0
    } else {
        config.backgroundFPS = 60
        config.foregroundFPS = 60
    }

    config.width = boot.getInt(BootConfigFlag.WIDTH)
    config.height = boot.getInt(BootConfigFlag.HEIGHT)
    config.vSyncEnabled = boot.getBoolean(BootConfigFlag.VSYNC)
    config.fullscreen = boot.getBoolean(BootConfigFlag.FULLSCREEN)

    return config
}

fun main(argv: Array<String>) {
    val year = Calendar.getInstance().get(Calendar.YEAR)
    println("${URacer.Name} (${URacer.versionInfo})\nCopyright (c) 2011-${year} Manuel Bua.\n\n")

    // load boot configuration
    val boot = BootConfig()

    // override boot config by command line flags, if any
    if (argv.isNotEmpty()) {
        if (!CommandLine.applyLaunchFlags(argv, boot)) {
            return
        }
    } else {
        println("Try --help for a list of valid command-line switches.\n")
    }

    System.setProperty("org.lwjgl.opengl.Window.undecorated", boot.getBoolean(BootConfigFlag.UNDECORATED).toString())
    val config = createLwjglConfig(boot)

    println("Resolution set at ${config.width}x${config.height} (x=${boot.getWindowX()}, y=${boot.getWindowY()})")
    println("Vertical sync: ${if (config.vSyncEnabled) "Yes" else "No"}")
    println("Fullscreen: ${if (config.fullscreen) "Yes" else "No"}")
    println("Window decorations: ${if (boot.getBoolean(BootConfigFlag.UNDECORATED)) "No" else "Yes"}")

    val uracer = URacer(boot)
    val app = LwjglApplication(uracer, config)
    val finalizer = URacerDesktopFinalizer(boot, app.getAudio() as OpenALAudio)
    uracer.setFinalizer(finalizer)

    if (!config.fullscreen) Display.setLocation(boot.getWindowX(), boot.getWindowY())

}
