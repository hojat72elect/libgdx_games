package com.nopalsoft.sokoban.parallax

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2


class ParallaxLayer(
    @JvmField val region: TextureRegion,
    @JvmField val parallaxRatio: Vector2,
    @JvmField val startPosition: Vector2,
    @JvmField val padding: Vector2,
    @JvmField val width: Float,
    @JvmField val height: Float
) {

    constructor(region: TextureRegion, parallaxRatio: Vector2, startPosition: Vector2, padding: Vector2) : this(
        region,
        parallaxRatio,
        startPosition,
        padding,
        region.regionWidth.toFloat(),
        region.regionHeight.toFloat()
    )

    constructor(region: TextureRegion, parallaxRatio: Vector2, padding: Vector2) : this(region, parallaxRatio, Vector2(0F, 0F), padding)

}
