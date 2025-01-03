package com.nopalsoft.dosmil.screens

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.MainGame

class HelpScreen(game: MainGame) : Screens(game) {

    private val labelTextHelp1 = Label(Assets.languages?.get("helpTop"), Assets.labelStyleSmall)
    private val labelTextHelp2 = Label(Assets.languages?.get("helpBottom"), Assets.labelStyleSmall)
    private val imagePuzzle = Image(Assets.puzzleSolved)
    private val buttonBack = Button(Assets.buttonBack)

    init {

        // first set up labelTextHelp1 label
        labelTextHelp1.wrap = true
        labelTextHelp1.width = (SCREEN_WIDTH - 20).toFloat()
        labelTextHelp1.setAlignment(Align.center)
        labelTextHelp1.setPosition(SCREEN_WIDTH / 2f - labelTextHelp1.width / 2f, 660f)
        labelTextHelp1.setScale(1.2f)

        // set up imagePuzzle image
        imagePuzzle.setSize(350f, 350f)
        imagePuzzle.setPosition(SCREEN_WIDTH / 2 - imagePuzzle.width / 2f, 290f)

        // set up labelTextHelp2 label
        labelTextHelp2.wrap = true
        labelTextHelp2.width = (SCREEN_WIDTH - 20).toFloat()
        labelTextHelp2.setAlignment(Align.center)
        labelTextHelp2.setPosition(SCREEN_WIDTH / 2f - labelTextHelp2.width / 2f, 200f)
        labelTextHelp2.setScale(1.2f)

        // set up buttonBack button
        buttonBack.setSize(60f, 60f)
        buttonBack.setPosition((SCREEN_WIDTH / 2 - 30).toFloat(), 80f)
        addPressEffect(buttonBack)
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        // Add all of them to our screen
        with(stage) {
            this?.addActor(labelTextHelp1)
            this?.addActor(labelTextHelp2)
            this?.addActor(buttonBack)
            this?.addActor(imagePuzzle)
        }

    }

    override fun draw(delta: Float) {
        with(batcher) {
            this?.begin()
            this?.draw(Assets.background, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
            this?.end()
        }

    }

    override fun update(delta: Float) {}

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game)
        }
        return super.keyDown(keycode)
    }
}