package com.nopalsoft.dragracer.game_objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.Settings

class Coin(x: Float, y: Float) : Actor() {

    val bounds = Rectangle()
    private var moveAction: MoveToAction
    private var state = 0
    private var isSuperSpeed = false
    private var renders = ShapeRenderer()

    init {

        // I subtract less 5 so that the bounds are not so big: See draw method.
        width = 10f
        height = 32f
        setPosition(x - width / 2f, y)

        addAction(Actions.forever(Actions.rotateBy(360f, 1f)))

        moveAction = MoveToAction()
        moveAction.setPosition(getX(), -height)
        moveAction.duration = 5f
        addAction(moveAction)

        state = STATE_NORMAL

    }

    override fun act(delta: Float) {
        super.act(delta)
        updateBounds()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            Assets.coin, x, y, width / 2f,
            height / 2f, width, height, 1f, 1f, rotation
        )

        if (Settings.DRAW_DEBUG_LINES) {
            batch.end()
            renders.projectionMatrix = batch.projectionMatrix
            renders.begin(ShapeType.Line)
            renders.rect(bounds.x, bounds.y, bounds.width, bounds.height)
            renders.end()
            batch.begin()
        }
    }

    private fun updateBounds() {
        bounds.set(x, y, width, height)
    }

    fun setSpeed() {
        if (isSuperSpeed.not()) {
            isSuperSpeed = true
            moveAction.reset()
            moveAction.duration = 1f
            addAction(moveAction)
        }
    }

    companion object {
        private const val STATE_NORMAL = 0
    }
}