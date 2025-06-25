

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.watabou.utils.Random;

public class MineSecretRoom extends SecretRoom {

    @Override
    public int maxWidth() {
        return 7;
    }

    @Override
    public int maxHeight() {
        return 7;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);

        entrance().set(Door.Type.HIDDEN);

        if (Blacksmith.Quest.Type() == Blacksmith.Quest.CRYSTAL) {
            Painter.fill(level, this, 1, Terrain.MINE_CRYSTAL);
        } else if (Blacksmith.Quest.Type() == Blacksmith.Quest.GNOLL) {
            Painter.fill(level, this, 1, Terrain.EMPTY_SP);
            level.drop(new DarkGold().quantity(Random.NormalIntRange(3, 5)), level.pointToCell(center())).type = Heap.Type.CHEST;
            return;
        } else if (Blacksmith.Quest.Type() == Blacksmith.Quest.FUNGI) {
            Painter.fill(level, this, 1, Terrain.HIGH_GRASS);

            //random plant?

        } else {
            Painter.fill(level, this, 1, Terrain.EMPTY);
        }

        int goldAmount = Random.NormalIntRange(3, 5);

        for (int i = 0; i < goldAmount; i++) {
            int cell;
            do {
                cell = level.pointToCell(random(1));
            } while (level.map[cell] == Terrain.WALL_DECO);
            Painter.set(level, random(1), Terrain.WALL_DECO);
        }
    }
}
