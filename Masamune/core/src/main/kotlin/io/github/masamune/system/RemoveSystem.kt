package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Remove
import ktx.log.logger

class RemoveSystem : IteratingSystem(family { all(Remove) }) {

    override fun onTickEntity(entity: Entity) = with(entity[Remove]) {
        time -= deltaTime
        if (time <= 0f) {
            log.debug { "Removing entity $entity" }
            entity.remove()
        }
    }

    companion object {
        private val log = logger<RemoveSystem>()
    }

}
