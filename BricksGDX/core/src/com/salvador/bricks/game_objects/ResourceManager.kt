package com.salvador.bricks.game_objects

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter

object ResourceManager {

    lateinit var assetManager: AssetManager

    @JvmStatic
    fun loadAssets() {

        assetManager = AssetManager()
        //Background
        assetManager.load("background.png", Texture::class.java)

        //Bricks
        assetManager.load("brick1.png", Texture::class.java)
        assetManager.load("brick2.png", Texture::class.java)
        assetManager.load("brick3.png", Texture::class.java)
        assetManager.load("brick4.png", Texture::class.java)
        assetManager.load("brick5.png", Texture::class.java)
        assetManager.load("brick6.png", Texture::class.java)

        //Power Up's
        assetManager.load("powerupBlue.png", Texture::class.java)
        assetManager.load("powerupGreen.png", Texture::class.java)
        assetManager.load("powerupRed.png", Texture::class.java)
        assetManager.load("powerupYellow.png", Texture::class.java)

        //Buttons
        assetManager.load("button.png", Texture::class.java)
        assetManager.load("btn_exit.png", Texture::class.java)
        assetManager.load("btn_info.png", Texture::class.java)
        assetManager.load("btn_reset.png", Texture::class.java)

        //Paddle
        assetManager.load("paddle.png", Texture::class.java)

        //Balls
        assetManager.load("ball.png", Texture::class.java)
        assetManager.load("ball_fire.png", Texture::class.java)

        //Sounds
        assetManager.load("sound.ogg", Sound::class.java)
        assetManager.load("music.mp3", Music::class.java)

        loadFont("font.ttf", "font.ttf", 40)
        loadFont("font.ttf", "font20.ttf", 20)

        assetManager.finishLoading()
    }

    @JvmStatic
    fun disposeAssets() {
        assetManager.dispose()
    }

    @JvmStatic
    fun getTexture(name: String): Texture = assetManager.get(name, Texture::class.java)

    fun getFont(name: String): BitmapFont = assetManager.get(name, BitmapFont::class.java)

    fun getSound(name: String): Sound = assetManager.get(name, Sound::class.java)

    fun getMusic(name: String): Music = assetManager.get(name, Music::class.java)

    fun loadFont(name: String, finalName: String, size: Int) {
        val resolver = InternalFileHandleResolver()
        assetManager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assetManager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))

        val params = FreeTypeFontLoaderParameter()
        params.fontFileName = name // path of .ttf file where that exists
        params.fontParameters.size = size
        assetManager.load(finalName, BitmapFont::class.java, params) // fileName with extension, sameName will be used to get from manager
    }
}
