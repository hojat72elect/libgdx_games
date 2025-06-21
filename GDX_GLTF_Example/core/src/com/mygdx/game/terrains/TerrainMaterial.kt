package com.mygdx.game.terrains

import com.mygdx.game.terrains.attributes.TerrainAttributes

class TerrainMaterial
/**
 * Create an empty material
 */ @JvmOverloads constructor(var id: String = "mtl" + (++counter)) : TerrainAttributes() {
    /**
     * Create an empty material
     */

    override fun hashCode(): Int {
        return super.hashCode() + 3 * id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other is TerrainMaterial) && ((other === this) || ((other.id == id) && super.equals(other)))
    }

    companion object {
        private var counter = 0
    }
}
