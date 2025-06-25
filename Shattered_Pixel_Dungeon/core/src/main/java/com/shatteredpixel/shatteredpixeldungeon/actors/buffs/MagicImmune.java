

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class MagicImmune extends FlavourBuff {

    public static final float DURATION = 20f;

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    {
        immunities.addAll(AntiMagic.RESISTS);
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            for (Buff b : target.buffs()) {
                for (Class immunity : immunities) {
                    if (b.getClass().isAssignableFrom(immunity)) {
                        b.detach();
                        break;
                    }
                }
            }
            if (target instanceof Hero) {
                ((Hero) target).updateHT(false);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        super.detach();
        if (target instanceof Hero) {
            ((Hero) target).updateHT(false);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.COMBO;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0, 1, 0);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }
}
