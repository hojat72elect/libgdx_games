package com.nopalsoft.sokoban.parallax

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

/**
 * This is the whole moving background we show to the user. It contains all the different [ParallaxLayer]s.
 *
 * @param layers The  background layers.
 * @param width  The screenWith.
 * @param height The screenHeight.
 * @param speed  A Vector2 attribute to point out the x and y speed.
 */
class ParallaxBackground(
    private val layers: Array<ParallaxLayer>,
    val width: Float,
    val height: Float,
    private val speed: Vector2
) {

    private val camera = OrthographicCamera(width, height)
    private val batch = SpriteBatch()

    fun render(delta: Float) {
        camera.position.add(speed.x * delta, speed.y * delta, 0f)
        batch.projectionMatrix = camera.projection
        batch.begin()


        for ((region, parallaxRatio, startPosition, padding, layerWidth, layerHeight) in layers) {
            var currentX = -camera.position.x * parallaxRatio.x % (region.regionWidth + padding.x)

            if (speed.x < 0)
                currentX -= region.regionWidth + padding.x

            do {
                var currentY = -camera.position.y * parallaxRatio.y % (region.regionHeight + padding.y)

                if (speed.y < 0) currentY -= region.regionHeight + padding.y
                do {
                    batch.draw(
                        region,
                        -camera.viewportWidth / 2.0f + currentX + startPosition.x,
                        -camera.viewportHeight / 2.0f + currentY + startPosition.y,
                        layerWidth,
                        layerHeight
                    )
                    currentY += (region.regionHeight + padding.y)
                } while (currentY < camera.viewportHeight)

                currentX += (region.regionWidth + padding.x)
            } while (currentX < camera.viewportWidth)
        }

        batch.end()
    }
}