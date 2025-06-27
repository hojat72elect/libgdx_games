@file:JvmName("TeaVMLauncher")

package io.github.masamune.teavm

import com.github.xpenatan.gdx.backends.teavm.TeaApplication
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration
import com.github.xpenatan.gdx.backends.teavm.TeaAssetPreloadListener
import io.github.masamune.Masamune

/** Launches the TeaVM/HTML application. */
fun main() {
    val config = TeaApplicationConfiguration("canvas").apply {
        //// If width and height are each greater than 0, then the app will use a fixed size.
        //config.width = 640;
        //config.height = 480;
        //// If width and height are both 0, then the app will use all available space.
        //config.width = 0;
        //config.height = 0;
        //// If width and height are both -1, then the app will fill the canvas size.
        width = 0
        height = 0
        preloadListener = TeaAssetPreloadListener { assetLoader -> assetLoader.loadScript("freetype.js") }
    }
    TeaApplication(Masamune(webLauncher = true), config)
}
