package io.github.masamune.combat.action

import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.buff.TransformBuff
import io.github.masamune.tiledmap.ActionType
import io.github.masamune.tiledmap.TiledObjectType

class TransformAction : Action(ActionType.TRANSFORM, ActionTargetType.NONE) {

    override fun ActionExecutorService.onCombatStart() {
        addBuff(TransformBuff(source, TiledObjectType.HERO, source.position.x, source.position.y))
    }

}
