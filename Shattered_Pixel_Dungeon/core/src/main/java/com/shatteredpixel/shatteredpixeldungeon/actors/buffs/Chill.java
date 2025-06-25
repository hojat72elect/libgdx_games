

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Chill extends FlavourBuff {

    public static final float DURATION = 10f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public boolean attachTo(Char target) {
        Buff.detach(target, Burning.class);

        return super.attachTo(target);
    }

    //reduces speed by 10% for every turn remaining, capping at 50%
    public float speedFactor() {
        return Math.max(0.5f, 1 - cooldown() * 0.1f);
    }

    @Override
    public int icon() {
        return BuffIndicator.FROST;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.CHILLED);
        else target.sprite.remove(CharSprite.State.CHILLED);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(), Messages.decimalFormat("#.##", (1f - speedFactor()) * 100f));
    }
}
