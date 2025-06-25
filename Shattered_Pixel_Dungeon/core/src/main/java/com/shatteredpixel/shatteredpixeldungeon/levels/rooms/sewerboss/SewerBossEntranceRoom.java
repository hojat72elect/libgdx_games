

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance.EntranceRoom;

public class SewerBossEntranceRoom extends EntranceRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 7);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 7);
    }

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Painter.fill(level, left + 1, top + 1, width() - 2, 1, Terrain.WALL_DECO);
        Painter.fill(level, left + 1, top + 2, width() - 2, 1, Terrain.WATER);

        int entrance;
        do {
            entrance = level.pointToCell(random(3));
        } while (level.findMob(entrance) != null);
        Painter.set(level, entrance, Terrain.ENTRANCE);
        level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));

        for (Room.Door door : connected.values()) {
            door.set(Room.Door.Type.REGULAR);

            if (door.y == top || door.y == top + 1) {
                Painter.drawInside(level, this, door, 1, Terrain.WATER);
            }
        }
    }
}
