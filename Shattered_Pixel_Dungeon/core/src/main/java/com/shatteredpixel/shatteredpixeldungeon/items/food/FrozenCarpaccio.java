

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class FrozenCarpaccio extends Food {

    {
        image = ItemSpriteSheet.CARPACCIO;
        energy = Hunger.HUNGRY / 2f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    public int value() {
        return 10 * quantity;
    }

    public static void effect(Hero hero) {
        switch (Random.Int(5)) {
            case 0:
                GLog.i(Messages.get(FrozenCarpaccio.class, "invis"));
                Buff.affect(hero, Invisibility.class, Invisibility.DURATION);
                break;
            case 1:
                GLog.i(Messages.get(FrozenCarpaccio.class, "hard"));
                Barkskin.conditionallyAppend(hero, hero.HT / 4, 1);
                break;
            case 2:
                GLog.i(Messages.get(FrozenCarpaccio.class, "refresh"));
                PotionOfHealing.cure(hero);
                break;
            case 3:
                GLog.i(Messages.get(FrozenCarpaccio.class, "better"));
                hero.HP = Math.min(hero.HP + hero.HT / 4, hero.HT);
                hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(hero.HT / 4), FloatingText.HEALING);
                break;
        }
    }

    public static Food cook(MysteryMeat ingredient) {
        FrozenCarpaccio result = new FrozenCarpaccio();
        result.quantity = ingredient.quantity();
        return result;
    }
}
