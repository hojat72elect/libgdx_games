

package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ChasmBridgeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.FissureRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PrisonPainter extends RegularPainter {

    @Override
    protected void decorate(Level level, ArrayList<Room> rooms) {

        int w = level.width();
        int l = level.length();
        int[] map = level.map;

        for (int i = w + 1; i < l - w - 1; i++) {
            if (map[i] == Terrain.EMPTY) {

                float c = 0.05f;
                if (map[i + 1] == Terrain.WALL && map[i + w] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i - 1] == Terrain.WALL && map[i + w] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i + 1] == Terrain.WALL && map[i - w] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i - 1] == Terrain.WALL && map[i - w] == Terrain.WALL) {
                    c += 0.2f;
                }

                if (Random.Float() < c) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        for (Room r : rooms) {
            if (r instanceof SpecialRoom) {
                continue;
            }
            int chance = 15; //1/15 by default, some rooms can be more common though
            if (r instanceof FissureRoom) {
                chance = 3;
            } else if (r instanceof ChasmBridgeRoom) {
                chance = 5;
            }

            int cell;
            for (int y = r.bottom - 1; y > r.top; y--) {
                cell = r.left + 1 + level.width() * y;
                for (int x = r.left + 1; x < r.right; x++) {
                    if (level.map[cell] == Terrain.CHASM && level.map[cell - level.width()] == Terrain.CHASM) {
                        if (Random.Int(chance) == 0) {
                            level.map[cell] = Terrain.REGION_DECO_ALT;
                        }
                    }
                    cell++;
                }
            }
        }

        for (int i = 0; i < w; i++) {
            if (map[i] == Terrain.WALL &&
                    (map[i + w] == Terrain.EMPTY || map[i + w] == Terrain.EMPTY_SP) &&
                    Random.Int(6) == 0) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i = w; i < l - w; i++) {
            if (map[i] == Terrain.WALL &&
                    map[i - w] == Terrain.WALL &&
                    (map[i + w] == Terrain.EMPTY || map[i + w] == Terrain.EMPTY_SP) &&
                    Random.Int(3) == 0) {

                map[i] = Terrain.WALL_DECO;
            }
        }
    }
}
