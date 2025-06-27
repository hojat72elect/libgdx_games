package io.github.masamune.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.HdpiUtils
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ScreenUtils
import io.github.masamune.asset.ShaderService.Companion.renderToFbo
import ktx.app.gdxError
import ktx.graphics.use
import ktx.log.logger
import ktx.math.vec2

/**
 * Service class for [ShaderProgram] management. Supports loading and usage of following shaders:
 * - outline
 * - blur
 * - grayscale
 * - dissolve
 *
 * If the loading of a shader fails then an exception with the shader's error log is thrown.
 */
class ShaderService(private val fileHandleResolver: FileHandleResolver = InternalFileHandleResolver()) : Disposable {

    /** a temporary [FrameBuffer] that can be used in [renderToFbo] (e.g. for blur shader). */
    var tmpFbo = FrameBuffer(FBO_FORMAT, Gdx.graphics.width, Gdx.graphics.height, false)
        private set

    // outline shader
    private val outlineShader by lazy { loadShader("default.vert", "outline.frag") }
    private var outlineColorIdx = -1
    private var outlinePixelSizeIdx = -1

    // blur shader
    private var blurFbo = FrameBuffer(FBO_FORMAT, Gdx.graphics.width, Gdx.graphics.height, false)
    private val blurShader by lazy { loadShader("default.vert", "blur.frag") }
    private var blurRadiusIdx = -1
    private var blurDirectionIdx = -1
    private var blurPixelSizeIdx = -1

    // grayscale shader
    private val grayscaleShader by lazy { loadShader("default.vert", "grayscale.frag") }
    private var grayscaleWeightIdx = -1

    // dissolve shader
    private val dissolveShader by lazy { loadShader("default.vert", "dissolve.frag") }
    private var dissolveIdx = -1
    private var dissolveUvOffsetIdx = -1
    private var dissolveMaxUvIdx = -1
    private var dissolveNumFragmentsIdx = -1

    // flash shader
    private val flashShader by lazy { loadShader("default.vert", "flash.frag") }
    private var flashColorIdx = -1
    private var flashWeightIdx = -1

    /**
     * Loads all shaders and stores uniform locations internally for better performance.
     */
    fun loadAllShader() {
        outlineColorIdx = outlineShader.getUniformLocation("u_outlineColor")
        outlinePixelSizeIdx = outlineShader.getUniformLocation("u_pixelSize")

        blurRadiusIdx = blurShader.getUniformLocation("u_radius")
        blurDirectionIdx = blurShader.getUniformLocation("u_direction")
        blurPixelSizeIdx = blurShader.getUniformLocation("u_pixelSize")

        grayscaleWeightIdx = grayscaleShader.getUniformLocation("u_weight")

        dissolveIdx = dissolveShader.getUniformLocation("u_dissolve")
        dissolveUvOffsetIdx = dissolveShader.getUniformLocation("u_uvOffset")
        dissolveMaxUvIdx = dissolveShader.getUniformLocation("u_atlasMaxUV")
        dissolveNumFragmentsIdx = dissolveShader.getUniformLocation("u_fragmentNumber")

        flashColorIdx = flashShader.getUniformLocation("u_flashColor")
        flashWeightIdx = flashShader.getUniformLocation("u_flashWeight")
    }

    /**
     * Loads a specific shader via its [vertexFile] and [fragmentFile].
     * If loading fails then an exception with the shader's error log is thrown.
     */
    private fun loadShader(vertexFile: String, fragmentFile: String): ShaderProgram {
        val vertexCode = fileHandleResolver.resolve("shader/$vertexFile").readString()
        val fragmentCode = fileHandleResolver.resolve("shader/$fragmentFile").readString()
        val shader = ShaderProgram(vertexCode, fragmentCode)
        if (!shader.isCompiled) {
            gdxError("Could not compile shader $vertexFile/$fragmentFile! Log:\n${shader.log}")
        }
        return shader
    }

    /**
     * Sets an **outline** shader as active shader for any draw calls of the [batch].
     * The line has the given [color].
     * The [texture] for the draw calls must be provided to correctly calculate the size of a pixel
     * inside the shader program.
     * After the draw [block] is executed, the previous shader of the [batch] is restored.
     */
    fun useOutlineShader(batch: Batch, color: Color, texture: Texture, block: () -> Unit) {
        // set outline shader and its uniforms
        batch.useShader(outlineShader) {
            outlineShader.use {
                it.setUniformf(outlineColorIdx, color)
                it.setUniformf(outlinePixelSizeIdx, TMP_VEC2.set(1f / texture.width, 1f / texture.height))
            }

            // run render block
            block()
        }
    }

    /**
     * Sets a **flash** shader as active shader for any draw calls of the [batch].
     * The flash has the given [color] and is applied with the given [weight].
     * A [weight] of zero means no flash is applied while a [weight] of 1 means the flash color is applied
     * for each pixel of the rendering [block].
     * After the draw [block] is executed, the previous shader of the [batch] is restored.
     */
    fun useFlashShader(batch: Batch, color: Color, weight: Float, block: () -> Unit) {
        batch.useShader(flashShader) {
            flashShader.use {
                it.setUniformf(flashColorIdx, color)
                it.setUniformf(flashWeightIdx, weight)
            }
            block()
        }
    }

    /**
     * Applies a **grayscale** shader to the [batch] with a given [weight].
     * A [weight] of 1 means 100% grayscale effect, while a [weight] of 0 means 0% grayscale effect.
     */
    fun applyGrayscaleShader(batch: Batch, weight: Float) {
        batch.shader = grayscaleShader
        grayscaleShader.use {
            it.setUniformf(grayscaleWeightIdx, weight)
        }
    }

    /**
     * Applies a **blur** shader to the given draw [block] before rendering it via the [batch].
     * The [radius] defines how much blur is applied. 0 means no blur. Good values are between 0 and 6.
     * If [targetFbo] is defined then the rendering is done to the [FrameBuffer] instead of the screen.
     */
    fun useBlurShader(batch: Batch, radius: Float, targetFbo: FrameBuffer? = null, block: () -> Unit) {
        val texture = tmpFbo.renderToFbo(block)
        batch.useShader(blurShader) {
            blurShader.use {
                it.setUniformf(blurRadiusIdx, radius)
                it.setUniformf(blurDirectionIdx, Vector2.X)
                it.setUniformf(blurPixelSizeIdx, TMP_VEC2.set(texture.width.toFloat(), texture.height.toFloat()))
            }

            // apply horizontal blur to texture by rendering it to FrameBuffer
            blurFbo.renderToFbo {
                // viewport is the entire screen since we render the frame buffer texture pixel perfect 1:1
                // use identity matrix to render pixel perfect
                batch.use(batch.projectionMatrix.idt()) {
                    it.draw(texture, -1f, 1f, 2f, -2f)
                }
            }

            // render to screen by applying vertical blur
            blurShader.use {
                it.setUniformf(blurRadiusIdx, radius)
                it.setUniformf(blurDirectionIdx, Vector2.Y)
                it.setUniformf(blurPixelSizeIdx, TMP_VEC2.set(texture.width.toFloat(), texture.height.toFloat()))
            }
            if (targetFbo != null) {
                targetFbo.renderToFbo {
                    batch.use(batch.projectionMatrix.idt()) {
                        it.draw(blurFbo.colorBufferTexture, -1f, 1f, 2f, -2f)
                    }
                }
            } else {
                batch.use(batch.projectionMatrix.idt()) {
                    it.draw(blurFbo.colorBufferTexture, -1f, 1f, 2f, -2f)
                }
            }
        }
    }

    /**
     * Applies a **dissolve** shader to the given draw [block] before rendering it via the [batch].
     * The [value] defines how much of the dissolve effect is applied (0=none, 1=completely dissolved).
     * The [uvOffset] and [atlasMaxUv] define the u,v and u2,v2 values of the [block] to be drawn.
     * The [numFragments] defines how many dissolve fragments will be used on the x- and y-axis.
     * The [block] must be a single draw call to a specific [TextureRegion].
     */
    fun useDissolveShader(
        batch: Batch,
        value: Float,
        uvOffset: Vector2,
        atlasMaxUv: Vector2,
        numFragments: Vector2,
        block: () -> Unit
    ) {
        // set dissolve shader and its uniforms
        batch.useShader(dissolveShader) {
            dissolveShader.use {
                it.setUniformf(dissolveIdx, value)
                it.setUniformf(dissolveUvOffsetIdx, uvOffset)
                it.setUniformf(dissolveMaxUvIdx, atlasMaxUv)
                it.setUniformf(dissolveNumFragmentsIdx, numFragments)
            }

            // run render block
            block()
        }
    }

    /**
     * Resizes the internal [FrameBuffer] objects.
     */
    fun resize(width: Int, height: Int) {
        log.debug { "Resizing FBOs" }
        blurFbo = blurFbo.resize(width, height)
        tmpFbo = tmpFbo.resize(width, height)
    }

    /**
     * Disposes all shaders.
     */
    override fun dispose() {
        outlineShader.dispose()
        blurShader.dispose()
        grayscaleShader.dispose()
        dissolveShader.dispose()
        flashShader.dispose()
        blurFbo.dispose()
        tmpFbo.dispose()
    }

    companion object {
        private val TMP_VEC2 = vec2()

        // FBO format requires ALPHA support for proper overlapping rendering of fbo textures like
        // in the blur screen transition
        val FBO_FORMAT = Format.RGBA8888
        private val log = logger<ShaderService>()

        /**
         * Renders anything inside [block] to the [FrameBuffer]
         * and returns its [colorBufferTexture][FrameBuffer.getColorBufferTexture].
         */
        fun FrameBuffer.renderToFbo(block: () -> Unit): Texture {
            this.use {
                // !! IMPORTANT !!
                // use a clear color with alpha 0f instead of 1f because otherwise,
                // its transparent pixels will overlap any pixels of previous render calls due
                // to missing transparency.
                ScreenUtils.clear(0f, 0f, 0f, 0f, false)
                HdpiUtils.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
                block()
            }
            return this.colorBufferTexture
        }


        /**
         * Resize [FrameBuffer] by disposing the current one and recreating a new one with the
         * given new [width] and [height].
         */
        fun FrameBuffer.resize(width: Int, height: Int): FrameBuffer {
            if (width > 0 && height > 0 && (this.width != width || this.height != height)) {
                this.dispose()
                return FrameBuffer(FBO_FORMAT, width, height, false)
            }
            return this
        }

        /**
         * Sets the given [shader] as active shader for any draw calls of the [Batch] inside [block].
         * After the draw [block] is executed, the previous shader of the [Batch] is restored.
         */
        fun Batch.useShader(shader: ShaderProgram?, block: () -> Unit) {
            val origShader = this.shader
            if (origShader != shader) {
                this.shader = shader
            }

            block()

            // reset shader to default
            this.shader = origShader
        }
    }

}
