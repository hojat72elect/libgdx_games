

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

public class RitualSiteRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 9);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 9);
    }

    public void paint(Level level) {

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        RitualMarker vis = new RitualMarker();
        Point c = center();
        vis.pos(c.x - 1, c.y - 1);

        level.customTiles.add(vis);

        Painter.fill(level, c.x - 1, c.y - 1, 3, 3, Terrain.CUSTOM_DECO_EMPTY);

        level.addItemToSpawn(new CeremonialCandle());
        level.addItemToSpawn(new CeremonialCandle());
        level.addItemToSpawn(new CeremonialCandle());
        level.addItemToSpawn(new CeremonialCandle());

        CeremonialCandle.ritualPos = c.x + (level.width() * c.y);
    }

    @Override
    public boolean canPlaceItem(Point p, Level l) {
        return super.canPlaceItem(p, l) && l.distance(CeremonialCandle.ritualPos, l.pointToCell(p)) >= 2;
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.distance(CeremonialCandle.ritualPos, l.pointToCell(p)) >= 2;
    }

    public static class RitualMarker extends CustomTilemap {

        {
            texture = Assets.Environment.PRISON_QUEST;

            tileW = tileH = 3;
        }

        final int TEX_WIDTH = 64;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(mapSimpleImage(0, 0, TEX_WIDTH), 3);
            return v;
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(this, "name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(this, "desc");
        }
    }
}
