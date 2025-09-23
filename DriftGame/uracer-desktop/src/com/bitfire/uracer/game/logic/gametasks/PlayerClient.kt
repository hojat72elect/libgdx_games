package com.bitfire.uracer.game.logic.gametasks

import com.bitfire.uracer.game.player.PlayerCar

open class PlayerClient {

    @JvmField
    protected var player: PlayerCar? = null

    @JvmField
    protected var hasPlayer = false

    open fun player(player: PlayerCar?) {
        this.player = player
        this.hasPlayer = (player != null)
    }
}
