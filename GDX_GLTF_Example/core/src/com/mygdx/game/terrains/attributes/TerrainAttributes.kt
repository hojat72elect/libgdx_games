package com.mygdx.game.terrains.attributes

import com.badlogic.gdx.utils.Array

open class TerrainAttributes : Iterable<TerrainAttribute?>, Comparator<TerrainAttribute>, Comparable<TerrainAttributes> {
    /**
     * @return Bitwise mask of the ID's of all the containing attributes
     */
    var mask: Long = 0
        protected set
    protected val attributes: Array<TerrainAttribute> = Array()

    protected var sorted: Boolean = true

    /**
     * Sort the attributes by their ID
     */
    private fun sort() {
        if (!sorted) {
            attributes.sort(this)
            sorted = true
        }
    }

    /**
     * Example usage: ((BlendingAttribute)material.get(BlendingAttribute.ID)).sourceFunction;
     *
     * @return The attribute (which can safely be cast) if any, otherwise null
     */
    fun get(type: Long): TerrainAttribute? {
        if (has(type)) for (i in 0..<attributes.size) if (attributes[i].type == type) return attributes[i]
        return null
    }

    /**
     * Example usage: ((BlendingAttribute)material.get(BlendingAttribute.ID)).sourceFunction;
     *
     * @return The attribute if any, otherwise null
     */
    fun <T : TerrainAttribute?> get(clazz: Class<T>?, type: Long): T? {
        return get(type) as T?
    }

    private fun enable(mask: Long) {
        this.mask = this.mask or mask
    }

    /**
     * Add a attribute to this material. If the material already contains an attribute of the same type it is overwritten.
     */
    fun set(attribute: TerrainAttribute) {
        val idx = indexOf(attribute.type)
        if (idx < 0) {
            enable(attribute.type)
            attributes.add(attribute)
            sorted = false
        } else {
            attributes[idx] = attribute
        }
        sort()
    }

    /**
     * @return True if this collection has the specified attribute, i.e. attributes.has(ColorAttribute.Diffuse); Or when multiple
     * attribute types are specified, true if this collection has all specified attributes, i.e. attributes.has(out,
     * ColorAttribute.Diffuse | ColorAttribute.Specular | TextureAttribute.Diffuse);
     */
    fun has(type: Long): Boolean {
        return type != 0L && (this.mask and type) == type
    }

    /**
     * @return the index of the attribute with the specified type or negative if not available.
     */
    protected fun indexOf(type: Long): Int {
        if (has(type)) for (i in 0..<attributes.size) if (attributes[i].type == type) return i
        return -1
    }

    /**
     * Check if this collection has the same attributes as the other collection. If compareValues is true, it also compares the
     * values of each attribute.
     *
     * @param compareValues True to compare attribute values, false to only compare attribute types
     * @return True if this collection contains the same attributes (and optionally attribute values) as the other.
     */
    fun same(other: TerrainAttributes?, compareValues: Boolean): Boolean {
        if (other === this) return true
        if ((other == null) || (mask != other.mask)) return false
        if (!compareValues) return true
        sort()
        other.sort()
        for (i in 0..<attributes.size) if (!attributes[i].equals(other.attributes[i])) return false
        return true
    }

    /**
     * Used for sorting attributes by type (not by value)
     */
    override fun compare(arg0: TerrainAttribute, arg1: TerrainAttribute): Int {
        return (arg0.type - arg1.type).toInt()
    }

    /**
     * Used for iterating through the attributes
     */
    override fun iterator(): MutableIterator<TerrainAttribute> {
        return attributes.iterator()
    }

    /**
     * @return A hash code based on only the attribute values, which might be different compared to [.hashCode] because the latter
     * might include other properties as well, i.e. the material id.
     */
    private fun attributesHash(): Int {
        sort()
        val n = attributes.size
        var result = 71 + mask
        var m = 1
        for (i in 0..<n) result += mask * attributes[i].hashCode() * (((m * 7) and 0xFFFF).also { m = it })
        return (result xor (result shr 32)).toInt()
    }

    override fun hashCode(): Int {
        return attributesHash()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TerrainAttributes) return false
        if (other === this) return true
        return same(other, true)
    }

    override fun compareTo(other: TerrainAttributes): Int {
        if (other === this) return 0
        if (mask != other.mask) return if (mask < other.mask) -1 else 1
        sort()
        other.sort()
        for (i in 0..<attributes.size) {
            val c = attributes[i].compareTo(other.attributes[i])
            if (c != 0) return c.compareTo(0)
        }
        return 0
    }
}
