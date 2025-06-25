

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

public class RegionDecoLineEntranceRoom extends StatueLineEntranceRoom {

    @Override
    protected int decoTerrain() {
        return Terrain.REGION_DECO;
    }
}
