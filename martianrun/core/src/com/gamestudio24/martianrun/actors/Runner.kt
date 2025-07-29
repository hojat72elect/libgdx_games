package com.gamestudio24.martianrun.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Body
import com.gamestudio24.martianrun.box2d.RunnerUserData
import com.gamestudio24.martianrun.enums.Difficulty
import com.gamestudio24.martianrun.enums.GameState
import com.gamestudio24.martianrun.utils.AssetsManager.getAnimation
import com.gamestudio24.martianrun.utils.AssetsManager.getTextureRegion
import com.gamestudio24.martianrun.utils.AudioUtils
import com.gamestudio24.martianrun.utils.Constants
import com.gamestudio24.martianrun.utils.GameManager

class Runner(body: Body) : GameActor(body) {
    private val runningAnimation: Animation? = getAnimation(Constants.RUNNER_RUNNING_ASSETS_ID)
    private val jumpingTexture: TextureRegion? = getTextureRegion(Constants.RUNNER_JUMPING_ASSETS_ID)
    private val dodgingTexture: TextureRegion? = getTextureRegion(Constants.RUNNER_DODGING_ASSETS_ID)
    private val hitTexture: TextureRegion? = getTextureRegion(Constants.RUNNER_HIT_ASSETS_ID)
    private val jumpSound: Sound? = AudioUtils.getInstance().jumpSound
    private val hitSound: Sound? = AudioUtils.getInstance().hitSound
    var isDodging: Boolean = false
        private set
    private var jumping = false
    var isHit: Boolean = false
        private set
    private var stateTime = 0f
    var jumpCount: Int = 0
        private set

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        val x = screenRectangle.x - (screenRectangle.width * 0.1f)
        val y = screenRectangle.y
        val width = screenRectangle.width * 1.2f

        if (this.isDodging) {
            batch.draw(dodgingTexture, x, y + screenRectangle.height / 4, width, screenRectangle.height * 3 / 4)
        } else if (this.isHit) {
            // When he's hit we also want to apply rotation if the body has been rotated
            batch.draw(
                hitTexture, x, y, width * 0.5f, screenRectangle.height * 0.5f, width, screenRectangle.height, 1f,
                1f, Math.toDegrees(body.angle.toDouble()).toFloat()
            )
        } else if (jumping) {
            batch.draw(jumpingTexture, x, y, width, screenRectangle.height)
        } else {
            // Running
            if (GameManager.getInstance().gameState == GameState.RUNNING) {
                stateTime += Gdx.graphics.deltaTime
            }
            batch.draw(runningAnimation!!.getKeyFrame(stateTime, true), x, y, width, screenRectangle.height)
        }
    }

    override fun getUserData(): RunnerUserData? {
        return userData as RunnerUserData?
    }

    fun jump() {
        if (!(jumping || this.isDodging || this.isHit)) {
            body.applyLinearImpulse(getUserData()!!.jumpingLinearImpulse, body.getWorldCenter(), true)
            jumping = true
            AudioUtils.getInstance().playSound(jumpSound)
            jumpCount++
        }
    }

    fun landed() {
        jumping = false
    }

    fun dodge() {
        if (!(jumping || this.isHit)) {
            body.setTransform(getUserData()!!.dodgePosition, getUserData()!!.dodgeAngle)
            this.isDodging = true
        }
    }

    fun stopDodge() {
        this.isDodging = false
        // If the runner is hit don't force him back to the running position
        if (!this.isHit) {
            body.setTransform(getUserData()!!.runningPosition, 0f)
        }
    }

    fun hit() {
        body.applyAngularImpulse(getUserData()!!.hitAngularImpulse, true)
        this.isHit = true
        AudioUtils.getInstance().playSound(hitSound)
    }

    fun onDifficultyChange(newDifficulty: Difficulty) {
        setGravityScale(newDifficulty.runnerGravityScale)
        getUserData()!!.jumpingLinearImpulse = newDifficulty.runnerJumpingLinearImpulse
    }

    fun setGravityScale(gravityScale: Float) {
        body.gravityScale = gravityScale
        body.resetMassData()
    }
}
