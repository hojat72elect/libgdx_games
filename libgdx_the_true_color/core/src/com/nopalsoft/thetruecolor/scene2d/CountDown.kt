package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.game.GameScreen
import com.nopalsoft.thetruecolor.screens.BaseScreen

class CountDown(gameScreen: GameScreen) : Group() {

    private val labelText = Label(Assets.languages.get("verdaderoFalse"), Label.LabelStyle(Assets.fontSmall, Color.BLACK))
    private val imageOne = Image(Assets.one)
    private val imageTwo = Image(Assets.two)
    private val imageThree = Image(Assets.three)
    private val timeByNumber = 1.25F

    init {
        setBounds(0f, 0f, BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT)
        labelText.setFontScale(1.2f)
        labelText.setPosition(width / 2f - labelText.width * labelText.fontScaleX / 2f, 300f)

        imageOne.setPosition(width / 2f - imageOne.width / 2f, 500f)
        imageTwo.setPosition(width / 2f - imageTwo.width / 2f, 500f)
        imageThree.setPosition(width / 2f - imageThree.width / 2f, 500f)


        val runAfterThree = Runnable {
            imageThree.remove()
            addActor(imageTwo)
        }
        imageThree.addAction(Actions.sequence(Actions.fadeOut(timeByNumber), Actions.run(runAfterThree)))

        val runAfterTwo = Runnable {
            imageTwo.remove()
            addActor(imageOne)
        }
        imageTwo.addAction(Actions.sequence(Actions.fadeOut(timeByNumber), Actions.run(runAfterTwo)))

        val runAfterOne = Runnable {
            imageOne.remove()
            gameScreen.setRunning()
            remove()
        }
        imageOne.addAction(Actions.sequence(Actions.fadeOut(timeByNumber),Actions.run(runAfterOne)))

        addActor(imageThree)
        addActor(labelText)
    }

}