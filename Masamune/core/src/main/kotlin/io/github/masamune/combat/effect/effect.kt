package io.github.masamune.combat.effect

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.ActionState
import io.github.masamune.combat.buff.OnDeathBuff
import io.github.masamune.component.Combat
import io.github.masamune.event.CombatEntityDeadEvent
import io.github.masamune.event.CombatEntityHealEvent
import io.github.masamune.event.CombatEntityManaUpdateEvent
import io.github.masamune.event.CombatEntityTakeDamageEvent
import io.github.masamune.event.CombatEntityTransformEvent
import io.github.masamune.event.CombatMissEvent
import io.github.masamune.event.EventService
import io.github.masamune.isEntityDead

sealed interface Effect {
    val source: Entity
    val target: Entity

    fun World.onStart() = Unit
    fun World.onUpdate(): Boolean = true
}

data object DefaultEffect : Effect {
    override val source: Entity = Entity.NONE
    override val target: Entity = Entity.NONE
}

data class EffectStack(
    private val world: World,
    private val stack: ArrayDeque<Effect> = ArrayDeque(),
    private val eventService: EventService = world.inject(),
    private val actionExecutorService: ActionExecutorService = world.inject(),
) {
    private var currentEffect: Effect = DefaultEffect

    val isNotEmpty: Boolean
        get() = stack.isNotEmpty() || currentEffect != DefaultEffect

    val last: Effect
        get() = stack.last()

    fun clear() {
        stack.clear()
        currentEffect = DefaultEffect
    }

    fun startNext() {
        if (stack.isEmpty()) {
            currentEffect = DefaultEffect
            return
        }

        currentEffect = stack.removeFirst()
        currentEffect.run { world.onStart() }
    }

    fun addLast(effect: Effect) = stack.addLast(effect)

    fun addAfter(effect: Effect, add: Effect) {
        val idx = stack.indexOfFirst { it === effect }
        stack.add(idx + 1, add)
    }

    fun addBefore(effect: Effect, add: Effect) {
        val idx = stack.indexOfFirst { it === effect }
        stack.add(idx, add)
    }

    private fun Entity.hasNoTransformEffect(): Boolean {
        return stack.filterIsInstance<TransformEffect>()
            .none { it.source == this || it.target == this }
    }

    fun update(): Boolean {
        if (stack.isEmpty() && currentEffect === DefaultEffect) {
            // no effects to perform
            return true
        }

        val effect = currentEffect
        // effect currently in progress
        if (!effect.run { world.onUpdate() }) {
            return currentEffect === DefaultEffect
        }

        // effect finished
        // 1) fire special events if necessary
        when {
            effect is DamageEffect && effect.damage > 0f -> {
                eventService.fire(
                    CombatEntityTakeDamageEvent(
                        effect.target,
                        effect.damage,
                        effect.targetLife,
                        effect.targetLifeMax,
                        effect.critical
                    )
                )

                if (world.isEntityDead(effect.target)) {
                    // entity is dead -> remove any of its effects from the stack
                    stack.removeIf { it.target == effect.target || it.source == effect.target }

                    // run any preDeath buffs
                    actionExecutorService.run {
                        effect.target.applyBuffs<OnDeathBuff> {
                            val deathEffects = onDeath(effect.source, effect.target)
                            for (i in deathEffects.lastIndex downTo 0) {
                                addAfter(currentEffect, deathEffects[i])
                            }
                        }
                    }

                    if (effect.target.hasNoTransformEffect()) {
                        // cleanup target buffs
                        with(world) {
                            effect.target[Combat].buffs.clear()
                        }
                        // fire dead event for dissolve effect and update combat UI
                        eventService.fire(CombatEntityDeadEvent(effect.target))
                    }
                }
            }

            effect is HealEffect -> {
                if (effect.amountLife > 0f) {
                    eventService.fire(
                        CombatEntityHealEvent(
                            effect.target,
                            effect.amountLife,
                            effect.targetLife,
                            effect.targetLifeMax
                        )
                    )
                }
                if (effect.amountMana > 0f) {
                    eventService.fire(
                        CombatEntityManaUpdateEvent(
                            effect.target,
                            effect.amountMana,
                            effect.targetMana,
                            effect.targetManaMax,
                            ActionState.FINISH
                        )
                    )
                }
            }

            effect is TransformEffect -> {
                eventService.fire(
                    CombatEntityTransformEvent(
                        effect.source,
                        effect.newEntity,
                        effect.newLife,
                        effect.newLifeMax
                    )
                )
            }

            effect is MissEffect -> {
                eventService.fire(CombatMissEvent(effect.target))
            }
        }

        // 2) remove consecutive sound effects of the same audio file to avoid super loud effects
        if (stack.isNotEmpty() && currentEffect is SoundEffect) {
            val currentSoundAsset = (currentEffect as SoundEffect).soundAsset
            var nextEffect = stack.first()
            while (nextEffect is SoundEffect && nextEffect.soundAsset == currentSoundAsset) {
                stack.removeFirst()
                if (stack.isEmpty()) {
                    break
                }
                nextEffect = stack.first()
            }
        }

        // 3) get next effect
        startNext()

        return currentEffect === DefaultEffect
    }
}
