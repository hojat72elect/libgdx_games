

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class HeroDisguise extends FlavourBuff {

    {
        announced = true;
    }

    private HeroClass cls = null;

    public static float DURATION = 1000f;

    public HeroClass getDisguise() {
        return cls;
    }

    @Override
    public int icon() {
        return BuffIndicator.DISGUISE;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public void fx(boolean on) {
        if (target instanceof Hero && target.sprite instanceof HeroSprite) {
            if (cls == null) {
                do {
                    cls = Random.oneOf(HeroClass.values());
                } while (cls == ((Hero) target).heroClass);
            }

            if (on) ((HeroSprite) target.sprite).disguise(cls);
            else ((HeroSprite) target.sprite).disguise(((Hero) target).heroClass);
            GameScene.updateAvatar();
        }
    }

    private static final String CLASS = "class";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CLASS, cls);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cls = bundle.getEnum(CLASS, HeroClass.class);
    }
}
