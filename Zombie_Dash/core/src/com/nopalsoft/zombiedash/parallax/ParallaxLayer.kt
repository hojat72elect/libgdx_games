package com.nopalsoft.zombiedash.parallax

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Vector2

class ParallaxLayer {
    var region: AtlasRegion?
    var parallaxRatio: Vector2?
    var startPosition: Vector2?
    var padding: Vector2?
    var width: Float
    var heigth: Float

    constructor(region: AtlasRegion, parallaxRatio: Vector2?, padding: Vector2?) : this(region, parallaxRatio, Vector2(0f, 0f), padding) {
        this.width = region.getRegionWidth().toFloat()
        this.heigth = region.getRegionHeight().toFloat()
    }

    /**
     * @param region        the TextureRegion to draw , this can be any width/height
     * @param parallaxRatio the relative speed of x,y [ParallaxBackground.ParallaxBackground]
     * @param startPosition the init position of x,y
     * @param padding       the padding of the region at x,y
     */
    constructor(region: AtlasRegion, parallaxRatio: Vector2?, startPosition: Vector2?, padding: Vector2?) {
        this.region = region
        this.parallaxRatio = parallaxRatio
        this.startPosition = startPosition
        this.padding = padding
        this.width = region.getRegionWidth().toFloat()
        this.heigth = region.getRegionHeight().toFloat()
    }

    /**
     * @param region        the TextureRegion to draw , this can be any width/height
     * @param parallaxRatio the relative speed of x,y [ParallaxBackground.ParallaxBackground]
     * @param startPosition the init position of x,y
     * @param padding       the padding of the region at x,y
     */
    constructor(region: AtlasRegion?, parallaxRatio: Vector2?, startPosition: Vector2?, padding: Vector2?, width: Float, height: Float) {
        this.region = region
        this.parallaxRatio = parallaxRatio
        this.startPosition = startPosition
        this.padding = padding
        this.width = width
        this.heigth = height
    }
}
