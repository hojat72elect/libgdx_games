

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CausticSlimeSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class CausticSlime extends Slime {

    {
        spriteClass = CausticSlimeSprite.class;

        properties.add(Property.ACIDIC);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(enemy, Ooze.class).set(Ooze.DURATION);
            enemy.sprite.burst(0x000000, 5);
        }

        return super.attackProc(enemy, damage);
    }

    @Override
    public void rollToDropLoot() {
        if (Dungeon.hero.lvl > maxLvl + 2) return;

        super.rollToDropLoot();

        int ofs;
        do {
            ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (Dungeon.level.solid[pos + ofs] && !Dungeon.level.passable[pos + ofs]);
        Dungeon.level.drop(new GooBlob(), pos + ofs).sprite.drop(pos);
    }
}
