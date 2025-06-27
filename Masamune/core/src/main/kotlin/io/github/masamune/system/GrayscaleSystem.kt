package io.github.masamune.system

import com.badlogic.gdx.graphics.g2d.Batch
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import io.github.masamune.asset.ShaderService
import io.github.masamune.component.Grayscale

class GrayscaleSystem(
    private val batch: Batch = inject(),
    private val shaderService: ShaderService = inject(),
) : IteratingSystem(family { all(Grayscale) }) {

    override fun onTickEntity(entity: Entity) = with(entity[Grayscale]) {
        alpha = (alpha + deltaTime * speed).coerceAtMost(1f)
        val weight = interpolation.apply(initWeight, finalWeight, alpha)
        shaderService.applyGrayscaleShader(batch, weight)
    }

}
