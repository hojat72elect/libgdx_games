package es.danirod.jddprototype.game;

import com.badlogic.gdx.Screen;

/**
 * This is the default screen. Every screen overrides this screen. This screen has two purposes.
 * One is to override every method so that they are not required by other screens. The second
 * is to provide some common boilerplate code for every screen.
 */
public abstract class BaseScreen implements Screen {

    /**
     * Game instance. By making this protected and getting this value from the constructor,
     * every screen can be connected to the game, because every screen can access the game
     * instance.
     */
    protected MainGame game;

    public BaseScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // This method is invoked when a screen is displayed.
    }

    @Override
    public void render(float delta) {
        // This method is invoked when a screen has to be rendered in a frame.
        // delta is the amount of seconds (order of 0.01 or so) between this and last frame.
    }

    @Override
    public void resize(int width, int height) {
        // This method is invoked when the game is resized (desktop).
    }

    @Override
    public void pause() {
        // This method is invoked when the game is paused.
    }

    @Override
    public void resume() {
        // This method is invoked when the game is resumed.
    }

    @Override
    public void hide() {
        // This method is invoked when the screen is no more displayed.
    }

    @Override
    public void dispose() {
        // This method is invoked when the game closes.
    }
}
