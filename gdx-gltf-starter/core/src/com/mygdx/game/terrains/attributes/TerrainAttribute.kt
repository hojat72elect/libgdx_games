package com.mygdx.game.terrains.attributes

import com.badlogic.gdx.utils.Array

/**
 * Extend this class to implement a material attribute. Register the attribute type by statically calling the
 * [.register] method, whose return value should be used to instantiate the attribute. A class can implement
 * multiple types.
 */
abstract class TerrainAttribute protected constructor(
    /**
     * The type of this attribute
     */
    @JvmField val type: Long
) : Comparable<TerrainAttribute?> {
    private val typeBit = java.lang.Long.numberOfTrailingZeros(type)

    fun equals(other: TerrainAttribute): Boolean {
        return other.hashCode() == hashCode()
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) return false
        if (obj === this) return true
        if (obj !is TerrainAttribute) return false
        val other = obj
        if (this.type != other.type) return false
        return equals(other)
    }

    override fun toString(): String {
        return getAttributeAlias(type)!!
    }

    override fun hashCode(): Int {
        return 7489 * typeBit
    }

    companion object {
        /**
         * The registered type aliases
         */
        private val types = Array<String>()

        /**
         * @return The ID of the specified attribute type, or zero if not available
         */
        private fun getAttributeType(alias: String): Long {
            for (i in 0..<types.size) if (types[i].compareTo(alias) == 0) return 1L shl i
            return 0
        }

        /**
         * @return The alias of the specified attribute type, or null if not available.
         */
        fun getAttributeAlias(type: Long): String? {
            var idx = -1
            while (type != 0L && ++idx < 63 && (((type shr idx) and 1L) == 0L));
            return if (idx >= 0 && idx < types.size) types[idx] else null
        }

        /**
         * Call this method to register a custom attribute type, see the wiki for an example. If the alias already exists, then that ID
         * will be reused. The alias should be unambiguously and will by default be returned by the call to [.toString].
         *
         * @param alias The alias of the type to register, must be different for each dirrect type, will be used for debugging
         * @return the ID of the newly registered type, or the ID of the existing type if the alias was already registered
         */
        @JvmStatic
        protected fun register(alias: String): Long {
            val result = getAttributeType(alias)
            if (result > 0) return result
            types.add(alias)
            return 1L shl (types.size - 1)
        }
    }
}
