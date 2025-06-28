package ca.hojat.sokoban.parallax

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

/**
 * @param layers The  background layers.
 * @param width  The screenWith.
 * @param height The screenHeight.
 * @param speed  A Vector2 attribute to point out the x and y speed.
 */
class ParallaxBackground(private val layers: Array<ParallaxLayer>, width: Float, height: Float, private val speed: Vector2) {
    private val camera = OrthographicCamera(width, height)
    private val batch = SpriteBatch()

    fun render(delta: Float) {
        camera.position.add(speed.x * delta, speed.y * delta, 0f)
        batch.projectionMatrix = camera.projection
        batch.begin()

        for (layer in layers) {
            var currentX = -camera.position.x * layer.parallaxRatio.x % (layer.region.regionWidth + layer.padding.x)

            if (speed.x < 0) currentX -= (layer.region.regionWidth + layer.padding.x)

            do {
                var currentY = -camera.position.y * layer.parallaxRatio.y % (layer.region.regionHeight + layer.padding.y)

                if (speed.y < 0) currentY -= (layer.region.regionHeight + layer.padding.y)
                do {
                    batch.draw(
                        layer.region,
                        -camera.viewportWidth / 2.0f + currentX + layer.startPosition.x,
                        -camera.viewportHeight / 2.0f + currentY + layer.startPosition.y,
                        layer.width,
                        layer.height
                    )
                    currentY += (layer.region.regionHeight + layer.padding.y)
                } while (currentY < camera.viewportHeight)

                currentX += (layer.region.regionWidth + layer.padding.x)
            } while (currentX < camera.viewportWidth)
        }

        batch.end()
    }
}