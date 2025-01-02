package com.nopalsoft.sokoban.parallax

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

data class ParallaxLayer(
    val region: TextureRegion,
    val parallaxRatio: Vector2,
    val startPosition: Vector2,
    val padding: Vector2,
    val width: Float,
    val height: Float,
)
