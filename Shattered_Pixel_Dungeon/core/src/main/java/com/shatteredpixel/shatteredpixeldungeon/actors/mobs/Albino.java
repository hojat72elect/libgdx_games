

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlbinoSprite;
import com.watabou.utils.Random;

public class Albino extends Rat {

    {
        spriteClass = AlbinoSprite.class;

        HP = HT = 15;
        EXP = 2;

        loot = MysteryMeat.class;
        lootChance = 1f;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        if (damage > 0 && Random.Int(2) == 0) {
            Buff.affect(enemy, Bleeding.class).set(damage);
        }

        return damage;
    }
}
