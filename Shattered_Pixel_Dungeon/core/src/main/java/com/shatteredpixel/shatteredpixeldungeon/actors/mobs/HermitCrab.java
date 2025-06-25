

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HermitCrabSprite;

public class HermitCrab extends Crab {

    {
        spriteClass = HermitCrabSprite.class;

        HP = HT = 25; //+67% HP
        baseSpeed = 1f; //-50% speed

        //3x more likely to drop meat, and drops a guaranteed armor
        lootChance = 0.5f;
    }

    @Override
    public void rollToDropLoot() {
        super.rollToDropLoot();

        if (Dungeon.hero.lvl <= maxLvl + 2) {
            Dungeon.level.drop(Generator.randomArmor(), pos).sprite.drop();
        }
    }

    @Override
    public int drRoll() {
        return super.drRoll() + 2; //2-6 DR total, up from 0-4
    }
}
