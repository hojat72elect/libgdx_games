

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;

public class TenguDartTrap extends PoisonDartTrap {

    {
        canBeHidden = true;
        canBeSearched = false;
    }

    @Override
    protected int poisonAmount() {
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            return 15; //50 damage total, equal to poison dart traps on floor 10
        } else {
            return 8; //17 damage total
        }
    }

    @Override
    protected boolean canTarget(Char ch) {
        return !(ch instanceof Tengu);
    }
}
