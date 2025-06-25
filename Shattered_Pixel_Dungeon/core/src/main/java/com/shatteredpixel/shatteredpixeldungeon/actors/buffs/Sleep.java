

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

public class Sleep extends FlavourBuff {

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.idle();
    }

    public static final float SWS = 1.5f;
}
