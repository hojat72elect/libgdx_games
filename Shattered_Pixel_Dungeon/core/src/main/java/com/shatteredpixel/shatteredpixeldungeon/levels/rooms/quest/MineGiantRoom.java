

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalSpire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FungalCore;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGeomancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CaveRoom;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class MineGiantRoom extends CaveRoom {

    @Override
    public float[] sizeCatProbs() {
        return new float[]{0, 0, 1};
    }

    @Override
    protected float fill() {
        return 0.70f;
    }

    @Override
    public int mobSpawnWeight() {
        //This room contains the boss
        // so don't amp up regular enemy spawns too
        return 1;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        if (Blacksmith.Quest.Type() == Blacksmith.Quest.CRYSTAL) {
            Painter.fillEllipse(level, this, 3, Terrain.EMPTY);

            for (int i = 0; i < width() * height() / 2; i++) {
                Point r = random(1);
                if (level.map[level.pointToCell(r)] != Terrain.WALL) {
                    Painter.set(level, r, Terrain.MINE_CRYSTAL);
                }
            }

            Point p = center();
            CrystalSpire m = new CrystalSpire();
            m.pos = level.pointToCell(p);
            level.mobs.add(m);
            Painter.set(level, p, Terrain.EMPTY);
        } else if (Blacksmith.Quest.Type() == Blacksmith.Quest.GNOLL) {
            Painter.fillEllipse(level, this, 3, Terrain.EMPTY);

            //connections to non-secret rooms have a 9/10 chance to become empty, otherwise wall
            for (Room n : connected.keySet()) {
                if (!(n instanceof SecretRoom) && connected.get(n).type == Door.Type.REGULAR) {
                    if (Random.Int(10) == 0) {
                        connected.get(n).set(Door.Type.EMPTY);
                    } else {
                        connected.get(n).set(Door.Type.WALL);
                    }
                    connected.get(n).lockTypeChanges(true);
                }
            }

            ArrayList<Door> doors = new ArrayList<>();
            for (Door d : connected.values()) {
                if (d.type == Door.Type.WALL) {
                    doors.add(d);
                }
            }

            for (Point p : getPoints()) {
                int cell = level.pointToCell(p);
                if (level.map[cell] == Terrain.EMPTY) {
                    float dist = 1000;
                    for (Door d : doors) {
                        dist = Math.min(dist, Point.distance(p, d));
                    }
                    dist = GameMath.gate(1f, dist - 0.5f, 3.1f);
                    float val = Random.Float((float) Math.pow(dist, 2));
                    if (val <= 0.75f || dist <= 1) {
                        Painter.set(level, cell, Terrain.MINE_BOULDER);
                    } else if (val <= 5f && dist <= 3) {
                        Painter.set(level, cell, Terrain.EMPTY_DECO);
                    }
                }
            }

            Point center = center();
            Rect centerArea = new Rect(center.x - 2, center.y - 2, center.x + 3, center.y + 3);
            Painter.fillEllipse(level, centerArea, 0, Terrain.MINE_BOULDER);
            Painter.fill(level, centerArea, 2, Terrain.EMPTY_DECO);

            GnollGeomancer g = new GnollGeomancer();
            g.pos = level.pointToCell(center);
            Buff.affect(g, GnollGeomancer.RockArmor.class).setShield(50);
            level.mobs.add(g);
        } else if (Blacksmith.Quest.Type() == Blacksmith.Quest.FUNGI) {
            Painter.fillEllipse(level, this, 2, Terrain.HIGH_GRASS);

            Painter.fillEllipse(level, this, 3, Terrain.GRASS);

            Painter.fillEllipse(level, this, 4, Terrain.HIGH_GRASS);
            Painter.fillEllipse(level, this, 5, Terrain.GRASS);

            Point p = center();
            FungalCore m = new FungalCore();
            m.pos = level.pointToCell(p);
            level.mobs.add(m);
        } else {
            Painter.fillEllipse(level, this, 3, Terrain.EMPTY);
        }
    }
}
