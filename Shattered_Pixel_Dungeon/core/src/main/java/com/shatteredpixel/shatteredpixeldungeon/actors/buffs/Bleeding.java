

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Sacrificial;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sickle;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Bleeding extends Buff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    protected float level;

    //used in specific cases where the source of the bleed is important for death logic
    private Class source;

    public float level() {
        return level;
    }

    private static final String LEVEL = "level";
    private static final String SOURCE = "source";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(SOURCE, source);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getFloat(LEVEL);
        source = bundle.getClass(SOURCE);
    }

    public void set(float level) {
        set(level, null);
    }

    public void set(float level, Class source) {
        if (this.level < level) {
            this.level = Math.max(this.level, level);
            this.source = source;
        }
    }

    public void extend(float amount) {
        level += amount;
    }

    @Override
    public int icon() {
        return BuffIndicator.BLEEDING;
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(Math.round(level));
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {

            level = Random.NormalFloat(level / 2f, level);
            int dmg = Math.round(level);

            if (dmg > 0) {

                target.damage(dmg, this);
                if (target.sprite.visible) {
                    Splash.at(target.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                            target.sprite.blood(), Math.min(10 * dmg / target.HT, 10));
                }

                if (target == Dungeon.hero && !target.isAlive()) {
                    if (source == Chasm.class) {
                        Badges.validateDeathFromFalling();
                    } else if (source == Sacrificial.class) {
                        Badges.validateDeathFromFriendlyMagic();
                    }
                    Dungeon.fail(this);
                    GLog.n(Messages.get(this, "ondeath"));
                }

                if (source == Sickle.HarvestBleedTracker.class && !target.isAlive()) {
                    MeleeWeapon.onAbilityKill(Dungeon.hero, target);
                }

                spend(TICK);
            } else {
                detach();
            }
        } else {

            detach();
        }

        return true;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", Math.round(level));
    }
}
