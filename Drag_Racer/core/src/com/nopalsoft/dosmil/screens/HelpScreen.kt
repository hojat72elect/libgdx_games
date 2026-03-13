package com.nopalsoft.dosmil.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.MainGame

class HelpScreen(game: MainGame) : Screens(game) {
    private var labelTextHelp1 = Label(Assets.languagesBundle!!["helpTop"], Assets.labelStyleSmall)
    private var labelTextHelp2: Label = Label(Assets.languagesBundle!!["helpBottom"], Assets.labelStyleSmall)
    private var imagePuzzle: Image = Image(Assets.puzzleSolvedAtlasRegion)

    private var buttonBack: Button = Button(Assets.buttonBack)

    init {
        labelTextHelp1.wrap = true
        labelTextHelp1.width = (SCREEN_WIDTH - 20).toFloat()
        labelTextHelp1.setAlignment(Align.center)
        labelTextHelp1.setPosition(SCREEN_WIDTH / 2f - labelTextHelp1.width / 2f, 660f)
        labelTextHelp1.setScale(1.2f)

        imagePuzzle.setSize(350f, 350f)
        imagePuzzle.setPosition(SCREEN_WIDTH / 2f - imagePuzzle.width / 2f, 290f)


        labelTextHelp2.wrap = true
        labelTextHelp2.width = (SCREEN_WIDTH - 20).toFloat()
        labelTextHelp2.setAlignment(Align.center)
        labelTextHelp2.setPosition(SCREEN_WIDTH / 2f - labelTextHelp2.width / 2f, 200f)
        labelTextHelp2.setScale(1.2f)

        buttonBack.setSize(60f, 60f)
        buttonBack.setPosition(SCREEN_WIDTH / 2f - 30, 80f)
        addPressEffect(buttonBack)
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })
        stage!!.addActor(labelTextHelp1)
        stage!!.addActor(labelTextHelp2)
        stage!!.addActor(buttonBack)
        stage!!.addActor(imagePuzzle)
    }

    override fun draw(delta: Float) {
        batch!!.begin()
        batch!!.draw(Assets.backgroundAtlasRegion!!, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batch!!.end()
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game)
        }
        return super.keyDown(keycode)
    }
}
