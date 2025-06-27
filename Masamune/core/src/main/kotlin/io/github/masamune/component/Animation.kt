package io.github.masamune.component

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.masamune.asset.CachingAtlas
import io.github.masamune.tiledmap.AnimationType
import ktx.collections.GdxArray

class GdxAnimation(
    frameDuration: Float,
    keyFrames: GdxArray<AtlasRegion>,
    val atlasKey: String,
    val type: AnimationType,
    val direction: FacingDirection,
) : com.badlogic.gdx.graphics.g2d.Animation<AtlasRegion>(frameDuration, keyFrames) {
    operator fun component1(): String = atlasKey
    operator fun component2(): AnimationType = type
    operator fun component3(): FacingDirection = direction
}

data class Animation(
    val atlas: CachingAtlas,
    var gdxAnimation: GdxAnimation,
    var stateTime: Float = 0f,
    var speed: Float = 1f,
    var playMode: PlayMode = PlayMode.LOOP,
    var changeTo: AnimationType = AnimationType.UNDEFINED,
) : Component<Animation> {
    override fun type() = Animation

    companion object : ComponentType<Animation>() {
        const val DEFAULT_FRAME_DURATION = 1 / 5f

        fun ofAtlas(
            atlas: CachingAtlas,
            mainKey: String,
            animationType: AnimationType,
            direction: FacingDirection = FacingDirection.UNDEFINED,
            speed: Float = 1f
        ): Animation {
            val gdxAnimation = atlas.gdxAnimation(mainKey, animationType, direction)
            return Animation(atlas, gdxAnimation, speed = speed)
        }

        fun ofAnimation(
            animation: Animation,
            direction: FacingDirection = animation.gdxAnimation.direction,
        ): Animation {
            val (atlas, gdxAnimation, _, speed) = animation
            val (atlasKey, animationType) = gdxAnimation
            return Animation(atlas, atlas.gdxAnimation(atlasKey, animationType, direction), speed)
        }
    }
}
