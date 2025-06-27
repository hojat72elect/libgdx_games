package io.github.masamune.combat.state

import com.badlogic.gdx.math.Interpolation
import com.github.quillraven.fleks.World
import io.github.masamune.asset.MusicAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Animation
import io.github.masamune.component.Combat
import io.github.masamune.component.Grayscale
import io.github.masamune.component.Player
import io.github.masamune.tiledmap.AnimationType
import ktx.log.logger

class CombatStateDefeat(
    private val world: World,
    private val audioService: AudioService = world.inject(),
) : CombatState {
    private val playerEntities = world.family { all(Player, Combat) }

    override fun onEnter() {
        log.debug { "Combat defeat!" }
        audioService.play(MusicAsset.COMBAT_DEFEAT, loop = false, keepPrevious = true)
        playerEntities.forEach { entity ->
            entity[Animation].changeTo = AnimationType.DEAD
        }
        world.entity {
            it += Grayscale(0.2f, 1f, 5f, Interpolation.linear)
        }
    }

    companion object {
        private val log = logger<CombatStateDefeat>()
    }
}
