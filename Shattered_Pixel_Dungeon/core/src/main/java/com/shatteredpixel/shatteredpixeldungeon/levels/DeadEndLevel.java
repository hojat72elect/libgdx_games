

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeadEndLevel extends Level {

    private static final int SIZE = 5;

    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CAVES;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    @Override
    protected boolean build() {

        setSize(7, 7);

        for (int i = 2; i < SIZE; i++) {
            for (int j = 2; j < SIZE; j++) {
                map[i * width() + j] = Terrain.EMPTY;
            }
        }

        for (int i = 1; i <= SIZE; i++) {
            map[width() + i] =
                    map[width() * SIZE + i] =
                            map[width() * i + 1] =
                                    map[width() * i + SIZE] =
                                            Terrain.WATER;
        }

        int entrance = SIZE * width() + SIZE / 2 + 1;

        //different exit behaviour depending on main branch or side one
        if (Dungeon.branch == 0) {
            transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
        } else {
            transitions.add(new LevelTransition(this,
                    entrance,
                    LevelTransition.Type.BRANCH_ENTRANCE,
                    Dungeon.depth,
                    0,
                    LevelTransition.Type.BRANCH_EXIT));
        }
        map[entrance] = Terrain.ENTRANCE;

        return true;
    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            for (Item i : bonesItems) {
                drop(i, entrance() - width()).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    @Override
    public int randomRespawnCell(Char ch) {
        return entrance() - width();
    }
}
