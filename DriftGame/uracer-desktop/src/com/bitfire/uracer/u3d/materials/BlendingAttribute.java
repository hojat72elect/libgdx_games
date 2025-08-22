package com.bitfire.uracer.u3d.materials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Pool;

public class BlendingAttribute extends MaterialAttribute {

    private final static Pool<BlendingAttribute> pool = new Pool<BlendingAttribute>() {
        @Override
        protected BlendingAttribute newObject() {
            return new BlendingAttribute();
        }
    };
    public int blendSrcFunc;
    public int blendDstFunc;

    protected BlendingAttribute() {
    }

    public BlendingAttribute(String name, int srcFunc, int dstFunc) {
        super(name);
        blendSrcFunc = srcFunc;
        blendDstFunc = dstFunc;
    }

    @Override
    public void bind(ShaderProgram program) {
        Gdx.gl.glBlendFunc(blendSrcFunc, blendDstFunc);
    }

    @Override
    public MaterialAttribute copy() {
        return new BlendingAttribute(this.name, this.blendSrcFunc, this.blendDstFunc);
    }

    @Override
    public void set(MaterialAttribute attr) {
        BlendingAttribute blendAttr = (BlendingAttribute) attr;
        name = blendAttr.name;
        blendDstFunc = blendAttr.blendDstFunc;
        blendSrcFunc = blendAttr.blendSrcFunc;
    }

    @Override
    public void free() {
        if (isPooled) pool.free(this);
    }
}
