

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfStrength extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_STRENGTH;

        unique = true;

        talentFactor = 2f;
    }

    @Override
    public void apply(Hero hero) {
        identify();

        hero.STR++;
        hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, "1", FloatingText.STRENGTH);

        GLog.p(Messages.get(this, "msg", hero.STR()));

        Badges.validateStrengthAttained();
        Badges.validateDuelistUnlock();
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
