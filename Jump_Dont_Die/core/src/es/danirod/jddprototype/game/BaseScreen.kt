package es.danirod.jddprototype.game

import com.badlogic.gdx.Screen

/**
 * This is the default screen. Every screen overrides this screen. This screen has two purposes.
 * One is to override every method so that they are not required by other screens. The second
 * is to provide some common boilerplate code for every screen.
 */
abstract class BaseScreen(
    /**
     * Game instance. By making this protected and getting this value from the constructor,
     * every screen can be connected to the game, because every screen can access the game
     * instance.
     */
    protected var game: MainGame?
) : Screen {
    override fun show() {
        // This method is invoked when a screen is displayed.
    }

    override fun render(delta: Float) {
        // This method is invoked when a screen has to be rendered in a frame.
        // delta is the amount of seconds (order of 0.01 or so) between this and last frame.
    }

    override fun resize(width: Int, height: Int) {
        // This method is invoked when the game is resized (desktop).
    }

    override fun pause() {
        // This method is invoked when the game is paused.
    }

    override fun resume() {
        // This method is invoked when the game is resumed.
    }

    override fun hide() {
        // This method is invoked when the screen is no more displayed.
    }

    override fun dispose() {
        // This method is invoked when the game closes.
    }
}
