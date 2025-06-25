

package com.shatteredpixel.shatteredpixeldungeon.items.remains;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;

import java.util.ArrayList;

public abstract class RemainsItem extends Item {

    {
        bones = false;

        defaultAction = AC_USE;
    }

    public static final String AC_USE = "USE";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_USE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_USE)) {
            hero.sprite.operate(hero.pos);

            Catalog.countUse(getClass());
            doEffect(hero);

            hero.spendAndNext(Actor.TICK);
            detach(hero.belongings.backpack);
        }
    }

    protected abstract void doEffect(Hero hero);

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public int value() {
        return 50;
    }

    public static RemainsItem get(HeroClass cls) {
        switch (cls) {
            case WARRIOR:
            default:
                return new SealShard();
            case MAGE:
                return new BrokenStaff();
            case ROGUE:
                return new CloakScrap();
            case HUNTRESS:
                return new BowFragment();
            case DUELIST:
                return new BrokenHilt();
            case CLERIC:
                return new TornPage();
        }
    }
}
