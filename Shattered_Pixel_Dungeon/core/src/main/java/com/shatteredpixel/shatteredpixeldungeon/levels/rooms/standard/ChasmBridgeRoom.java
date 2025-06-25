

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

public class ChasmBridgeRoom extends StandardBridgeRoom {

    protected int maxBridgeWidth(int roomDimension) {
        return roomDimension >= 7 ? 2 : 1;
    }

    protected int spaceTile() {
        return Terrain.CHASM;
    }
}
