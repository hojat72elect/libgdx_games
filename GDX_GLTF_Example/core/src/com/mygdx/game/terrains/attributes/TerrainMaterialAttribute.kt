package com.mygdx.game.terrains.attributes

import com.badlogic.gdx.graphics.g3d.Attribute
import com.mygdx.game.terrains.TerrainMaterial

/**
 * A standard libGDX attribute to hold our TerrainMaterial.
 */
class TerrainMaterialAttribute : Attribute {
    @JvmField
    var terrainMaterial: TerrainMaterial? = null

    private constructor(type: Long) : super(type)

    constructor(type: Long, terrainMaterial: TerrainMaterial?) : super(type) {
        this.terrainMaterial = terrainMaterial
    }

    override fun copy(): Attribute {
        return TerrainMaterialAttribute(this.type)
    }

    override fun compareTo(o: Attribute): Int {
        return 0
    }

    companion object {
        private const val TERRAIN_MATERIAL_ALIAS = "terrainData"

        @JvmField
        val TerrainMaterial: Long = register(TERRAIN_MATERIAL_ALIAS)

        @JvmStatic
        fun createTerrainMaterialAttribute(terrainMaterial: TerrainMaterial?): TerrainMaterialAttribute {
            return TerrainMaterialAttribute(TerrainMaterial, terrainMaterial)
        }
    }
}
