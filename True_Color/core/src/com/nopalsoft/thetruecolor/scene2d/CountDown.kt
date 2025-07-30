package com.nopalsoft.thetruecolor.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.nopalsoft.thetruecolor.Assets
import com.nopalsoft.thetruecolor.game.GameScreen
import com.nopalsoft.thetruecolor.screens.BaseScreen

class CountDown(screen: GameScreen) : Group() {
    var imageOne: Image
    var imageTwo: Image
    var imageThree: Image
    var gameScreen: GameScreen
    var labelText: Label

    var countDownDuration: Float = 1.25f

    init {
        setBounds(0f, 0f, BaseScreen.SCREEN_WIDTH.toFloat(), BaseScreen.SCREEN_HEIGHT.toFloat())
        gameScreen = screen

        labelText = Label(Assets.languagesBundle!!.get("verdaderoFalse"), LabelStyle(Assets.fontSmall, Color.BLACK))
        labelText.setFontScale(1.2f)
        labelText.setPosition(getWidth() / 2f - labelText.getWidth() * labelText.fontScaleX / 2f, 300f)

        imageOne = Image(Assets.oneDrawable)
        imageOne.setPosition(getWidth() / 2f - imageOne.getWidth() / 2f, 500f)

        imageTwo = Image(Assets.twoDrawable)
        imageTwo.setPosition(getWidth() / 2f - imageTwo.getWidth() / 2f, 500f)

        imageThree = Image(Assets.threeDrawable)
        imageThree.setPosition(getWidth() / 2f - imageThree.getWidth() / 2f, 500f)

        val runAfterThree = Runnable {
            imageThree.remove()
            addActor(imageTwo)
        }
        imageThree.addAction(Actions.sequence(Actions.fadeOut(countDownDuration), Actions.run(runAfterThree)))

        val runAfterTwo = Runnable {
            imageTwo.remove()
            addActor(imageOne)
        }
        imageTwo.addAction(Actions.sequence(Actions.fadeOut(countDownDuration), Actions.run(runAfterTwo)))

        val runAfterOne = Runnable {
            imageOne.remove()
            gameScreen.setRunning()
            remove()
        }
        imageOne.addAction(Actions.sequence(Actions.fadeOut(countDownDuration), Actions.run(runAfterOne)))

        addActor(imageThree)
        addActor(labelText)
    }
}
