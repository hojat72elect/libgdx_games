

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;

public class WaterBridgeRoom extends StandardBridgeRoom {

    protected int maxBridgeWidth(int roomDimension) {
        return roomDimension >= 8 ? 3 : 2;
    }

    protected int spaceTile() {
        return Terrain.WATER;
    }

    @Override
    public boolean canPlaceWater(Point p) {
        return false;
    }
}
