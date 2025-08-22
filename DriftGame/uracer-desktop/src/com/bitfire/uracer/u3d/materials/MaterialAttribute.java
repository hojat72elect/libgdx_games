package com.bitfire.uracer.u3d.materials;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class MaterialAttribute {
    private static final String FLAG = "Flag";
    protected final boolean isPooled;
    public String name;

    protected MaterialAttribute() {
        isPooled = true;
    }

    public MaterialAttribute(String name) {
        this.name = name;
        isPooled = false;
    }

    public abstract void bind(ShaderProgram program);

    public abstract MaterialAttribute copy();

    public abstract MaterialAttribute pooledCopy();

    public abstract void free();

    public abstract void set(MaterialAttribute attr);

    public String getShaderFlag() {
        return name + FLAG;
    }
}
