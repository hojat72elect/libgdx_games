

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class LifeLink extends FlavourBuff {

    public int object = 0;

    private static final String OBJECT = "object";

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    @Override
    public void detach() {
        super.detach();
        Char ch = (Char) Actor.findById(object);
        if (!target.isActive() && ch != null) {
            for (LifeLink l : ch.buffs(LifeLink.class)) {
                if (l.object == target.id()) {
                    l.detach();
                }
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(OBJECT, object);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        object = bundle.getInt(OBJECT);
    }

    @Override
    public int icon() {
        return BuffIndicator.HERB_HEALING;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1, 0, 1);
    }

    @Override
    public float iconFadePercent() {
        int duration = Math.round(6.67f + 3.33f * Dungeon.hero.pointsInTalent(Talent.LIFE_LINK));
        return Math.max(0, (duration - visualcooldown()) / duration);
    }
}
