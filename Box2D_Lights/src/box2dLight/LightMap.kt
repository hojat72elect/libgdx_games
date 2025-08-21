package box2dLight

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import shaders.DiffuseShader
import shaders.DynamicShadowShader
import shaders.GaussianBlur.createShader
import shaders.ShadowShader
import shaders.WithoutShadowShader

class LightMap(rayHandler: RayHandler, fboWidth: Int, fboHeight: Int) {
    private val lightMapMesh: Mesh
    private val pingPongBuffer: FrameBuffer
    private val rayHandler: RayHandler
    private val fboWidth: Int
    private val fboHeight: Int
    var frameBuffer: FrameBuffer
    var shadowBuffer: FrameBuffer
    var lightMapDrawingDisabled: Boolean = false
    private var shadowShader: ShaderProgram? = null
    private var withoutShadowShader: ShaderProgram? = null
    private var blurShader: ShaderProgram? = null
    private var diffuseShader: ShaderProgram? = null

    init {
        var fboWidth = fboWidth
        var fboHeight = fboHeight
        this.rayHandler = rayHandler

        if (fboWidth <= 0) fboWidth = 1
        if (fboHeight <= 0) fboHeight = 1

        this.fboWidth = fboWidth
        this.fboHeight = fboHeight

        frameBuffer = FrameBuffer(
            Pixmap.Format.RGBA8888, fboWidth,
            fboHeight, false
        )
        pingPongBuffer = FrameBuffer(
            Pixmap.Format.RGBA8888, fboWidth,
            fboHeight, false
        )
        shadowBuffer = FrameBuffer(
            Pixmap.Format.RGBA8888, fboWidth,
            fboHeight, false
        )

        lightMapMesh = createLightMapMesh()

        createShaders()
    }

    fun render() {
        val needed = rayHandler.lightRenderedLastFrame > 0

        if (lightMapDrawingDisabled) return

        if (rayHandler.pseudo3d) {
            frameBuffer.colorBufferTexture.bind(1)
            shadowBuffer.colorBufferTexture.bind(0)
        } else {
            frameBuffer.colorBufferTexture.bind(0)
        }

        // at last lights are rendered over scene
        if (rayHandler.shadows) {
            val c = rayHandler.ambientLight
            var shader = shadowShader
            if (rayHandler.pseudo3d) {
                shader!!.bind()
                if (RayHandler.isDiffuse) {
                    rayHandler.diffuseBlendFunc.apply()
                    shader.setUniformf("ambient", c.r, c.g, c.b, c.a)
                } else {
                    rayHandler.shadowBlendFunc.apply()
                    shader.setUniformf(
                        "ambient", c.r * c.a, c.g * c.a,
                        c.b * c.a, 1f - c.a
                    )
                }
                shader.setUniformi("isDiffuse", if (RayHandler.isDiffuse) 1 else 0)
                shader.setUniformi("u_texture", 1)
                shader.setUniformi("u_shadows", 0)
            } else if (RayHandler.isDiffuse) {
                shader = diffuseShader
                shader!!.bind()
                rayHandler.diffuseBlendFunc.apply()
                shader.setUniformf("ambient", c.r, c.g, c.b, c.a)
            } else {
                shader!!.bind()
                rayHandler.shadowBlendFunc.apply()
                shader.setUniformf(
                    "ambient", c.r * c.a, c.g * c.a,
                    c.b * c.a, 1f - c.a
                )
            }

            lightMapMesh.render(shader, GL20.GL_TRIANGLE_FAN)
        } else if (needed) {
            rayHandler.simpleBlendFunc.apply()
            withoutShadowShader!!.bind()

            lightMapMesh.render(withoutShadowShader, GL20.GL_TRIANGLE_FAN)
        }

        Gdx.gl20.glDisable(GL20.GL_BLEND)
    }

    fun gaussianBlur(buffer: FrameBuffer, blurNum: Int) {
        Gdx.gl20.glDisable(GL20.GL_BLEND)
        for (i in 0..<blurNum) {
            buffer.colorBufferTexture.bind(0)
            // horizontal
            pingPongBuffer.begin()
            run {
                blurShader!!.bind()
                blurShader!!.setUniformf("dir", 1f, 0f)
                lightMapMesh.render(blurShader, GL20.GL_TRIANGLE_FAN, 0, 4)
            }
            pingPongBuffer.end()

            pingPongBuffer.colorBufferTexture.bind(0)
            // vertical
            buffer.begin()
            run {
                blurShader!!.bind()
                blurShader!!.setUniformf("dir", 0f, 1f)
                lightMapMesh.render(blurShader, GL20.GL_TRIANGLE_FAN, 0, 4)
            }
            if (rayHandler.customViewport) {
                buffer.end(
                    rayHandler.viewportX,
                    rayHandler.viewportY,
                    rayHandler.viewportWidth,
                    rayHandler.viewportHeight
                )
            } else {
                buffer.end()
            }
        }

        Gdx.gl20.glEnable(GL20.GL_BLEND)
    }

    fun dispose() {
        disposeShaders()

        lightMapMesh.dispose()

        frameBuffer.dispose()
        shadowBuffer.dispose()
        pingPongBuffer.dispose()
    }

    fun createShaders() {
        disposeShaders()

        shadowShader = if (rayHandler.pseudo3d) DynamicShadowShader.createShadowShader() else ShadowShader.createShadowShader()
        diffuseShader = DiffuseShader.createShadowShader()

        withoutShadowShader = WithoutShadowShader.createShadowShader()

        blurShader = createShader(fboWidth, fboHeight)
    }

    private fun disposeShaders() {
        if (shadowShader != null) shadowShader!!.dispose()
        if (diffuseShader != null) diffuseShader!!.dispose()
        if (withoutShadowShader != null) withoutShadowShader!!.dispose()
        if (blurShader != null) blurShader!!.dispose()
    }

    private fun createLightMapMesh(): Mesh {
        val verts = FloatArray(VERT_SIZE)
        // vertex coord
        verts[X1] = -1f
        verts[Y1] = -1f

        verts[X2] = 1f
        verts[Y2] = -1f

        verts[X3] = 1f
        verts[Y3] = 1f

        verts[X4] = -1f
        verts[Y4] = 1f

        // tex coords
        verts[U1] = 0f
        verts[V1] = 0f

        verts[U2] = 1f
        verts[V2] = 0f

        verts[U3] = 1f
        verts[V3] = 1f

        verts[U4] = 0f
        verts[V4] = 1f

        val tmpMesh = Mesh(
            true, 4, 0, VertexAttribute(
                VertexAttributes.Usage.Position, 2, "a_position"
            ), VertexAttribute(
                VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord"
            )
        )

        tmpMesh.setVertices(verts)
        return tmpMesh
    }

    companion object {
        const val VERT_SIZE: Int = 16
        const val X1: Int = 0
        const val Y1: Int = 1
        const val U1: Int = 2
        const val V1: Int = 3
        const val X2: Int = 4
        const val Y2: Int = 5
        const val U2: Int = 6
        const val V2: Int = 7
        const val X3: Int = 8
        const val Y3: Int = 9
        const val U3: Int = 10
        const val V3: Int = 11
        const val X4: Int = 12
        const val Y4: Int = 13
        const val U4: Int = 14
        const val V4: Int = 15
    }
}
