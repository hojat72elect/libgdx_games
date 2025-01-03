package com.nopalsoft.dragracer.scene2D

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.screens.BaseScreen

class SwipeHorizontalTutorial : Group() {

    init {
        setSize(BaseScreen.SCREEN_WIDTH.toFloat(), 195f)
        setPosition(BaseScreen.SCREEN_WIDTH / 2f - width / 2f, 0f)

        val swipeArrows = Image(Assets.swipeArrows)
        swipeArrows.setPosition(
            width / 2f - swipeArrows.width / 2f,
            160f
        )

        val labelSwipeToMove = Label(
            "Swipe to move",
            Assets.labelStyleLarge
        )
        labelSwipeToMove.setPosition(
            width / 2f - (labelSwipeToMove.width
                    / 2f), 100f
        )
        labelSwipeToMove.color.a = 0f
        labelSwipeToMove.addAction(Actions.fadeIn(1f))


        val swipeHand = Image(Assets.swipeHand)
        swipeHand.setPosition(180f, 10f)
        swipeHand.setOrigin(
            swipeHand.width / 2f,
            swipeHand.height / 2f
        )
        swipeHand.setScale(1.2f)
        swipeHand.addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, .25f),
                Actions.run { swipeHand.drawable = Assets.swipeHandDown },
                Actions.moveTo(250f, 10f, .5f),
                Actions.run { swipeHand.drawable = Assets.swipeHand },
                Actions.scaleTo(1.1f, 1.1f, .15f),
                Actions.run {
                    swipeHand.remove()
                    addActor(labelSwipeToMove)
                }
            ))

        addActor(swipeHand)
        addActor(swipeArrows)

    }
}