

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.watabou.utils.Random;

public class Crab extends Mob {

    {
        spriteClass = CrabSprite.class;

        HP = HT = 15;
        defenseSkill = 5;
        baseSpeed = 2f;

        EXP = 4;
        maxLvl = 9;

        loot = MysteryMeat.class;
        lootChance = 0.167f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 7);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 4);
    }
}
