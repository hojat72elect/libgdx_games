

package com.shatteredpixel.shatteredpixeldungeon.tiles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

public class RaisedTerrainTilemap extends DungeonTilemap {

    public RaisedTerrainTilemap() {
        super(Dungeon.level.tilesTex());
        map(Dungeon.level.map, Dungeon.level.width());
    }

    @Override
    protected int getTileVisual(int pos, int tile, boolean flat) {

        if (flat) return -1;

        if (DungeonWallsTilemap.skipCells.contains(pos)) {
            return -1;
        }

        if (tile == Terrain.HIGH_GRASS) {
            return DungeonTileSheet.getVisualWithAlts(
                    DungeonTileSheet.HIGH_GRASS_UNDERHANG,
                    pos);
        } else if (tile == Terrain.FURROWED_GRASS) {
            return DungeonTileSheet.getVisualWithAlts(
                    DungeonTileSheet.FURROWED_UNDERHANG,
                    pos);
        }

        return -1;
    }
}
