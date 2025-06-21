package com.mygdx.game.terrains.attributes

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.NumberUtils

class TerrainTextureAttribute(type: Long) : TerrainAttribute(type) {
    @JvmField
    val textureDescription: TextureDescriptor<Texture>

    @JvmField
    var offsetU: Float = 0f

    @JvmField
    var offsetV: Float = 0f

    @JvmField
    var scaleU: Float = 1f

    @JvmField
    var scaleV: Float = 1f

    /**
     * The index of the texture coordinate vertex attribute to use for this TextureAttribute. Whether this value is used, depends
     * on the shader and [Attribute.type] value. For basic (model specific) types
     * etc.), this value is usually ignored and the first texture coordinate vertex attribute is used.
     */
    var uvIndex: Int = 0

    init {
        if (!`is`(type)) throw GdxRuntimeException("Invalid type specified")
        textureDescription = TextureDescriptor()
    }

    constructor(type: Long, texture: Texture) : this(type) {
        textureDescription.texture = texture
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 991 * result + textureDescription.hashCode()
        result = 991 * result + NumberUtils.floatToRawIntBits(offsetU)
        result = 991 * result + NumberUtils.floatToRawIntBits(offsetV)
        result = 991 * result + NumberUtils.floatToRawIntBits(scaleU)
        result = 991 * result + NumberUtils.floatToRawIntBits(scaleV)
        result = 991 * result + uvIndex
        return result
    }

    override fun compareTo(o: TerrainAttribute?): Int {
        if (type != o!!.type) return if (type < o.type) -1 else 1
        val other = o as TerrainTextureAttribute
        val c = textureDescription.compareTo(other.textureDescription)
        if (c != 0) return c
        if (uvIndex != other.uvIndex) return uvIndex - other.uvIndex
        if (!MathUtils.isEqual(scaleU, other.scaleU)) return if (scaleU > other.scaleU) 1 else -1
        if (!MathUtils.isEqual(scaleV, other.scaleV)) return if (scaleV > other.scaleV) 1 else -1
        if (!MathUtils.isEqual(offsetU, other.offsetU)) return if (offsetU > other.offsetU) 1 else -1
        if (!MathUtils.isEqual(offsetV, other.offsetV)) return if (offsetV > other.offsetV) 1 else -1
        return 0
    }

    companion object {
        const val DiffuseBaseAlias: String = "diffuseBaseTexture"

        @JvmField
        val DiffuseBase: Long = register(DiffuseBaseAlias)

        const val DiffuseHeightAlias: String = "diffuseHeightTexture"

        @JvmField
        val DiffuseHeight: Long = register(DiffuseHeightAlias)

        const val DiffuseSlopeAlias: String = "diffuseSlopeTexture"

        @JvmField
        val DiffuseSlope: Long = register(DiffuseSlopeAlias)

        protected var Mask: Long = DiffuseHeight or DiffuseBase or DiffuseSlope

        fun `is`(mask: Long): Boolean {
            return (mask and Mask) != 0L
        }

        @JvmStatic
        fun createDiffuseBase(texture: Texture): TerrainTextureAttribute {
            return TerrainTextureAttribute(DiffuseBase, texture)
        }

        @JvmStatic
        fun createDiffuseSlope(texture: Texture): TerrainTextureAttribute {
            return TerrainTextureAttribute(DiffuseSlope, texture)
        }

        @JvmStatic
        fun createDiffuseHeight(texture: Texture): TerrainTextureAttribute {
            return TerrainTextureAttribute(DiffuseHeight, texture)
        }
    }
}
