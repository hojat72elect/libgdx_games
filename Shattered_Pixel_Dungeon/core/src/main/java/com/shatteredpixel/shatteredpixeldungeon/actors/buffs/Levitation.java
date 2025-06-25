

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class Levitation extends FlavourBuff {

    {
        type = buffType.POSITIVE;
    }

    public static final float DURATION = 20f;

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            target.flying = true;
            Roots.detach(target, Roots.class);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        target.flying = false;
        super.detach();
        //only press tiles if we're current in the game screen
        if (ShatteredPixelDungeon.scene() instanceof GameScene) {
            Dungeon.level.occupyCell(target);
        }
    }

    //used to determine if levitation is about to end
    public boolean detachesWithinDelay(float delay) {
        if (target.buff(Swiftthistle.TimeBubble.class) != null) {
            return false;
        }

        if (target.buff(TimekeepersHourglass.timeFreeze.class) != null) {
            return false;
        }

        return cooldown() < delay;
    }

    @Override
    public int icon() {
        return BuffIndicator.LEVITATION;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 2.1f, 2.5f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.LEVITATING);
        else target.sprite.remove(CharSprite.State.LEVITATING);
    }
}
