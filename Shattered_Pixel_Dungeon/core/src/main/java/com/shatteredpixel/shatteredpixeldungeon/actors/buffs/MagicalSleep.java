

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class MagicalSleep extends Buff {

    private static final float STEP = 1f;

    @Override
    public boolean attachTo(Char target) {
        if (!target.isImmune(Sleep.class) && super.attachTo(target)) {

            target.paralysed++;

            if (target.alignment == Char.Alignment.ALLY) {
                if (target.HP == target.HT) {
                    if (target instanceof Hero) GLog.i(Messages.get(this, "toohealthy"));
                    detach();
                    return true;
                } else {
                    if (target instanceof Hero) GLog.i(Messages.get(this, "fallasleep"));
                }
            }

            if (target instanceof Mob) {
                ((Mob) target).state = ((Mob) target).SLEEPING;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean act() {
        if (target instanceof Mob && ((Mob) target).state != ((Mob) target).SLEEPING) {
            detach();
            return true;
        }
        if (target.alignment == Char.Alignment.ALLY) {
            target.HP = Math.min(target.HP + 1, target.HT);
            if (target instanceof Hero) ((Hero) target).resting = true;
            if (target.HP == target.HT) {
                if (target instanceof Hero) GLog.p(Messages.get(this, "wakeup"));
                detach();
            }
        }
        spend(STEP);
        return true;
    }

    @Override
    public void detach() {
        if (target.paralysed > 0) {
            target.paralysed--;
        }
        if (target instanceof Hero) {
            ((Hero) target).resting = false;
        } else if (target instanceof Mob && target.alignment == Char.Alignment.ALLY && ((Mob) target).state == ((Mob) target).SLEEPING) {
            ((Mob) target).state = ((Mob) target).WANDERING;
        }
        super.detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.MAGIC_SLEEP;
    }

    @Override
    public void fx(boolean on) {
        if (!on && (target.paralysed <= 1)) {
            //in case the character has visual paralysis from another source
            target.sprite.remove(CharSprite.State.PARALYSED);
        }
    }
}