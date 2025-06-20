package com.mygdx.game.shaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.Attributes
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.mygdx.game.terrains.TerrainMaterial
import com.mygdx.game.terrains.attributes.TerrainFloatAttribute
import com.mygdx.game.terrains.attributes.TerrainMaterialAttribute
import com.mygdx.game.terrains.attributes.TerrainTextureAttribute

class TerrainShader(renderable: Renderable, config: DefaultShader.Config?) : BaseShader() {
    object TerrainInputs {
        val diffuseUVTransform: Uniform = Uniform("u_diffuseUVTransform")
        val diffuseBaseTexture: Uniform = Uniform("u_diffuseBaseTexture")
        val diffuseHeightTexture: Uniform = Uniform("u_diffuseHeightTexture")
        val diffuseSlopeTexture: Uniform = Uniform("u_diffuseSlopeTexture")
        val minSlope: Uniform = Uniform("u_minSlope")
    }

    object TerrainSetters {
        val diffuseUVTransform: Setter = object : LocalSetter() {
            override fun set(shader: BaseShader, inputID: Int, renderable: Renderable, combinedAttributes: Attributes) {
                val mat = getTerrainMaterial(renderable)
                val attr = mat!!.get(TerrainTextureAttribute.DiffuseBase) as TerrainTextureAttribute?
                shader[inputID, attr!!.offsetU, attr.offsetV, attr.scaleU] = attr.scaleV
            }
        }

        val diffuseBaseTexture: Setter = object : LocalSetter() {
            override fun set(shader: BaseShader, inputID: Int, renderable: Renderable, combinedAttributes: Attributes) {
                val mat = getTerrainMaterial(renderable)
                val attr = mat!!.get(TerrainTextureAttribute.DiffuseBase) as TerrainTextureAttribute?
                val unit = shader.context.textureBinder.bind(attr!!.textureDescription)
                shader[inputID] = unit
            }
        }
        val diffuseHeightTexture: Setter = object : LocalSetter() {
            override fun set(shader: BaseShader, inputID: Int, renderable: Renderable, combinedAttributes: Attributes) {
                val mat = getTerrainMaterial(renderable)
                val attr = mat!!.get(TerrainTextureAttribute.DiffuseHeight) as TerrainTextureAttribute?
                val unit = shader.context.textureBinder.bind(attr!!.textureDescription)
                shader[inputID] = unit
            }
        }
        val diffuseSlopeTexture: Setter = object : LocalSetter() {
            override fun set(shader: BaseShader, inputID: Int, renderable: Renderable, combinedAttributes: Attributes) {
                val mat = getTerrainMaterial(renderable)
                val attr = mat!!.get(TerrainTextureAttribute.DiffuseSlope) as TerrainTextureAttribute?
                val unit = shader.context.textureBinder.bind(attr!!.textureDescription)
                shader[inputID] = unit
            }
        }
        val minSlope: Setter = object : LocalSetter() {
            override fun set(shader: BaseShader, inputID: Int, renderable: Renderable, combinedAttributes: Attributes) {
                val mat = getTerrainMaterial(renderable)
                val attr = mat!!.get(TerrainFloatAttribute.MinSlope) as TerrainFloatAttribute?
                shader[inputID] = attr!!.value
            }
        }
    }

    /**
     * The renderable used to create this shader, invalid after the call to init
     */
    private var renderable: Renderable?

    // Global uniforms
    private val u_projViewTrans: Int

    // Object uniforms
    private val u_worldTrans: Int
    private val u_normalMatrix: Int

    // Material uniforms
    private val u_diffuseUVTransform: Int
    private val u_diffuseBaseTexture: Int
    private val u_diffuseHeightTexture: Int
    private val u_diffuseSlopeTexture: Int
    private val u_minSlope: Int

    //Lights
    private var u_ambientLight: Int = 0
    private var u_dirLights0color: Int = 0
    private var u_dirLights0direction: Int = 0

    // Masks
    private val attributesMask: Long
    private val terrainMaterialMask: Long

    init {
        this.renderable = renderable

        var prefix = DefaultShader.createPrefix(renderable, config)

        val terrainMaterial = getTerrainMaterial(renderable)

        attributesMask = combineAttributeMasks(renderable)
        terrainMaterialMask = terrainMaterial!!.mask

        // Add defines to our prefix
        var attribute = terrainMaterial.get(TerrainTextureAttribute::class.java, TerrainTextureAttribute.DiffuseSlope)
        if (attribute != null) {
            prefix += "#define " + TerrainTextureAttribute.DiffuseSlopeAlias + "Flag\n"
        }

        attribute = terrainMaterial.get(TerrainTextureAttribute::class.java, TerrainTextureAttribute.DiffuseHeight)
        if (attribute != null) {
            prefix += "#define " + TerrainTextureAttribute.DiffuseHeightAlias + "Flag\n"
        }

        if (renderable.environment.has(ColorAttribute.AmbientLight)) {
            prefix += "#define ambientLightFlag\n"
        }

        // Compile the shaders
        this.program = ShaderProgram(prefix + defaultVertexShader, prefix + defaultFragmentShader)

        u_projViewTrans = register(DefaultShader.Inputs.projViewTrans, DefaultShader.Setters.projViewTrans)
        u_worldTrans = register(DefaultShader.Inputs.worldTrans, DefaultShader.Setters.worldTrans)
        u_normalMatrix = register(DefaultShader.Inputs.normalMatrix, DefaultShader.Setters.normalMatrix)

        u_diffuseUVTransform = register(TerrainInputs.diffuseUVTransform, TerrainSetters.diffuseUVTransform)
        u_diffuseBaseTexture = register(TerrainInputs.diffuseBaseTexture, TerrainSetters.diffuseBaseTexture)
        u_diffuseHeightTexture = register(TerrainInputs.diffuseHeightTexture, TerrainSetters.diffuseHeightTexture)
        u_diffuseSlopeTexture = register(TerrainInputs.diffuseSlopeTexture, TerrainSetters.diffuseSlopeTexture)
        u_minSlope = register(TerrainInputs.minSlope, TerrainSetters.minSlope)
    }

    override fun init() {
        u_dirLights0color = register(Uniform("u_dirLights[0].color"))
        u_dirLights0direction = register(Uniform("u_dirLights[0].direction"))

        val program = this.program
        this.program = null
        init(program, renderable)
        renderable = null

        u_ambientLight = program.fetchUniformLocation("u_ambientLight", false)
    }

    override fun begin(camera: Camera, context: RenderContext) {
        super.begin(camera, context)
        context.setDepthTest(GL20.GL_LESS, 0f, 1f)
        context.setCullFace(GL20.GL_BACK)
        context.setDepthMask(true)
    }

    override fun render(renderable: Renderable, combinedAttributes: Attributes) {
        bindLights(combinedAttributes)
        super.render(renderable, combinedAttributes)
    }

    private fun bindLights(combinedAttributes: Attributes) {
        val dla = combinedAttributes.get(DirectionalLightsAttribute::class.java, DirectionalLightsAttribute.Type)
        val dirs = dla?.lights

        if (dirs != null) {
            set(
                u_dirLights0color, dirs[0].color.r, dirs[0].color.g,
                dirs[0].color.b
            )
            set(
                u_dirLights0direction, dirs[0].direction.x,
                dirs[0].direction.y, dirs[0].direction.z
            )
        }

        val ambientLight = combinedAttributes.get(ColorAttribute::class.java, ColorAttribute.AmbientLight)
        if (ambientLight != null) {
            program.setUniformf(u_ambientLight, ambientLight.color.r, ambientLight.color.g, ambientLight.color.b)
        }
    }

    override fun compareTo(other: Shader?): Int {
        if (other == null) return -1
        return 0
    }

    override fun canRender(instance: Renderable): Boolean {
        if (combineAttributeMasks(instance) != attributesMask) {
            return false
        }

        return terrainMaterialMask == getTerrainMaterial(instance)!!.mask
    }

    companion object {
        private fun combineAttributeMasks(renderable: Renderable): Long {
            var mask: Long = 0
            if (renderable.environment != null) mask = mask or renderable.environment.mask
            if (renderable.material != null) mask = mask or renderable.material.mask
            return mask
        }

        val defaultVertexShader: String
            get() = Gdx.files.internal("shaders/terrain.vert.glsl").readString()

        val defaultFragmentShader: String
            get() = Gdx.files.internal("shaders/terrain.frag.glsl").readString()

        private fun getTerrainMaterial(renderable: Renderable): TerrainMaterial? {
            return renderable.material.get(TerrainMaterialAttribute::class.java, TerrainMaterialAttribute.TerrainMaterial).terrainMaterial
        }
    }
}
