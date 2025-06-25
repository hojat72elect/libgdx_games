

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;

public class ImpShopkeeper extends Shopkeeper {

    {
        spriteClass = ImpSprite.class;
    }

    private boolean seenBefore = false;

    @Override
    protected boolean act() {

        if (!seenBefore && Dungeon.level.heroFOV[pos]) {
            if (Dungeon.hero.buff(AscensionChallenge.class) == null) {
                yell(Messages.get(this, "greetings", Messages.titleCase(Dungeon.hero.name())));
            } else {
                yell(Messages.get(this, "greetings_ascent", Messages.titleCase(Dungeon.hero.name())));
            }
            seenBefore = true;
        }

        return super.act();
    }
}
