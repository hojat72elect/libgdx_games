

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class ExitRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 5);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 5);
    }

    @Override
    public boolean isExit() {
        return true;
    }

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        for (Room.Door door : connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }

        int exit = level.pointToCell(random(2));
        Painter.set(level, exit, Terrain.EXIT);
        level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

    @Override
    public boolean canPlaceCharacter(Point p, Level l) {
        return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
    }

    private static final ArrayList<Class<? extends StandardRoom>> rooms = new ArrayList<>();

    static {
        rooms.add(WaterBridgeExitRoom.class);
        rooms.add(RegionDecoPatchExitRoom.class);
        rooms.add(RingExitRoom.class);
        rooms.add(CircleBasinExitRoom.class);

        rooms.add(RegionDecoLineExitRoom.class);
        rooms.add(ChasmBridgeExitRoom.class);
        rooms.add(PillarsExitRoom.class);
        rooms.add(CellBlockExitRoom.class);

        rooms.add(CaveExitRoom.class);
        rooms.add(RegionDecoBridgeExitRoom.class);
        rooms.add(CavesFissureExitRoom.class);
        rooms.add(CircleWallExitRoom.class);

        rooms.add(HallwayExitRoom.class);
        rooms.add(StatuesExitRoom.class);
        rooms.add(LibraryHallExitRoom.class);
        rooms.add(LibraryRingExitRoom.class);

        rooms.add(RegionDecoPatchExitRoom.class);
        rooms.add(RuinsExitRoom.class);
        rooms.add(ChasmExitRoom.class);
        rooms.add(RitualExitRoom.class);
    }

    private static final float[][] chances = new float[27][];

    static {
        //floor 1 only uses simpler exit rooms
        chances[1] = new float[]{4, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        chances[2] = new float[]{4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        chances[5] = chances[4] = chances[3] = chances[2];

        chances[6] = new float[]{0, 0, 0, 0, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        chances[10] = chances[9] = chances[8] = chances[7] = chances[6];

        chances[11] = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0};
        chances[15] = chances[14] = chances[13] = chances[12] = chances[11];

        chances[16] = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 3, 2, 1, 0, 0, 0, 0};
        chances[20] = chances[19] = chances[18] = chances[17] = chances[16];

        chances[21] = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 3, 2, 1};
        chances[26] = chances[25] = chances[24] = chances[23] = chances[22] = chances[21];
    }

    public static StandardRoom createExit() {
        return Reflection.newInstance(rooms.get(Random.chances(chances[Dungeon.depth])));
    }
}
