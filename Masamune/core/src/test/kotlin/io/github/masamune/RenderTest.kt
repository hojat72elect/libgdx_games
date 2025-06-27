package io.github.masamune

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.configureWorld
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.CachingAtlas
import io.github.masamune.asset.ShaderService
import io.github.masamune.component.Animation
import io.github.masamune.component.Graphic
import io.github.masamune.component.Transform
import io.github.masamune.system.AnimationSystem
import io.github.masamune.system.DebugRenderSystem
import io.github.masamune.system.RenderSystem
import io.github.masamune.tiledmap.AnimationType
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.assets.toClasspathFile
import ktx.graphics.color
import ktx.math.vec2
import ktx.math.vec3

/**
 * Test for [RenderSystem] and [AnimationSystem].
 *
 * Bottom left corner shows graphics with default size, bigger size and scaling greater than 1f.
 * The sized and scaled version must be rendered identically. Scaling is done from the center of the graphic.
 *
 * Above those graphics there are rotated versions. They must look the same as before but with a 90° rotation.
 *
 * Above those graphics are animated versions of a special effect.
 *
 * Above those graphics are tinted versions of a graphic (red, green, transparent blue).
 *
 * The center of the screen shows a permanently scaling graphic and a permanently rotating graphic.
 *
 * Finally, at the bottom center you will see the combination of a graphic + animated version with and without scaling.
 */

fun main() = gdxTest("Render Test", RenderTest())

private class RenderTest : KtxApplicationAdapter {

    private val batch: Batch by lazy { SpriteBatch() }
    private val texture by lazy { Texture("hero.png".toClasspathFile()) }
    private val atlas by lazy { CachingAtlas(AtlasAsset.SFX, TextureAtlas("sfx.atlas".toClasspathFile())) }
    private val gameViewport: Viewport = ExtendViewport(16f, 9f)
    private val world by lazy { gameWorld() }
    private var alpha = 0f
    private var direction = 1f

    private lateinit var scaleEntity: Entity
    private lateinit var rotateEntity: Entity


    private fun gameWorld() = configureWorld {
        injectables {
            add(batch)
            add(gameViewport)
            add(ShaderService())
        }

        systems {
            add(AnimationSystem())
            add(RenderSystem())
            add(DebugRenderSystem())
        }
    }

    override fun create() {
        // test different sizes
        newCharacter(0f, 0f, size = vec2(1f, 1f))
        newCharacter(1f, 0f, size = vec2(2f, 2f))
        newCharacter(3.5f, 0.5f, size = vec2(1f, 1f), scale = 2f)

        // test rotation
        newCharacter(0f, 3f, size = vec2(1f, 1f), rotation = 90f)
        newCharacter(1f, 3f, size = vec2(2f, 2f), rotation = 90f)
        newCharacter(3.5f, 3.5f, size = vec2(1f, 1f), scale = 2f, rotation = 90f)

        // test animation which has an aspect ratio != 1:1
        newSfx(0f, 6f, size = vec2(1f, 1f))
        newSfx(1f, 6f, size = vec2(2f, 2f))
        newSfx(3.5f, 6.5f, size = vec2(1f, 1f), scale = 2f)

        // test color
        newCharacter(0f, 8f, size = vec2(1f, 1f), color = color(1f, 0f, 0f, 1f))
        newCharacter(1f, 8f, size = vec2(1f, 1f), color = color(0f, 1f, 0f, 0.5f))
        newCharacter(2f, 8f, size = vec2(1f, 1f), color = color(0f, 0f, 1f, 0.33f))

        // test sorting order (SFX before character)
        newCharacter(8f, 0f, size = vec2(1f, 1f))
        newSfx(8f, 0f, size = vec2(1f, 1f))

        newCharacter(10.5f, 0.5f, size = vec2(1f, 1f))
        newSfx(10.5f, 0.5f, size = vec2(1f, 1f), scale = 2f)

        // test changes over time
        scaleEntity = newCharacter(8f, 5f, size = vec2(1f, 1f))
        rotateEntity = newCharacter(10f, 5f, size = vec2(1f, 1f))
    }

    private fun newCharacter(
        x: Float, y: Float,
        size: Vector2,
        scale: Float = 1f,
        rotation: Float = 0f,
        color: Color = color(1f, 1f, 1f, 1f)
    ): Entity =
        world.entity {
            it += Graphic(TextureRegion(texture), color)
            it += Transform(position = vec3(x, y, 0f), size = size, scale = scale, rotation = rotation)
        }

    private fun newSfx(
        x: Float, y: Float,
        size: Vector2,
        scale: Float = 1f,
        rotation: Float = 0f
    ) {
        world.entity {
            Animation.ofAtlas(atlas, "shield-yellow", AnimationType.IDLE, speed = 3f).also { animation ->
                it += animation
                it += Graphic(animation.gdxAnimation.getKeyFrame(0f))
            }
            it += Transform(position = vec3(x, y, 1f), size = size, scale = scale, rotation = rotation)
        }
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)

        val deltaTime = Gdx.graphics.deltaTime.coerceAtMost(1 / 30f)
        with(world) {
            update(deltaTime)

            alpha += deltaTime * 0.25f * direction
            if (alpha !in 0f..1f) {
                alpha = alpha.coerceIn(0f, 1f)
                direction *= -1
            }
            val rotation = MathUtils.lerp(0f, 360f, alpha)
            val scale = MathUtils.lerp(0.5f, 2f, alpha)

            rotateEntity[Transform].rotation = rotation
            scaleEntity[Transform].scale = scale
        }
    }

    override fun dispose() {
        batch.dispose()
        texture.dispose()
        atlas.dispose()
        world.dispose()
    }

}
