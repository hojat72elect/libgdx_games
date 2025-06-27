package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Selector
import io.github.masamune.component.Transform
import kotlin.math.max
import kotlin.math.min

class SelectorSystem : IteratingSystem(family { all(Selector, Transform) }) {

    override fun onTickEntity(entity: Entity) {
        val (target) = entity[Selector]
        val (targetPos, targetSize) = target[Transform]
        val (selectorPos, selectorSize) = entity[Transform]

        val diffMaxX = targetSize.x - MAX_SELECTOR_SIZE
        val diffMaxY = targetSize.y - MAX_SELECTOR_SIZE

        selectorPos.set(targetPos.x + max(0f, diffMaxX) * 0.5f, targetPos.y + max(0f, diffMaxY) * 0.5f, 5f)
        selectorSize.set(min(MAX_SELECTOR_SIZE, targetSize.x), min(MAX_SELECTOR_SIZE, targetSize.y))
    }

    companion object {
        private const val MAX_SELECTOR_SIZE = 1.75f
    }

}
