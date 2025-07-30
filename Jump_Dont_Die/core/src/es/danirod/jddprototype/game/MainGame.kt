package es.danirod.jddprototype.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture

/**
 * This is our main game. This is the class that we pass to the Application in Android launcher
 * as well as in desktop launcher. Because we want to create a screen-based game, we use Game
 * class, which has methods for creating multiple screens.
 */
class MainGame : Game() {
    /**
     * These are the screens that we use in this game. I invite you to use a better system than
     * just public variables. For instance, you could create an ArrayList or maybe use some
     * structure such as a map where you can associate a number or a string to a screen.
     */
    var loadingScreen: BaseScreen? = null

    @JvmField
    var menuScreen: BaseScreen? = null

    @JvmField
    var gameScreen: BaseScreen? = null

    @JvmField
    var gameOverScreen: BaseScreen? = null
    var creditsScreen: BaseScreen? = null

    /**
     * This is the asset manager we use to centralize the assets.
     */
    private var manager: AssetManager? = null

    override fun create() {
        // Initialize the asset manager. We add every aset to the manager so that it can be loaded
        // inside the LoadingScreen screen. Remember to put the name of the asset in the first
        // argument, then the type of the asset in the second argument.
        manager = AssetManager()
        manager!!.load("floor.png", Texture::class.java)
        manager!!.load("gameover.png", Texture::class.java)
        manager!!.load("overfloor.png", Texture::class.java)
        manager!!.load("logo.png", Texture::class.java)
        manager!!.load("spike.png", Texture::class.java)
        manager!!.load("player.png", Texture::class.java)
        manager!!.load("audio/die.ogg", Sound::class.java)
        manager!!.load("audio/jump.ogg", Sound::class.java)
        manager!!.load("audio/song.ogg", Music::class.java)

        // Enter the loading screen to load the assets.
        loadingScreen = LoadingScreen(this)
        setScreen(loadingScreen)
    }

    /**
     * This method is invoked by LoadingScreen when all the assets are loaded. Use this method
     * as a second-step loader. You can load the rest of the screens here and jump to the main
     * screen now that everything is loaded.
     */
    fun finishLoading() {
        menuScreen = MenuScreen(this)
        gameScreen = GameScreen(this)
        gameOverScreen = GameOverScreen(this)
        creditsScreen = CreditsScreen(this)
        setScreen(menuScreen)
    }

    fun getManager(): AssetManager {
        return manager!!
    }
}
