

package com.shatteredpixel.shatteredpixeldungeon.items.spells;


import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public abstract class Spell extends Item {

    public static final String AC_CAST = "CAST";

    //affects how strongly on-scroll talents trigger from this scroll
    protected float talentFactor = 1;
    //the chance (0-1) of whether on-scroll talents trigger from this potion
    protected float talentChance = 1;

    {
        stackable = true;
        defaultAction = AC_CAST;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_CAST);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_CAST)) {

            if (curUser.buff(MagicImmune.class) != null) {
                GLog.w(Messages.get(this, "no_magic"));
                return;
            }

            onCast(hero);
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    protected abstract void onCast(Hero hero);
}
