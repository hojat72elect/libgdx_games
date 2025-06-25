

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfStamina extends ExoticPotion {

    {
        icon = ItemSpriteSheet.Icons.POTION_STAMINA;
    }

    @Override
    public void apply(Hero hero) {
        identify();

        Buff.prolong(hero, Stamina.class, Stamina.DURATION);
        SpellSprite.show(hero, SpellSprite.HASTE, 0.5f, 1, 0.5f);
    }
}
