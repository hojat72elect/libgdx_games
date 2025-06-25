

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Poison extends Buff implements Hero.Doom {

    protected float left;

    private static final String LEFT = "left";

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat(LEFT);
    }

    public void set(float duration) {
        this.left = Math.max(duration, left);
    }

    public void extend(float duration) {
        this.left += duration;
    }

    @Override
    public int icon() {
        return BuffIndicator.POISON;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0.6f, 0.2f, 0.6f);
    }

    public String iconTextDisplay() {
        return Integer.toString((int) left);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target) && target.sprite != null) {
            CellEmitter.center(target.pos).burst(PoisonParticle.SPLASH, 5);
            return true;
        } else
            return false;
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {

            target.damage((int) (left / 3) + 1, this);
            spend(TICK);

            if ((left -= TICK) <= 0) {
                detach();
            }
        } else {

            detach();
        }

        return true;
    }

    @Override
    public void onDeath() {
        Badges.validateDeathFromPoison();

        Dungeon.fail(this);
        GLog.n(Messages.get(this, "ondeath"));
    }
}
