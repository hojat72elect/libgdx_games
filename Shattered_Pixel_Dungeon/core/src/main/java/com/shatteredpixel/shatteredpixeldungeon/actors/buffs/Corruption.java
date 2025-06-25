

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Corruption extends AllyBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    private float buildToDamage = 0f;

    //corrupted enemies are usually fully healed and cleansed of most debuffs
    public static void corruptionHeal(Char target) {
        target.HP = target.HT;
        target.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(target.HT), FloatingText.HEALING);
        for (Buff buff : target.buffs()) {
            if (buff.type == Buff.buffType.NEGATIVE
                    && !(buff instanceof SoulMark)) {
                buff.detach();
            }
        }
    }

    @Override
    public boolean act() {
        buildToDamage += target.HT / 100f;

        int damage = (int) buildToDamage;
        buildToDamage -= damage;

        if (damage > 0)
            target.damage(damage, this);

        spend(TICK);

        return true;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.DARKENED);
        else if (target.invisible == 0) target.sprite.remove(CharSprite.State.DARKENED);
    }

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }
}
