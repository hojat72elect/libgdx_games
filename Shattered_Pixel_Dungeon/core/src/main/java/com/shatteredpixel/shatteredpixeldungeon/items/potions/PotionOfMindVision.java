

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfMindVision extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_MINDVIS;
    }

    @Override
    public void apply(Hero hero) {
        identify();
        Buff.prolong(hero, MindVision.class, MindVision.DURATION);
        SpellSprite.show(hero, SpellSprite.VISION, 1, 0.77f, 0.9f);
        Dungeon.observe();

        if (Dungeon.level.mobs.size() > 0) {
            GLog.i(Messages.get(this, "see_mobs"));
        } else {
            GLog.i(Messages.get(this, "see_none"));
        }
    }

    @Override
    public int value() {
        return isKnown() ? 30 * quantity : super.value();
    }
}
