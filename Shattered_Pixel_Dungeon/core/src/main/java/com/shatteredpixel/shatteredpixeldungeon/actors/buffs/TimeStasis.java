

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;

//this is largely a copy-paste from timekeeper's hourglass with the artifact-specific code removed
public class TimeStasis extends FlavourBuff {

    {
        type = Buff.buffType.POSITIVE;
        actPriority = BUFF_PRIO - 3; //acts after all other buffs, so they are prevented
    }

    @Override
    public boolean attachTo(Char target) {

        if (super.attachTo(target)) {

            target.invisible++;
            target.paralysed++;
            target.next();

            if (Dungeon.hero != null) {
                Dungeon.observe();
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void spend(float time) {
        super.spend(time);

        //don't punish the player for going into stasis frequently
        Hunger hunger = Buff.affect(target, Hunger.class);
        if (hunger != null && !hunger.isStarving()) {
            hunger.affectHunger(cooldown(), true);
        }
    }

    @Override
    public void detach() {
        if (target.invisible > 0) target.invisible--;
        if (target.paralysed > 0) target.paralysed--;
        super.detach();
        Dungeon.observe();
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.PARALYSED);
        else {
            if (target.paralysed == 0) target.sprite.remove(CharSprite.State.PARALYSED);
            if (target.invisible == 0) target.sprite.remove(CharSprite.State.INVISIBLE);
        }
    }
}
