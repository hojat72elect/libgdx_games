package com.nopalsoft.sokoban.parallax

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

data class ParallaxLayer(
    @JvmField
    val region: TextureRegion,
    @JvmField
    val parallaxRatio: Vector2,
    @JvmField
    val startPosition: Vector2,
    @JvmField
    val padding: Vector2,
    @JvmField
    val width: Float,
    @JvmField
    val height: Float,
)
