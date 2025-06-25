

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfHaste extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_HASTE;
    }

    @Override
    public void apply(Hero hero) {
        identify();

        GLog.w(Messages.get(this, "energetic"));
        Buff.prolong(hero, Haste.class, Haste.DURATION);
        SpellSprite.show(hero, SpellSprite.HASTE, 1, 1, 0);
    }

    @Override
    public int value() {
        return isKnown() ? 40 * quantity : super.value();
    }
}
