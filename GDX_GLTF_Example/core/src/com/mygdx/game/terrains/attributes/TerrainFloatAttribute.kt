package com.mygdx.game.terrains.attributes

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.NumberUtils

class TerrainFloatAttribute(type: Long, @JvmField var value: Float) : TerrainAttribute(type) {
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 977 * result + NumberUtils.floatToRawIntBits(value)
        return result
    }

    override fun compareTo(o: TerrainAttribute?): Int {
        if (type != o!!.type) return (type - o.type).toInt()
        val v = (o as TerrainFloatAttribute).value
        return if (MathUtils.isEqual(value, v)) 0 else if (value < v) -1 else 1
    }

    companion object {
        const val MinSlopeAlias: String = "minSlope"

        @JvmField
        val MinSlope: Long = register(MinSlopeAlias)

        @JvmStatic
        fun createMinSlope(value: Float): TerrainFloatAttribute {
            return TerrainFloatAttribute(MinSlope, value)
        }
    }
}
