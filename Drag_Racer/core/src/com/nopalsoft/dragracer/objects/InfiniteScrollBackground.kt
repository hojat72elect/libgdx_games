package com.nopalsoft.dragracer.objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.nopalsoft.dragracer.Assets

class InfiniteScrollBackground(width: Float, height: Float) : Actor() {
    private var moveAction: MoveToAction

    init {
        setWidth(width)
        setHeight(height)
        setPosition(0f, height)

        moveAction = MoveToAction()
        moveAction.setPosition(0f, 0f)
        moveAction.duration = 1.75f

        addAction(Actions.forever(Actions.sequence(moveAction, Actions.moveTo(0f, height))))
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.draw(
            Assets.street!!, x, y - height, width,
            height * 2
        )
    }

    fun setSpeed() {
        moveAction.duration = .3f
    }

    fun stopSpeed() {
        moveAction.duration = 1.75f
    }
}
