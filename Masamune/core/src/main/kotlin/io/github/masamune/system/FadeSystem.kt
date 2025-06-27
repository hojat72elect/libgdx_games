package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Fade
import io.github.masamune.component.Graphic

class FadeSystem : IteratingSystem(family { all(Fade, Graphic) }) {

    override fun onTickEntity(entity: Entity) = with(entity[Fade]) {
        val color = entity[Graphic].color

        alpha = (alpha - deltaTime * speed).coerceAtLeast(0f)
        val invAlpha = 1f - alpha
        val newAlpha = interpolation.apply(from, to, invAlpha)
        color.a = newAlpha

        if (alpha == 0f) {
            // fade is finished -> remove it
            entity.configure {
                it -= Fade
            }
        }
    }

}
