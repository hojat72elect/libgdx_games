package com.mygdx.game.terrains;

import com.mygdx.game.terrains.attributes.TerrainAttributes;

public class TerrainMaterial extends TerrainAttributes {
    private static int counter = 0;

    public String id;

    /**
     * Create an empty material
     */
    public TerrainMaterial() {
        this("mtl" + (++counter));
    }

    /**
     * Create an empty material
     */
    public TerrainMaterial(final String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 3 * id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof TerrainMaterial) && ((other == this) || ((((TerrainMaterial) other).id.equals(id)) && super.equals(other)));
    }
}
