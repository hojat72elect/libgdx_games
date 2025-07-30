package es.danirod.jddprototype.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.FitViewport

/**
 * Extra screen to show the credits for the work.
 */
class CreditsScreen(game: MainGame) : BaseScreen(game) {

    /**
     * The stage where all the buttons are added.
     */
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

        // The back button you use to jump to the game screen.
        val back = TextButton("Back", skin)

        // The label with all the information.
        val credits = Label(
            "Jump Don't Die v1.0.2\n" +
                    "Copyright (C) 2015-2016 Dani Rodriguez\n" +
                    "This game is GNU GPL. Get the code at github.com/danirod/JumpDontDie\n\n" +
                    "Music: \"Long Time Coming\" Kevin MacLeod (incompetech.com)\n" +
                    "Licensed under Creative Commons: By Attribution 3.0", skin
        )

        // Add capture listeners. Capture listeners have one method, changed, that is executed
        // when the button is pressed or when the user interacts somehow with the widget. They are
        // cool because they let you execute some code when you press them.
        back.addCaptureListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                // Take me to the game screen!
                game.setScreen(game.menuScreen)
            }
        })

        // Now I position things on screen. Sorry for making this the hardest part of this screen.
        // I position things on the screen so that they look centered. This is why I make the
        // buttons the same size.
        credits.setPosition(20f, 340 - credits.getHeight())
        back.setSize(200f, 80f)
        back.setPosition(40f, 50f)

        // Do not forget to add actors to the stage or we wouldn't see anything.
        stage.addActor(back)
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
