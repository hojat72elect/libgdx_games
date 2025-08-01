package com.nopalsoft.impossibledial.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.impossibledial.Assets
import com.nopalsoft.impossibledial.game.GameScreen
import com.nopalsoft.impossibledial.screens.Screens

class CountDown(screen: GameScreen) : Group() {
    var one: Image
    var two: Image
    var three: Image
    var gameScreen: GameScreen

    var tiempoPorNumero: Float = 1.15f

    init {
        setBounds(0f, 0f, Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        gameScreen = screen

        val positionY = 350f

        one = Image(Assets.one)
        one.setPosition(getWidth() / 2f - one.getWidth() / 2f, positionY)

        two = Image(Assets.two)
        two.setPosition(getWidth() / 2f - two.getWidth() / 2f, positionY)

        three = Image(Assets.three)
        three.setPosition(getWidth() / 2f - three.getWidth() / 2f, positionY)

        val runAfterThree = Runnable {
            three.remove()
            addActor(two)
        }
        three.addAction(Actions.sequence(Actions.fadeOut(tiempoPorNumero), Actions.run(runAfterThree)))

        val runAfterTwo = Runnable {
            two.remove()
            addActor(one)
        }
        two.addAction(Actions.sequence(Actions.fadeOut(tiempoPorNumero), Actions.run(runAfterTwo)))

        val runAfterOne = Runnable {
            one.remove()
            gameScreen.setRunning()
            remove()
        }
        one.addAction(Actions.sequence(Actions.fadeOut(tiempoPorNumero), Actions.run(runAfterOne)))

        addActor(three)
    }
}
