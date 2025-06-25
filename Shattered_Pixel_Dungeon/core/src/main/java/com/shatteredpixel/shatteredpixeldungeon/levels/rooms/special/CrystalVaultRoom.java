

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.MimicTooth;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.RatSkull;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class CrystalVaultRoom extends SpecialRoom {

    //fixed size to improve presentation and provide space for crystal mimics
    @Override
    public int minHeight() {
        return 7;
    }

    public int maxHeight() {
        return 7;
    }

    public int minWidth() {
        return 7;
    }

    public int maxWidth() {
        return 7;
    }

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);
        Painter.fill(level, this, 2, Terrain.EMPTY);

        int c = level.pointToCell(center());
        Random.shuffle(prizeClasses);

        Item i1, i2;
        i1 = prize();
        i2 = prize();

        int i1Pos, i2Pos;
        int doorPos = level.pointToCell(entrance());
        do {
            int neighbourIdx = Random.Int(PathFinder.CIRCLE8.length);
            i1Pos = c + PathFinder.CIRCLE8[neighbourIdx];
            i2Pos = c + PathFinder.CIRCLE8[(neighbourIdx + 4) % 8];
        } while (level.adjacent(i1Pos, doorPos) || level.adjacent(i2Pos, doorPos));

        level.drop(i1, i1Pos).type = Heap.Type.CRYSTAL_CHEST;
        float altChance = 1 / 10f * RatSkull.exoticChanceMultiplier();
        if (altChance > 0.1f) altChance = (altChance + 0.1f) / 2f; //rat skull is 1/2 as effective here
        altChance *= MimicTooth.mimicChanceMultiplier(); //mimic tooth has full effectiveness
        if (Random.Float() < altChance) {
            level.mobs.add(Mimic.spawnAt(i2Pos, CrystalMimic.class, i2));
        } else {
            level.drop(i2, i2Pos).type = Heap.Type.CRYSTAL_CHEST;
        }
        Painter.set(level, i1Pos, Terrain.PEDESTAL);
        Painter.set(level, i2Pos, Terrain.PEDESTAL);

        level.addItemToSpawn(new CrystalKey(Dungeon.depth));

        entrance().set(Door.Type.LOCKED);
        level.addItemToSpawn(new IronKey(Dungeon.depth));
    }

    private Item prize() {
        Generator.Category cat = prizeClasses.remove(0);
        prizeClasses.add(cat);
        Item prize;
        do {
            prize = Generator.random(cat);
        } while (prize == null || Challenges.isItemBlocked(prize));
        return prize;
    }

    private final ArrayList<Generator.Category> prizeClasses = new ArrayList<>(
            Arrays.asList(Generator.Category.WAND,
                    Generator.Category.RING,
                    Generator.Category.ARTIFACT));
}
