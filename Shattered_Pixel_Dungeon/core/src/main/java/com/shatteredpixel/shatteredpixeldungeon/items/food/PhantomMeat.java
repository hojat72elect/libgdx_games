

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PhantomMeat extends Food {

    {
        image = ItemSpriteSheet.PHANTOM_MEAT;
        energy = Hunger.STARVING;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    public int value() {
        return 30 * quantity;
    }

    public static void effect(Hero hero) {

        Barkskin.conditionallyAppend(hero, hero.HT / 4, 1);
        Buff.affect(hero, Invisibility.class, Invisibility.DURATION);
        hero.HP = Math.min(hero.HP + hero.HT / 4, hero.HT);
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(hero.HT / 4), FloatingText.HEALING);
        PotionOfHealing.cure(hero);
    }
}
