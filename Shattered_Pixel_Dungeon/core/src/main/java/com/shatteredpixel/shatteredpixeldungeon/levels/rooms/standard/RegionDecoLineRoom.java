

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

public class RegionDecoLineRoom extends StatueLineRoom {

    @Override
    protected int decoTerrain() {
        return Terrain.REGION_DECO;
    }
}
