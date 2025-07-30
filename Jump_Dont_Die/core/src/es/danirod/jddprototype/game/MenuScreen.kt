package es.danirod.jddprototype.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.FitViewport

/**
 * This is the screen that you see when you enter the game. It has a button for playing the game.
 * When you press this button, you go to the game screen so that you can start to play. This
 * screen was done by copying the code from GameOverScreen. All the cool comments have been
 * copy-pasted.
 */
class MenuScreen(game: MainGame) : BaseScreen(game) {
    /**
     * The stage where all the buttons are added.
     */
    // Create a new stage, as usual.
    private val stage: Stage = Stage(FitViewport(640f, 360f))

    /**
     * The skin that we use to set the style of the buttons.
     */

    // Load the skin file. The skin file contains information about the skins. It can be
    // passed to any widget in Scene2D UI to set the style. It just works, amazing.
    private val skin: Skin = Skin(Gdx.files.internal("skin/uiskin.json"))

    init {

        // For instance, here you see that I create a new button by telling the label of the
        // button as well as the skin file. The background image for the button is in the skin
        // file.

        /*
         * The play button you use to jump to the game screen.
         */
        val play = TextButton("Play", skin)
        val credits = TextButton("Credits", skin)

        // Also, create an image. Images are actors that only display some texture. Useful if you
        // want to display a texture in a Scene2D based screen but you don't want to rewrite code.

        /*
         * The logo image you see on top of the screen.
         */
        val logo = Image(game.getManager().get("logo.png", Texture::class.java))

        // Add capture listeners. Capture listeners have one method, changed, that is executed
        // when the button is pressed or when the user interacts somehow with the widget. They are
        // cool because they let you execute some code when you press them.
        play.addCaptureListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                // Take me to the game screen!
                game.setScreen(game.gameScreen)
            }
        })

        credits.addCaptureListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.setScreen(game.creditsScreen)
            }
        })

        // Now I position things on screen. Sorry for making this the hardest part of this screen.
        // I position things on the screen so that they look centered. This is why I make the
        // buttons the same size.
        logo.setPosition(440 - logo.getWidth() / 2, 320 - logo.getHeight())
        play.setSize(200f, 80f)
        credits.setSize(200f, 80f)
        play.setPosition(40f, 140f)
        credits.setPosition(40f, 40f)

        // Do not forget to add actors to the stage or we wouldn't see anything.
        stage.addActor(play)
        stage.addActor(logo)
        stage.addActor(credits)
    }

    override fun show() {
        // Now this is important. If you want to be able to click the button, you have to make
        // the Input system handle input using this Stage. Stages are also InputProcessors. By
        // making the Stage the default input processor for this game, it is now possible to
        // click on buttons and even to type on input fields.
        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
        // When the screen is no more visible, you have to remember to unset the input processor.
        // Otherwise, input might act weird, because even if you aren't using this screen, you are
        // still using the stage for handling input.
        Gdx.input.inputProcessor = null
    }

    override fun dispose() {
        // Dispose assets.
        stage.dispose()
        skin.dispose()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
    }
}
