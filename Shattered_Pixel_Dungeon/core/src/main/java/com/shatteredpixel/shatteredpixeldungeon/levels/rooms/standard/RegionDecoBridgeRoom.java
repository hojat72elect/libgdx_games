

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

//doesn't look much like a bridge, but can easily use it internally
public class RegionDecoBridgeRoom extends StandardBridgeRoom {

    //can be large because the line breaks the space up
    public float[] sizeCatProbs() {
        return new float[]{2, 1, 0};
    }

    protected int maxBridgeWidth(int roomDimension) {
        return 1;
    }

    protected int spaceTile() {
        return Terrain.REGION_DECO_ALT;
    }

    @Override
    protected int bridgeTile() {
        return Terrain.EMPTY_SP;
    }
}
