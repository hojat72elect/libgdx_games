package com.bitfire.uracer.u3d.materials

import com.badlogic.gdx.graphics.glutils.ShaderProgram

abstract class MaterialAttribute @JvmOverloads constructor(@JvmField var name: String? = null, @JvmField protected val isPooled: Boolean = name == null) {

    abstract fun bind(program: ShaderProgram)

    abstract fun copy(): MaterialAttribute

    abstract fun free()

    abstract fun set(attr: MaterialAttribute)
}
