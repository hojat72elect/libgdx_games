package com.bitfire.uracer.u3d.materials

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Array

class Material(var name: String?, private val attributes: Array<MaterialAttribute>) : Iterable<MaterialAttribute> {

    constructor(name: String, vararg attributes: MaterialAttribute) : this(name, Array<MaterialAttribute>(attributes))

    // This flag is true if the material contains blendingAttribute.
    private var needBlending = false

    // This flag is true if material contains TextureAttribute.
    private var hasTexture = false


    private var shader: ShaderProgram? = null

    init {
        // this way we foresee if blending is needed with this material and rendering can deferred more easily
        for (i in 0..<this.attributes.size) {
            if (!needBlending && this.attributes.get(i) is BlendingAttribute)
                this.needBlending = true
            else if (!hasTexture && this.attributes.get(i) is TextureAttribute)
                this.hasTexture = true
        }
    }

    fun bind(program: ShaderProgram) {
        attributes.forEach { it.bind(program) }
    }

    fun copy(): Material {
        val attributes = Array<MaterialAttribute>(this.attributes.size)
        for (i in 0..<attributes.size) {
            attributes.add(this.attributes.get(i).copy())
        }
        val copy = Material(name, attributes)
        copy.shader = this.shader
        return copy
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + attributes.hashCode()
        result = prime * result + (name?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val other = other as Material
        if (other.attributes.size != attributes.size) return false
        for (i in 0..<attributes.size) {
            if (attributes.get(i) != other.attributes.get(i)) return false
        }
        return if (name == null) {
            other.name == null
        } else name == other.name
    }

    override fun iterator() = attributes.iterator()

}
