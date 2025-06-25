

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class FireImbue extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION = 50f;

    protected float left;

    private static final String LEFT = "left";

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
        this.left = duration;
    }

    public void extend(float duration) {
        left += duration;
    }

    @Override
    public boolean act() {
        if (Dungeon.level.map[target.pos] == Terrain.GRASS) {
            Level.set(target.pos, Terrain.EMBERS);
            GameScene.updateMap(target.pos);
        }

        spend(TICK);
        left -= TICK;
        if (left <= 0) {
            detach();
        }

        return true;
    }

    public void proc(Char enemy) {
        if (Random.Int(2) == 0)
            Buff.affect(enemy, Burning.class).reignite(enemy);

        enemy.sprite.emitter().burst(FlameParticle.FACTORY, 2);
    }

    @Override
    public int icon() {
        return BuffIndicator.IMBUE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(2f, 0.75f, 0f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - left) / DURATION);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int) left);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    {
        immunities.add(Burning.class);
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            Buff.detach(target, Burning.class);
            return true;
        } else {
            return false;
        }
    }
}
