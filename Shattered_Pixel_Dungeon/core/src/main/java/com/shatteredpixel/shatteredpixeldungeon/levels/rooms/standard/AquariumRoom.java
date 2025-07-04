

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class AquariumRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 7);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 7);
    }

    @Override
    public float[] sizeCatProbs() {
        return new float[]{3, 1, 0};
    }

    @Override
    public boolean canPlaceItem(Point p, Level l) {
        return super.canPlaceItem(p, l) && l.map[l.pointToCell(p)] != Terrain.WATER;
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.map[l.pointToCell(p)] != Terrain.WATER;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);
        Painter.fill(level, this, 2, Terrain.EMPTY_SP);
        Painter.fill(level, this, 3, Terrain.WATER);

        int minDim = Math.min(width(), height());
        int numFish = (minDim - 4) / 3; //1-3 fish, depending on room size

        for (int i = 0; i < numFish; i++) {
            Piranha piranha = Piranha.random();
            do {
                piranha.pos = level.pointToCell(random(3));
            } while (level.map[piranha.pos] != Terrain.WATER || level.findMob(piranha.pos) != null);
            level.mobs.add(piranha);
        }

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }
    }
}
