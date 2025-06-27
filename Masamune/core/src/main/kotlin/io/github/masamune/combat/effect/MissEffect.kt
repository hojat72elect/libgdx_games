package io.github.masamune.combat.effect

import com.github.quillraven.fleks.Entity

data class MissEffect(
    override val source: Entity,
    override val target: Entity,
) : Effect
