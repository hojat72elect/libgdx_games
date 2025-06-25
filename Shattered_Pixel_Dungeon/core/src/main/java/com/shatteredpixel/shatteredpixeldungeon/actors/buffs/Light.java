

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Light extends FlavourBuff {

    {
        type = buffType.POSITIVE;
    }

    public static final float DURATION = 250f;
    public static final int DISTANCE = 6;

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            if (Dungeon.level != null) {
                target.viewDistance = Math.max(Dungeon.level.viewDistance, DISTANCE);
                Dungeon.observe();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        target.viewDistance = Dungeon.level.viewDistance;
        Dungeon.observe();
        super.detach();
    }

    public void weaken(int amount) {
        spend(-amount);
    }

    @Override
    public int icon() {
        return BuffIndicator.LIGHT;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.ILLUMINATED);
        else target.sprite.remove(CharSprite.State.ILLUMINATED);
    }
}
