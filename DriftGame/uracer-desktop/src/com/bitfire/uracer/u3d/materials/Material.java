package com.bitfire.uracer.u3d.materials;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

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

    public Material() {
        attributes = new Array<>(2);
    }

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

    public void addAttribute(MaterialAttribute... attributes) {
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] instanceof BlendingAttribute)
                needBlending = true;
            else if (attributes[i] instanceof TextureAttribute) hasTexture = true;
            this.attributes.add(attributes[i]);
        }
    }

    public void removeAttribute(MaterialAttribute... attributes) {
        for (int i = 0; i < attributes.length; i++)
            this.attributes.removeValue(attributes[i], true);
        checkAttributes();
    }

    public void clearAttributes() {
        attributes.clear();
        needBlending = false;
    }

    public MaterialAttribute getAttribute(int index) {
        if (index >= 0 && index < attributes.size) return attributes.get(index);
        return null;
    }

    public int getNumberOfAttributes() {
        return attributes.size;
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

    public boolean shaderEquals(Material other) {
        if (this == other) return true;

        int len = this.attributes.size;
        if (len != other.attributes.size) return false;

        for (int i = 0; i < len; i++) {
            final String str = this.attributes.get(i).name;
            if (str == null) return false;

            boolean matchFound = false;
            for (int j = 0; j < len; j++) {
                if (str.equals(other.attributes.get(j).name)) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) return false;
        }

        return true;
    }

    public void setPooled(Material material) {
        name = material.name;
        shader = material.shader;
        needBlending = material.needBlending;
        hasTexture = material.hasTexture;
        attributes.clear();
        for (int i = 0, len = material.attributes.size; i < len; i++) {
            attributes.add(material.attributes.get(i).pooledCopy());
        }
    }

    public boolean isNeedBlending() {
        return needBlending;
    }

    public boolean hasTexture() {
        return hasTexture;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public void setShader(final ShaderProgram shader) {
        this.shader = shader;
    }

    public void resetShader() {
        shader = null;
    }

    @Override
    public Iterator<MaterialAttribute> iterator() {
        return attributes.iterator();
    }

    /*
     * TODO: Sits in Experimental only used for ProtoRenderer public void generateShader (MaterialShaderHandler
     * materialShaderHandler) { shader = materialShaderHandler.getShader(this); }
     */
}
