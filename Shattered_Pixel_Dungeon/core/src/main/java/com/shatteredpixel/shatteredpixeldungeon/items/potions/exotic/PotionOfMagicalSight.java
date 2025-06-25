

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfMagicalSight extends ExoticPotion {

    {
        icon = ItemSpriteSheet.Icons.POTION_MAGISIGHT;
    }

    @Override
    public void apply(Hero hero) {
        identify();
        Buff.prolong(hero, MagicalSight.class, MagicalSight.DURATION);
        SpellSprite.show(hero, SpellSprite.VISION);
        Dungeon.observe();
    }
}
