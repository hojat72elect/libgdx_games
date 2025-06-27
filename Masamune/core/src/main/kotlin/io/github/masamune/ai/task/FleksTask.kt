package io.github.masamune.ai.task

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.math.MathUtils.random
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.EntityBag
import io.github.masamune.combat.action.Action
import io.github.masamune.combat.action.ActionTargetType
import io.github.masamune.component.Combat
import io.github.masamune.component.Player
import ktx.app.gdxError

abstract class FleksTask(val world: World) : LeafTask<Entity>() {
    val entity: Entity
        get() = `object`

    private val playerEntities = world.family { all(Combat, Player) }
    private val enemyEntities = world.family { all(Combat).none(Player) }

    override fun copyTo(task: Task<Entity>) = task

    override fun execute(): Status {
        world.run { onExecute() }
        return Status.SUCCEEDED
    }

    abstract fun World.onExecute()

    fun useAction(
        targetCondition: (World.(target: Entity) -> Boolean)? = null,
        block: Combat.() -> Action,
    ) = with(world) {
        val combatCmp = entity[Combat]

        // set action
        combatCmp.action = block(combatCmp)

        // update targets
        val targets = combatCmp.targets
        targets.clear()
        if (combatCmp.action.targetType == ActionTargetType.NONE) {
            // self cast action
            return@with
        }

        var potentialTargets: EntityBag = when {
            combatCmp.action.defensive -> enemyEntities.entities
            else -> playerEntities.entities
        }
        targetCondition?.let { condition ->
            potentialTargets = potentialTargets.filter { condition(it) }
        }
        when (combatCmp.action.targetType) {
            ActionTargetType.ALL -> targets += potentialTargets
            ActionTargetType.MULTI -> targets += potentialTargets.take(random(1, potentialTargets.size))
            ActionTargetType.SINGLE -> targets += potentialTargets.random()
            else -> gdxError("$this action is configured incorrectly. Target type NONE is not supported at this point.")
        }
    }
}
