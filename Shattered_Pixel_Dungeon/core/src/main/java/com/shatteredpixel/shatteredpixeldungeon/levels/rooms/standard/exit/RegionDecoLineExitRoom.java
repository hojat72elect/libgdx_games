

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

public class RegionDecoLineExitRoom extends StatueLineExitRoom {

    @Override
    protected int decoTerrain() {
        return Terrain.REGION_DECO;
    }
}
