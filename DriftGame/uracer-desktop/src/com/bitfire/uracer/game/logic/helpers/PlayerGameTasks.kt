package com.bitfire.uracer.game.logic.helpers

import com.bitfire.uracer.URacer
import com.bitfire.uracer.configuration.UserProfile
import com.bitfire.uracer.game.logic.gametasks.GameTasksManager
import com.bitfire.uracer.game.logic.gametasks.hud.elements.HudLapInfo
import com.bitfire.uracer.game.logic.gametasks.hud.elements.HudPlayer
import com.bitfire.uracer.game.logic.gametasks.hud.elements.HudPlayerStatic
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.PlayerDriftSoundEffect
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.PlayerEngineSoundEffect
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.PlayerImpactSoundEffect
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.PlayerTensiveMusic
import com.bitfire.uracer.game.logic.gametasks.trackeffects.effects.PlayerSkidMarks
import com.bitfire.uracer.game.logic.gametasks.trackeffects.effects.PlayerSmokeTrails
import com.bitfire.uracer.game.logic.replaying.LapManager

/**
 * Manages the creation and destruction of the player-bound game tasks.
 */
class PlayerGameTasks(private val userProfile: UserProfile, private val manager: GameTasksManager) {
    /*
     * These fields keep track of the concrete player tasks (note that they are all publicly accessible for performance reasons).
     */
    lateinit var hudPlayer: HudPlayer
    lateinit var hudPlayerStatic: HudPlayerStatic
    lateinit var hudLapInfo: HudLapInfo

    fun dispose() {
        destroyTasks()
    }

    fun createTasks(lapManager: LapManager, progressData: TrackProgressData) {
        // sounds
        manager.sound.add(PlayerDriftSoundEffect())
        manager.sound.add(PlayerImpactSoundEffect())
        manager.sound.add(PlayerEngineSoundEffect(progressData))
        manager.sound.add(PlayerTensiveMusic(progressData))

        // track effects
        val maxSkidMarks = if (URacer.Game.isDesktop()) 150 else 100
        val maxLife = if (URacer.Game.isDesktop()) 5F else 3F
        manager.effects.addBeforeCars(PlayerSkidMarks(maxSkidMarks, maxLife))
        manager.effects.addAfterCars(PlayerSmokeTrails())

        // hud
        hudPlayer = HudPlayer()
        hudPlayerStatic = HudPlayerStatic(userProfile)
        hudLapInfo = HudLapInfo(lapManager)
        manager.hud.addBeforePostProcessing(hudPlayer)
        manager.hud.addAfterPostProcessing(hudLapInfo)
        manager.hud.addAfterPostProcessing(hudPlayerStatic)
    }

    fun destroyTasks() {
        manager.sound.disposeTasks()
        manager.effects.disposeTasks()
        manager.hud.disposeTasks()
    }
}
