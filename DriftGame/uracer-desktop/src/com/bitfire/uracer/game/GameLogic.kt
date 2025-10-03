package com.bitfire.uracer.game

import com.bitfire.uracer.configuration.UserProfile
import com.bitfire.uracer.game.actors.GhostCar
import com.bitfire.uracer.game.logic.gametasks.messager.Message
import com.bitfire.uracer.game.logic.replaying.ReplayManager.ReplayResult
import com.bitfire.uracer.game.logic.types.helpers.TimeModulator

interface GameLogic {
    fun dispose()
    fun addPlayer()
    fun removePlayer()
    fun restartGame()
    fun resetGame()
    fun quitGame()
    fun pauseGame()
    fun resumeGame()
    val isQuitPending: Boolean
    val isPaused: Boolean
    val collisionFactor: Float
    val collisionFrontRatio: Float
    fun endCollisionTime()
    fun getNextTarget(): GhostCar?
    fun startTimeDilation()
    fun endTimeDilation()
    val isTimeDilationAvailable: Boolean
    fun tick()
    fun tickCompleted()
    val outOfTrackTimer: Time
    val timeDilationTimer: Time
    val timeModulator: TimeModulator
    fun getGhost(handle: Int): GhostCar
    fun isGhostActive(handle: Int): Boolean
    val isWarmUp: Boolean
    fun hasPlayer(): Boolean
    val userProfile: UserProfile
    val lastRecordedInfo: ReplayResult
    fun showMessage(message: String, durationSecs: Float, type: Message.Type, position: Message.Position, size: Message.Size)
    fun chooseNextTarget(backward: Boolean)
}
