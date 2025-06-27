package io.github.masamune.ai

import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.utils.random.ConstantFloatDistribution
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.ai.guard.hasAttack
import io.github.masamune.ai.guard.hasDefensiveMagic
import io.github.masamune.ai.guard.hasHealMagic
import io.github.masamune.ai.guard.hasHurtFriends
import io.github.masamune.ai.guard.hasOffensiveMagic
import io.github.masamune.ai.task.useAttack
import io.github.masamune.ai.task.useDefensiveMagic
import io.github.masamune.ai.task.useHealMagic
import io.github.masamune.ai.task.useNothing
import io.github.masamune.ai.task.useOffensiveMagic
import ktx.ai.behaviorTree
import ktx.ai.random
import ktx.ai.selector
import ktx.ai.sequence

fun supportBehavior(
    world: World,
    entity: Entity,
    offensiveMagicChange: Float = 0.2f,
    defensiveMagicChange: Float = 0.2f,
    healMagicChange: Float = 0.8f,
): BehaviorTree<Entity> = behaviorTree {
    `object` = entity

    selector {
        // special support behavior:
        // heal friends if they are hurt
        sequence {
            random(ConstantFloatDistribution(healMagicChange))
            hasHurtFriends(world)
            hasHealMagic(world)
            useHealMagic(world)
        }

        sequence {
            random(ConstantFloatDistribution(offensiveMagicChange))
            hasOffensiveMagic(world)
            useOffensiveMagic(world)
        }

        sequence {
            random(ConstantFloatDistribution(defensiveMagicChange))
            hasDefensiveMagic(world)
            useDefensiveMagic(world)
        }

        sequence {
            hasAttack(world)
            useAttack(world)
        }

        useNothing(world)
    }
}
