package com.bitfire.uracer.game

import com.badlogic.gdx.math.Vector2
import com.bitfire.uracer.game.actors.GhostCar
import com.bitfire.uracer.game.events.CarEvent
import com.bitfire.uracer.game.player.PlayerCar

interface GameLogicObserver {
    fun handleExtraInput()
    fun beforeRender()
    fun updateCameraZoom(timeModFactor: Float): Float
    fun updateCameraPosition(positionPx: Vector2)
    fun collision(data: CarEvent.Data)
    fun physicsForcesReady(eventData: CarEvent.Data)
    fun ghostReplayStarted(ghost: GhostCar)
    fun ghostReplayEnded(ghost: GhostCar)
    fun ghostLapCompleted(ghost: GhostCar)
    fun ghostFadingOut(ghost: GhostCar)
    fun playerLapStarted()
    fun playerLapCompleted()
    fun warmUpStarted()
    fun warmUpCompleted()
    fun driftBegins(player: PlayerCar)
    fun driftEnds(player: PlayerCar)
    fun wrongWayBegins()
    fun wrongWayEnds()
    fun outOfTrack()
    fun backInTrack()
    fun doQuit()
}
