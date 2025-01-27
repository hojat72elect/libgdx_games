package com.mygdx.game.terrains.attributes;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.NumberUtils;

public class TerrainFloatAttribute extends TerrainAttribute {
    public static final String MinSlopeAlias = "minSlope";
    public static final long MinSlope = register(MinSlopeAlias);

    public static TerrainFloatAttribute createMinSlope(float value) {
        return new TerrainFloatAttribute(MinSlope, value);
    }

    public float value;

    public TerrainFloatAttribute(long type, float value) {
        super(type);
        this.value = value;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 977 * result + NumberUtils.floatToRawIntBits(value);
        return result;
    }

    @Override
    public int compareTo(TerrainAttribute o) {
        if (type != o.type) return (int) (type - o.type);
        final float v = ((TerrainFloatAttribute) o).value;
        return MathUtils.isEqual(value, v) ? 0 : value < v ? -1 : 1;
    }
}
