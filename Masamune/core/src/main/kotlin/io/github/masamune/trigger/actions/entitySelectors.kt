package io.github.masamune.trigger.actions

import com.github.quillraven.fleks.Entity

class EntitySelector(private val selector: () -> Entity) {
    val entity: Entity by lazy { selector() }
}
