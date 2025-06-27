package io.github.masamune.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.masamune.component.Animation
import io.github.masamune.component.Facing
import io.github.masamune.component.FacingDirection
import io.github.masamune.component.Graphic
import io.github.masamune.tiledmap.AnimationType

class AnimationSystem : IteratingSystem(family { all(Animation, Graphic) }) {

    override fun onTickEntity(entity: Entity) = with(entity[Animation]) {
        val facing = entity.getOrNull(Facing)
        if (changeTo != AnimationType.UNDEFINED) {
            // change animation and reset state timer
            val direction = facing?.direction ?: FacingDirection.UNDEFINED
            gdxAnimation = atlas.gdxAnimation(gdxAnimation.atlasKey, changeTo, direction)
            changeTo = AnimationType.UNDEFINED
            stateTime = 0f
        } else if (facing != null && facing.hasChanged()) {
            // change animation and reset state timer
            gdxAnimation = atlas.gdxAnimation(gdxAnimation.atlasKey, gdxAnimation.type, facing.direction)
            stateTime = 0f
        } else {
            // update animation
            stateTime += deltaTime * speed
        }

        // update graphic region
        gdxAnimation.playMode = playMode
        val keyFrame = gdxAnimation.getKeyFrame(stateTime)
        entity[Graphic].region = keyFrame
    }

}
