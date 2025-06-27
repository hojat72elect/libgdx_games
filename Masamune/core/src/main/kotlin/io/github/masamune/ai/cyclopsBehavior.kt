package io.github.masamune.ai

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.ai.guard.canPerform
import io.github.masamune.ai.guard.hasAttack
import io.github.masamune.ai.guard.isLifeLowerThan
import io.github.masamune.ai.guard.isTurnMultipleOf
import io.github.masamune.ai.task.perform
import io.github.masamune.ai.task.useAttack
import io.github.masamune.ai.task.useNothing
import io.github.masamune.tiledmap.ActionType
import ktx.ai.behaviorTree
import ktx.ai.selector
import ktx.ai.sequence

fun cyclopsBehavior(
    world: World,
    entity: Entity,
): BehaviorTree<Entity> = behaviorTree {
    `object` = entity

    selector {
        // regenerate once when below 30% life
        onlyOnce {
            sequence {
                isLifeLowerThan(world, 0.3f)
                canPerform(world, ActionType.REGENERATE1)
                perform(world, ActionType.REGENERATE1)
            }
        }

        // cast DEMI once when below 20% life
        onlyOnce {
            sequence {
                isLifeLowerThan(world, 0.2f)
                canPerform(world, ActionType.DEMI1)
                perform(world, ActionType.DEMI1)
            }
        }

        // cast DEMI once in the second round
        onlyOnce {
            sequence {
                isTurnMultipleOf(world, 1, ignoreFirstTurn = true)
                canPerform(world, ActionType.DEMI1)
                perform(world, ActionType.DEMI1)
            }
        }

        // perform double attack every 4 turns
        sequence {
            isTurnMultipleOf(world, 4, ignoreFirstTurn = true)
            canPerform(world, ActionType.DOUBLE_STRIKE)
            perform(world, ActionType.DOUBLE_STRIKE)
        }

        // perform slow every 5 turns
        // double attack has priority if we have a turn where slow and double attack shall be performed
        sequence {
            isTurnMultipleOf(world, 5)
            canPerform(world, ActionType.SLOW)
            perform(world, ActionType.SLOW)
        }

        // otherwise, just attack
        sequence {
            hasAttack(world)
            useAttack(world)
        }

        useNothing(world)
    }
}
