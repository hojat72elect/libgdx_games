package io.github.masamune.system

import com.badlogic.gdx.math.Interpolation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.CachingAtlas
import io.github.masamune.component.Combat
import io.github.masamune.component.Fade
import io.github.masamune.component.Graphic
import io.github.masamune.component.Player
import io.github.masamune.component.StatusAilment
import io.github.masamune.component.StatusType
import io.github.masamune.component.Sticky
import io.github.masamune.component.Transform
import io.github.masamune.event.CombatPlayerBuffAddEvent
import io.github.masamune.event.CombatPlayerBuffRemoveEvent
import io.github.masamune.event.CombatStartEvent
import io.github.masamune.event.Event
import io.github.masamune.event.EventListener
import ktx.graphics.color
import ktx.math.component1
import ktx.math.component2
import ktx.math.component3
import ktx.math.vec2
import ktx.math.vec3

class PlayerStatusAilmentSystem : IteratingSystem(family = family { all(StatusAilment, Sticky) }), EventListener {
    private val playerFamily = family { all(Player, Combat, Transform) }

    override fun onTickEntity(entity: Entity) {
        val (target, offset) = entity[Sticky]
        if (target !in world) {
            entity.remove()
            return
        }

        val targetPosition = target[Transform].position
        entity[Transform].position.set(
            targetPosition.x + offset.x,
            targetPosition.y + offset.y,
            targetPosition.z + 1
        )
    }

    override fun onEvent(event: Event) {
        when (event) {
            is CombatStartEvent -> {
                val player = playerFamily.single()
                val (playerPosition) = player[Transform]
                val (playerX, playerY, playerZ) = playerPosition

                // spawn player status ailment entities
                val charAndPropsAtlas = world.inject<CachingAtlas>(AtlasAsset.CHARS_AND_PROPS.name)
                world.entity {
                    it += Transform(
                        vec3(playerX - 0.25f, playerY + 0.8f, playerZ + 1f),
                        vec2(0.5f, 0.5f),
                        rotation = 10f
                    )
                    it += Sticky(player, vec2(-0.25f, 0.8f))
                    it += Graphic(charAndPropsAtlas.findRegion("status/slow"), color = color(1f, 1f, 1f, 0f))
                    it += StatusAilment(StatusType.SLOW)
                }
                world.entity {
                    it += Transform(
                        vec3(playerX + 0.35f, playerY + 0.9f, playerZ + 1f),
                        vec2(0.5f, 0.5f),
                        rotation = 340f
                    )
                    it += Sticky(player, vec2(0.35f, 0.9f))
                    it += Graphic(charAndPropsAtlas.findRegion("status/poison"), color = color(1f, 1f, 1f, 0f))
                    it += StatusAilment(StatusType.POISON)
                }
            }

            is CombatPlayerBuffAddEvent -> {
                family.singleOrNull { it[StatusAilment].type == event.type }?.let { entity ->
                    entity.configure {
                        it += Fade(0.2f, 1f, 1f, Interpolation.bounceIn)
                    }
                }
            }

            is CombatPlayerBuffRemoveEvent -> {
                family.singleOrNull { it[StatusAilment].type == event.type }?.let { entity ->
                    entity.configure {
                        it += Fade(1f, 0f, 1f, Interpolation.bounceOut)
                    }
                }
            }

            else -> Unit
        }
    }
}
