package com.mygdx.game.terrains.attributes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

public class TerrainTextureAttribute extends TerrainAttribute {
    public final static String DiffuseBaseAlias = "diffuseBaseTexture";
    public final static long DiffuseBase = register(DiffuseBaseAlias);

    public final static String DiffuseHeightAlias = "diffuseHeightTexture";
    public final static long DiffuseHeight = register(DiffuseHeightAlias);

    public final static String DiffuseSlopeAlias = "diffuseSlopeTexture";
    public final static long DiffuseSlope = register(DiffuseSlopeAlias);

    protected static long Mask = DiffuseHeight | DiffuseBase | DiffuseSlope;

    public static boolean is(final long mask) {
        return (mask & Mask) != 0;
    }

    public static TerrainTextureAttribute createDiffuseBase(final Texture texture) {
        return new TerrainTextureAttribute(DiffuseBase, texture);
    }

    public static TerrainTextureAttribute createDiffuseSlope(final Texture texture) {
        return new TerrainTextureAttribute(DiffuseSlope, texture);
    }

    public static TerrainTextureAttribute createDiffuseHeight(final Texture texture) {
        return new TerrainTextureAttribute(DiffuseHeight, texture);
    }

    public final TextureDescriptor<Texture> textureDescription;
    public float offsetU = 0;
    public float offsetV = 0;
    public float scaleU = 1;
    public float scaleV = 1;
    /**
     * The index of the texture coordinate vertex attribute to use for this TextureAttribute. Whether this value is used, depends
     * on the shader and {@link Attribute#type} value. For basic (model specific) types
     * etc.), this value is usually ignored and the first texture coordinate vertex attribute is used.
     */
    public int uvIndex = 0;

    public TerrainTextureAttribute(final long type) {
        super(type);
        if (!is(type)) throw new GdxRuntimeException("Invalid type specified");
        textureDescription = new TextureDescriptor<>();
    }

    public TerrainTextureAttribute(final long type, final Texture texture) {
        this(type);
        textureDescription.texture = texture;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 991 * result + textureDescription.hashCode();
        result = 991 * result + NumberUtils.floatToRawIntBits(offsetU);
        result = 991 * result + NumberUtils.floatToRawIntBits(offsetV);
        result = 991 * result + NumberUtils.floatToRawIntBits(scaleU);
        result = 991 * result + NumberUtils.floatToRawIntBits(scaleV);
        result = 991 * result + uvIndex;
        return result;
    }

    @Override
    public int compareTo(TerrainAttribute o) {
        if (type != o.type) return type < o.type ? -1 : 1;
        TerrainTextureAttribute other = (TerrainTextureAttribute) o;
        final int c = textureDescription.compareTo(other.textureDescription);
        if (c != 0) return c;
        if (uvIndex != other.uvIndex) return uvIndex - other.uvIndex;
        if (!MathUtils.isEqual(scaleU, other.scaleU)) return scaleU > other.scaleU ? 1 : -1;
        if (!MathUtils.isEqual(scaleV, other.scaleV)) return scaleV > other.scaleV ? 1 : -1;
        if (!MathUtils.isEqual(offsetU, other.offsetU)) return offsetU > other.offsetU ? 1 : -1;
        if (!MathUtils.isEqual(offsetV, other.offsetV)) return offsetV > other.offsetV ? 1 : -1;
        return 0;
    }
}
