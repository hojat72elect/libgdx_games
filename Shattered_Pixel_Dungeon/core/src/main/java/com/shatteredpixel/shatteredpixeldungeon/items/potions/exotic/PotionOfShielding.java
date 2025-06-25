

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfShielding extends ExoticPotion {

    {
        icon = ItemSpriteSheet.Icons.POTION_SHIELDING;
    }

    @Override
    public void apply(Hero hero) {
        identify();

        if (Dungeon.isChallenged(Challenges.NO_HEALING)) {
            PotionOfHealing.pharmacophobiaProc(hero);
        } else {
            //~75% of a potion of healing
            Buff.affect(hero, Barrier.class).setShield((int) (0.6f * hero.HT + 10));
            hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString((int) (0.6f * hero.HT + 10)), FloatingText.SHIELDING);
        }
    }
}
