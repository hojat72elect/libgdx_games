

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

//A magical version of barkskin, essentially
public class ArcaneArmor extends Buff {

    {
        type = buffType.POSITIVE;
    }

    private int level = 0;
    private int interval = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend(interval);
            if (--level <= 0) {
                detach();
            }
        } else {

            detach();
        }

        return true;
    }

    public int level() {
        return level;
    }

    public void set(int value, int time) {
        //decide whether to override, preferring high value + low interval
        if (Math.sqrt(interval) * level < Math.sqrt(time) * value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    public void delay(float value) {
        spend(value);
    }

    @Override
    public int icon() {
        return BuffIndicator.ARMOR;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 0.5f, 2f);
    }

    @Override
    public float iconFadePercent() {
        if (target instanceof Hero) {
            float max = ((Hero) target).lvl / 2 + 5;
            return (max - level) / max;
        }
        return 0;
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(level);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
    }

    private static final String LEVEL = "level";
    private static final String INTERVAL = "interval";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(INTERVAL, interval);
        bundle.put(LEVEL, level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        interval = bundle.getInt(INTERVAL);
        level = bundle.getInt(LEVEL);
    }
}
