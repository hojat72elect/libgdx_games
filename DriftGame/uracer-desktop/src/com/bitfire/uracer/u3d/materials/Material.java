package com.bitfire.uracer.u3d.materials;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Material implements Iterable<MaterialAttribute> {
    protected String name;
    /**
     * This flag is true if material contain blendingAttribute
     */
    protected boolean needBlending;
    /**
     * This flag is true if material contain TextureAttribute
     */
    protected boolean hasTexture;
    protected ShaderProgram shader;
    private final Array<MaterialAttribute> attributes;

    public Material(String name, Array<MaterialAttribute> attributes) {
        this.name = name;
        this.attributes = attributes;

        checkAttributes();
    }

    public Material(String name, MaterialAttribute... attributes) {
        this(name, new Array<>(attributes));
    }

    protected void checkAttributes() {
        // this way we foresee if blending is needed with this material and rendering can deferred more easily
        this.needBlending = false;
        this.hasTexture = false;
        for (int i = 0; i < this.attributes.size; i++) {
            if (!needBlending && this.attributes.get(i) instanceof BlendingAttribute)
                this.needBlending = true;
            else if (!hasTexture && this.attributes.get(i) instanceof TextureAttribute) this.hasTexture = true;
        }
    }

    public void bind(ShaderProgram program) {
        for (int i = 0; i < attributes.size; i++) {
            attributes.get(i).bind(program);
        }
    }

    public String getName() {
        return name;
    }

    public Material copy() {
        Array<MaterialAttribute> attributes = new Array<>(this.attributes.size);
        for (int i = 0; i < attributes.size; i++) {
            attributes.add(this.attributes.get(i).copy());
        }
        final Material copy = new Material(name, attributes);
        copy.shader = this.shader;
        return copy;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + attributes.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Material other = (Material) obj;
        if (other.attributes.size != attributes.size) return false;
        for (int i = 0; i < attributes.size; i++) {
            if (!attributes.get(i).equals(other.attributes.get(i))) return false;
        }
        if (name == null) {
            return other.name == null;
        } else return name.equals(other.name);
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public void setShader(final ShaderProgram shader) {
        this.shader = shader;
    }

    @NotNull
    @Override
    public Iterator<MaterialAttribute> iterator() {
        return attributes.iterator();
    }
}
