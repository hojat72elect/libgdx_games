

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BanditSprite;
import com.watabou.utils.Random;

public class Bandit extends Thief {

    public Item item;

    {
        spriteClass = BanditSprite.class;

        //guaranteed first drop, then 1/3, 1/9, etc.
        lootChance = 1f;
    }

    @Override
    protected boolean steal(Hero hero) {
        if (super.steal(hero)) {

            Buff.prolong(hero, Blindness.class, Blindness.DURATION / 2f);
            Buff.affect(hero, Poison.class).set(Random.IntRange(5, 6));
            Buff.prolong(hero, Cripple.class, Cripple.DURATION / 2f);
            Dungeon.observe();

            return true;
        } else {
            return false;
        }
    }
}
