

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class PitRoom extends SpecialRoom {

    @Override //increase min size slightly to prevent tiny 3x3 wraith fights
    public int minWidth() {
        return 6;
    }

    public int minHeight() {
        return 6;
    }

    @Override //reduce max size to ensure well is visible in normal circumstances
    public int maxWidth() {
        return 9;
    }

    public int maxHeight() {
        return 9;
    }

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Door entrance = entrance();
        entrance.set(Door.Type.CRYSTAL);

        Point well = null;
        if (entrance.x == left) {
            well = new Point(right - 1, Random.Int(2) == 0 ? top + 1 : bottom - 1);
        } else if (entrance.x == right) {
            well = new Point(left + 1, Random.Int(2) == 0 ? top + 1 : bottom - 1);
        } else if (entrance.y == top) {
            well = new Point(Random.Int(2) == 0 ? left + 1 : right - 1, bottom - 1);
        } else if (entrance.y == bottom) {
            well = new Point(Random.Int(2) == 0 ? left + 1 : right - 1, top + 1);
        }
        Painter.set(level, well, Terrain.EMPTY_WELL);

        int remains = level.pointToCell(center());

        Item mainLoot = null;
        do {
            switch (Random.Int(3)) {
                case 0:
                    mainLoot = Generator.random(Generator.Category.RING);
                    break;
                case 1:
                    mainLoot = Generator.random(Generator.Category.ARTIFACT);
                    break;
                case 2:
                    mainLoot = Generator.random(Random.oneOf(
                            Generator.Category.WEAPON,
                            Generator.Category.ARMOR));
                    break;
            }
        } while (mainLoot == null || Challenges.isItemBlocked(mainLoot));
        level.drop(mainLoot, remains).setHauntedIfCursed().type = Heap.Type.SKELETON;

        int n = Random.IntRange(1, 2);
        for (int i = 0; i < n; i++) {
            level.drop(prize(level), remains).setHauntedIfCursed();
        }

        level.drop(new CrystalKey(Dungeon.depth), remains);
    }

    private static Item prize(Level level) {
        return Generator.random(Random.oneOf(
                Generator.Category.POTION,
                Generator.Category.SCROLL,
                Generator.Category.FOOD,
                Generator.Category.GOLD
        ));
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        //the player is already weak after landing, and will likely need to kite the ghost.
        //having traps here just seems unfair
        return false;
    }

    @Override
    public boolean canPlaceGrass(Point p) {
        return false; //We want the player to be able to see the well through the door
    }
}
