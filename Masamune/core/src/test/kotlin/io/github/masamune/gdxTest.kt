package io.github.masamune

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ray3k.stripe.FreeTypeSkin
import ktx.assets.toClasspathFile
import ktx.math.vec2

fun gdxTest(title: String, testListener: ApplicationListener, windowSize: Vector2 = vec2(1280f, 960f)) {
    Lwjgl3Application(testListener, Lwjgl3ApplicationConfiguration().apply {
        setTitle(title)
        setWindowedMode(windowSize.x.toInt(), windowSize.y.toInt())
    })
}

fun testSkin(): Skin {
    val atlas = TextureAtlas("ui/skin.atlas".toClasspathFile())
    return FreeTypeSkin("ui/skin.json".toClasspathFile(), atlas).apply {
        getAll(BitmapFont::class.java).values().forEach { (it as BitmapFont).data.markupEnabled = true }
    }
}
