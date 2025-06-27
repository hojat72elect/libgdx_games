package io.github.masamune.combat.state

import com.github.quillraven.fleks.World
import io.github.masamune.asset.MusicAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Animation
import io.github.masamune.component.Combat
import io.github.masamune.component.Player
import io.github.masamune.tiledmap.AnimationType
import ktx.log.logger

class CombatStateVictory(
    private val world: World,
    private val audioService: AudioService = world.inject(),
) : CombatState {
    private val playerEntities = world.family { all(Player, Combat) }

    override fun onEnter() {
        log.debug { "Combat victory!" }
        audioService.play(MusicAsset.COMBAT_VICTORY, loop = false, keepPrevious = true)
        playerEntities.forEach { entity ->
            val aniCmp = entity[Animation]
            aniCmp.changeTo = AnimationType.SPECIAL
            aniCmp.speed = 0.25f
        }
    }

    companion object {
        private val log = logger<CombatStateVictory>()
    }
}
