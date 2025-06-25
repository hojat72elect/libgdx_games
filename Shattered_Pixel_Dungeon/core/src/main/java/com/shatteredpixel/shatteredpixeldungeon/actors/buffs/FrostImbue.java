

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class FrostImbue extends FlavourBuff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION = 50f;

    public void proc(Char enemy) {
        Buff.affect(enemy, Chill.class, 3f);
        enemy.sprite.emitter().burst(SnowParticle.FACTORY, 3);
    }

    @Override
    public int icon() {
        return BuffIndicator.IMBUE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0, 2f, 3f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    {
        immunities.add(Frost.class);
        immunities.add(Chill.class);
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            Buff.detach(target, Frost.class);
            Buff.detach(target, Chill.class);
            return true;
        } else {
            return false;
        }
    }
}
