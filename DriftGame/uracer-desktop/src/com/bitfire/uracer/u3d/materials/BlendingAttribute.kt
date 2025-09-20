package com.bitfire.uracer.u3d.materials

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Pool

class BlendingAttribute (var attributeName: String? = null, var blendSrcFunc: Int = 0, var blendDstFunc: Int = 0) : MaterialAttribute(attributeName) {

    override fun bind(program: ShaderProgram) {
        Gdx.gl.glBlendFunc(blendSrcFunc, blendDstFunc)
    }

    override fun copy(): MaterialAttribute = BlendingAttribute(this.attributeName, this.blendSrcFunc, this.blendDstFunc)

    override fun set(attr: MaterialAttribute) {
        val blendAttr = attr as BlendingAttribute
        attributeName = blendAttr.name
        blendDstFunc = blendAttr.blendDstFunc
        blendSrcFunc = blendAttr.blendSrcFunc
    }

    override fun free() {
        if (isPooled) pool.free(this)
    }

    companion object {
        private val pool = object : Pool<BlendingAttribute>() {
            override fun newObject() = BlendingAttribute()
        }
    }
}
