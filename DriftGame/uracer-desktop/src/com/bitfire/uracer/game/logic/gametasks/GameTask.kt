package com.bitfire.uracer.game.logic.gametasks

import com.badlogic.gdx.utils.Disposable
import com.bitfire.uracer.game.GameEvents
import com.bitfire.uracer.game.events.GameLogicEvent
import com.bitfire.uracer.game.events.TaskManagerEvent
import com.bitfire.uracer.game.player.PlayerCar
import com.bitfire.uracer.game.task.Task
import com.bitfire.uracer.utils.URacerRuntimeException

abstract class GameTask @JvmOverloads constructor(order: TaskManagerEvent.Order = TaskManagerEvent.Order.DEFAULT) : Task(order), Disposable {

    protected var player: PlayerCar? = null
    protected var hasPlayer = false
    private val logicListener = GameLogicEvent.Listener { _: Any, type: GameLogicEvent.Type, _: GameLogicEvent.Order ->
        when (type) {
            GameLogicEvent.Type.PlayerAdded -> onPlayer(GameEvents.logicEvent.player)
            GameLogicEvent.Type.PlayerRemoved -> {
                if (GameEvents.logicEvent.player != null) throw URacerRuntimeException("Player zombieing")

                onPlayer(null)
            }

            GameLogicEvent.Type.GameRestart -> onGameRestart()
            GameLogicEvent.Type.GameReset -> onGameReset()
            GameLogicEvent.Type.GameQuit -> onGameQuit()
        }
    }

    init {
        GameEvents.logicEvent.addListener(logicListener, GameLogicEvent.Type.GameRestart)
        GameEvents.logicEvent.addListener(logicListener, GameLogicEvent.Type.GameReset)
        GameEvents.logicEvent.addListener(logicListener, GameLogicEvent.Type.GameQuit)
        GameEvents.logicEvent.addListener(logicListener, GameLogicEvent.Type.PlayerAdded)
        GameEvents.logicEvent.addListener(logicListener, GameLogicEvent.Type.PlayerRemoved)
    }

    override fun dispose() {
        super.dispose()
        GameEvents.logicEvent.removeListener(logicListener, GameLogicEvent.Type.GameRestart)
        GameEvents.logicEvent.removeListener(logicListener, GameLogicEvent.Type.GameReset)
        GameEvents.logicEvent.removeListener(logicListener, GameLogicEvent.Type.GameQuit)
        GameEvents.logicEvent.removeListener(logicListener, GameLogicEvent.Type.PlayerAdded)
        GameEvents.logicEvent.removeListener(logicListener, GameLogicEvent.Type.PlayerRemoved)
    }

    open fun onPlayer(player: PlayerCar?) {
        this.player = player
        this.hasPlayer = (player != null)
    }

    open fun onGameReset() {}
    open fun onGameRestart() {}
    open fun onGameQuit() {}
}
