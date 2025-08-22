package com.bitfire.uracer.u3d.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Pool;

public class ColorAttribute extends MaterialAttribute {

    static final public String diffuse = "diffuseColor";
    static final public String specular = "specularColor";
    static final public String emissive = "emissiveColor";
    static final public String rim = "rimColor";
    static final public String fog = "fogColor";
    private final static Pool<ColorAttribute> pool = new Pool<ColorAttribute>() {
        @Override
        protected ColorAttribute newObject() {
            return new ColorAttribute();
        }
    };
    public final Color color = new Color();

    protected ColorAttribute() {
    }

    /**
     * Creates a {@link MaterialAttribute} that is a pure {@link Color}.
     *
     * @param color The {@link Color} that you wish the attribute to represent.
     * @param name  The name of the uniform in the {@link ShaderProgram} that will have its value set to this color. (A 'name' does
     *              not matter for a game that uses {@link GL10}).
     */
    public ColorAttribute(Color color, String name) {
        super(name);
        this.color.set(color);
    }

    @Override
    public void bind(ShaderProgram program) {
        program.setUniformf(name, color.r, color.g, color.b, color.a);
    }

    @Override
    public MaterialAttribute copy() {
        return new ColorAttribute(color, name);
    }

    @Override
    public void set(MaterialAttribute attr) {
        ColorAttribute colAttr = (ColorAttribute) attr;
        name = colAttr.name;
        final Color c = colAttr.color;
        color.r = c.r;
        color.g = c.g;
        color.b = c.b;
        color.a = c.a;
    }

    @Override
    public MaterialAttribute pooledCopy() {
        ColorAttribute attr = pool.obtain();
        attr.set(this);
        return attr;
    }

    @Override
    public void free() {
        if (isPooled) pool.free(this);
    }
}
