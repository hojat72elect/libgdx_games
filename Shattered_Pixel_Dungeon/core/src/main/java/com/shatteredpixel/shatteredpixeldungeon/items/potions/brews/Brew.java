

package com.shatteredpixel.shatteredpixeldungeon.items.potions.brews;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

import java.util.ArrayList;

public abstract class Brew extends Potion {

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_DRINK);
        return actions;
    }

    @Override
    public String defaultAction() {
        return AC_THROW;
    }

    @Override
    public void doThrow(Hero hero) {
        GameScene.selectCell(thrower);
    }

    @Override
    public abstract void shatter(int cell);

    @Override
    public boolean isKnown() {
        return true;
    }

    @Override
    public int value() {
        return quantity * 60;
    }

    @Override
    public int energyVal() {
        return quantity * 12;
    }
}
