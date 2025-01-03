package com.nopalsoft.dragracer.scene2D

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.screens.BaseScreen

/**
 * This is part of the HUD system of the game; which teaches the gamer how to do turbo in mobile phone.
 */
class SwipeVerticalTutorial(stage: Stage) {

    private val labelSwipeToMove = Label(
        "Swipe for turbo!",
        Assets.labelStyleLarge
    )
    private val swipeHand = Image(Assets.swipeHand)


    init {

        labelSwipeToMove.setPosition(
            BaseScreen.SCREEN_WIDTH / 2f - (labelSwipeToMove.width / 2f),
            600f
        )

        labelSwipeToMove.color.a = 0f
        swipeHand.setPosition(BaseScreen.SCREEN_WIDTH / 2f, 400f)
        swipeHand.setOrigin(swipeHand.width / 2f, swipeHand.height / 2f)
        swipeHand.setScale(1.2f)

        swipeHand.addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, .25f),
                Actions.run { swipeHand.drawable = Assets.swipeHandDown },
                Actions.moveTo(swipeHand.x, 500f, .65f),  //
                Actions.run { swipeHand.drawable = Assets.swipeHand },
                Actions.scaleTo(1.1f, 1.1f, .125f),  //
                Actions.run { swipeHand.remove() }
            ))

        labelSwipeToMove.addAction(
            Actions.sequence(
                Actions.fadeIn(1f),
                Actions.run {
                    labelSwipeToMove.remove()
                    stage.addActor(swipeHand)
                })
        )


        stage.addActor(labelSwipeToMove)

    }


}