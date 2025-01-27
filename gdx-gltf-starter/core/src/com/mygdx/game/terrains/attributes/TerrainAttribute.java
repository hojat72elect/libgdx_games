package com.mygdx.game.terrains.attributes;

import com.badlogic.gdx.utils.Array;

/**
 * Extend this class to implement a material attribute. Register the attribute type by statically calling the
 * {@link #register(String)} method, whose return value should be used to instantiate the attribute. A class can implement
 * multiple types.
 */
public abstract class TerrainAttribute implements Comparable<TerrainAttribute> {
    /**
     * The registered type aliases
     */
    private final static Array<String> types = new Array<>();

    /**
     * @return The ID of the specified attribute type, or zero if not available
     */
    public static long getAttributeType(final String alias) {
        for (int i = 0; i < types.size; i++)
            if (types.get(i).compareTo(alias) == 0) return 1L << i;
        return 0;
    }

    /**
     * @return The alias of the specified attribute type, or null if not available.
     */
    public static String getAttributeAlias(final long type) {
        int idx = -1;
        while (type != 0 && ++idx < 63 && (((type >> idx) & 1) == 0))
            ;
        return (idx >= 0 && idx < types.size) ? types.get(idx) : null;
    }

    /**
     * Call this method to register a custom attribute type, see the wiki for an example. If the alias already exists, then that ID
     * will be reused. The alias should be unambiguously and will by default be returned by the call to {@link #toString()}.
     *
     * @param alias The alias of the type to register, must be different for each dirrect type, will be used for debugging
     * @return the ID of the newly registered type, or the ID of the existing type if the alias was already registered
     */
    protected static long register(final String alias) {
        long result = getAttributeType(alias);
        if (result > 0) return result;
        types.add(alias);
        return 1L << (types.size - 1);
    }

    /**
     * The type of this attribute
     */
    public final long type;

    private final int typeBit;

    protected TerrainAttribute(final long type) {
        this.type = type;
        this.typeBit = Long.numberOfTrailingZeros(type);
    }

    protected boolean equals(TerrainAttribute other) {
        return other.hashCode() == hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof TerrainAttribute)) return false;
        final TerrainAttribute other = (TerrainAttribute) obj;
        if (this.type != other.type) return false;
        return equals(other);
    }

    @Override
    public String toString() {
        return getAttributeAlias(type);
    }

    @Override
    public int hashCode() {
        return 7489 * typeBit;
    }
}
