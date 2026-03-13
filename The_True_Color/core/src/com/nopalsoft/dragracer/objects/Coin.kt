package com.nopalsoft.dragracer.objects

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

    private var state: Int

    val bounds: Rectangle = Rectangle()
    private val moveAction: MoveToAction
    private var isSuperSpeed: Boolean = false

    override fun act(delta: Float) {
        super.act(delta)
        updateBounds()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            Assets.coin!!, x, y, width / 2f,
            height / 2f, width, height, 1f, 1f, rotation
        )

        if (Settings.drawDebugLines) {
            batch.end()
            renders.projectionMatrix = batch.getProjectionMatrix()
            renders.begin(ShapeType.Line)
            renders.rect(bounds.x, bounds.y, bounds.width, bounds.height)
            renders.end()
            batch.begin()
        }
    }

    private var renders: ShapeRenderer = ShapeRenderer()

    init {
        // I subtract minus 5 so that the bounds are not so large: See draw method

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

    private fun updateBounds() {
        bounds[x, y, width] = height
    }

    fun setSpeed() {
        if (!isSuperSpeed) {
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
