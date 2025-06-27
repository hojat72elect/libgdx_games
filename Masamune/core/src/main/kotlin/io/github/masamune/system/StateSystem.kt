package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.State

class StateSystem : IteratingSystem(family { all(State) }) {
    override fun onTickEntity(entity: Entity) {
        entity[State].animationFsm.update()
    }
}
