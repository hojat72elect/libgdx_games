package com.gamestudio24.martianrun.actors

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.Actor
import com.gamestudio24.martianrun.box2d.UserData
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager.Companion.instance

abstract class GameActor(@JvmField protected var body: Body) : Actor() {
    @JvmField
    protected var userData: UserData

    @JvmField
    protected var screenRectangle: Rectangle

    init {
        this.userData = body.userData as UserData
        screenRectangle = Rectangle()
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (instance.gameState == GameState.PAUSED) {
            return
        }

        if (body.userData != null) {
            updateRectangle()
        } else {
            // This means the world destroyed the body (enemy or runner went out of bounds)
            remove()
        }
    }

    abstract fun getUserData(): UserData?

    private fun updateRectangle() {
        screenRectangle.x = transformToScreen(body.getPosition().x - userData.width / 2)
        screenRectangle.y = transformToScreen(body.getPosition().y - userData.height / 2)
        screenRectangle.width = transformToScreen(userData.width)
        screenRectangle.height = transformToScreen(userData.height)
    }

    protected fun transformToScreen(n: Float): Float {
        return Constants.WORLD_TO_SCREEN * n
    }
}
