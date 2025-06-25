

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeniorSprite;
import com.watabou.utils.Random;

public class Senior extends Monk {

    {
        spriteClass = SeniorSprite.class;

        loot = Pasty.class;
        lootChance = 1f;
    }

    @Override
    public void move(int step, boolean travelling) {
        // on top of the existing move bonus, senior monks get a further 1.66 cooldown reduction
        // for a total of 3.33, double the normal 1.67 for regular monks
        if (travelling) focusCooldown -= 1.66f;
        super.move(step, travelling);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(16, 25);
    }
}
