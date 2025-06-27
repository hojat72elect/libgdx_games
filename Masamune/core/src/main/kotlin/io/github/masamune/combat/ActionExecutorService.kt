package io.github.masamune.combat

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.EntityBag
import com.github.quillraven.fleks.collection.MutableEntityBag
import io.github.masamune.asset.SoundAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.combat.ActionQueueEntry.Companion.DEFAULT_QUEUE_ACTION
import io.github.masamune.combat.action.Action
import io.github.masamune.combat.action.DefaultAction
import io.github.masamune.combat.buff.Buff
import io.github.masamune.combat.buff.OnAttackDamageBuff
import io.github.masamune.combat.buff.OnAttackDamageTakenBuff
import io.github.masamune.combat.buff.OnMagicDamageBuff
import io.github.masamune.combat.buff.OnMagicDamageTakenBuff
import io.github.masamune.combat.buff.OnTurnBuff
import io.github.masamune.combat.buff.PoisonBuff
import io.github.masamune.combat.buff.SlowBuff
import io.github.masamune.combat.effect.DamageEffect
import io.github.masamune.combat.effect.DefaultEffect
import io.github.masamune.combat.effect.DelayEffect
import io.github.masamune.combat.effect.Effect
import io.github.masamune.combat.effect.EffectStack
import io.github.masamune.combat.effect.HealEffect
import io.github.masamune.combat.effect.MissEffect
import io.github.masamune.combat.effect.SfxEffect
import io.github.masamune.combat.effect.SoundEffect
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.CharacterStats.Companion.MAG_DAM_PER_INT
import io.github.masamune.component.Combat
import io.github.masamune.component.Item
import io.github.masamune.component.ItemStats
import io.github.masamune.component.MoveBy
import io.github.masamune.component.Player
import io.github.masamune.component.StatusType
import io.github.masamune.component.Transform
import io.github.masamune.event.CombatActionStartEvent
import io.github.masamune.event.CombatActionsPerformedEvent
import io.github.masamune.event.CombatEntityManaUpdateEvent
import io.github.masamune.event.CombatPlayerBuffAddEvent
import io.github.masamune.event.CombatPlayerBuffRemoveEvent
import io.github.masamune.event.CombatTurnEndEvent
import io.github.masamune.event.EventService
import io.github.masamune.isEntityAlive
import io.github.masamune.isEntityDead
import ktx.log.logger
import ktx.math.vec2
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

enum class ActionState {
    START, UPDATE, FINISH, END;
}

data class ActionQueueEntry(val entity: Entity, val action: Action, val targets: EntityBag) {
    companion object {
        val DEFAULT_QUEUE_ACTION = ActionQueueEntry(Entity.NONE, DefaultAction, MutableEntityBag(0))
    }
}

class ActionExecutorService(
    val audioService: AudioService,
    val eventService: EventService,
) {
    private val actionStack = ArrayDeque<ActionQueueEntry>()
    private var state: ActionState = ActionState.START

    @PublishedApi
    internal var currentQueueEntry: ActionQueueEntry = DEFAULT_QUEUE_ACTION
    var itemOwner: Entity = Entity.NONE
        private set
    private var endTurnPerformed = false
    private var moveTimer = 0f
    private var ignoreDamageCalls = false

    lateinit var world: World
        private set
    private lateinit var allEnemies: Family
    private lateinit var allPlayers: Family
    private lateinit var effectStack: EffectStack

    val hasEffects: Boolean
        get() = effectStack.isNotEmpty

    inline val source: Entity
        get() = currentQueueEntry.entity

    inline val action: Action
        get() = currentQueueEntry.action

    inline val singleTarget: Entity
        get() = currentQueueEntry.targets.first()

    inline val allTargets: EntityBag
        get() = currentQueueEntry.targets

    inline val numTargets: Int
        get() = currentQueueEntry.targets.size

    inline val deltaTime: Float
        get() = world.deltaTime

    inline val Entity.stats: CharacterStats
        get() = with(world) { this@stats[CharacterStats] }

    inline val Entity.itemStats: ItemStats
        get() = with(world) { this@itemStats[ItemStats] }

    inline val Entity.position: Vector3
        get() = with(world) { this@position[Transform].position }

    inline val Entity.itemAction: Action
        get() = with(world) { this@itemAction[Item].action }

    infix fun withWorld(world: World) {
        this.world = world
        this.allEnemies = world.family { none(Player).all(Combat) }
        this.allPlayers = world.family { all(Player, Combat) }
        this.effectStack = EffectStack(world)
    }

    fun queueAction(entity: Entity, action: Action, targets: EntityBag) {
        actionStack += ActionQueueEntry(entity, action, targets)
    }

    private fun perform(queueEntry: ActionQueueEntry, moveEntity: Boolean = true) {
        val (source, action, targets) = queueEntry
        log.debug { "Performing action ${action::class.simpleName}: source=$source, targets(${targets.size})=$targets" }

        this.state = ActionState.START
        this.currentQueueEntry = queueEntry
        if (moveEntity) {
            moveEntityBy(queueEntry.entity, PERFORM_OFFSET, 0.75f)
        }

        if (itemOwner != Entity.NONE) {
            eventService.fire(CombatActionStartEvent(itemOwner, queueEntry.action.type))
        } else {
            eventService.fire(CombatActionStartEvent(source, queueEntry.action.type))
        }
    }

    fun performItemAction(itemOwner: Entity, item: Entity, action: Action, targets: EntityBag) {
        this.itemOwner = itemOwner
        with(world) {
            // Just reduce amount instead of calling world.removeItem because
            // removeItem will remove the item entity already, but we still need it to get its stats, etc.
            // The real item removal happens at the end of the combat in the CombatScreen.
            --item[Item].amount
        }
        perform(ActionQueueEntry(item, action, targets), false)
    }

    private fun moveEntityBy(entity: Entity, amount: Float, duration: Float) = with(world) {
        moveTimer = duration + 0.25f
        entity.configure {
            val direction = if (entity has Player) 1 else -1
            it += MoveBy(vec2(0f, amount * direction), duration, Interpolation.fastSlow)
        }
    }

    private fun changeState(newState: ActionState) {
        // never go back to a previous state (can happen if 'end' is called during an update).
        // In that case we want to remain in END state
        if (newState.ordinal > state.ordinal) {
            state = newState
        }
    }

    fun update() {
        if (moveTimer > 0f) {
            moveTimer = (moveTimer - deltaTime).coerceAtLeast(0f)
            return
        }

        if (actionStack.isEmpty()) {
            if (!endTurnPerformed) {
                // all actions of turn performed -> fire turn end event which might add some actions on the stack again
                log.debug { "Combat turn end" }
                endTurnPerformed = true
                eventService.fire(CombatTurnEndEvent)
            } else {
                if (!effectStack.update()) {
                    // there are still effects on the stack -> wait for them to be finished
                    // they got added by the CombatTurnEndEvent (e.g. onTurnEnd buffs)
                    return
                }

                log.debug { "Combat trigger next turn" }
                eventService.fire(CombatActionsPerformedEvent)
            }
            return
        }

        when (state) {
            ActionState.START -> {
                if (itemOwner == Entity.NONE) {
                    // action is not a use item action -> reduce mana cost of source entity
                    val amount = -action.manaCost.toFloat()
                    with(world) {
                        val targetStats = source[CharacterStats]
                        targetStats.mana = (targetStats.mana + amount).coerceIn(0f, targetStats.manaMax)
                        if (amount != 0f) {
                            eventService.fire(
                                CombatEntityManaUpdateEvent(
                                    source,
                                    abs(amount),
                                    targetStats.mana,
                                    targetStats.manaMax,
                                    state
                                )
                            )
                        }
                    }
                }
                action.run { this@ActionExecutorService.onStart() }
                changeState(ActionState.UPDATE)
            }

            ActionState.UPDATE -> {
                action.run {
                    if (this@ActionExecutorService.onUpdate()) {
                        changeState(ActionState.FINISH)
                    }
                }
            }

            ActionState.FINISH -> {
                action.run { this@ActionExecutorService.onFinish() }
                changeState(ActionState.END)
                effectStack.startNext()
            }

            ActionState.END -> {
                // action is finished -> execute effect stack
                if (!effectStack.update()) {
                    // there are still effects on the stack -> wait for them to be finished
                    return
                }

                // all effects got executed -> perform next action
                actionStack.removeFirst()
                clearAction()
                performNext()
            }
        }
    }

    /**
     * Performs an attack against the [target] entity and waits [delay] seconds before continuing.
     */
    fun attack(target: Entity, delay: Float = 1f) = with(world) {
        val realTarget = verifyTarget(target)
        if (realTarget == Entity.NONE) {
            // target is already dead and no other target is available -> do nothing
            return
        }

        // will target evade?
        val targetStats = realTarget[CharacterStats]
        val evadeChance = targetStats.physicalEvade
        if (evadeChance > 0f && MathUtils.random() <= evadeChance) {
            effectStack.addLast(MissEffect(source, realTarget))
            effectStack.addLast(SoundEffect(source, realTarget, SoundAsset.ATTACK_MISS))
            effectStack.addLast(SfxEffect(source, realTarget, source[Combat].attackSFX, delay * 0.5f, 2f))
            if (delay > 0f) {
                effectStack.addLast(DelayEffect(source, realTarget, delay))
            }
            return@with
        }

        // add strength to physical damage
        val sourceStats = source[CharacterStats]
        var damage = sourceStats.damage

        // critical strike?
        val critChance = sourceStats.criticalStrike
        val isCritical = critChance > 0f && MathUtils.random() <= critChance
        if (isCritical) {
            damage *= 2f
        }

        // reduce damage by armor
        val armor = targetStats.armor
        val reduction = 100f / (100f + armor)
        damage *= reduction

        // apply damage
        val minDamage = ceil(damage * 0.9f)
        val maxDamage = floor(damage * 1.1f)
        damage = MathUtils.random(minDamage, maxDamage)

        // pre attack buffs
        source.applyBuffs<OnAttackDamageBuff> { damage = preAttackDamage(source, realTarget, damage) }
        realTarget.applyBuffs<OnAttackDamageTakenBuff> { damage = preAttackDamageTaken(source, realTarget, damage) }

        // add attack effects on effect stack (sound then sfx then damage)
        val combat = source[Combat]
        effectStack.addLast(SoundEffect(source, realTarget, combat.attackSnd))
        effectStack.addLast(SfxEffect(source, realTarget, combat.attackSFX, delay * 0.5f, 2f))
        effectStack.addLast(DamageEffect(source, realTarget, damage, isCritical))
        if (delay > 0f) {
            effectStack.addLast(DelayEffect(source, realTarget, delay))
        }

        // post attack buffs
        realTarget.applyBuffs<OnAttackDamageTakenBuff> { postAttackDamageTaken(source, realTarget, damage) }
        source.applyBuffs<OnAttackDamageBuff> { postAttackDamage(source, realTarget, damage) }
    }

    inline fun <reified T : Buff> Entity.applyBuffs(block: T.() -> Unit) = with(world) {
        this@applyBuffs[Combat].buffs
            .filterIsInstance<T>()
            .forEach { it.block() }
    }

    fun addSfx(to: Entity, sfxAtlasKey: String, duration: Float, scale: Float = 1f) {
        effectStack.addLast(SfxEffect(to, to, sfxAtlasKey, duration, scale))
    }

    fun play(asset: SoundAsset) {
        effectStack.addLast(SoundEffect(source, Entity.NONE, asset))
    }

    fun wait(seconds: Float) {
        effectStack.addLast(DelayEffect(source, Entity.NONE, seconds))
    }

    private fun verifyTarget(target: Entity): Entity = with(world) {
        if (isEntityDead(target)) {
            // target is dead -> replace with a different target
            if (target hasNo Player) {
                return@with allEnemies.firstOrNull { it !in allTargets && isEntityAlive(it) } ?: Entity.NONE
            }
            return@with allPlayers.firstOrNull { it !in allTargets && isEntityAlive(it) } ?: Entity.NONE
        }
        return target
    }

    // Damage without a source that cannot be dodged and doesn't apply source buffs.
    // Also, doesn't redirect damage because it is coming from a buff that is applied to a specific entity.
    fun dealDoTDamage(
        amount: Float,
        target: Entity,
        sfxAtlasKey: String,
        sfxDuration: Float,
        sfxScale: Float,
        soundAsset: SoundAsset?,
        delay: Float,
    ) {

        if (world.isEntityDead(target)) {
            // target is already dead -> do nothing
            return
        }

        // reduce damage by resistance
        val targetStats = target.stats
        val resistance = targetStats.resistance
        val reduction = 75f / (75f + resistance)
        var damage = amount * reduction

        // pre magic buffs
        target.applyBuffs<OnMagicDamageTakenBuff> { damage = preMagicDamageTaken(Entity.NONE, target, damage) }

        // add effects on effect stack (sfx, sound, damage, ...)
        soundAsset?.let { effectStack.addLast(SoundEffect(Entity.NONE, target, it)) }
        effectStack.addLast(SfxEffect(Entity.NONE, target, sfxAtlasKey, sfxDuration, sfxScale))
        val damageEffect = DamageEffect(Entity.NONE, target, damage, false)
        effectStack.addLast(damageEffect)
        if (delay > 0f) {
            effectStack.addLast(DelayEffect(Entity.NONE, target, delay))
        }

        // post magic buffs
        target.applyBuffs<OnMagicDamageTakenBuff> { postMagicDamageTaken(Entity.NONE, target, damage) }
    }

    fun dealMagicDamage(
        source: Entity,
        amount: Float,
        target: Entity,
        sfxAtlasKey: String,
        sfxDuration: Float,
        sfxScale: Float,
        soundAsset: SoundAsset?,
        delay: Float,
        withBuffs: Boolean = true,
    ): Effect = with(world) {
        if (ignoreDamageCalls) {
            return DefaultEffect
        }
        ignoreDamageCalls = !withBuffs

        val realTarget = verifyTarget(target)
        if (realTarget == Entity.NONE) {
            // target is already dead and no other target is available -> do nothing
            ignoreDamageCalls = false
            return DefaultEffect
        }

        // will target evade?
        val targetStats = realTarget.stats
        val evadeChance = targetStats.magicalEvade
        if (evadeChance > 0f && MathUtils.random() <= evadeChance) {
            soundAsset?.let { effectStack.addLast(SoundEffect(source, realTarget, it)) }
            effectStack.addLast(SfxEffect(source, realTarget, sfxAtlasKey, sfxDuration, sfxScale))
            val missEffect = MissEffect(source, realTarget)
            effectStack.addLast(missEffect)
            if (delay > 0f) {
                effectStack.addLast(DelayEffect(source, realTarget, delay))
            }
            ignoreDamageCalls = false
            return missEffect
        }

        // add intelligence to magic damage
        val sourceStats = source[CharacterStats]
        var damage = amount + (sourceStats.intelligence * MAG_DAM_PER_INT)

        // arcane strike?
        val critChance = sourceStats.arcaneStrike
        val isCritical = critChance > 0f && MathUtils.random() <= critChance
        if (isCritical) {
            damage *= 1.5f
        }

        // reduce damage by resistance
        val resistance = targetStats.resistance
        val reduction = 75f / (75f + resistance)
        damage *= reduction

        // apply damage
        val minDamage = ceil(damage * 0.9f)
        val maxDamage = floor(damage * 1.1f)
        damage = MathUtils.random(minDamage, maxDamage)

        // pre magic buffs
        source.applyBuffs<OnMagicDamageBuff> { damage = preMagicDamage(source, realTarget, damage) }
        realTarget.applyBuffs<OnMagicDamageTakenBuff> { damage = preMagicDamageTaken(source, realTarget, damage) }

        // add effects on effect stack (sfx, sound, damage, ...)
        soundAsset?.let { effectStack.addLast(SoundEffect(source, realTarget, it)) }
        effectStack.addLast(SfxEffect(source, realTarget, sfxAtlasKey, sfxDuration, sfxScale))
        val damageEffect = DamageEffect(source, realTarget, damage, isCritical)
        effectStack.addLast(damageEffect)
        if (delay > 0f) {
            effectStack.addLast(DelayEffect(source, realTarget, delay))
        }

        // post magic buffs
        realTarget.applyBuffs<OnMagicDamageTakenBuff> { postMagicDamageTaken(source, realTarget, damage) }
        source.applyBuffs<OnMagicDamageBuff> { postMagicDamage(source, realTarget, damage) }
        ignoreDamageCalls = false
        return damageEffect
    }

    fun dealMagicDamage(
        source: Entity,
        amount: Float,
        targets: EntityBag,
        sfxAtlasKey: String,
        sfxDuration: Float,
        sfxScale: Float,
        soundAsset: SoundAsset,
        delay: Float,
    ) {
        val damageEffects = mutableListOf<Effect>()
        targets.forEach {
            damageEffects += dealMagicDamage(source, amount, it, sfxAtlasKey, sfxDuration, sfxScale, null, 0f)
        }
        val lastDamageEffect = damageEffects.last() // this is either a DamageEffect or MissEffect
        if (lastDamageEffect === effectStack.last) {
            // no post magic damage effects triggered -> process everything at once
            // we need to add a sound effect for each target to avoid that the
            // sound effect is completely skipped if the target dies.
            // The EffectStack class takes care to remove consecutive sound effects
            damageEffects.forEach {
                effectStack.addBefore(damageEffects.first(), SoundEffect(source, it.target, soundAsset))
            }
            effectStack.addLast(DelayEffect(source, lastDamageEffect.target, delay))
        } else {
            // post magic damage effects -> add delay/audio to each damage effect to process post reaction one by one
            damageEffects.forEach { damageEffect ->
                // add sound before damage to play sound also if target dies
                // add delay after damage
                effectStack.addBefore(damageEffect, SoundEffect(source, damageEffect.target, soundAsset))
                effectStack.addAfter(damageEffect, DelayEffect(source, damageEffect.target, delay))
            }
        }
    }

    fun heal(
        life: Float,
        mana: Float,
        target: Entity,
        sfxAtlasKey: String,
        sfxDuration: Float,
        sfxScale: Float,
        soundAsset: SoundAsset,
        delay: Float,
    ) {
        val realTarget = verifyTarget(target)
        if (realTarget == Entity.NONE) {
            // target is already dead and no other target is available -> do nothing
            return
        }

        // add effects on effect stack (sfx, sound, damage, ...)
        effectStack.addLast(SoundEffect(source, realTarget, soundAsset))
        effectStack.addLast(SfxEffect(source, realTarget, sfxAtlasKey, sfxDuration, sfxScale))
        effectStack.addLast(HealEffect(source, realTarget, life, mana))
        if (delay > 0f) {
            effectStack.addLast(DelayEffect(source, realTarget, delay))
        }
    }

    fun hasMana(entity: Entity, amount: Int): Boolean = with(world) {
        entity[CharacterStats].mana >= amount
    }

    private fun clearAction() = with(world) {
        log.debug { "Cleaning up ${this@ActionExecutorService}" }

        if (itemOwner != Entity.NONE) {
            moveEntityBy(itemOwner, -PERFORM_OFFSET, 0.5f)
            itemOwner[Combat].clearAction()
            itemOwner = Entity.NONE
        } else {
            moveEntityBy(source, -PERFORM_OFFSET, 0.5f)
            source[Combat].clearAction()
        }
        currentQueueEntry = DEFAULT_QUEUE_ACTION
    }

    fun clear() {
        actionStack.clear()
        effectStack.clear()
        currentQueueEntry = DEFAULT_QUEUE_ACTION
    }

    inline fun <reified T : Buff> addBuff(buff: T) = with(world) {
        val ownerBuffs = buff.owner[Combat].buffs
        ownerBuffs.removeIf { it is T }
        with(buff) { onApply() }
        ownerBuffs += buff

        // fire special events for specific player buffs to show them (=PlayerStatusAilmentSystem)
        if (buff.owner has Player) {
            if (buff is PoisonBuff) {
                eventService.fire(CombatPlayerBuffAddEvent(StatusType.POISON))
            } else if (buff is SlowBuff) {
                eventService.fire(CombatPlayerBuffAddEvent(StatusType.SLOW))
            }
        }
    }

    fun Buff.removeBuff() = with(world) {
        onRemove()
        owner[Combat].buffs -= this@removeBuff

        // fire special events for specific player buffs to show them (=PlayerStatusAilmentSystem)
        if (owner has Player) {
            if (this@removeBuff is PoisonBuff) {
                eventService.fire(CombatPlayerBuffRemoveEvent(StatusType.POISON))
            } else if (this@removeBuff is SlowBuff) {
                eventService.fire(CombatPlayerBuffRemoveEvent(StatusType.SLOW))
            }
        }
    }

    fun performFirst() {
        log.debug { "Starting first action of stack of size ${actionStack.size}" }
        perform(actionStack.first())
        endTurnPerformed = false
    }

    private fun performNext() {
        if (actionStack.isEmpty()) {
            return
        }

        var nextQueueEntry = actionStack.first()
        while (world.isEntityDead(nextQueueEntry.entity)) {
            // entity died already -> remove its action from the stack and find
            // next executable action
            actionStack.removeFirst()
            if (actionStack.isEmpty()) {
                return
            }
            nextQueueEntry = actionStack.first()
        }
        perform(nextQueueEntry)
    }

    inline fun performPassiveActions(entities: EntityBag, crossinline action: (Action) -> Unit) = with(world) {
        val tmpQueueEntry = currentQueueEntry
        val emptyBag = MutableEntityBag(0)
        entities.forEach { entity ->
            entity[Combat].passiveActions.forEach { passiveAction ->
                currentQueueEntry = ActionQueueEntry(entity, passiveAction, emptyBag)
                action(passiveAction)
            }
        }
        currentQueueEntry = tmpQueueEntry
    }

    fun performOnTurnBeginBuffs(entity: Entity) {
        entity.applyBuffs<OnTurnBuff> { onTurnBegin() }
    }

    fun performOnTurnEndBuffs(entity: Entity) {
        entity.applyBuffs<OnTurnBuff> { onTurnEnd() }
    }

    override fun toString(): String {
        return "ActionExecutorService(action=${action::class.simpleName}, source=${source.id}, state=$state)"
    }

    companion object {
        private val log = logger<ActionExecutorService>()
        private const val PERFORM_OFFSET = 0.75f // how many units will a unit move up/down when performing its action
    }
}


