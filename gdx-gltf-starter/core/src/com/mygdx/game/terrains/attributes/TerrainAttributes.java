package com.mygdx.game.terrains.attributes;

import com.badlogic.gdx.utils.Array;
import java.util.Comparator;
import java.util.Iterator;

public class TerrainAttributes implements Iterable<TerrainAttribute>, Comparator<TerrainAttribute>, Comparable<TerrainAttributes> {
    protected long mask;
    protected final Array<TerrainAttribute> attributes = new Array<>();

    protected boolean sorted = true;

    /**
     * Sort the attributes by their ID
     */
    public final void sort() {
        if (!sorted) {
            attributes.sort(this);
            sorted = true;
        }
    }

    /**
     * @return Bitwise mask of the ID's of all the containing attributes
     */
    public final long getMask() {
        return mask;
    }

    /**
     * Example usage: ((BlendingAttribute)material.get(BlendingAttribute.ID)).sourceFunction;
     *
     * @return The attribute (which can safely be cast) if any, otherwise null
     */
    public final TerrainAttribute get(final long type) {
        if (has(type)) for (int i = 0; i < attributes.size; i++)
            if (attributes.get(i).type == type) return attributes.get(i);
        return null;
    }

    /**
     * Example usage: ((BlendingAttribute)material.get(BlendingAttribute.ID)).sourceFunction;
     *
     * @return The attribute if any, otherwise null
     */
    public final <T extends TerrainAttribute> T get(Class<T> clazz, final long type) {
        return (T) get(type);
    }

    private void enable(final long mask) {
        this.mask |= mask;
    }

    /**
     * Add a attribute to this material. If the material already contains an attribute of the same type it is overwritten.
     */
    public final void set(final TerrainAttribute attribute) {
        final int idx = indexOf(attribute.type);
        if (idx < 0) {
            enable(attribute.type);
            attributes.add(attribute);
            sorted = false;
        } else {
            attributes.set(idx, attribute);
        }
        sort();
    }

    /**
     * @return True if this collection has the specified attribute, i.e. attributes.has(ColorAttribute.Diffuse); Or when multiple
     * attribute types are specified, true if this collection has all specified attributes, i.e. attributes.has(out,
     * ColorAttribute.Diffuse | ColorAttribute.Specular | TextureAttribute.Diffuse);
     */
    public final boolean has(final long type) {
        return type != 0 && (this.mask & type) == type;
    }

    /**
     * @return the index of the attribute with the specified type or negative if not available.
     */
    protected int indexOf(final long type) {
        if (has(type)) for (int i = 0; i < attributes.size; i++)
            if (attributes.get(i).type == type) return i;
        return -1;
    }

    /**
     * Check if this collection has the same attributes as the other collection. If compareValues is true, it also compares the
     * values of each attribute.
     *
     * @param compareValues True to compare attribute values, false to only compare attribute types
     * @return True if this collection contains the same attributes (and optionally attribute values) as the other.
     */
    public final boolean same(final TerrainAttributes other, boolean compareValues) {
        if (other == this) return true;
        if ((other == null) || (mask != other.mask)) return false;
        if (!compareValues) return true;
        sort();
        other.sort();
        for (int i = 0; i < attributes.size; i++)
            if (!attributes.get(i).equals(other.attributes.get(i))) return false;
        return true;
    }

    /**
     * Used for sorting attributes by type (not by value)
     */
    @Override
    public final int compare(final TerrainAttribute arg0, final TerrainAttribute arg1) {
        return (int) (arg0.type - arg1.type);
    }

    /**
     * Used for iterating through the attributes
     */
    @Override
    public final Iterator<TerrainAttribute> iterator() {
        return attributes.iterator();
    }

    /**
     * @return A hash code based on only the attribute values, which might be different compared to {@link #hashCode()} because the latter
     * might include other properties as well, i.e. the material id.
     */
    public int attributesHash() {
        sort();
        final int n = attributes.size;
        long result = 71 + mask;
        int m = 1;
        for (int i = 0; i < n; i++)
            result += mask * attributes.get(i).hashCode() * (m = (m * 7) & 0xFFFF);
        return (int) (result ^ (result >> 32));
    }

    @Override
    public int hashCode() {
        return attributesHash();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TerrainAttributes)) return false;
        if (other == this) return true;
        return same((TerrainAttributes) other, true);
    }

    @Override
    public int compareTo(TerrainAttributes other) {
        if (other == this)
            return 0;
        if (mask != other.mask)
            return mask < other.mask ? -1 : 1;
        sort();
        other.sort();
        for (int i = 0; i < attributes.size; i++) {
            final int c = attributes.get(i).compareTo(other.attributes.get(i));
            if (c != 0)
                return Integer.compare(c, 0);
        }
        return 0;
    }
}
