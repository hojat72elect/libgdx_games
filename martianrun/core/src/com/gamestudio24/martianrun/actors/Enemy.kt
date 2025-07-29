package com.gamestudio24.martianrun.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.physics.box2d.Body
import com.gamestudio24.martianrun.box2d.EnemyUserData
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.AssetsManager.getAnimation
import com.gamestudio24.martianrun.utils.GameManager.Companion.instance

class Enemy(body: Body) : GameActor(body) {
    private val animation: Animation?
    private var stateTime = 0f

    init {
        animation = getAnimation(getUserData()!!.animationAssetId)
    }

    override fun getUserData(): EnemyUserData? {
        return userData as EnemyUserData
    }

    override fun act(delta: Float) {
        super.act(delta)
        body.linearVelocity = getUserData()!!.linearVelocity
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        if (instance.gameState != GameState.PAUSED) {
            stateTime += Gdx.graphics.deltaTime
        }

        batch.draw(
            animation!!.getKeyFrame(stateTime, true), (screenRectangle.x - (screenRectangle.width * 0.1f)),
            screenRectangle.y, screenRectangle.width * 1.2f, screenRectangle.height * 1.1f
        )
    }
}
