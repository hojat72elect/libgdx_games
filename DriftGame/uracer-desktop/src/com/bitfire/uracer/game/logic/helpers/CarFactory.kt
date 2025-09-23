package com.bitfire.uracer.game.logic.helpers

import com.bitfire.uracer.game.GameLogic
import com.bitfire.uracer.game.actors.CarPreset
import com.bitfire.uracer.game.actors.GhostCar
import com.bitfire.uracer.game.player.PlayerCar
import com.bitfire.uracer.game.world.GameWorld

object CarFactory {

    @JvmStatic
    fun createPlayer(gameWorld: GameWorld, gameLogic: GameLogic, presetType: CarPreset.Type) = PlayerCar(gameWorld, gameLogic, presetType)

    @JvmStatic
    fun createGhost(id: Int, gameWorld: GameWorld, presetType: CarPreset.Type) = GhostCar(id, gameWorld, presetType)
}
