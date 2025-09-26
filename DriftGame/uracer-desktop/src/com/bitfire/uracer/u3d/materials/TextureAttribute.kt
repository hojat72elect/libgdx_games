package com.bitfire.uracer.u3d.materials

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.Texture.TextureWrap
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Pool

class TextureAttribute : MaterialAttribute {

    var texture: Texture? = null
    var unit = 0
    var minFilter = 0
    var magFilter = 0

    @JvmField
    var uWrap = 0

    @JvmField
    var vWrap = 0

    private constructor()

    @JvmOverloads
    constructor(
        texture: Texture,
        unit: Int,
        name: String,
        minFilter: TextureFilter = texture.getMinFilter(),
        magFilter: TextureFilter = texture.getMagFilter(),
        uWrap: TextureWrap = texture.getUWrap(),
        vWrap: TextureWrap = texture.getVWrap()
    ) : this(texture, unit, name, minFilter.getGLEnum(), magFilter.getGLEnum(), uWrap.getGLEnum(), vWrap.getGLEnum())

    constructor(texture: Texture, unit: Int, name: String, minFilter: Int, magFilter: Int, uWrap: Int, vWrap: Int) : super(name) {
        this.texture = texture
        if (unit > MAX_TEXTURE_UNITS) throw RuntimeException("$MAX_TEXTURE_UNITS is max texture units supported")
        this.unit = unit
        this.uWrap = uWrap
        this.vWrap = vWrap
        this.minFilter = minFilter
        this.magFilter = magFilter
    }

    override fun bind(program: ShaderProgram) {
        texture!!.bind(unit)
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, minFilter.toFloat())
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, magFilter.toFloat())
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, uWrap.toFloat())
        Gdx.gl.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, vWrap.toFloat())
        program.setUniformi(name, unit)
    }

    override fun copy() = TextureAttribute(texture!!, unit, name!!, minFilter, magFilter, uWrap, vWrap)

    override fun set(attr: MaterialAttribute) {
        val texAttr = attr as TextureAttribute
        name = texAttr.name
        texture = texAttr.texture
        unit = texAttr.unit
        magFilter = texAttr.magFilter
        minFilter = texAttr.minFilter
        uWrap = texAttr.uWrap
        vWrap = texAttr.vWrap
    }

    override fun free() {
        if (isPooled) pool.free(this)
    }

    companion object {
        const val MAX_TEXTURE_UNITS = 16

        private val pool: Pool<TextureAttribute> = object : Pool<TextureAttribute>() {
            override fun newObject() = TextureAttribute()
        }
    }
}
