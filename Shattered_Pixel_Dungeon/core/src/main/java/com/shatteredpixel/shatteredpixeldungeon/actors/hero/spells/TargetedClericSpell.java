

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public abstract class TargetedClericSpell extends ClericSpell {

    @Override
    public void onCast(HolyTome tome, Hero hero) {
        GameScene.selectCell(new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                onTargetSelected(tome, hero, cell);
            }

            @Override
            public String prompt() {
                return targetingPrompt();
            }
        });
    }

    @Override
    public int targetingFlags() {
        return Ballistica.MAGIC_BOLT;
    }

    protected String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    protected abstract void onTargetSelected(HolyTome tome, Hero hero, Integer target);
}
