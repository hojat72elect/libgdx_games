package com.bitfire.uracer.game.logic.post

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.bitfire.uracer.game.logic.helpers.TrackProgressData
import com.bitfire.uracer.game.player.PlayerCar

interface PostProcessingAnimator {

    fun update(
        cameraPos: Vector2,
        progressData: TrackProgressData,
        ambient: Color,
        trees: Color,
        zoom: Float,
        warmUpCompletion: Float,
        collisionFactor: Float,
        paused: Boolean
    )

    fun alertBegins(milliseconds: Int)

    fun alertEnds(milliseconds: Int)

    fun gamePause(milliseconds: Int)

    fun gameResume(milliseconds: Int)

    fun reset()

    fun setPlayer(player: PlayerCar?)
}
