package es.danirod.jddprototype.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport

/**
 * This screen is executed when you start the screen and it is used to load things in the
 * asset manager. There are a lot of ways for loading things in an AssetManager. This is a
 * simple example that displays a label saying "LOADING" as well as the percentage of the
 * game that has been loaded.
 */
class LoadingScreen(game: MainGame?) : BaseScreen(game) {
    /*
     * Labels are also Actors. We will use Scene2D UI.
     */
    // Set up the stage and the skin. See GameOverScreen for more comments on this.
    private val stage: Stage = Stage(FitViewport(640f, 360f))

    /**
     * This is the skin file (see GameOverScreen for more information on this).
     */
    private val skin: Skin = Skin(Gdx.files.internal("skin/uiskin.json"))

    /**
     * This is the label that we use to display some text on the screen.
     */

    // Create some loading text using this skin file and position it on screen.
    private val loading: Label = Label("Loading...", skin)

    init {
        loading.setPosition(320 - loading.getWidth() / 2, 180 - loading.getHeight() / 2)
        stage.addActor(loading)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // This is important. To load an asset from the asset manager you call update() method.
        // this method will return true if it has finished loading. Else it will return false.
        // You usually want to do the code that changes to the main menu screen if it has finished
        // loading, else you update the screen to not make the user angry and keep loading.
        if (game!!.getManager().update()) {
            // I'll notify the game that all the assets are loaded so that it can load the
            // remaining set of screens and enter the main menu. This avoids Exceptions because
            // screens cannot be loaded until all the assets are loaded.
            game?.finishLoading()
        } else {
            // getProgress() returns the progress of the load in a range of [0,1]. We multiply
            // this progress per * 100 so that we can display it as a percentage.
            val progress = (game!!.getManager().getProgress() * 100).toInt()
            loading.setText("Loading... $progress%")
        }

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        skin.dispose()
    }
}
