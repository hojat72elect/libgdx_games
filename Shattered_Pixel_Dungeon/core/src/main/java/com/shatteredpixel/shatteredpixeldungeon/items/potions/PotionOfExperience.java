

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfExperience extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_EXP;

        bones = true;

        talentFactor = 2f;
    }

    @Override
    public void apply(Hero hero) {
        identify();
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(hero.maxExp()), FloatingText.EXPERIENCE);
        hero.earnExp(hero.maxExp(), getClass());
        new Flare(6, 32).color(0xFFFF00, true).show(curUser.sprite, 2f);
    }

    @Override
    public int value() {
        return isKnown() ? 50 * quantity : super.value();
    }

    @Override
    public int energyVal() {
        return isKnown() ? 10 * quantity : super.energyVal();
    }
}
